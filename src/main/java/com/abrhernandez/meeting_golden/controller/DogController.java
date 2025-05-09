package com.abrhernandez.meeting_golden.controller;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.service.DogService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class DogController {

    private final DogService dogService;

    @QueryMapping
    public List<Dog> findAllDogs(){
       return dogService.findAll();
    }

    @MutationMapping
    public Dog createDog(@Argument DogInput dogInput) {
        return dogService.createDog(dogInput);
    }

}
