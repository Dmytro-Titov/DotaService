package com.andersenlab.dotaservice.controller;

import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.service.MatchHeroService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match-heroes")
@RequiredArgsConstructor
public class MatchHeroController {

  private final MatchHeroService matchHeroService;

  @PostMapping("/{matchId}/heroes/{heroId}")
  public ResponseEntity<MatchHero> addHeroToMatch(@PathVariable Long matchId, @PathVariable Long heroId) {
    MatchHero createdHero = matchHeroService.addHeroToMatch(matchId, heroId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdHero);
  }

  @GetMapping("/{matchId}/heroes")
  public ResponseEntity<List<MatchHero>> getHeroesInMatch(@PathVariable Long matchId) {
    return ResponseEntity.ok(matchHeroService.getHeroesInMatch(matchId));
  }

  @PostMapping("/{matchHeroId}/items/{itemId}")
  public ResponseEntity<MatchHero> addItemToHero(@PathVariable Long matchHeroId, @PathVariable Long itemId) {
    return ResponseEntity.ok(matchHeroService.addItemToHero(matchHeroId, itemId));
  }

  @DeleteMapping("/{matchHeroId}/items/{itemId}")
  public ResponseEntity<MatchHero> removeItemFromHero(@PathVariable Long matchHeroId, @PathVariable Long itemId) {
    return ResponseEntity.ok(matchHeroService.removeItemFromHero(matchHeroId, itemId));
  }

  @DeleteMapping("/{matchHeroId}")
  public ResponseEntity<Void> removeHeroFromMatch(@PathVariable Long matchHeroId) {
    boolean deleted = matchHeroService.removeHeroFromMatch(matchHeroId);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
