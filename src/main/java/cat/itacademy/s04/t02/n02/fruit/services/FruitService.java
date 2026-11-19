package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;

import java.util.List;

public interface FruitService {
    FruitResponseDto createFruit (FruitRequestDto requestDto);
    List<FruitResponseDto> getAllFruits ();
    FruitResponseDto getFruitById (Long Id);
    FruitResponseDto updateFruit (Long id, FruitRequestDto requestDto);
    void deleteFruit (Long id);
}
