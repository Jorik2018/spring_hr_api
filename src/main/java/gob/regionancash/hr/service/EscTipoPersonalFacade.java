package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;
import gob.regionancash.hr.model.EscTipoPersonal;

public interface EscTipoPersonalFacade{

    public List<EscTipoPersonal> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(EscTipoPersonal esctipopersonal);

}