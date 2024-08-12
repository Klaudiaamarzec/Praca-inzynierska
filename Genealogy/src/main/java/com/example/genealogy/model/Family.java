package com.example.genealogy.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "family")
@Data
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Person child;

    @ManyToOne
    @JoinColumn(name = "fid")
    private Person father;

    @ManyToOne
    @JoinColumn(name = "mid")
    private Person mother;
}
