package com.example.genealogy.model;

import com.example.genealogy.validator.OnCreate;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "urls")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class URLs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idurl")
    @NotNull(message = "URL id nie może być puste", groups = OnCreate.class)
    //@JsonManagedReference("url-location")
    private Location urlID;

    @Column(name = "url", length = 256)
    @NotBlank(message = "URL nie może być pusty")
    private String url;

    @Column(name = "comment", columnDefinition = "text")
    @Size(max = 256, message = "Komentarz nie może mieć więcej niż 256 znaków")
    private String comment;
}
