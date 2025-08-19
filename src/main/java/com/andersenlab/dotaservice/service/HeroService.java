package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.repository.HeroRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroService {
  private final HeroRepository heroRepository;

  public List<Hero> getAllHeroes() {
    return heroRepository.findAll();
  }
}
