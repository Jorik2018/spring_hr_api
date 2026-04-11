package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.remuneracion.service.PayrollPeopleFacadeLocal;
import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import gob.regionancash.remuneracion.repository.PayrollPeopleRepository;
import gob.regionancash.remuneracion.repository.PayrollPeopleSpecification;
import org.springframework.data.jpa.domain.Specification;
import gob.regionancash.util.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.isobit.util.AbstractFacade;
import lombok.*;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;

//@Service
public class PayrollPeopleServiceImpl extends BaseServiceImpl<PayrollPeople, PayrollPeoplePK> implements PayrollPeopleFacadeLocal {

    public PayrollPeopleServiceImpl(PayrollPeopleRepository repository) {
        super(repository);
    }

    //@Override
    public Page<PayrollPeople> load(int first, int pageSize, String sortField, Map<String, Object> filters) {

        String fullName = (String) filters.get("fullName");

        Specification<PayrollPeople> spec =
                PayrollPeopleSpecification.filterByFullName(fullName);

        Pageable pageable = pageSize > 0
                ? PageRequest.of(first / pageSize, pageSize, Sort.by(Sort.Direction.DESC, "payroll.year")
                        .and(Sort.by("people.fullName")))
                : Pageable.unpaged();

        Page<PayrollPeople> page = ((org.springframework.data.jpa.repository.JpaSpecificationExecutor<PayrollPeople>) repository)
        .findAll(spec, pageable);

        return page;
    }
}