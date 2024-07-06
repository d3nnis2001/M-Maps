package com.gpse.basis;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/*
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

 */
