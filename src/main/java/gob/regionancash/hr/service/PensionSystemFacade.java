package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.PensionSystem;

public interface PensionSystemFacade{

    public List<PensionSystem> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(PensionSystem pensionsystem);

}