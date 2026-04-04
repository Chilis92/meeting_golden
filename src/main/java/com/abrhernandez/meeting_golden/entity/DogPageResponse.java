package com.abrhernandez.meeting_golden.entity;

import java.util.List;

public record DogPageResponse(
        List<Dog> content,
        long totalElements,
        int totalPages,
        int currentPage,
        boolean hasNext
) {}
