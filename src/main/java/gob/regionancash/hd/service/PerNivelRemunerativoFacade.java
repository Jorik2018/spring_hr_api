package gob.regionancash.hd.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hd.model.PerNivelRemunerativo;

public interface PerNivelRemunerativoFacade{

    public List<PerNivelRemunerativo> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(PerNivelRemunerativo pernivelremunerativo);

}