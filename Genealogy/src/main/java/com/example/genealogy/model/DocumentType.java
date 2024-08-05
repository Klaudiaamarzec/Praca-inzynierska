package com.example.genealogy.model;

import lombok.Data;
import jakarta.persistence.*;

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

    @Column(name="template", columnDefinition = "TEXT")
    private String template;

    @OneToMany(mappedBy = "DocumentType")
    private Set<Document> documents;
}
