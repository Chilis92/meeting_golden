package com.abrhernandez.meeting_golden.entity;

import org.springframework.web.multipart.MultipartFile;

public record DogInput(String name, int age, String gender, String instagram, String city, String apodo, MultipartFile file) {
}
