package com.abrhernandez.meeting_golden.service;

import com.abrhernandez.meeting_golden.entity.Dog;
import com.abrhernandez.meeting_golden.entity.Person;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessagePublisher {

    @Autowired
    private KafkaTemplate<String,Object> template;


    public void sendPersonMessage(Person person){
        CompletableFuture<SendResult<String, Object>> quickstart = template.send("dogtopic", person);
        quickstart.whenComplete((result,ex)->{
            if(ex == null){
                log.info("Sent message : {} with offset: {}", person.toString(), result.getRecordMetadata().offset());
            }else{
                log.error("Unable to send message {}", ex.getMessage());
            }
        });

    }

}
