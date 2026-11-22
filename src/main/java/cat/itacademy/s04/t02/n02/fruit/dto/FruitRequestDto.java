package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitRequestDto(
        @NotBlank (message = "fruit name is required")
        String name,

        @NotNull (message = " fruit weight in kg can't be null")
        @Positive (message = "fruit weight in kg must be positive")
        Integer weightInKg,

        @NotNull(message = "provider Id is required")
        Long providerId
) {
}

