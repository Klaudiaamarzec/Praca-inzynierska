package com.example.genealogy.service;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;

import java.util.List;

public interface FamilyService {

    boolean existsById(Long id);

    boolean familyExists(Family family);

    Family getFamilyById(Long id);

    boolean saveFamily(Family family);

    boolean updateFamily(Family family);

    boolean deleteFamily(Family family);

    List<Family> getAllFamilies();

    List<Family> findFamiliesByChild(Person child);

    List<Family> findFamiliesByMother(Person mother);

    List<Family> findFamiliesByFather(Person father);

    List<Family> findFamiliesByParent(Person parent);
}
