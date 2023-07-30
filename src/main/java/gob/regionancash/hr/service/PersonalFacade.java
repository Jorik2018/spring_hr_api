package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Personal;

public interface PersonalFacade {

    public Personal prepareCreate();

    void remove(List<Personal> personal);

    public List<Personal> load(int first, int pageSize, String sortField, Map<String, Object> filters);

}
