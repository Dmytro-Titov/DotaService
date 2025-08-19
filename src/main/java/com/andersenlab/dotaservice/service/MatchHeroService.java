package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.repository.CurrentMatchRepository;
import com.andersenlab.dotaservice.repository.HeroRepository;
import com.andersenlab.dotaservice.repository.ItemRepository;
import com.andersenlab.dotaservice.repository.MatchHeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MatchHeroService {

  private final MatchHeroRepository matchHeroRepository;
  private final CurrentMatchRepository currentMatchRepository;
  private final HeroRepository heroRepository;
  private final ItemRepository itemRepository;

  public MatchHero addHeroToMatch(Long matchId, Long heroId) {
    CurrentMatch match = currentMatchRepository.findById(matchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match not found with id: " + matchId));
    Hero hero = heroRepository.findById(heroId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hero not found with id: " + heroId));

    if (match.getMatchHeroes().size() >= 6) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A match cannot have more than 6 heroes");
    }

    MatchHero matchHero = new MatchHero();
    matchHero.setHero(hero);
    matchHero.setCurrentMatch(match);

    return matchHeroRepository.save(matchHero);
  }

  public boolean removeHeroFromMatch(Long matchHeroId) {
    if (matchHeroRepository.existsById(matchHeroId)) {
      matchHeroRepository.deleteById(matchHeroId);
      return true;
    }
    return false;
  }

  public MatchHero addItemToHero(Long matchHeroId, Long itemId) {
    MatchHero matchHero = matchHeroRepository.findById(matchHeroId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "MatchHero not found with id: " + matchHeroId));
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Item not found with id: " + itemId));

    matchHero.addItem(item);
    return matchHeroRepository.save(matchHero);
  }

  public MatchHero removeItemFromHero(Long matchHeroId, Long itemId) {
    MatchHero matchHero = matchHeroRepository.findById(matchHeroId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "MatchHero not found with id: " + matchHeroId));
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found with id: " + itemId));

    matchHero.removeItem(item);
    return matchHeroRepository.save(matchHero);
  }

  public List<MatchHero> getHeroesInMatch(Long matchId) {
    CurrentMatch match = currentMatchRepository.findById(matchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match not found with id: " + matchId));
    return match.getMatchHeroes();
  }
}
