package com.store.book.dao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for creating a discount")
public class DiscountDtoRequestAll {

    @NotNull
    @DecimalMin(value = "0.01", message = "CannotBeNegative")
    @DecimalMax(value = "100.00", message = "CannotAboveHundred")
    @Digits(integer = 2, fraction = 2)
    @Schema(description = "Discount percentage", example = "15.75")
    private BigDecimal percentage;

    @Schema(description = "Start date of discount", example = "2025-07-09T15:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private String startDate;

    @Schema(description = "End date of discount", example = "2025-07-10T15:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private String endDate;
}