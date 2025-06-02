package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.DogInput;
import com.abrhernandez.meeting_golden.entity.Person;
import com.abrhernandez.meeting_golden.entity.PersonInput;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class DogService {

    @Value("${aws.s3.bucket.name}")
    private  String bucketName;
    private final AmazonS3 amazonS3;
    private final DogRepository dogRepository;
    private final PersonRepository personRepository;

    private final KafkaMessagePublisher kafkaMessagePublisher;

    public List<Dog> findAll(){
       return Streamable.of(dogRepository.findAll()).toList();

    }

    public Dog createDog(DogInput dogInput, MultipartFile file){

        String imageURL = uploadFile(file);
        //String imageURL = "";

        Dog dog = new Dog();
        dog.setAge(dogInput.age());
        dog.setColor(dogInput.color());
        dog.setBread(dogInput.bread());
        dog.setGender(dogInput.gender());
        dog.setName(dogInput.name());

        if(!imageURL.isEmpty() || !imageURL.isBlank()){
            dog.setImageURL(imageURL);
        }

        PersonInput personInput =  dogInput.owner();
        Person person = new Person();
        person.setName(personInput.name());
        person.setAge(personInput.age());
        person.setGender(personInput.gender());
        person.setEmail(personInput.email());
        person.setPhone(personInput.phone());
        person.setCity(personInput.city());
        Person personSaved = personRepository.save(person);
        dog.setOwner(personSaved);

        Dog dogSaved = dogRepository.save(dog);
        log.info("Dog stored in DB "+dogInput.name());
        if(dogSaved != null){
            kafkaMessagePublisher.sendPersonMessage(person);
        }

        return dogSaved;
    }

    // Upload file to S3 bucket
    private String uploadFile(MultipartFile file) {
        try {
            String s3FileName = file.getOriginalFilename();
            String region = amazonS3.getRegionName();
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            objectMetadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata));
            log.info("Image successfully saved to S3, size: "+file.getSize());
            return "https://"+bucketName+".s3."+region+".amazonaws.com/"+s3FileName;
        } catch (Exception e) {
            log.error("Error uploading file : "+e.getMessage());
            return "";
        }
    }

}
