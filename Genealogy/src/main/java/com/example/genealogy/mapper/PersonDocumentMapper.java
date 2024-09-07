package com.example.genealogy.mapper;

import com.example.genealogy.dto.PersonDocumentDTO;
import com.example.genealogy.model.PersonDocument;
import org.springframework.stereotype.Component;

@Component
public class PersonDocumentMapper {

    private final PersonMapper personMapper;
    private final DocumentMapper documentMapper;

    // Constructor-based injection
    public PersonDocumentMapper(PersonMapper personMapper, DocumentMapper documentMapper) {
        this.personMapper = personMapper;
        this.documentMapper = documentMapper;
    }

    // Mapping from PersonDocument to PersonDocumentDTO
    public PersonDocumentDTO mapToDTO(PersonDocument personDocument) {
        if (personDocument == null) {
            return null;
        }

        PersonDocumentDTO dto = new PersonDocumentDTO();
        dto.setId(personDocument.getId());
        dto.setPerson(personMapper.mapToDTO(personDocument.getPerson()));
        dto.setDocument(documentMapper.mapToDTO(personDocument.getDocument()));
        dto.setComment(personDocument.getComment());
        dto.setX(personDocument.getX());
        dto.setY(personDocument.getY());

        return dto;
    }

    // Mapping from PersonDocumentDTO to PersonDocument
    public PersonDocument mapToEntity(PersonDocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        PersonDocument personDocument = new PersonDocument();
        personDocument.setId(dto.getId());
        personDocument.setPerson(personMapper.mapToEntity(dto.getPerson()));
        personDocument.setDocument(documentMapper.mapToEntity(dto.getDocument()));
        personDocument.setComment(dto.getComment());
        personDocument.setX(dto.getX());
        personDocument.setY(dto.getY());

        return personDocument;
    }
}
