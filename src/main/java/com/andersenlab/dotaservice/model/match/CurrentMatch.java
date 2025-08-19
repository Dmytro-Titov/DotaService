package com.andersenlab.dotaservice.model.match;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "current_matches")
public class CurrentMatch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "currentMatch", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MatchHero> matchHeroes = new ArrayList<>();

  public void addMatchHero(MatchHero matchHero) {
    matchHero.setCurrentMatch(this);
    matchHeroes.add(matchHero);
  }

  public void removeMatchHero(MatchHero matchHero) {
    matchHeroes.remove(matchHero);
    matchHero.setCurrentMatch(null);
  }
}