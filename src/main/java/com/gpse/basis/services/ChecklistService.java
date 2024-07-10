package com.gpse.basis.services;

import com.gpse.basis.domain.Checklist;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The interface Checklist service.
 */
public interface ChecklistService {
    /**
     * Gets all checklist names.
     *
     * @return the all checklist names
     */
    List<String> getAllChecklistNames();

    /**
     * Gets a ll templates.
     *
     * @return the a ll templates
     */
    List<Checklist> getALlTemplates();

    /**
     * Add checklist boolean.
     *
     * @param name     the name
     * @param tasks    the tasks
     * @param material the material
     * @return the boolean
     */
    boolean addChecklist(String name, List<String> tasks, List<String> material);

    /**
     * Gets template.
     *
     * @param name the name
     * @return the template
     */
    Optional<Checklist> getTemplate(String name);

    /**
     * Delete template.
     *
     * @param templateName the template name
     */
    void deleteTemplate(String templateName);

    /**
     * Duplicate template string.
     *
     * @param template the template
     * @return the string
     */
    String duplicateTemplate(Checklist template);

    /**
     * Edit checklist boolean.
     *
     * @param template the template
     * @return the boolean
     */
    boolean editChecklist(Checklist template);

    /**
     * Gets all names.
     *
     * @return the all names
     */
    ArrayList<String> getAllNames();

    /**
     * Load checklist by name checklist.
     *
     * @param name the name
     * @return the checklist
     * @throws UsernameNotFoundException the username not found exception
     */
    Checklist loadChecklistByName(final String name) throws UsernameNotFoundException;

    /**
     * Change rep checklist boolean.
     *
     * @param id     the id
     * @param ticked the ticked
     * @return the boolean
     */
    Boolean changeRepChecklist(final String id, String[] ticked);

    /**
     * Gets tickedwith id.
     *
     * @param id the id
     * @return the tickedwith id
     */
    ArrayList<String> getTickedwithId(String id);

    /**
     * Sets terminated date.
     *
     * @param id   the id
     * @param term the term
     * @return the terminated date
     */
    Boolean setTerminatedDate(String id, LocalDate term);

    /**
     * Gets termination date.
     *
     * @param id the id
     * @return the termination date
     */
    LocalDate getTerminationDate(String id);
}
