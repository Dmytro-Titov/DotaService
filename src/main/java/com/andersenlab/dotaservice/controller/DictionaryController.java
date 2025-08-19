package com.andersenlab.dotaservice.controller;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.service.HeroService;
import com.andersenlab.dotaservice.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

  private final HeroService heroService;
  private final ItemService itemService;

  @GetMapping("/heroes")
  public ResponseEntity<List<Hero>> getAllHeroes() {
    return ResponseEntity.ok(heroService.getAllHeroes());
  }

  @GetMapping("/items")
  public ResponseEntity<List<Item>> getAllItems() {
    return ResponseEntity.ok(itemService.getAllItems());
  }
}
