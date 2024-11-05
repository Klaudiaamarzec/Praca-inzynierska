package com.example.genealogy.repository;

import com.example.genealogy.model.URLs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface URLsRepository extends JpaRepository<URLs, Long>{

    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM urls u
        WHERE (u.url = :url)
        AND (lower(u.comment) = lower(:comment) OR :comment IS NULL)
    """, nativeQuery = true)
    boolean existsURL(@Param("url") String url,
                      @Param("comment") String comment);
}
