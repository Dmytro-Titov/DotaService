package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.model.MainAttribute;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.repository.CurrentMatchRepository;
import com.andersenlab.dotaservice.repository.MatchHeroRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchHeroServiceTest {


  @Mock
  private MatchHeroRepository matchHeroRepository;

  @Mock
  private CurrentMatchRepository currentMatchRepository;

  @Mock
  private HeroService heroService;

  @Mock
  private ItemService itemService;

  @InjectMocks
  private MatchHeroService matchHeroService;

  private CurrentMatch match;
  private Hero hero;
  private Item item;
  private MatchHero matchHero;

  @BeforeEach
  void setUp() {
    hero =new Hero(1L, "Axe", MainAttribute.STRENGTH, 1009, 286, "Knock");
    item = new Item(1L, "Blink Dagger", 2250, 30, 100, "Active: Blink");
    match = new CurrentMatch();
    matchHero = new MatchHero(hero, Collections.singletonList(item));
  }

  @Test
  void createMatch_ShouldReturnSavedMatch() {
    when(currentMatchRepository.save(Mockito.any(CurrentMatch.class))).thenReturn(match);

    CurrentMatch result = matchHeroService.createMatch();

    assertEquals(match, result);
    verify(currentMatchRepository).save(Mockito.any(CurrentMatch.class));
  }

  @Test
  void getAllMatches_ShouldReturnAllMatches() {
    CurrentMatch match1 = new CurrentMatch();
    CurrentMatch match2 = new CurrentMatch();
    List<CurrentMatch> matches = Arrays.asList(match1, match2);

    when(currentMatchRepository.findAll()).thenReturn(matches);

    List<CurrentMatch> result = matchHeroService.getAllMatches();

    assertEquals(2, result.size());
    assertTrue(result.contains(match1));
    assertTrue(result.contains(match2));

    verify(currentMatchRepository).findAll();
  }

  @Test
  void getAllMatches_ShouldReturnEmptyList_WhenNoMatches() {
    when(currentMatchRepository.findAll()).thenReturn(Collections.emptyList());

    List<CurrentMatch> result = matchHeroService.getAllMatches();

    assertTrue(result.isEmpty());
    verify(currentMatchRepository).findAll();
  }

  @Test
  void getMatchById_ShouldReturnMatch_WhenExists() {
    when(currentMatchRepository.findById(1L)).thenReturn(Optional.of(match));

    CurrentMatch result = matchHeroService.getMatchById(1L);

    assertEquals(match, result);
  }

  @Test
  void getMatchById_ShouldThrowException_WhenNotFound() {
    when(currentMatchRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> matchHeroService.getMatchById(1L));

    assertEquals(404, exception.getStatusCode().value());
  }

  @Test
  void addSixHeroesToMatch_ShouldThrowException_WhenNotSixHeroes() {
    List<MatchHero> heroes = Collections.singletonList(matchHero);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> matchHeroService.addSixHeroesToMatch(heroes, 1L));

    assertEquals(400, exception.getStatusCode().value());
  }

  @Test
  void addSixHeroesToMatch_ShouldAddHeroes_WhenValid() {
    List<MatchHero> heroes = IntStream.range(0, 6)
        .mapToObj(i -> {
          Hero h = new Hero((long) i, "Hero" + i, MainAttribute.STRENGTH, 10, 20, "Ulta");
          MatchHero mh = new MatchHero(h, Collections.singletonList(item));
          return mh;
        }).collect(Collectors.toList());

    when(currentMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));
    when(heroService.getHeroById(anyLong())).thenAnswer(
        i -> heroes.get(i.getArgument(0, Long.class).intValue()).getHero());
    when(itemService.getItemById(anyLong())).thenReturn(item);
    when(matchHeroRepository.save(Mockito.any(MatchHero.class))).thenAnswer(i -> i.getArgument(0));

    CurrentMatch result = matchHeroService.addSixHeroesToMatch(heroes, 1L);

    assertEquals(6, result.getMatchHeroes().size());
  }

  @Test
  void updateHeroesInMatch_ShouldUpdateItemsAndHeroes() {
    List<MatchHero> existingHeroes = IntStream.range(0, 6)
        .mapToObj(i -> {
          Hero h = new Hero(i, "Hero" + i, MainAttribute.STRENGTH, 100, 50, "Skill" + i);
          Item it = new Item(i, "Item" + i, 100, 0, 0, "Desc");
          MatchHero mh = new MatchHero(h, Collections.singletonList(it));
          return mh;
        })
        .collect(Collectors.toList());
    match.setMatchHeroes(existingHeroes);

    when(currentMatchRepository.findById(anyLong())).thenReturn(Optional.of(match));

    CurrentMatch result = matchHeroService.updateHeroesInMatch(existingHeroes, 1L);
    assertEquals(6, result.getMatchHeroes().size());
    for (int i = 0; i < 6; i++) {
      MatchHero mh = result.getMatchHeroes().get(i);
      assertEquals("Hero" + i, mh.getHero().getName());
      assertEquals(1, mh.getItems().size());
    }
  }

  @Test
  void updateHeroesInMatch_ShouldThrowException_WhenMoreThanSixHeroes() {
    List<MatchHero> heroes = IntStream.range(0, 7)
        .mapToObj(i -> {
          MatchHero mh = new MatchHero();
          mh.setHero(new Hero((long) i, "Hero" + i, null, 0, 0, null));
          mh.setItems(Collections.emptyList());
          return mh;
        }).collect(Collectors.toList());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> matchHeroService.updateHeroesInMatch(heroes, 1L));

    assertEquals(400, exception.getStatusCode().value());
  }

  @Test
  void deleteMatch_ShouldDelete_WhenExists() {
    when(currentMatchRepository.existsById(1L)).thenReturn(true);

    matchHeroService.deleteMatch(1L);

    verify(currentMatchRepository).deleteById(1L);
  }

  @Test
  void deleteMatch_ShouldThrowException_WhenNotExists() {
    when(currentMatchRepository.existsById(1L)).thenReturn(false);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> matchHeroService.deleteMatch(1L));

    assertEquals(404, exception.getStatusCode().value());
    assertTrue(exception.getReason().contains("1"));

    verify(currentMatchRepository, never()).deleteById(1L);
  }

}
