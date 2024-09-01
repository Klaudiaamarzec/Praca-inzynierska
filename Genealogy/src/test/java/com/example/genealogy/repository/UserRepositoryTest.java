package com.example.genealogy.repository;

import com.example.genealogy.model.User;
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

public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindGenealogist() {

        List<User> genealogists = userRepository.findGenealogist();
        assertThat(genealogists).hasSize(2);

        // IDs check
        assertThat(genealogists.get(0).getId()).isEqualTo(1L);
        assertThat(genealogists.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void testFindUsers() {

        List<User> users = userRepository.findUsers();
        assertThat(users) .hasSize(3);

        // IDs check
        assertThat(users.get(0).getId()).isEqualTo(2L);
        assertThat(users.get(1).getId()).isEqualTo(3L);
        assertThat(users.get(2).getId()).isEqualTo(4L);
    }
}
