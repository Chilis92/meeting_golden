package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
import com.abrhernandez.meeting_golden.mappers.DogMapper;
import com.abrhernandez.meeting_golden.mappers.PersonMapper;
import com.abrhernandez.meeting_golden.repository.DogRepository;
import com.abrhernandez.meeting_golden.repository.PersonRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final PersonRepository personRepository;
    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final DogMapper dogMapper;

    public List<Dog> findAll(){
       return Streamable.of(dogRepository.findAll()).toList();

    }

    public List<Dog> createDog(List<DogInput> dogInput, PersonInput personInput){

        Person person = PersonMapper.mapPersonFromPersonInput(personInput);
        Person personSaved = personRepository.save(person);
        log.info("Person saved, ID : "+personSaved.getPersonId());

        List<Dog> dogsToBeSaved = dogMapper.mapDogListFromDogInput(dogInput,personSaved);
        dogRepository.saveAll(dogsToBeSaved);
        log.info("All dogs saved, size : "+dogsToBeSaved.size());

        if(personSaved != null){
            kafkaMessagePublisher.sendPersonMessage(person);
        }

        return dogsToBeSaved;
    }

}
