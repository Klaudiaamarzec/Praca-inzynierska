package com.example.genealogy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "urls")
@Data
public class URLs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "idurl")
    @NotNull(message = "URL id nie może być puste")
    private Location urlID;

    @Column(name = "url", length = 256)
    @NotNull(message = "URL nie może być pusta")
    private String url;

    @Column(name = "comment", columnDefinition = "text")
    @Size(max = 256, message = "Komentarz nie może mieć więcej niż 256 znaków")
    private String comment;
}
