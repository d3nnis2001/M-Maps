package com.gpse.basis.services;

import com.gpse.basis.domain.Vorlage;

import java.util.ArrayList;

public interface VorlagenService {
    void editVorlage(Vorlage vorlage);
    ArrayList<Vorlage> getVorlagenData();
    Boolean deleteVorlage(String vorlagenId);
}
