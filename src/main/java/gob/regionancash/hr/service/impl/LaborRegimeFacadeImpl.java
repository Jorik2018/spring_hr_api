package gob.regionancash.hr.service.impl;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.service.LaborRegimeFacade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class LaborRegimeFacadeImpl implements LaborRegimeFacade {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        return em.createQuery("SELECT o FROM LaborRegime o ORDER BY o.name").getResultList();
    }

}
