package com.andersenlab.dotaservice.repository;

import com.andersenlab.dotaservice.model.match.CurrentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentMatchRepository extends JpaRepository<CurrentMatch, Long> {
}
