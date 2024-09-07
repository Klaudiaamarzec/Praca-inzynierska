package com.example.genealogy.serviceimplementation;

import com.example.genealogy.dto.FamilyDTO;
import com.example.genealogy.dto.PersonDTO;
import com.example.genealogy.mapper.FamilyMapper;
import com.example.genealogy.mapper.PersonMapper;
import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import com.example.genealogy.repository.FamilyRepository;
import com.example.genealogy.service.FamilyService;

import java.util.List;

public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;

    private final FamilyMapper familyMapper;
    private final PersonMapper personMapper;

    public FamilyServiceImpl(FamilyRepository familyRepository, FamilyMapper familyMapper, PersonMapper personMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
        this.personMapper = personMapper;
    }
    @Override
    public void saveFamily(FamilyDTO familyDTO) {
        Family family = familyMapper.mapToEntity(familyDTO);
        familyRepository.save(family);
    }

    @Override
    public boolean deleteFamily(FamilyDTO familyDTO) {
        if (familyRepository.existsById(familyDTO.getId())) {
            familyRepository.deleteById(familyDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public List<Family> findFamiliesByChild(PersonDTO childDTO) {
        Person child = personMapper.mapToEntity(childDTO);
        return familyRepository.findFamiliesByChild(child);
    }

    @Override
    public List<Family> findFamiliesByMother(PersonDTO motherDTO) {
        Person mother = personMapper.mapToEntity(motherDTO);
        return familyRepository.findFamiliesByMother(mother);
    }

    @Override
    public List<Family> findFamiliesByFather(PersonDTO fatherDTO) {
        Person father = personMapper.mapToEntity(fatherDTO);
        return familyRepository.findFamiliesByFather(father);
    }

    @Override
    public List<Family> findFamiliesByParent(PersonDTO parentDTO) {
        Person parent = personMapper.mapToEntity(parentDTO);
        return familyRepository.findFamiliesByParent(parent);
    }
}
