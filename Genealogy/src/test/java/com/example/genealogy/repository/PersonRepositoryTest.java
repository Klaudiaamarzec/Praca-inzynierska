package com.example.genealogy.repository;

import com.example.genealogy.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testGetPersonList() {

        List<Object[]> personList = personRepository.getPersonList();
        assertThat(personList).hasSize(8);

        // Check name type
        assertThat(personList.get(0)[0]).isInstanceOf(String.class);
        // Check surname type
        assertThat(personList.get(0)[1]).isInstanceOf(String.class);
        // Check name and surname for user 0
        assertThat(personList.get(0)[0]).isEqualTo("Kris");
        assertThat(personList.get(0)[1]).isEqualTo("Jenner");

        assertThat(personList.get(1)[0]).isInstanceOf(String.class);
        assertThat(personList.get(1)[1]).isInstanceOf(String.class);
        assertThat(personList.get(1)[0]).isEqualTo("William");
        assertThat(personList.get(1)[1]).isEqualTo("Jenner");
    }

    @Test
    void testFindPersonByParameter() {

        List<Person> personList = personRepository.findPersonByParameter("Jenner");
        assertThat(personList).hasSize(4);

        // IDs Check
        assertThat(personList.get(0).getId()).isEqualTo(1L);
        assertThat(personList.get(1).getId()).isEqualTo(2L);
        assertThat(personList.get(2).getId()).isEqualTo(3L);
        assertThat(personList.get(3).getId()).isEqualTo(4L);
    }

    @Test
    void testFindPersonByParameter2() {

        List<Person> personList = personRepository.findPersonByParameter("kardash");
        assertThat(personList).hasSize(2);

        // IDs Check
        assertThat(personList.get(0).getId()).isEqualTo(7L);
        assertThat(personList.get(1).getId()).isEqualTo(8L);
    }

    @Test
    void testFindAllPersonsInDocument() {

        List<Person> personList = personRepository.findAllPersonsInDocument(2L);
        assertThat(personList).hasSize(3);

        // IDs Check
        assertThat(personList.get(0).getId()).isEqualTo(1L);
        assertThat(personList.get(1).getId()).isEqualTo(4L);
        assertThat(personList.get(2).getId()).isEqualTo(8L);
    }
}
