package com.abrhernandez.meeting_golden.entity;

import org.springframework.web.multipart.MultipartFile;

public record DogInput(String name, String breed, String color, int age, String gender, MultipartFile file) {
}
