package com.example.genealogy.service;

import com.example.genealogy.model.URLs;

import java.util.List;

public interface URLsService {

    boolean existsById(Long id);

    boolean urlExists(URLs url);

    URLs getURLById(Long id);

    boolean saveURL(URLs url);

    boolean updateURL(URLs url);

    boolean deleteURL(URLs url);

    List<URLs> getAllURLs();
}
