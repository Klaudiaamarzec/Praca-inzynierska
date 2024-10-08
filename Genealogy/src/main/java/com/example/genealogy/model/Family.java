package com.example.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "family")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cid")
    @NotNull(message = "Pole 'Dziecko' nie może być puste")
    private Person child;

    @ManyToOne
    @JoinColumn(name = "fid")
    @NotNull(message = "Pole 'Ojciec' nie może być puste")
    private Person father;

    @ManyToOne
    @JoinColumn(name = "mid")
    @NotNull(message = "Pole 'Matka' nie może być puste")
    private Person mother;
}
