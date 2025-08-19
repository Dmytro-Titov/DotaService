package com.andersenlab.dotaservice.controller;

import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.service.CurrentMatchService;
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
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class CurrentMatchController {

  private final CurrentMatchService currentMatchService;

  @PostMapping
  public ResponseEntity<CurrentMatch> createMatch() {
    return ResponseEntity.status(HttpStatus.CREATED).body(currentMatchService.createMatch());
  }

  @GetMapping
  public ResponseEntity<List<CurrentMatch>> getAllMatches() {
    return ResponseEntity.ok(currentMatchService.getAllMatches());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CurrentMatch> getMatchById(@PathVariable Long id) {
    return currentMatchService.getMatchById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
    boolean deleted = currentMatchService.deleteMatch(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
