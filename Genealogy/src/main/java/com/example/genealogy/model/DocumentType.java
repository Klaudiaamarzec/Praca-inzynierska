package com.example.genealogy.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;

@Entity
@Table(name="documenttype")
@Data
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="typename", length = 64)
    @NotBlank(message = "Nazwa typu nie może być pusta")
    @Size(max = 64, message = "Nazwa typu nie może być dłuższa niż 64 znaki")
    private String typeName;

    @Column(name = "template", columnDefinition = "TEXT")
    @NotBlank(message = "Szablon nie może być pusty")
    private String template;

    @OneToMany(mappedBy = "type")
    private Set<Document> documents;

    @Transient
    private JsonNode parsedTemplate;

    @PostLoad
    public void postLoad() {
        if (template != null && !template.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                parsedTemplate = mapper.readTree(template);
            } catch (Exception e) {
                System.err.println("Error parsing template JSON: " + e.getMessage());
            }
        }
    }
}
