package com.example.genealogy.repository;

import com.example.genealogy.model.LocalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalAddressRepository extends JpaRepository<LocalAddress, Long>{

}
