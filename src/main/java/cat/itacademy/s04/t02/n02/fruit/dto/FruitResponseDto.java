package cat.itacademy.s04.t02.n02.fruit.dto;

import cat.itacademy.s04.t02.n02.fruit.model.Provider;

public record FruitResponseDto(
        Long id,
        String name,
        Integer weightInKg,
        Long providerId,
        String providerName
) {
}
