package com.abrhernandez.meeting_golden.mappers;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.service.GcsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DogMapper {

    private final GcsService gcsService;

    public  List<Dog> mapDogListFromDogInput(List<DogInput> dogInput, Person person){
        List<Dog> result = new ArrayList<>();
        for(DogInput doggie : dogInput){
            Dog dog = new Dog();

            if(doggie.file() != null){
                String imageURL = gcsService.uploadFile(doggie.file());
                dog.setImageURL(imageURL);
            }

            dog.setAge(doggie.age());
            dog.setGender(doggie.gender());
            dog.setName(doggie.name());
            dog.setInstagram(doggie.instagram());
            dog.setCity(doggie.city());
            dog.setOwner(person);
            dog.setToken(UUID.randomUUID().toString());
            result.add(dog);

        }
        return result;
    }

}
