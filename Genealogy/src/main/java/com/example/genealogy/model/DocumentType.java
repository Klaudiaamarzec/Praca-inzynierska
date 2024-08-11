package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;

@Entity
@Table(name="DocumentType")
@Data
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="typename")
    private String typeName;

    //@Column(name="template", columnDefinition = "TEXT")
    //private String template;

    @Lob
    @Column(name = "template", columnDefinition = "TEXT")
    private String template;

    @OneToMany(mappedBy = "type")
    private Set<Document> documents;

    @Transient
    private JsonNode parsedTemplate;

    @PostLoad
    public void postLoad() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            parsedTemplate = mapper.readTree(template);
        } catch (Exception e) {
            System.err.println("Error parsing template JSON: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
