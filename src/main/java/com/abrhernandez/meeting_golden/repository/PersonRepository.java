package com.abrhernandez.meeting_golden.repository;

import com.abrhernandez.meeting_golden.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEmail(String email);
}
