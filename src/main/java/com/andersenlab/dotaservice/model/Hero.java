package com.andersenlab.dotaservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "heroes")
public class Hero {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  @Enumerated(EnumType.STRING)
  private MainAttribute mainAttribute;
  private int baseMana;
  private int baseHealth;
  private String ultimateAbilityName;

  public Hero(long id) {
    this.id = id;
  }
}
