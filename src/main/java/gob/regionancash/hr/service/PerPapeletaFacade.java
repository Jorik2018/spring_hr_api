package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Ballot;

public interface PerPapeletaFacade{

    public List<Ballot> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Ballot perpapeleta);

}