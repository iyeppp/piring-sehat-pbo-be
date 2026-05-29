package com.piring.sehat.api.calories.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalorieRequest {

    @NotBlank(message = "Nama makanan tidak boleh kosong")
    private String name;

    @NotNull(message = "Kalori tidak boleh kosong")
    @Min(value = 0, message = "Kalori tidak boleh negatif")
    private Double calories;

    @Min(value = 0, message = "Protein tidak boleh negatif")
    private Double protein = 0.0;

    @Min(value = 0, message = "Karbohidrat tidak boleh negatif")
    private Double carbs = 0.0;

    @Min(value = 0, message = "Lemak tidak boleh negatif")
    private Double fat = 0.0;

    @NotBlank(message = "Tipe makan (mealType) tidak boleh kosong")
    private String mealType;

    @NotNull(message = "Tanggal pencatatan (entryDate) tidak boleh kosong")
    private LocalDate entryDate;
}
