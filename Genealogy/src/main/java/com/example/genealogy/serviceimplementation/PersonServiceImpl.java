package com.example.genealogy.serviceimplementation;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;
import com.example.genealogy.repository.PersonRepository;
import com.example.genealogy.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public boolean existsById(@NotNull Long id) {
        return personRepository.existsById(id);
    }

    @Override
    public boolean personExists(@NotNull Person person) {
        return  personRepository.existsPerson(person.getName(), person.getSurname(), person.getRin(), person.getBirthDate());
    }

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono osoby o id: " + id));
    }

    @Override
    public List<Person> findPersonByIds(List<Long> ids) {
        return personRepository.findPersonByIds(ids);
    }

    @Override
    public boolean savePerson(@NotNull Person person) {
        if (personExists(person)) {
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
        if (parameter.contains(" ")) {
            String[] parts = parameter.split(" ");
            String name = parts[0];
            String surname = parts[1];

            return personRepository.findPersonByNameAndSurname(name, surname);
        } else {
            return personRepository.findPersonByParameter(parameter);
        }
    }


    @Override
    public List<Person> findAllPersonsInDocument(@NotNull Document document) {
        return personRepository.findAllPersonsInDocument(document.getId());
    }

    private void validatePerson(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));

            throw new ConstraintViolationException("Walidacja osoby nie powiodła się:\n" + messages, violations);
        }
    }

}
