package com.example.genealogy.mapper;

import com.example.genealogy.dto.FamilyDTO;
import com.example.genealogy.model.Family;
import org.springframework.stereotype.Component;

@Component
public class FamilyMapper {

    private final PersonMapper personMapper;

    // Constructor-based injection
    public FamilyMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    // Mapping from Family to FamilyDTO
    public FamilyDTO mapToDTO(Family family) {
        if (family == null) {
            return null;
        }

        FamilyDTO dto = new FamilyDTO();
        dto.setId(family.getId());
        dto.setChild(personMapper.mapToDTO(family.getChild()));
        dto.setFather(personMapper.mapToDTO(family.getFather()));
        dto.setMother(personMapper.mapToDTO(family.getMother()));

        return dto;
    }

    // Mapping from FamilyDTO to Family
    public Family mapToEntity(FamilyDTO dto) {
        if (dto == null) {
            return null;
        }

        Family family = new Family();
        family.setId(dto.getId());
        family.setChild(personMapper.mapToEntity(dto.getChild()));
        family.setFather(personMapper.mapToEntity(dto.getFather()));
        family.setMother(personMapper.mapToEntity(dto.getMother()));

        return family;
    }
}
