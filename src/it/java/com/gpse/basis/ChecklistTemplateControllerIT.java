package com.gpse.basis;

import com.gpse.basis.domain.ChecklistTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ChecklistTemplateControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllChecklistNames() throws Exception {
        this.mvc.perform(get("/api/templates/all/names"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllTemplates() throws Exception {
        this.mvc.perform(get("/api/templates/all"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void addChecklist() throws Exception {
        ChecklistTemplate newTemplate = new ChecklistTemplate(
            "Test 1",
            new LinkedList<>(List.of("eine Aufgabe")),
            new LinkedList<>(Arrays.asList("ein Material", " zweites Material", "drittes Material"))
        );
        this.mvc.perform(post("/api/templates/all", newTemplate))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
