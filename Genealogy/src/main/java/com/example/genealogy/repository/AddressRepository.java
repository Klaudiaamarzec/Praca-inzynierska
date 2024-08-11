package com.example.genealogy.repository;

import com.example.genealogy.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Tutaj możesz dodać dodatkowe metody wyszukiwania, jeśli są potrzebne
}
