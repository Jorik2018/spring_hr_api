package gob.regionancash.hr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.People;
import org.isobit.directory.service.CompanyFacade;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

import gob.regionancash.hr.model.Study;
import gob.regionancash.hr.service.EscStudyFacade;

public class EscStudyFacadeImpl implements EscStudyFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CompanyFacade companyFacade;

    @Override
    public List<Study> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object people = XUtil.isEmpty(filters.get("study:people"), null);
        List<Query> ql = new ArrayList();
        String sql;
        ql.add(em.createQuery("SELECT o " + (sql = "FROM EscStudy o WHERE 1=1 "
                + (people != null ? "AND " + (people instanceof People ? "o.peopleId=" : "UPPER(o.people.fullName) LIKE ") + ":people" : ""))
                + "  ORDER BY 1 ASC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (people != null) {
                q.setParameter("people", people instanceof People ? ((People)people).getId() : ("%" + people + "%"));
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return ql.get(0).getResultList();
    }

    @Override
    public void edit(Study entity) {
        Map ext = (Map) entity.getExt();
        if (ext.containsKey("company")) {
            Company company = (Company) ext.get("company");
            companyFacade.edit(company);
            entity.setEntity(company);
        }
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public Study load(Object o) {
        Study e = em.find(Study.class,o instanceof Study ? ((Study) o).getId() : XUtil.intValue(o));
        e.setExt(new HashMap());
        return e;
    }

}
