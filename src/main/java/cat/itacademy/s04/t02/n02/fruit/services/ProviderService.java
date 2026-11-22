package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDto;

import java.util.List;

public interface ProviderService {
    ProviderResponseDto createProvider (ProviderRequestDto requestDto);
    List<ProviderResponseDto> getAllProviders();
    ProviderResponseDto getProviderById (Long id);
    ProviderResponseDto updateProvider ( Long id, ProviderRequestDto requestDto);
//    void deleteProvider (Long id);
}
