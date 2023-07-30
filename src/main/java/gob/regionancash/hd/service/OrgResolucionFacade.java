package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hd.model.OrgResolucion;

public interface OrgResolucionFacade{

    public List<OrgResolucion> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(OrgResolucion orgresolucion);

}