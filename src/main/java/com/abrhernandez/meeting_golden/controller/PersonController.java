package com.abrhernandez.meeting_golden.controller;

import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import com.abrhernandez.meeting_golden.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @MutationMapping
    public Person createPerson(@Argument PersonInput personInput){
        return personService.createPerson(personInput);
    }

}
