package com.abrhernandez.meeting_golden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity
@Data
public class Person implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int personId;
    @Column(nullable = false)
    private String name;
    private Integer age;
    private String email;
    private String phone;
    @OneToMany(mappedBy = "owner")
    private Set<Dog> dogs;
}
