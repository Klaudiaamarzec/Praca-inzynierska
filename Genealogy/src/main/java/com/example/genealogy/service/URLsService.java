package com.example.genealogy.service;

import com.example.genealogy.model.URLs;

import java.util.List;

public interface URLsService {

    boolean saveURL(URLs url);

    boolean updateURL(URLs url);

    boolean existsById(URLs url);

    boolean urlExist(URLs url);

    boolean deleteURL(URLs url);

    List<URLs> getAllURLs();
}
