package com.example.genealogy.repository;

import com.example.genealogy.model.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {
    // Tutaj możesz dodać dodatkowe metody wyszukiwania, jeśli są potrzebne
}
