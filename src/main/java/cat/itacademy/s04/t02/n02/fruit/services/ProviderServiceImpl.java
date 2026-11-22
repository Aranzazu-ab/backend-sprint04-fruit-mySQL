package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mapper.ProviderMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;

import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;
    private final ProviderMapper providerMapper;


    public ProviderServiceImpl (ProviderRepository providerRepository,
                                FruitRepository fruitRepository,
                                ProviderMapper providerMapper){
        this.providerRepository= providerRepository;
        this.fruitRepository = fruitRepository;
        this.providerMapper = providerMapper;
    }

    @Override
    public ProviderResponseDto createProvider(ProviderRequestDto requestDto) {
        if (providerRepository.findByName(requestDto.name()).isPresent()){
            throw new ProviderAlreadyExistsException("Provider "+ requestDto.name()+ " already exists");
        }

        Provider provider = providerMapper.toEntity(requestDto);
        Provider providerSaved =providerRepository.save(provider);
        return providerMapper.toDto(providerSaved);
    }

    @Override
    public List<ProviderResponseDto> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toDto)
                .toList();
    }

    @Override
    public ProviderResponseDto getProviderById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(()-> new ProviderNotFoundException("Provider with id: " +id +" not found."));

        return providerMapper.toDto(provider)




    }

    @Override
    public ProviderResponseDto updateProvider(Long id, ProviderRequestDto requestDto) {
        Provider provider=providerRepository.findById(id)
                .orElseThrow(()-> new ProviderNotFoundException("Provider with id: " +id +" not found."));

        if (!provider.getName().equals(requestDto.name()) &&
                providerRepository.findByName(requestDto.name()).isPresent()){
            throw new ProviderAlreadyExistsException("Provider with name: " + requestDto.name()+" already exists.");
        }

        provider.setName(requestDto.name());
        provider.setCountry(requestDto.country());

        return providerMapper.toDto(providerRepository.save(provider));
    }

    @Override
    public void deleteProvider(Long id) {
//        Provider provider=providerRepository.findById(id)
//                .orElseThrow(() -> new  ProviderNotFoundException("Provider with id: " +id +" not found."));
//

    }
}
