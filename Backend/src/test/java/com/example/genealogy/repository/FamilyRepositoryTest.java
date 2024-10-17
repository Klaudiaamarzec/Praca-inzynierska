package com.example.genealogy.repository;

import com.example.genealogy.model.Family;
import com.example.genealogy.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FamilyRepositoryTest {

    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void checkIfExist() {

        boolean result = familyRepository.existsFamily(4L, 2L, 1L);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testFindFamiliesByChild() {

        Optional<Person> optChild = personRepository.findById(6L);
        assertThat(optChild).isPresent();

        Person child = optChild.get();

        Family family = familyRepository.findFamilyByChild(child);

        assertThat(family.getChild().getId()).isEqualTo(6L);

        assertThat(family.getFather().getId()).isEqualTo(5L);

        assertThat(family.getMother().getId()).isEqualTo(3L);
    }

    @Test
    void testFindFamiliesByMother() {

        Optional<Person> optMom = personRepository.findById(1L);
        assertThat(optMom).isPresent();

        Person mother = optMom.get();

        List<Family> families = familyRepository.findFamiliesByMother(mother);
        assertThat(families).hasSize(3);
    }

    @Test
    void testFindFamiliesByFather() {

        Optional<Person> optDad = personRepository.findById(2L);
        assertThat(optDad).isPresent();

        Person father = optDad.get();

        List<Family> families = familyRepository.findFamiliesByFather(father);
        assertThat(families).hasSize(2);
    }

    @Test
    void testFindFamiliesByParent() {

        Optional<Person> optParent = personRepository.findById(5L);
        assertThat(optParent).isPresent();

        Person parent = optParent.get();

        List<Family> families = familyRepository.findFamiliesByParent(parent);
        assertThat(families).hasSize(1);

    }

    @Test
    void testFindFamiliesByInvalidParent() {

        Optional<Person> optParent = personRepository.findById(4L);
        assertThat(optParent).isPresent();

        Person parent = optParent.get();

        List<Family> families = familyRepository.findFamiliesByParent(parent);
        assertThat(families).hasSize(0);

    }
}
