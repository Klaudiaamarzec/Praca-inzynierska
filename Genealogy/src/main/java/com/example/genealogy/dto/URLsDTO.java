package com.example.genealogy.dto;

import lombok.Data;

@Data
public class URLsDTO {

    private long id;
    private LocationDTO urlID;
    private String url;
    private String comment;
}
