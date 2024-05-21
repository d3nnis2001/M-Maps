package com.gpse.basis;

import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.services.ChecklistTemplateService;
import com.gpse.basis.web.ChecklistTemplateController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/*
class ChecklistTemplateControllerTest {

    private AutoCloseable closeable;

    @Mock
    private ChecklistTemplateService checklistTemplateService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllChecklistNames() {
        ChecklistTemplateController checklistTemplateController = new ChecklistTemplateController(checklistTemplateService);
        List<String> checklistNames = checklistTemplateController.getAllChecklistNames();

        assertThat(checklistNames.size()).isEqualTo(0);
    }

    @Test
    void getAllTemplates() {
        ChecklistTemplateController checklistTemplateController = new ChecklistTemplateController(checklistTemplateService);
        List<ChecklistTemplate> templates = checklistTemplateController.getAllTemplates();

        assertThat(templates.size()).isEqualTo(0);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }
}

 */
