package com.gpse.basis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TemplateApplication.class)
public class ExampleApplicationIT {

    @Test
    public void contextLoads() {
        //Tests Application-Start.
    }
}
