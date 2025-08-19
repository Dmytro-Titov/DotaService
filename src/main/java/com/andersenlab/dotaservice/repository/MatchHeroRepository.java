package com.andersenlab.dotaservice.repository;

import com.andersenlab.dotaservice.model.match.MatchHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchHeroRepository extends JpaRepository<MatchHero, Long> {
}
