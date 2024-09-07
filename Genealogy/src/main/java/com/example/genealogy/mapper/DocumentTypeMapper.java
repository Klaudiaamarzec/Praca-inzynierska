package com.example.genealogy.mapper;

import com.example.genealogy.dto.DocumentTypeDTO;
import com.example.genealogy.model.DocumentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DocumentTypeMapper {

    private final DocumentMapper documentMapper;

    // Constructor-based injection for dependent mappers
    public DocumentTypeMapper(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    // Mapping from DocumentType to DocumentTypeDTO
    public DocumentTypeDTO mapToDTO(DocumentType documentType) {
        if (documentType == null) {
            return null;
        }

        DocumentTypeDTO dto = new DocumentTypeDTO();
        dto.setId(documentType.getId());
        dto.setTypeName(documentType.getTypeName());
        dto.setTemplate(documentType.getTemplate());

        // Copy the parsed template directly (already processed by @PostLoad)
        dto.setParsedTemplate(documentType.getParsedTemplate());

        // Map related documents using DocumentMapper
        if (documentType.getDocuments() != null) {
            dto.setDocuments(documentType.getDocuments().stream()
                    .map(documentMapper::mapToDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Mapping from DocumentTypeDTO to DocumentType
    public DocumentType mapToEntity(DocumentTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        DocumentType documentType = new DocumentType();
        documentType.setId(dto.getId());
        documentType.setTypeName(dto.getTypeName());
        documentType.setTemplate(dto.getTemplate());

        // Parse template string back to parsedTemplate (JSON)
        if (dto.getTemplate() != null && !dto.getTemplate().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                documentType.setParsedTemplate(mapper.readTree(dto.getTemplate()));
            } catch (Exception e) {
                System.err.println("Error parsing template JSON: " + e.getMessage());
            }
        }

        // Map related documents using DocumentMapper
        if (dto.getDocuments() != null) {
            documentType.setDocuments(dto.getDocuments().stream()
                    .map(documentMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return documentType;
    }
}
