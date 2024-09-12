package com.example.genealogy.service;

import com.example.genealogy.model.Document;
import com.example.genealogy.model.Person;

import java.util.List;

public interface PersonService {

    boolean savePerson(Person person);

    boolean updatePerson(Person person);

    boolean existsById(long id);

    boolean deletePerson(Person person);

    List<Object[]> getPersonList();

    List<Person> getAllPeople();

    List<Person> findPersonByParameter(String parameter);

    List<Person> findAllPersonsInDocument(Document document);
}
