package com.andersenlab.dotaservice.converter;

import com.andersenlab.dotaservice.dto.CurrentMatchDto;
import com.andersenlab.dotaservice.dto.ItemDto;
import com.andersenlab.dotaservice.dto.MatchHeroDto;
import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.model.match.CurrentMatch;
import com.andersenlab.dotaservice.model.match.MatchHero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DotaConverter {

  public CurrentMatchDto convert(CurrentMatch currentMatch) {
    List<MatchHeroDto> heroes = currentMatch.getMatchHeroes().stream().map(this::convert).collect(Collectors.toList());
    return CurrentMatchDto.builder().id(currentMatch.getId()).heroes(heroes).build();
  }

  public MatchHero convert(MatchHeroDto matchHeroDto) {
    List<Item> items = !CollectionUtils.isEmpty(matchHeroDto.getItems())
        ? matchHeroDto.getItems().stream().map(this::convert).collect(Collectors.toList())
        : new ArrayList<>();
    return new MatchHero(new Hero(matchHeroDto.getId()), items);
  }

  private MatchHeroDto convert(MatchHero matchHero) {
    MatchHeroDto matchHeroDto = MatchHeroDto.builder()
        .id(matchHero.getHero().getId())
        .name(matchHero.getHero().getName())
        .mainAttribute(matchHero.getHero().getMainAttribute())
        .ultimateAbilityName(matchHero.getHero().getUltimateAbilityName())
        .baseMana(matchHero.getTotalIntelligence())
        .baseHealth(matchHero.getTotalStrength())
        .build();
    List<ItemDto> items = matchHero.getItems().stream().map(this::convert).collect(Collectors.toList());
    matchHeroDto.setItems(items);
    return matchHeroDto;
  }

  private Item convert(ItemDto itemDto) {
    return new Item(itemDto.getId());
  }

  private ItemDto convert(Item item) {
    return ItemDto.builder()
        .id(item.getId())
        .name(item.getName())
        .bonusStrength(item.getBonusStrength())
        .bonusIntelligence(item.getBonusIntelligence())
        .bonusAgility(item.getBonusAgility())
        .description(item.getDescription())
        .build();
  }

}
