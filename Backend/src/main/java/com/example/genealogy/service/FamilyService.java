package com.example.genealogy.service;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FamilyService {

    boolean existsById(Long id);

    boolean familyExists(Family family);

    Family getFamilyById(Long id);

    ResponseEntity<String> saveFamily(Family family);

    boolean updateFamily(Family family);

    boolean deleteFamily(Family family);

    List<Family> getAllFamilies();

    Family findFamilyByChild(Person child);

    List<Family> findFamiliesByMother(Person mother);

    List<Family> findFamiliesByFather(Person father);

    List<Family> findFamiliesByParent(Person parent);
}
