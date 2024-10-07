package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;

import java.util.List;

public interface PersonService {

    boolean existsById(Long id);

    boolean personExists(Person person);

    Person getPersonById(Long id);

    boolean savePerson(Person person);

    boolean updatePerson(Person person);

    boolean deletePerson(Person person);

    List<Person> getAllPeople();

    List<Object[]> getPersonList();

    List<Person> findPersonByParameter(String parameter);

    List<Person> findAllPersonsInDocument(Document document);
}
