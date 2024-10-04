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
        AND (:comment IS NULL OR lower(u.comment) LIKE lower(CONCAT('%', :comment, '%')))
        AND (u.idurl = :idurl)
    """, nativeQuery = true)
    boolean existsURL(@Param("idurl") Long idurl,
                      @Param("url") String url,
                      @Param("comment") String comment);
}
