package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.services.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {
    ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    public ResponseEntity<ProviderResponseDto> createProvider(@Valid @RequestBody ProviderRequestDto requestDto) {
        ProviderResponseDto response = providerService.createProvider(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> getAllProviders() {
        List<ProviderResponseDto> providers = providerService.getAllProviders();

        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> getProviderById(@PathVariable Long id) {
        ProviderResponseDto provider = providerService.getProviderById(id);

        return ResponseEntity.ok(provider);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ProviderResponseDto> updateProvider (
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequestDto requestDto){
        ProviderResponseDto updateProvider = providerService.updateProvider(id, requestDto);

        return ResponseEntity.ok(updateProvider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider (@PathVariable Long id) {
        providerService.deleteProvider(id);

        return ResponseEntity.noContent().build();
    }
}
