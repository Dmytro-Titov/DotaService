package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.MainAttribute;
import com.andersenlab.dotaservice.repository.HeroRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

  @Mock
  private HeroRepository heroRepository;

  @InjectMocks
  private HeroService heroService;

  private Hero hero;

  @BeforeEach
  void setUp() {
    hero = new Hero(1L, "Ancient Apparition", MainAttribute.INTELLIGENCE, 30, 30, "Ice Blast");
  }

  @Test
  void getAllHeroes_ShouldReturnHeroList() {
    when(heroRepository.findAll()).thenReturn(Arrays.asList(hero));

    List<Hero> result = heroService.getAllHeroes();

    assertEquals(1, result.size());
    assertEquals("Ancient Apparition", result.get(0).getName());
  }

  @Test
  void getAllHeroes_ShouldReturnEmptyList_WhenNoHeroes() {
    when(heroRepository.findAll()).thenReturn(Collections.emptyList());

    List<Hero> result = heroService.getAllHeroes();

    assertTrue(result.isEmpty());
  }

  @Test
  void getHeroById_ShouldReturnHero_WhenExists() {
    when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));

    Hero result = heroService.getHeroById(1L);

    assertEquals(hero, result);
  }

  @Test
  void getHeroById_ShouldThrowException_WhenNotFound() {
    when(heroRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> heroService.getHeroById(1L));

    assertEquals(404, exception.getStatusCode().value());
    assertTrue(exception.getReason().contains("1"));
  }
}
