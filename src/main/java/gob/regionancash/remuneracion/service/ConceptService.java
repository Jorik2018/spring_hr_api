package gob.regionancash.remuneracion.service;

import gob.regionancash.remuneracion.model.Concept;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.*;

public interface ConceptService {
  
  Concept load(Object paramObject);
  
  Page<Concept> load(int paramInt1, int paramInt2, String paramString, Map<String, Object> paramMap);

}