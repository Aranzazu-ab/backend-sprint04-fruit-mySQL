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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitMapper fruitMapper;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void shouldCreateFruitSuccessfully() {
        FruitRequestDto request = new FruitRequestDto("Apple", 5, 1L);
        Provider provider = new Provider(1L, "Mercabarna", "Spain");
        Fruit fruitEntity = new Fruit(1L, "Apple", 5, provider);
        FruitResponseDto response = new FruitResponseDto(1L, "Apple", 5, 1L, "Mercabarna");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitMapper.toEntity(request, provider)).thenReturn(fruitEntity);
        when(fruitRepository.save(fruitEntity)).thenReturn(fruitEntity);
        when(fruitMapper.toDto(fruitEntity)).thenReturn(response);

        FruitResponseDto result = fruitService.createFruit(request);

        assertNotNull(result);
        assertEquals("Apple", result.name());
        verify(fruitRepository, times(1)).save(fruitEntity);
    }

    @Test
    void shouldThrowExceptionWhenCreatingFruitWithInvalidProvider() {
        FruitRequestDto request = new FruitRequestDto("Pear", 3, 99L);
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> fruitService.createFruit(request));

        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    void shouldReturnFruitsWhenFilteringByValidProvider() {
        Long providerId = 1L;
        Provider provider = new Provider(providerId, "Mercabarna", "Spain");
        Fruit fruit = new Fruit(1L, "Apple", 5, provider);
        FruitResponseDto dto = new FruitResponseDto(1L, "Apple", 5, providerId, "Mercabarna");

        when(providerRepository.existsById(providerId)).thenReturn(true);
        when(fruitRepository.findByProviderId(providerId)).thenReturn(List.of(fruit));
        when(fruitMapper.toDto(fruit)).thenReturn(dto);

        List<FruitResponseDto> result = fruitService.getFruitsByProviderId(providerId);

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());
    }

    @Test
    void shouldThrowExceptionWhenFilteringByNonExistentProvider() {
        Long providerId = 99L;
        when(providerRepository.existsById(providerId)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, () -> fruitService.getFruitsByProviderId(providerId));
    }

    @Test
    void shouldUpdateFruitSuccessfully() {
        Long fruitId = 1L;
        FruitRequestDto request = new FruitRequestDto("Green Apple", 6, 1L);
        Provider provider = new Provider(1L, "Mercabarna", "Spain");
        Fruit existingFruit = new Fruit(fruitId, "Apple", 5, provider);
        FruitResponseDto response = new FruitResponseDto(fruitId, "Green Apple", 6, 1L, "Mercabarna");

        when(fruitRepository.findById(fruitId)).thenReturn(Optional.of(existingFruit));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitRepository.save(existingFruit)).thenReturn(existingFruit);
        when(fruitMapper.toDto(any(Fruit.class))).thenReturn(response);

        FruitResponseDto result = fruitService.updateFruit(fruitId, request);

        assertNotNull(result);
        assertEquals("Green Apple", result.name());
        assertEquals("Green Apple", existingFruit.getName());
        assertEquals(6, existingFruit.getWeightInKg());
        verify(fruitRepository, times(1)).save(existingFruit);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentFruit() {
        Long fruitId = 99L;
        FruitRequestDto request = new FruitRequestDto("Apple", 5, 1L);

        when(fruitRepository.findById(fruitId)).thenReturn(Optional.empty());
        
        assertThrows(FruitNotFoundException.class, () -> fruitService.updateFruit(fruitId, request));
    }
}
