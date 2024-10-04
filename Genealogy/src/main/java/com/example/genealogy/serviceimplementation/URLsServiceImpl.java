package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.URLs;
import com.example.genealogy.repository.URLsRepository;
import com.example.genealogy.service.URLsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class URLsServiceImpl implements URLsService {

    private final URLsRepository urlsRepository;
    private final Validator validator;

    @Autowired
    public URLsServiceImpl(URLsRepository urlsRepository, Validator validator) {
        this.urlsRepository = urlsRepository;
        this.validator = validator;
    }

    @Override
    public boolean existsById(@NotNull URLs url) {
        return urlsRepository.existsById(url.getId());
    }

    @Override
    public boolean urlExist(@NotNull URLs url) {
        return urlsRepository.existsURL(url.getUrlID().getUrl(), url.getUrl(), url.getComment());
    }

    @Override
    public boolean saveURL(@NotNull URLs url) {
        if (urlExist(url)) {
            return false;
        }

        validateURL(url);

        try {
            urlsRepository.save(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateURL(@NotNull URLs url) {
        if (!existsById(url)) {
            return false;
        }

        validateURL(url);

        try {
            urlsRepository.save(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteURL(URLs url) {
        try {
            if (existsById(url)) {
                urlsRepository.delete(url);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<URLs> getAllURLs() {
        return urlsRepository.findAll();
    }

    private void validateURL(URLs url) {
        Set<ConstraintViolation<URLs>> violations = validator.validate(url);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<URLs> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja URL-a nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
