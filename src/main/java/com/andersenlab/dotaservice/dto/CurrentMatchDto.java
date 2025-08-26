package com.andersenlab.dotaservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentMatchDto {

  private Long id;
  private List<MatchHeroDto> heroes = new ArrayList<>();
}
