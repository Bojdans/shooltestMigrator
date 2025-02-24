package org.example.shooltest.repos;

import org.example.shooltest.Entities.NewRubric;
import org.example.shooltest.Entities.OldRubric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewRubricRepository extends JpaRepository<NewRubric, Long> {
    NewRubric findByNameContaining(String name);
}
