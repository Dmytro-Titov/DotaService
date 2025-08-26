package com.andersenlab.dotaservice.facade;

import com.andersenlab.dotaservice.converter.DotaConverter;
import com.andersenlab.dotaservice.dto.CurrentMatchDto;
import com.andersenlab.dotaservice.dto.MatchHeroDto;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.service.MatchHeroService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentMatchFacade {

  private final MatchHeroService matchHeroService;
  private final DotaConverter dotaConverter;

  public CurrentMatchDto createMatch() {
    return dotaConverter.convert(matchHeroService.createMatch());
  }

  public CurrentMatchDto addSixHeroesToMatch(Long matchId, List<MatchHeroDto> matchHeroDtos) {
    List<MatchHero> convertedMatchHeroes = matchHeroDtos.stream()
        .map(dotaConverter::convert)
        .collect(Collectors.toList());
    CurrentMatch updatedMatch = matchHeroService.addSixHeroesToMatch(convertedMatchHeroes, matchId);
    return dotaConverter.convert(updatedMatch);
  }

  public CurrentMatchDto getHeroesInMatch(Long matchId) {
    CurrentMatch currentMatch = matchHeroService.getMatchById(matchId);
    return dotaConverter.convert(currentMatch);
  }

  public CurrentMatchDto updateHeroesInMatch(Long matchId, List<MatchHeroDto> matchHeroDtos) {
    List<MatchHero> convertedMatchHeroes = matchHeroDtos.stream()
        .map(dotaConverter::convert)
        .collect(Collectors.toList());
    CurrentMatch updatedMatch = matchHeroService.updateHeroesInMatch(convertedMatchHeroes, matchId);
    return dotaConverter.convert(updatedMatch);
  }

  public List<CurrentMatchDto> getAllCurrentMatches() {
    List<CurrentMatch> convertedMatchHeroes = matchHeroService.getAllMatches();
    return convertedMatchHeroes.stream().map(dotaConverter::convert).collect(Collectors.toList());
  }

  public void deleteCurrentMatch(Long currentMatchId) {
    matchHeroService.deleteMatch(currentMatchId);
  }

}
