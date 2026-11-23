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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private FruitRepository fruitRepository;

    @BeforeEach
    void setUp(){
        fruitRepository.deleteAll();
        providerRepository.deleteAll();
    }

    @Test
    void shouldReturnCreatedWhenPostProvider() throws Exception{
        String body= """
                {"name": "Condis", "country": "Spain"}
                """;

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Condis"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void shouldReturn400WhenProviderNameAlreadyExists () throws Exception{
        Provider provider = new Provider(null, "Condis", "Spain");
        providerRepository.save(provider);

        String body= """
                {"name": "Condis", "country": "Mexico"}
                """;

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getShouldReturnAllProviders() throws Exception {
        providerRepository.save(new Provider(null, "Condis", "Spain"));
        providerRepository.save(new Provider(null, "Aurrera", "Mexico"));

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Condis"));
    }

    @Test
    void shouldReturnProviderWhenIdExists() throws Exception {
        Provider saved = providerRepository.save(new Provider(null, "Condis", "Andorra"));

        mockMvc.perform(get("/providers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Condis"));
    }

    @Test
    void shouldReturn404WhenProviderIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/providers/-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldUpdateProviderWhenDataIsValid() throws Exception {
        Provider original = providerRepository.save(new Provider(null, "Condis", "Spain"));

        String updateBody = """
            {"name": "Walmart", "country": "Portugal"}
            """;

        mockMvc.perform(put("/providers/" + original.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Portugal"));
    }

    @Test
    void shouldReturn400WhenDeletingProviderWithFruits() throws Exception {
        Provider provider = new Provider(null, "Condis", "Spain");
        Provider savedProvider = providerRepository.save(provider);

        Fruit fruit= new Fruit();
        fruit.setName("Apples");
        fruit.setWeightInKg(5);
        fruit.setProvider(savedProvider);
        fruitRepository.save(fruit);

        mockMvc.perform(delete("/providers/" + savedProvider.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn204WhenDeletingProviderWithoutFruits() throws Exception {
        Provider saved = providerRepository.save(new Provider(null, "Condis", "Spain"));

        mockMvc.perform(delete("/providers/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertTrue(providerRepository.findById(saved.getId()).isEmpty());
    }



}
