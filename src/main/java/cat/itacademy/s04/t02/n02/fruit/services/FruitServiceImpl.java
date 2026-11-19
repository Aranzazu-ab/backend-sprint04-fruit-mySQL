package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService {
    private final FruitRepository fruitRepository;

    public FruitServiceImpl (FruitRepository fruitRepository){
        this.fruitRepository = fruitRepository;
    }

    @Override
    public FruitResponseDto createFruit(FruitRequestDto requestDTO) {
        Fruit fruit = new Fruit();
        fruit.setName(requestDTO.name());
        fruit.setWeightInKg(requestDTO.weightInKg());

        Fruit saveFruit= fruitRepository.save(fruit);

        return new FruitResponseDto(saveFruit.getId(),saveFruit.getName(),saveFruit.getWeightInKg());
    }

    @Override
    public List<FruitResponseDto> getAllFruits () {
        List<Fruit> fruits = fruitRepository.findAll();

        return fruits.stream()
                .map(fruit -> new FruitResponseDto(
                        fruit.getId(),
                        fruit.getName(),
                        fruit.getWeightInKg()
                ))
                .toList();
    }

    @Override
    public FruitResponseDto getFruitById(Long id) {
        Fruit fruit= fruitRepository.findById(id)
                .orElseThrow(()-> new FruitNotFoundException("Fruit not found with id: "+ id));

        return new FruitResponseDto(fruit.getId(), fruit.getName(), fruit.getWeightInKg());
    }

    @Override
    public FruitResponseDto updateFruit(Long id, FruitRequestDto requestDto) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found with id: " + id));

        fruit.setName(requestDto.name());
        fruit.setWeightInKg(requestDto.weightInKg());
        Fruit updatedFruit = fruitRepository.save(fruit);

        return new FruitResponseDto(updatedFruit.getId(), updatedFruit.getName(), updatedFruit.getWeightInKg());
    }

    @Override
    public void deleteFruit(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException("Fruit not found with id: " + id));

        fruitRepository.delete(fruit);
    }

}
