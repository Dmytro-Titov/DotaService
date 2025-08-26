package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.repository.CurrentMatchRepository;
import com.andersenlab.dotaservice.repository.MatchHeroRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.andersenlab.dotaservice.errors.ErrorMessages.HEROES_LIMIT_ERROR;
import static com.andersenlab.dotaservice.errors.ErrorMessages.ITEMS_LIMIT_ERROR;
import static com.andersenlab.dotaservice.errors.ErrorMessages.MATCH_NOT_FOUNT_ERROR;

@Service
@RequiredArgsConstructor
public class MatchHeroService {

  private final MatchHeroRepository matchHeroRepository;
  private final CurrentMatchRepository currentMatchRepository;
  private final HeroService heroService;
  private final ItemService itemService;

  public CurrentMatch createMatch() {
    CurrentMatch match = new CurrentMatch();
    return currentMatchRepository.save(match);
  }

  public List<CurrentMatch> getAllMatches() {
    return currentMatchRepository.findAll();
  }

  public CurrentMatch getMatchById(Long id) {
    return currentMatchRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MATCH_NOT_FOUNT_ERROR + id));
  }

  public void deleteMatch(Long matchId) {
    if (currentMatchRepository.existsById(matchId)) {
      currentMatchRepository.deleteById(matchId);
    } else {
      new ResponseStatusException(HttpStatus.NOT_FOUND, MATCH_NOT_FOUNT_ERROR + matchId);
    }
  }

  public CurrentMatch addSixHeroesToMatch(List<MatchHero> heroes, Long matchId) {
    if (heroes.size() != 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HEROES_LIMIT_ERROR);
    }
    CurrentMatch match = getMatchById(matchId);
    List<MatchHero> matchHeroes = heroes.stream().map(hero -> addHeroToMatch(match, hero)).collect(Collectors.toList());
    match.setMatchHeroes(matchHeroes);
    return match;
  }

  public CurrentMatch updateHeroesInMatch(List<MatchHero> heroes, Long matchId) {
    if (heroes.size() != 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HEROES_LIMIT_ERROR);
    }

    CurrentMatch match = getMatchById(matchId);
    match.setMatchHeroes(getUpdatedHeroes(match, heroes));
    return match;
  }

  private MatchHero addHeroToMatch(CurrentMatch match, MatchHero matchHero) {
    Hero hero = heroService.getHeroById(matchHero.getHero().getId());

    if (match.getMatchHeroes().size() > 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HEROES_LIMIT_ERROR);
    }

    matchHero.setHero(hero);
    matchHero.setCurrentMatch(match);

    if (matchHero.getItems().size() > 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ITEMS_LIMIT_ERROR);
    }

    List<Item> items = matchHero.getItems().stream()
        .map(item -> itemService.getItemById(item.getId()))
        .collect(Collectors.toList());
    matchHero.setItems(items);
    return matchHeroRepository.save(matchHero);
  }

  private List<MatchHero> getUpdatedHeroes(CurrentMatch currentMatch, List<MatchHero> updatedHeroes) {
    Map<Long, MatchHero> updatedHeroMap = updatedHeroes.stream()
        .collect(Collectors.toMap(
            matchHero -> matchHero.getHero().getId(),
            matchHero -> matchHero
        ));
    List<MatchHero> currentMatchHeroes = new ArrayList<>();

    currentMatch.getMatchHeroes().forEach(existingHero -> {
      Long heroId = existingHero.getHero().getId();
      MatchHero updatedMatchHero = updatedHeroMap.remove(heroId);
      if (updatedMatchHero != null) {
        existingHero.setItems(getUpdatedExistingHeroItems(existingHero, updatedMatchHero.getItems()));
        currentMatchHeroes.add(existingHero);
      } else {
        removeHeroFromMatch(existingHero.getId());
      }
    });

    if (!updatedHeroMap.isEmpty()) {
      List<MatchHero> addedMatchHeroes = updatedHeroes.stream()
          .filter(matchHero -> updatedHeroMap.containsKey(matchHero.getHero().getId()))
          .map(matchHero -> addHeroToMatch(currentMatch, matchHero))
          .collect(Collectors.toList());
      currentMatchHeroes.addAll(addedMatchHeroes);
    }
    return currentMatchHeroes;
  }

  private List<Item> getUpdatedExistingHeroItems(MatchHero matchHero, List<Item> updatedItems) {
    if (updatedItems.size() > 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ITEMS_LIMIT_ERROR);
    }
    Set<Long> updatedItemIds = updatedItems.stream().map(Item::getId).collect(Collectors.toSet());
    List<Item> currentHeroItems = new ArrayList<>();

    matchHero.getItems().forEach(existingItem -> {
      Long itemId = existingItem.getId();
      if (updatedItemIds.remove(itemId)) {
        currentHeroItems.add(existingItem);
      } else {
        removeItemFromHero(matchHero.getId(), existingItem.getId());
      }
    });

    if (!updatedItemIds.isEmpty()) {
      List<Item> addedMatchHeroes = updatedItems.stream()
          .filter(item -> updatedItemIds.contains(matchHero.getHero().getId()))
          .map(item -> itemService.getItemById(item.getId()))
          .collect(Collectors.toList());
      currentHeroItems.addAll(addedMatchHeroes);
    }
    return currentHeroItems;
  }

  private void removeHeroFromMatch(Long matchHeroId) {
    matchHeroRepository.deleteById(matchHeroId);
  }

  private MatchHero removeItemFromHero(Long matchHeroId, Long itemId) {
    MatchHero matchHero = matchHeroRepository.findById(matchHeroId).get();
    Item item = itemService.getItemById(itemId);
    matchHero.removeItem(item);
    return matchHeroRepository.save(matchHero);
  }
}
