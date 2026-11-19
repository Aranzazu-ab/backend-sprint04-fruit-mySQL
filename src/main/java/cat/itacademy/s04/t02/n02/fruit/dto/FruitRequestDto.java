package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitRequestDto(
        @NotBlank (message = "name is required")
        String name,

        @NotNull (message = "weight in kg can't be null")
        @Positive (message = "weight in kg must be positive")
        Integer weightInKg
) {
}

