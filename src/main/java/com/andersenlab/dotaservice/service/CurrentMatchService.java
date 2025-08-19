package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.repository.CurrentMatchRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentMatchService {

  private final CurrentMatchRepository currentMatchRepository;

  public CurrentMatch createMatch() {
    CurrentMatch match = new CurrentMatch();
    return currentMatchRepository.save(match);
  }

  public List<CurrentMatch> getAllMatches() {
    return currentMatchRepository.findAll();
  }

  public Optional<CurrentMatch> getMatchById(Long id) {
    return currentMatchRepository.findById(id);
  }

  public boolean deleteMatch(Long matchId) {
    if (currentMatchRepository.existsById(matchId)) {
      currentMatchRepository.deleteById(matchId);
      return true;
    }
    return false;
  }
}
