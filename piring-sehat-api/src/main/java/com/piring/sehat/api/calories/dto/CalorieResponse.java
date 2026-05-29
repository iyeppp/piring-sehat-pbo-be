package com.piring.sehat.api.calories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalorieResponse {
    private UUID id;
    private String name;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private String mealType;
    private LocalDate entryDate;
    private String timestamp; // Untuk format jam di frontend
}
