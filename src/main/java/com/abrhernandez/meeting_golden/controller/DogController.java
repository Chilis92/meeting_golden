package com.abrhernandez.meeting_golden.controller;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import com.abrhernandez.meeting_golden.service.DogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.abrhernandez.meeting_golden.entity.DogPageResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class DogController {

    private final DogService dogService;

    @QueryMapping
    public DogPageResponse findAllDogs(@Argument int page, @Argument int size) {
        return dogService.findAll(page, size);
    }

    @QueryMapping
    public Dog findDogByID(@Argument Integer id) {
        return dogService.findDogById(id);
    }

    @MutationMapping
    public List<Dog> createDog(@Argument List<DogInput> dogInput, @Argument PersonInput personInput) throws IOException {
        return dogService.createDog(dogInput, personInput);
    }

    @MutationMapping
    public Dog updateDog(@Argument Integer id, @Argument DogInput dogInput, @Argument String token) {
        return dogService.updateDog(id, dogInput, token);
    }

    @MutationMapping
    public Boolean deleteDog(@Argument Integer id, @Argument String token) {
        return dogService.deleteDog(id, token);
    }
}
