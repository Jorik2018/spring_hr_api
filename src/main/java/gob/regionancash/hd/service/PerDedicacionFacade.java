package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hd.model.PerDedicacion;

public interface PerDedicacionFacade{

    public List<PerDedicacion> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(PerDedicacion perdedicacion);

}