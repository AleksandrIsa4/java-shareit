package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.config.DbConfig;

@AutoConfigureDataJpa
@SpringBootTest(classes = DbConfig.class,
        properties = {
                "spring.jpa.properties.hibernate.format_sql=true",
                "spring.jpa.show-sql=true",
                "spring.main.allow-circular-references=true"
        })
class ShareItTests {

    @Test
    void contextLoads() {
    }

}
