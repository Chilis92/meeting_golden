package com.abrhernandez.meeting_golden.mappers;

import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import lombok.RequiredArgsConstructor;

public class PersonMapper {

    public static Person mapPersonFromPersonInput(PersonInput personInput){
        Person person = new Person();
        person.setName(personInput.name());
        person.setAge(personInput.age());
        person.setGender(personInput.gender());
        person.setEmail(personInput.email());
        person.setPhone(personInput.phone());
        person.setCity(personInput.city());
        return person;
    }


}
