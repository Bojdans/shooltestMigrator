package org.example.shooltest.repos;

import org.example.shooltest.Entities.OldEntityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldEntityPostRepository extends JpaRepository<OldEntityPost, Long> {
    @Query("SELECT e FROM OldEntityPost e " +
            "WHERE CAST(e.postTitle AS string) LIKE CONCAT('%', :text, '%') "
    )
    List<OldEntityPost> findAllByPostTitle(@Param("text") String text);
}
