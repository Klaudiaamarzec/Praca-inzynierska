package com.example.genealogy.controller;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Notification;
import com.example.genealogy.model.User;
import com.example.genealogy.service.FamilyService;
import com.example.genealogy.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.genealogy.model.Person;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/API/People")
public class PersonController {

    private final PersonService personService;
    private final FamilyService familyService;

    public PersonController(PersonService personService, FamilyService familyService) {
        this.personService = personService;
        this.familyService = familyService;
    }

    @GetMapping("All")
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> people = personService.getAllPeople();
        return ResponseEntity.ok(people);
    }

    @GetMapping("Person/{id}")
    public ResponseEntity<Map<String, Object>> getPerson(@PathVariable Long id) {

        Person person = personService.getPersonById(id);

        Map<String, Person> parents = getParents(id).getBody();
        List<Person> children = getChildren(id).getBody();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("person", person);
        response.put("parents", parents);
        response.put("children", children);

        return ResponseEntity.ok(response);
    }

    @GetMapping("PersonList")
    public ResponseEntity<List<Object[]>> getPersonList() {
        List<Object[]> personList = personService.getPersonList();
        return ResponseEntity.ok(personList);
    }

    @PostMapping("Add")
    public ResponseEntity<String> addPerson(@Valid @RequestBody Person person) {


        if (personService.personExists(person)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Osoba już istnieje");
        }

        boolean isSaved = personService.savePerson(person);

        if(!isSaved)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nie udało się dodać osoby");

        return ResponseEntity.status(HttpStatus.OK).body("{\"personId\": " + person.getId() + "}");
    }

    @GetMapping("GetParents/{personId}")
    public ResponseEntity<Map<String, Person>> getParents(@PathVariable Long personId) {

        Person child = personService.getPersonById(personId);
        Family family = familyService.findFamilyByChild(child);

        if (family == null) {
            // Jeśli rodzina nie istnieje, zwróć pustą tablicę
            return ResponseEntity.ok(Collections.emptyMap());
        }

        Person mother = family.getMother() != null ? family.getMother() : null;
        Person father = family.getFather() != null ? family.getFather() : null;

        Map<String, Person> response = new HashMap<>();
        response.put("mother", mother);
        response.put("father", father);

        return ResponseEntity.ok(response);
    }

    @GetMapping("GetChildren/{parentId}")
    public ResponseEntity<List<Person>> getChildren(@PathVariable Long parentId) {

        Person parent = personService.getPersonById(parentId);

        List<Family> families = familyService.findFamiliesByParent(parent);
        List<Person> children = new ArrayList<>();

        for(Family family : families) {

            if (family.getChild() != null) {
                children.add(family.getChild());
            }
        }

        return ResponseEntity.ok(children);
    }

    @GetMapping("Search/{parameter}")
    public ResponseEntity<?> search(@PathVariable String parameter) {

        try {

            List<Person> people = personService.findPersonByParameter(parameter);
            return ResponseEntity.ok(people);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }

    }
}

