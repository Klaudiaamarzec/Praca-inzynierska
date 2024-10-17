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
    Date findDate(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    @Query("SELECT d FROM Date d WHERE " +
            "(d.day = :day AND d.month = :month AND d.year = :year) OR " +
            "(d.day = :day AND d.year = :year) OR " +
            "(d.month = :month AND d.year = :year) OR " +
            "(d.year = :year) " +
            "ORDER BY " +
            "CASE " +
            "    WHEN (d.day = :day AND d.month = :month AND d.year = :year) THEN 1 " +
            "    WHEN (d.day = :day AND d.year = :year) THEN 2 " +
            "    WHEN (d.month = :month AND d.year = :year) THEN 3 " +
            "    WHEN (d.year = :year) THEN 4 " +
            "    ELSE 5 " +
            "END")
    List<Date> findDates(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Date d WHERE d.day = :day AND d.month = :month AND d.year = :year")
    boolean exist(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    // Return all dates based on the date range
    @Query("""
    SELECT d FROM Date d
    WHERE
        (d.year > :fromYear AND d.year < :toYear)
        OR
        (d.year = :fromYear AND d.month > :fromMonth)
        OR
        (d.year = :toYear AND d.month < :toMonth)
        OR
        (d.year = :fromYear AND d.month = :fromMonth AND d.day >= :fromDay)
        OR
        (d.year = :toYear AND d.month = :toMonth AND d.day <= :toDay)
    """)
    List<Date> findDatesByDateRange(
            @Param("fromYear") int fromYear,
            @Param("fromMonth") int fromMonth,
            @Param("fromDay") int fromDay,
            @Param("toYear") int toYear,
            @Param("toMonth") int toMonth,
            @Param("toDay") int toDay
    );

}
