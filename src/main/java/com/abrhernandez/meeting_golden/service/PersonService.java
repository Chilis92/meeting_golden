package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import com.abrhernandez.meeting_golden.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Person createPerson(PersonInput personInput){

        Person person = new Person();
        person.setPhone(personInput.phone());
        person.setEmail(personInput.email());
        person.setGender(personInput.gender());
        person.setName(personInput.name());
        person.setAge(personInput.age());
        person.setCity(personInput.city());

        return personRepository.save(person);
    }
}
