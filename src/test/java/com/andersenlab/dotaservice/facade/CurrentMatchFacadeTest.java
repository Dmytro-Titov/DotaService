package com.andersenlab.dotaservice.facade;

import com.andersenlab.dotaservice.converter.DotaConverter;
import com.andersenlab.dotaservice.dto.CurrentMatchDto;
import com.andersenlab.dotaservice.dto.MatchHeroDto;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import com.andersenlab.dotaservice.service.MatchHeroService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentMatchFacadeTest {

  @Mock
  private MatchHeroService matchHeroService;

  @Mock
  private DotaConverter dotaConverter;

  @InjectMocks
  private CurrentMatchFacade currentMatchFacade;

  private CurrentMatch match;
  private CurrentMatchDto matchDto;
  private MatchHero matchHero;
  private MatchHeroDto matchHeroDto;

  public CurrentMatchFacadeTest() {
  }

  @BeforeEach
  void setUp() {
    match = new CurrentMatch();
    matchDto = CurrentMatchDto.builder().build();
    matchHero = new MatchHero();
    matchHeroDto = MatchHeroDto.builder().build();
  }

  @Test
  void createMatch_ShouldReturnConvertedDto() {
    when(matchHeroService.createMatch()).thenReturn(match);
    when(dotaConverter.convert(match)).thenReturn(matchDto);

    CurrentMatchDto result = currentMatchFacade.createMatch();

    assertEquals(matchDto, result);
    verify(matchHeroService).createMatch();
    verify(dotaConverter).convert(match);
  }

  @Test
  void addSixHeroesToMatch_ShouldConvertAndReturnDto() {
    List<MatchHeroDto> heroDtos = Collections.singletonList(matchHeroDto);
    List<MatchHero> heroes = Collections.singletonList(matchHero);

    when(dotaConverter.convert(matchHeroDto)).thenReturn(matchHero);
    when(matchHeroService.addSixHeroesToMatch(heroes, 1L)).thenReturn(match);
    when(dotaConverter.convert(match)).thenReturn(matchDto);

    CurrentMatchDto result = currentMatchFacade.addSixHeroesToMatch(1L, heroDtos);

    assertEquals(matchDto, result);
    verify(dotaConverter).convert(matchHeroDto);
    verify(matchHeroService).addSixHeroesToMatch(heroes, 1L);
    verify(dotaConverter).convert(match);
  }

  @Test
  void getHeroesInMatch_ShouldReturnConvertedDto() {
    when(matchHeroService.getMatchById(1L)).thenReturn(match);
    when(dotaConverter.convert(match)).thenReturn(matchDto);

    CurrentMatchDto result = currentMatchFacade.getHeroesInMatch(1L);

    assertEquals(matchDto, result);
    verify(matchHeroService).getMatchById(1L);
    verify(dotaConverter).convert(match);
  }

  @Test
  void updateHeroesInMatch_ShouldConvertAndReturnDto() {
    List<MatchHeroDto> heroDtos = Collections.singletonList(matchHeroDto);
    List<MatchHero> heroes = Collections.singletonList(matchHero);

    when(dotaConverter.convert(matchHeroDto)).thenReturn(matchHero);
    when(matchHeroService.updateHeroesInMatch(heroes, 1L)).thenReturn(match);
    when(dotaConverter.convert(match)).thenReturn(matchDto);

    CurrentMatchDto result = currentMatchFacade.updateHeroesInMatch(1L, heroDtos);

    assertEquals(matchDto, result);
    verify(dotaConverter).convert(matchHeroDto);
    verify(matchHeroService).updateHeroesInMatch(heroes, 1L);
    verify(dotaConverter).convert(match);
  }

  @Test
  void getAllCurrentMatches_ShouldReturnConvertedList() {
    List<CurrentMatch> matches = Collections.singletonList(match);
    List<CurrentMatchDto> dtos = Collections.singletonList(matchDto);

    when(matchHeroService.getAllMatches()).thenReturn(matches);
    when(dotaConverter.convert(match)).thenReturn(matchDto);

    List<CurrentMatchDto> result = currentMatchFacade.getAllCurrentMatches();

    assertEquals(dtos, result);
    verify(matchHeroService).getAllMatches();
    verify(dotaConverter).convert(match);
  }

  @Test
  void deleteCurrentMatch_ShouldCallService() {
    doNothing().when(matchHeroService).deleteMatch(1L);

    currentMatchFacade.deleteCurrentMatch(1L);

    verify(matchHeroService).deleteMatch(1L);
  }

}
