package com.andersenlab.dotaservice.dto;

import com.andersenlab.dotaservice.model.MainAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchHeroDto {
  private long id;
  private String name;
  private MainAttribute mainAttribute;
  private int baseMana;
  private int baseHealth;
  private String ultimateAbilityName;
  private List<ItemDto> items;

}
