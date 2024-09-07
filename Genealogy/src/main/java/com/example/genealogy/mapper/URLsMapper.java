package com.example.genealogy.mapper;

import com.example.genealogy.dto.URLsDTO;
import com.example.genealogy.model.URLs;
import org.springframework.stereotype.Component;

@Component
public class URLsMapper {

    private final LocationMapper locationMapper;

    // Constructor-based injection
    public URLsMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    // Mapping from URLs to URLsDTO
    public URLsDTO mapToDTO(URLs urls) {
        if (urls == null) {
            return null;
        }

        URLsDTO dto = new URLsDTO();
        dto.setId(urls.getId());
        dto.setUrlID(locationMapper.mapToDTO(urls.getUrlID()));
        dto.setUrl(urls.getUrl());
        dto.setComment(urls.getComment());

        return dto;
    }

    // Mapping from URLsDTO to URLs
    public URLs mapToEntity(URLsDTO dto) {
        if (dto == null) {
            return null;
        }

        URLs urls = new URLs();
        urls.setId(dto.getId());
        urls.setUrlID(locationMapper.mapToEntity(dto.getUrlID()));
        urls.setUrl(dto.getUrl());
        urls.setComment(dto.getComment());

        return urls;
    }
}
