package org.example.shooltest.repos;

import org.example.shooltest.Entities.ShooltestTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShooltestTestRepository extends JpaRepository<ShooltestTest, Long> {
}
