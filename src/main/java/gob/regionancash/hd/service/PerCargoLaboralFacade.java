package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;


import gob.regionancash.hd.model.PerCargoLaboral;

public interface PerCargoLaboralFacade {

    public List<PerCargoLaboral> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(PerCargoLaboral percargolaboral);

}