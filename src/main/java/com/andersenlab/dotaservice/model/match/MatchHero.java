package com.andersenlab.dotaservice.model.match;


import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.model.Item;
import com.andersenlab.dotaservice.model.MainAttribute;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

  public MatchHero(Hero hero, List<Item> items) {
    this.hero = hero;
    this.items = items;
  }

  public void addItem(Item item) {
    items.add(item);
  }

  public void removeItem(Item item) {
    items.remove(item);
  }


  public int getTotalStrength() {
    int base = hero.getMainAttribute() == MainAttribute.STRENGTH ? hero.getBaseHealth() : 0;
    int bonus = items.stream().mapToInt(Item::getBonusStrength).sum();
    return base + bonus;
  }

  public int getTotalIntelligence() {
    int base = hero.getMainAttribute() == MainAttribute.INTELLIGENCE ? hero.getBaseMana() : 0;
    int bonus = items.stream().mapToInt(Item::getBonusIntelligence).sum();
    return base + bonus;
  }
}
