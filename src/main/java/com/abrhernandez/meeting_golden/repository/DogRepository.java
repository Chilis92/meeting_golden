package com.abrhernandez.meeting_golden.repository;

import com.abrhernandez.meeting_golden.entity.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer> {

    @Query("SELECT d FROM Dog d LEFT JOIN FETCH d.owner")
    List<Dog> findAllWithOwner();

    @Override
    @EntityGraph(attributePaths = {"owner"})
    Page<Dog> findAll(Pageable pageable);
}
