package com.gpse.basis.services;

import com.gpse.basis.domain.Vorlage;

import java.util.ArrayList;

/**
 * Vorlagenservice.
 */
public interface VorlagenService {
    void editVorlage(Vorlage vorlage);
    ArrayList<Vorlage> getVorlagenData();
    Boolean deleteVorlage(String vorlagenId);
}
