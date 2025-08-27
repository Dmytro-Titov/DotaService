package com.andersenlab.dotaservice.configuration;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.HeroRepository;
import com.andersenlab.dotaservice.repository.ItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DictionaryDataInitializer implements CommandLineRunner {

  private static final String HEROES_JSON = "dictionaryData/heroes.json";
  private static final String ITEMS_JSON = "dictionaryData/items.json";

  private final HeroRepository heroRepository;
  private final ItemRepository itemRepository;

  @Override
  public void run(String... args) throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    loadData(HEROES_JSON, new TypeReference<List<Hero>>() {}, heroRepository, mapper);
    loadData(ITEMS_JSON, new TypeReference<List<Item>>() {}, itemRepository, mapper);
  }

  private <T> void loadData(String path, TypeReference<List<T>> type, JpaRepository<T, Long> repository,
      ObjectMapper mapper) throws IOException {
    if (repository.count() == 0) {
      InputStream input = getClass().getClassLoader().getResourceAsStream(path);
      if (input == null) {
        throw new RuntimeException("File %s not found in resources/dictionaryData".formatted(path));
      }

      List<T> data = mapper.readValue(input, type);
      repository.saveAll(data);
    }
  }
}
