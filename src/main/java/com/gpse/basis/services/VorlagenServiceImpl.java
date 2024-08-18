package com.gpse.basis.services;

import com.gpse.basis.domain.Vorlage;
import com.gpse.basis.repositories.VorlagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation des Vorlagenservices.
 */
@Service
public class VorlagenServiceImpl implements VorlagenService {

    @Autowired
    private VorlagenRepository vorlagenrepo;
    private MongoTemplate template;

    @Autowired
    public VorlagenServiceImpl(VorlagenRepository vorlagenrepo, MongoTemplate template) {
        this.vorlagenrepo = vorlagenrepo;
        this.template = template;
    }

    @Override
    public void editVorlage(Vorlage vorlage) {
        System.out.println(vorlage.getStringSubject());
        System.out.println(vorlage.getStringBody());
        vorlagenrepo.save(vorlage);
    }

    @Override
    public ArrayList<Vorlage> getVorlagenData() {
        System.out.println("getVorlagenData");
        Iterable iterable = vorlagenrepo.findAll();
        ArrayList<Vorlage> vorlagenArray = new ArrayList<>();
        Iterator<Vorlage> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Vorlage vorlage = iterator.next();
            vorlagenArray.add(vorlage);
        }
        return vorlagenArray;
    }

    @Override
    public Boolean deleteVorlage(String vorlagenId) {
        System.out.println("DELETE");
        try {
            Iterable iterable = vorlagenrepo.findAll();
            Iterator<Vorlage> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                Vorlage vorlage = iterator.next();
                if (vorlagenId.equals(vorlage.getVorlagenId())) {
                    vorlagenrepo.deleteById(vorlage.getVorlagenId());
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("CATCH");
            return false;
        }
    }
}
