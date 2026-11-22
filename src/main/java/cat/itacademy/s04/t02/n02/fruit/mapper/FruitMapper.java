package cat.itacademy.s04.t02.n02.fruit.mapper;


import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import org.springframework.stereotype.Component;

@Component
public class FruitMapper {
    public FruitResponseDto toDto (Fruit fruit){
        return new FruitResponseDto (
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKg(),
                fruit.getProvider().getId(),
                fruit.getProvider().getName()
        );
    }

    public Fruit toEntity (FruitRequestDto requestDto, Provider provider){
        Fruit fruit = new Fruit();
        fruit.setName(requestDto.name());
        fruit.setWeightInKg(requestDto.weightInKg());
        fruit.setProvider(provider);
        return fruit;
    }
}
