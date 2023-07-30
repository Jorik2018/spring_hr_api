package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.LaborRegime;

public interface LaborRegimeFacade{

    public List<LaborRegime> load(int first, int pageSize, String sortField, Map<String, Object> filters);

}