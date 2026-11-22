package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mapper.FruitMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService {
    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;
    private final FruitMapper fruitMapper;

    public FruitServiceImpl (FruitRepository fruitRepository,
                             ProviderRepository providerRepository,
                             FruitMapper fruitMapper){
        this.fruitRepository = fruitRepository;
        this.providerRepository = providerRepository;
        this.fruitMapper = fruitMapper;
    }

    @Override
    public FruitResponseDto createFruit(FruitRequestDto requestDto) {
        Provider provider = providerRepository.findById(requestDto.providerId())
                .orElseThrow(()->new ProviderNotFoundException("Provider with id: " +requestDto.providerId() +" not found."));

        Fruit fruit = fruitMapper.toEntity(requestDto, provider);
        Fruit fruitSaved= fruitRepository.save(fruit);

        return fruitMapper.toDto(fruitSaved);
    }

    @Override
    public List<FruitResponseDto> getAllFruits () {
       return fruitRepository.findAll().stream()
               .map(fruitMapper::toDto)
               .toList();
    }

    @Override
    public FruitResponseDto getFruitById(Long id) {
        Fruit fruit= fruitRepository.findById(id)
                .orElseThrow(()-> new FruitNotFoundException("Fruit not found with id: "+ id));

        return fruitMapper.toDto(fruit);
    }

    @Override
    public FruitResponseDto updateFruit(Long id, FruitRequestDto requestDto) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found with id: " + id));

        Provider provider = providerRepository.findById(requestDto.providerId())
                .orElseThrow(()->new ProviderNotFoundException("Provider with id: " +requestDto.providerId() +" not found."));

        fruit.setName(requestDto.name());
        fruit.setWeightInKg(requestDto.weightInKg());
        fruit.setProvider((provider));

        Fruit fruitUpdated = fruitRepository.save(fruit);

        return fruitMapper.toDto(fruitUpdated);
    }

    @Override
    public void deleteFruit(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found with id: " + id));

        fruitRepository.delete(fruit);
    }

    @Override
    public List<FruitResponseDto> getFruitsByProviderId(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider with id: " +providerId +" not found.");
        }

        return fruitRepository.findByProviderId(providerId).stream()
                .map(fruitMapper::toDto)
                .toList();
    }

}
