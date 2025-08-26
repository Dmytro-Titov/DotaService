package com.andersenlab.dotaservice.controller;

import com.andersenlab.dotaservice.dto.CurrentMatchDto;
import com.andersenlab.dotaservice.dto.MatchHeroDto;
import com.andersenlab.dotaservice.facade.CurrentMatchFacade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class CurrentMatchController {

  private final CurrentMatchFacade currentMatchFacade;

  @PostMapping
  public ResponseEntity<CurrentMatchDto> createMatch() {
    return ResponseEntity.status(HttpStatus.CREATED).body(currentMatchFacade.createMatch());
  }

  @GetMapping
  public ResponseEntity<List<CurrentMatchDto>> getAllMatches() {
    return ResponseEntity.ok(currentMatchFacade.getAllCurrentMatches());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CurrentMatchDto> getMatchById(@PathVariable Long id) {
    return ResponseEntity.ok(currentMatchFacade.getHeroesInMatch(id));
  }

  @PatchMapping("/{id}/addHeroes")
  public ResponseEntity<CurrentMatchDto> addSixHeroesToMatch(@PathVariable Long id,
      @RequestBody List<MatchHeroDto> heroes) {
    return ResponseEntity.ok(currentMatchFacade.addSixHeroesToMatch(id, heroes));
  }

  @PatchMapping("/{id}/updateHeroes")
  public ResponseEntity<CurrentMatchDto> updateHeroesInMatch(@PathVariable Long id,
      @RequestBody List<MatchHeroDto> heroes) {
    return ResponseEntity.ok(currentMatchFacade.updateHeroesInMatch(id, heroes));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
    currentMatchFacade.deleteCurrentMatch(id);
    return ResponseEntity.noContent().build();
  }
}
