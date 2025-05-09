package com.abrhernandez.meeting_golden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "Dog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dog implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    @Column(nullable = false)
    private String name;
    private String bread;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String gender;
    private byte[] image;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Person owner;
}
