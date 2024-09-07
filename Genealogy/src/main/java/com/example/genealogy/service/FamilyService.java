package com.example.genealogy.service;

import com.example.genealogy.dto.FamilyDTO;
import com.example.genealogy.dto.PersonDTO;
import com.example.genealogy.model.Family;

import java.util.List;

public interface FamilyService {

    // Add or update a date
    void saveFamily(FamilyDTO familyDTO);

    boolean deleteFamily(FamilyDTO familyDTO);

    List<Family> findFamiliesByChild(PersonDTO child);

    List<Family> findFamiliesByMother(PersonDTO mother);

    List<Family> findFamiliesByFather(PersonDTO father);

    List<Family> findFamiliesByParent(PersonDTO parent);
}
