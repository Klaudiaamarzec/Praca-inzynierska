package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column(name="typename", length = 64)
    @NotBlank(message = "Nazwa typu nie może być pusta")
    @Size(max = 64, message = "Nazwa typu nie może być dłuższa niż 64 znaki")
    private String typeName;

    @Column(name = "template", columnDefinition = "TEXT")
    private String template;

    @OneToMany(mappedBy = "type")
    @JsonIgnore
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
