package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import com.abrhernandez.meeting_golden.exception.CustomGraphQLException;
import com.abrhernandez.meeting_golden.mappers.DogMapper;
import com.abrhernandez.meeting_golden.mappers.PersonMapper;
import com.abrhernandez.meeting_golden.repository.DogRepository;
import com.abrhernandez.meeting_golden.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final PersonRepository personRepository;
    private final Optional<KafkaMessagePublisher> kafkaMessagePublisher;
    private final DogMapper dogMapper;
    private final GcsService gcsService;

    public List<Dog> findAll() {
        return dogRepository.findAll();
    }

    public Dog findDogById(Integer id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new CustomGraphQLException(404, "Dog not found with id: " + id));
    }

    public List<Dog> createDog(List<DogInput> dogInput, PersonInput personInput) {
        Person personSaved = null;
        if (personInput != null) {
            Person person = PersonMapper.mapPersonFromPersonInput(personInput);
            final Person savedPerson = personRepository.save(person);
            personSaved = savedPerson;
            log.info("Person saved, ID : " + savedPerson.getPersonId());
            kafkaMessagePublisher.ifPresent(k -> k.sendPersonMessage(savedPerson));
        }

        List<Dog> dogsToBeSaved = dogMapper.mapDogListFromDogInput(dogInput, personSaved);
        dogRepository.saveAll(dogsToBeSaved);
        log.info("All dogs saved, size : " + dogsToBeSaved.size());

        return dogsToBeSaved;
    }

    public Dog updateDog(Integer id, DogInput dogInput) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new CustomGraphQLException(404, "Dog not found with id: " + id));

        dog.setName(dogInput.name());
        dog.setAge(dogInput.age());
        dog.setGender(dogInput.gender());
        dog.setInstagram(dogInput.instagram());
        dog.setCity(dogInput.city());

        if (dogInput.file() != null) {
            String imageURL = gcsService.uploadFile(dogInput.file());
            dog.setImageURL(imageURL);
        }

        return dogRepository.save(dog);
    }

    public boolean deleteDog(Integer id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new CustomGraphQLException(404, "Dog not found with id: " + id));
        dogRepository.delete(dog);
        return true;
    }
}
