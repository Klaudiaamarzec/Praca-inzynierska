package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.repository.PersonRepository;
import com.example.genealogy.service.PersonService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final Validator validator;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, Validator validator) {
        this.personRepository = personRepository;
        this.validator = validator;
    }

    @Override
    public boolean savePerson(@NotNull Person person) {
        if (existsById(person.getId())) {
            return false;
        }

        validatePerson(person);

        try {
            personRepository.save(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updatePerson(@NotNull Person person) {
        if (!existsById(person.getId())) {
            return false;
        }

        validatePerson(person);

        try {
            personRepository.save(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(long id) {
        return personRepository.existsById(id);
    }

    @Override
    public boolean deletePerson(Person person) {
        try {
            if (existsById(person.getId())) {
                personRepository.delete(person);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<Object[]> getPersonList() {
        return personRepository.getPersonList();
    }

    @Override
    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    @Override
    public List<Person> findPersonByParameter(String parameter) {
        return personRepository.findPersonByParameter(parameter);
    }

    @Override
    public List<Person> findAllPersonsInDocument(@NotNull Document document) {
        return personRepository.findAllPersonsInDocument(document.getId());
    }

    private void validatePerson(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Person> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("\n");
            }
            throw new ConstraintViolationException("Walidacja osoby nie powiodła się:\n" + sb.toString(), violations);
        }
    }
}
