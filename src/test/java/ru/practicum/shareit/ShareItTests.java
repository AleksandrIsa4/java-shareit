package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = "ru.practicum.shareit")
@EnableJpaRepositories(basePackages = "ru.practicum.shareit")
class ShareItTests {

    @Test
    void contextLoads() {
    }

}
