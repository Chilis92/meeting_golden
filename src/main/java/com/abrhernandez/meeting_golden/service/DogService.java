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

@Slf4j
@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final PersonRepository personRepository;
    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final DogMapper dogMapper;
    private final AmazonS3Service amazonS3Service;

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
            personSaved = personRepository.save(person);
            log.info("Person saved, ID : " + personSaved.getPersonId());
            kafkaMessagePublisher.sendPersonMessage(personSaved);
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
        dog.setBreed(dogInput.breed());
        dog.setColor(dogInput.color());
        dog.setAge(dogInput.age());
        dog.setGender(dogInput.gender());

        if (dogInput.file() != null) {
            String imageURL = amazonS3Service.uploadFile(dogInput.file());
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
