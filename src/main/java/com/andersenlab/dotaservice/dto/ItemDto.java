package com.andersenlab.dotaservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {

  private long id;
  private String name;
  private int bonusStrength;
  private int bonusAgility;
  private int bonusIntelligence;
  private String description;

}
