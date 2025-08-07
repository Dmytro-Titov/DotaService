package com.andersenlab.dotaservice.dictionaryData;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.HeroRepository;
import com.andersenlab.dotaservice.repository.ItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DictionaryDataInitializer implements CommandLineRunner {

  @Autowired
  private HeroRepository heroRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Override
  public void run(String... args) throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    if (heroRepository.count() > 0) {
      heroRepository.deleteAll();
    }
    TypeReference<List<Hero>> heroType = new TypeReference<>() {};
    InputStream heroInput = getClass().getClassLoader().getResourceAsStream("dictionaryData/heroes.json");
    List<Hero> heroes = mapper.readValue(heroInput, heroType);
    heroRepository.saveAll(heroes);

    if (itemRepository.count() > 0) {
      itemRepository.deleteAll();
    }
    TypeReference<List<Item>> itemType = new TypeReference<>() {
    };
    InputStream itemInput = getClass().getClassLoader().getResourceAsStream("dictionaryData/items.json");
    List<Item> items = mapper.readValue(itemInput, itemType);
    itemRepository.saveAll(items);
  }
}
