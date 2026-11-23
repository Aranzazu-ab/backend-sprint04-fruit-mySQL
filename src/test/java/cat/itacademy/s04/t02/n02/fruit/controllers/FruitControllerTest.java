package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private ProviderRepository providerRepository;

    private Provider savedProvider;

    @BeforeEach
    void setUp() {
        fruitRepository.deleteAll();
        providerRepository.deleteAll();

        Provider provider = new Provider(null, "Mercabarna", "Spain");
        savedProvider = providerRepository.save(provider);
    }

    @Test
    void shouldCreateFruitWhenDataIsValid() throws Exception {
        String body = String.format("""
                {"name": "Apple", "weightInKg": 5, "providerId": %d}
                """, savedProvider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.providerName").value("Mercabarna"));
    }

    @Test
    void shouldReturn404WhenCreatingFruitWithInvalidProvider() throws Exception {
        String body = """
                {"name": "Apple", "weightInKg": 5, "providerId": -1}
                """;

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn400WhenCreatingFruitWithInvalidData() throws Exception {
        String body = String.format("""
                {"name": "", "weightInKg": -5, "providerId": %d}
                """, savedProvider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnEmptyArrayWhenNoFruitsExist() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnAllFruitsWhenTheyExist() throws Exception {
        Fruit fruit = new Fruit(null, "Pear", 3, savedProvider);
        fruitRepository.save(fruit);

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pear"));
    }

    @Test
    void shouldReturnFilteredFruitsWhenProviderIdIsProvided() throws Exception {
        Provider otherProvider = providerRepository.save(new Provider(null, "Condis", "Mexico"));
        fruitRepository.save(new Fruit(null, "Apple", 5, savedProvider));
        fruitRepository.save(new Fruit(null, "Uva", 2, otherProvider));


        mockMvc.perform(get("/fruits?providerId=" + savedProvider.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void shouldReturn404WhenFilteringByNonExistentProvider() throws Exception {
        mockMvc.perform(get("/fruits?providerId=-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFruitWhenIdExists() throws Exception {
        Fruit saved = fruitRepository.save(new Fruit(null, "Banana", 4, savedProvider));

        mockMvc.perform(get("/fruits/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Banana"));
    }

    @Test
    void shouldReturn404WhenFruitIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/fruits/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateFruitWhenDataIsValid() throws Exception {
        Fruit saved = fruitRepository.save(new Fruit(null, "Apple", 5, savedProvider));

        String updateBody = String.format("""
                {"name": "Green Apple", "weightInKg": 6, "providerId": %d}
                """, savedProvider.getId());

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Apple"))
                .andExpect(jsonPath("$.weightInKg").value(6));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentFruit() throws Exception {
        String updateBody = String.format("""
                {"name": "Green Apple", "weightInKg": 6, "providerId": %d}
                """, savedProvider.getId());

        mockMvc.perform(put("/fruits/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn204WhenDeletingFruit() throws Exception {
        Fruit saved = fruitRepository.save(new Fruit(null, "Kiwi", 2, savedProvider));

        mockMvc.perform(delete("/fruits/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertTrue(fruitRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentFruit() throws Exception {
        mockMvc.perform(delete("/fruits/-1"))
                .andExpect(status().isNotFound());
    }
}
