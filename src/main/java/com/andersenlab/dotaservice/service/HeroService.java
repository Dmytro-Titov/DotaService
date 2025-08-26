package com.andersenlab.dotaservice.service;

import com.andersenlab.dotaservice.model.Hero;
import com.andersenlab.dotaservice.repository.HeroRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.andersenlab.dotaservice.errors.ErrorMessages.HERO_NOT_FOUNT_ERROR;

@Service
@RequiredArgsConstructor
public class HeroService {

  private final HeroRepository heroRepository;

  public List<Hero> getAllHeroes() {
    return heroRepository.findAll();
  }

  public Hero getHeroById(Long id) {
    return heroRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, HERO_NOT_FOUNT_ERROR + id));
  }
}
