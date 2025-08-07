package com.andersenlab.dotaservice.controller;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.repository.HeroRepository;
import com.andersenlab.dotaservice.repository.ItemRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

  private final HeroRepository heroRepository;
  private final ItemRepository itemRepository;

  public DictionaryController(HeroRepository heroRepository, ItemRepository itemRepository) {
    this.heroRepository = heroRepository;
    this.itemRepository = itemRepository;
  }

  @GetMapping("/heroes")
  public List<Hero> getAllHeroes() {
    return heroRepository.findAll();
  }

  @GetMapping("/items")
  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }
}
