package com.andersenlab.dotaservice.repository;

import com.andersenlab.dotaservice.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Long> {

}
