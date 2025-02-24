package org.example.shooltest.repos;

import org.example.shooltest.Entities.NewRubric;
import org.example.shooltest.Entities.OldRubric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldRubricRepository extends JpaRepository<OldRubric, Long> {
    OldRubric findByName(String name);
}