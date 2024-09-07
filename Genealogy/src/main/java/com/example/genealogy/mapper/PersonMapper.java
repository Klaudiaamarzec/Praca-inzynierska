package com.example.genealogy.mapper;

import com.example.genealogy.dto.PersonDTO;
import com.example.genealogy.model.Person;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    private final PersonDocumentMapper personDocumentMapper;
    private final FamilyMapper familyMapper;

    // Constructor-based injection
    public PersonMapper(PersonDocumentMapper personDocumentMapper, FamilyMapper familyMapper) {
        this.personDocumentMapper = personDocumentMapper;
        this.familyMapper = familyMapper;
    }

    // Mapping from Person to PersonDTO
    public PersonDTO mapToDTO(Person person) {
        if (person == null) {
            return null;
        }

        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setRin(person.getRin());
        dto.setBirthDate(person.getBirthDate());

        // Map person documents
        if (person.getPersonDocuments() != null) {
            dto.setPersonDocuments(person.getPersonDocuments().stream()
                    .map(personDocumentMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        // Map family relations
        dto.setChildFamily(familyMapper.mapToDTO(person.getChildFamily()));
        dto.setFatherFamilies(person.getFatherFamilies().stream()
                .map(familyMapper::mapToDTO)
                .collect(Collectors.toSet()));
        dto.setMotherFamilies(person.getMotherFamilies().stream()
                .map(familyMapper::mapToDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    // Mapping from PersonDTO to Person
    public Person mapToEntity(PersonDTO dto) {
        if (dto == null) {
            return null;
        }

        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setRin(dto.getRin());
        person.setBirthDate(dto.getBirthDate());

        // Map person documents
        if (dto.getPersonDocuments() != null) {
            person.setPersonDocuments(dto.getPersonDocuments().stream()
                    .map(personDocumentMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        // Map family relations
        person.setChildFamily(familyMapper.mapToEntity(dto.getChildFamily()));
        person.setFatherFamilies(dto.getFatherFamilies().stream()
                .map(familyMapper::mapToEntity)
                .collect(Collectors.toSet()));
        person.setMotherFamilies(dto.getMotherFamilies().stream()
                .map(familyMapper::mapToEntity)
                .collect(Collectors.toSet()));

        return person;
    }
}
