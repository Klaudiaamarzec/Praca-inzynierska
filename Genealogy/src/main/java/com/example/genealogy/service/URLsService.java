package com.example.genealogy.service;

import com.example.genealogy.model.URLs;

import java.util.List;

public interface URLsService {

    boolean saveURL(URLs url);

    boolean updateURL(URLs url);

    boolean existsById(long id);

    boolean deleteURL(URLs url);

    List<URLs> getAllURLs();
}
