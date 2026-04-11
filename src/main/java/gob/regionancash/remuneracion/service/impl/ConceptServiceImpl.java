package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.remuneracion.model.Concept;
import gob.regionancash.remuneracion.repository.ConceptRepository;
import gob.regionancash.remuneracion.service.ConceptService;
import gob.regionancash.util.BaseServiceImpl;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

//@Service
public class ConceptServiceImpl extends BaseServiceImpl<Concept, Integer> implements ConceptService {

    public ConceptServiceImpl(ConceptRepository repository){
        super(repository);
    }

    @Override
    public Concept load(Object selected) {
        Concept concept = (Concept)selected;
        if (concept == null || concept.getId() == null) {
            return null;
        }
        return repository.findById(concept.getId()).orElse(null);
    }

    @Override
    public Page<Concept> load(int first, int pageSize, String sortField, Map<String, Object> filters) {

        // Orden por defecto
        Sort sort = Sort.by("typeId").ascending().and(Sort.by("name"));

        if (sortField != null && !sortField.isEmpty()) {
            sort = Sort.by(sortField).ascending();
        }

        int page = first / pageSize;

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Concept> result = repository.findAll(pageable);
        return result;
    }

}