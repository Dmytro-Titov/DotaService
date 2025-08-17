package com.andersenlab.dotaservice.model.match;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "match_heroes")
public class MatchHero {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hero_id", nullable = false)
  private Hero hero;

  @ManyToMany
  @JoinTable(
      name = "match_hero_items",
      joinColumns = @JoinColumn(name = "match_hero_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id")
  )
  private List<Item> items = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "current_match_id", nullable = false)
  private CurrentMatch currentMatch;

  public void addItem(Item item) {
    if (items.size() >= 6) {
      throw new IllegalStateException("Cannot add more than 6 items to a hero");
    }
    items.add(item);
  }

  public void removeItem(Item item) {
    items.remove(item);
  }

  public int getTotalStrength() {
    int base = hero.getMainAttribute() == com.andersenlab.dotaservice.model.MainAttribute.STRENGTH
        ? hero.getBaseHealth() : 0;
    int bonus = items.stream().mapToInt(Item::getBonusStrength).sum();
    return base + bonus;
  }

  public int getTotalAgility() {
    int base = hero.getMainAttribute() == com.andersenlab.dotaservice.model.MainAttribute.AGILITY
        ? hero.getBaseHealth() : 0;
    int bonus = items.stream().mapToInt(Item::getBonusAgility).sum();
    return base + bonus;
  }

  public int getTotalIntelligence() {
    int base = hero.getMainAttribute() == com.andersenlab.dotaservice.model.MainAttribute.INTELLIGENCE
        ? hero.getBaseHealth() : 0;
    int bonus = items.stream().mapToInt(Item::getBonusIntelligence).sum();
    return base + bonus;
  }
}
