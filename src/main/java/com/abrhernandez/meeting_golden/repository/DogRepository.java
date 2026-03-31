package com.abrhernandez.meeting_golden.repository;

import com.abrhernandez.meeting_golden.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer> {
}
