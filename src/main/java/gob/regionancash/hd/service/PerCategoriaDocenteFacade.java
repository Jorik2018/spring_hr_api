package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hd.model.PerCategoriaDocente;

public interface PerCategoriaDocenteFacade{

    public List<PerCategoriaDocente> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(PerCategoriaDocente percategoriadocente);

}