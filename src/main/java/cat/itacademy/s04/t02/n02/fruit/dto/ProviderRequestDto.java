package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDto(
        @NotBlank (message = " provider name is required")
        String name,
        @NotBlank (message = "provider country is required")
        String country
) {

}

