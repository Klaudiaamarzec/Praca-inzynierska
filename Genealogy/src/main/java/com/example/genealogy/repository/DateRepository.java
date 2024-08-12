package com.example.genealogy.repository;

import com.example.genealogy.model.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {

    @Query("SELECT d FROM Date d WHERE d.day = :day AND d.month = :month AND d.year = :year")
    List<Date> findDate(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Date d WHERE d.day = :day AND d.month = :month AND d.year = :year")
    boolean exist(@Param("day") int day, @Param("month") int month, @Param("year") int year);

}
