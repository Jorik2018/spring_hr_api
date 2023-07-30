package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.escalafon.model.Merit;

public interface MeritFacade{

    public List<Merit> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Merit escdemerito);

    Merit find(Object id);

}