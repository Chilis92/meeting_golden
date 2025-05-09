package com.abrhernandez.meeting_golden.repository;

import com.abrhernandez.meeting_golden.entity.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends CrudRepository<Dog,Integer> {

    //List<Dog> findAll();
}
