package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDto(
        @NotBlank (message = "name is required")
        String name,
        @NotBlank (message = "name is required")
        String country
) {

}

