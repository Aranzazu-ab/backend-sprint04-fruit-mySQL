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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Test
    void shouldCreateProviderWhenCorrectData() {
        ProviderRequestDto request = new ProviderRequestDto("Mercadona", "Spain");
        Provider providerEntity = new Provider(1L, "Mercadona", "Spain");
        ProviderResponseDto response = new ProviderResponseDto(1L, "Mercadona", "Spain");

        when(providerRepository.findByName("Mercadona")).thenReturn(Optional.empty());
        when(providerMapper.toEntity(request)).thenReturn(providerEntity);
        when(providerRepository.save(providerEntity)).thenReturn(providerEntity);
        when(providerMapper.toDto(providerEntity)).thenReturn(response);

        ProviderResponseDto result = providerService.createProvider(request);

        assertNotNull(result);
        assertEquals("Mercadona", result.name());
        verify(providerRepository, times(1)).save(providerEntity);
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        ProviderRequestDto request = new ProviderRequestDto("Condis", "Spain");

        Provider existingProvider = new Provider(1L, "Condis", "Spain");
        when(providerRepository.findByName("Condis")).thenReturn(Optional.of(existingProvider));

        assertThrows(ProviderAlreadyExistsException.class, () -> {
            providerService.createProvider(request);
        });

        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void shouldDeleteProviderSuccessfully() {
        Long id = 1L;
        Provider provider = new Provider(id, "Condis", "Spain");

        when(providerRepository.findById(id)).thenReturn(Optional.of(provider));
        when(fruitRepository.existsByProviderId(id)).thenReturn(false);

        providerService.deleteProvider(id);

        verify(providerRepository, times(1)).delete(provider);
    }

    @Test
    void shouldThrowExceptionWhenDeletingProviderWithFruits() {
        Long id = 1L;
        Provider provider = new Provider(id, "Mercadona", "Spain");

        when(providerRepository.findById(id)).thenReturn(Optional.of(provider));
        when(fruitRepository.existsByProviderId(id)).thenReturn(true);

        assertThrows(ProviderHasFruitsException.class, () -> providerService.deleteProvider(id));

        verify(providerRepository, never()).delete(any(Provider.class));
    }

    @Test
    void shouldThrowExceptionWhenProviderToDeleteDoesNotExist() {
        Long id = 99L;
        when(providerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> providerService.deleteProvider(id));
    }
}
