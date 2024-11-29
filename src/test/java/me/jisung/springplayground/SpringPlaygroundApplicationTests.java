package me.jisung.springplayground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SpringPlaygroundApplicationTests {

    @Test
    void contextLoads() {
        int expected = 1; int actual = 1;
        Assertions.assertEquals(expected, actual);
    }

}
