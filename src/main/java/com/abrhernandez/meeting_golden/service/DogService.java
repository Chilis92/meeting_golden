package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.repository.DogRepository;
import com.abrhernandez.meeting_golden.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final PersonRepository personRepository;

    public List<Dog> findAll(){
       return Streamable.of(dogRepository.findAll()).toList();

    }

    public Dog createDog(DogInput dogInput){
        Dog dog = new Dog();
        dog.setAge(dogInput.age());
        dog.setColor(dogInput.color());
        dog.setBread(dogInput.bread());
        dog.setGender(dogInput.gender());
        dog.setName(dogInput.name());

        Optional<Person> personByEmail = personRepository.findPersonByEmail(dogInput.owner().email());

        dog.setOwner(personByEmail.get());
        return dogRepository.save(dog);
    }

}
