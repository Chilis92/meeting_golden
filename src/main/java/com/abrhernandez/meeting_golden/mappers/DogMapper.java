package com.abrhernandez.meeting_golden.mappers;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.service.AmazonS3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DogMapper {

    private final AmazonS3Service amazonS3Service;

    public  List<Dog> mapDogListFromDogInput(List<DogInput> dogInput, Person person){
        List<Dog> result = new ArrayList<>();
        for(DogInput doggie : dogInput){
            Dog dog = new Dog();

            if(doggie.file() != null){
                String imageURL = amazonS3Service.uploadFile(doggie.file());
                dog.setImageURL(imageURL);
            }

            dog.setAge(doggie.age());
            dog.setColor(doggie.color());
            dog.setBreed(doggie.breed());
            dog.setGender(doggie.gender());
            dog.setName(doggie.name());
            dog.setOwner(person);
            result.add(dog);

        }
        return result;
    }

}
