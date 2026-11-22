package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;

import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private final ProviderRepository providerRepository;

    public ProviderServiceImpl (ProviderRepository providerRepository){
        this.providerRepository= providerRepository;
    }

    @Override
    public ProviderResponseDto createProvider(ProviderRequestDto requestDto) {
        return null;
    }

    @Override
    public List<ProviderResponseDto> getAllProviders() {
        return List.of();
    }

    @Override
    public ProviderResponseDto getProviderById(Long id) {
        return null;
    }

    @Override
    public ProviderResponseDto updateProvider(Long id, ProviderRequestDto requestDto) {
        return null;
    }

    @Override
    public void deleteProvider(Long id) {

    }
}
