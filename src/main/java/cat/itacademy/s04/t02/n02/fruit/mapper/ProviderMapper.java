package cat.itacademy.s04.t02.n02.fruit.mapper;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;

public class ProviderMapper {

    public ProviderResponseDto toDto (Provider provider){
        return new ProviderResponseDto (
                provider.getId(),
                provider.getName(),
                provider.getCountry()
        );
    }

    public Provider toEntity (ProviderRequestDto requestDto){
        Provider provider = new Provider();
        provider.setName(requestDto.name());
        provider.setCountry(requestDto.country());
        return provider;
    }
}
