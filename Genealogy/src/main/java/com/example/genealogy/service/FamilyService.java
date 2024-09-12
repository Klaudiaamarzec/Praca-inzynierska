package com.example.genealogy.service;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;

import java.util.List;

public interface FamilyService {

    // Add or update a date
    boolean saveFamily(Family family);

    boolean updateFamily(Family family);

    boolean existsById(long id);

    List<Family> getAllFamilies();

    boolean deleteFamily(Family family);

    List<Family> findFamiliesByChild(Person child);

    List<Family> findFamiliesByMother(Person mother);

    List<Family> findFamiliesByFather(Person father);

    List<Family> findFamiliesByParent(Person parent);
}
