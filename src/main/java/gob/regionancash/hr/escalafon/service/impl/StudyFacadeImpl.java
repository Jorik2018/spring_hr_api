package gob.regionancash.hr.escalafon.service.impl;

import gob.regionancash.hr.escalafon.service.StudyFacade;
import gob.regionancash.hr.model.Study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.People;
import org.isobit.util.XUtil;

public class StudyFacadeImpl implements StudyFacade {

    @PersistenceContext
    private EntityManager em;

    @Override
    //@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
    public List<Study> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object people = filters.get("people");
        List<Query> ql = new ArrayList();
        String sql;

        ql.add(em.createQuery("SELECT e " + (sql = "FROM Study e JOIN e.entity p WHERE 1=1 "
                + (people != null ? " AND e.peopleId=:people " : "")+ " ORDER BY 1 ASC")));
        
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(e) " + sql));
        }
        for (Query q : ql) {
            if (people != null) {
                q.setParameter("people", XUtil.intValue(people));
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List<Study> el = ql.get(0).getResultList();
        List lid = new ArrayList();
        List lid2 = new ArrayList();
        Map tmp = new HashMap();
        for (Study e : el) {
            int idi = XUtil.intValue(e.getEntityId());
            if (idi != 0) {
                lid.add(e.getId());
            }
            tmp.put(e.getId(), e);
        }

        if (lid.size() > 0) {
            try {
                for (Object[] r : (List<Object[]>) em.createQuery("SELECT es.id,i.id_inst,i.nombre_inst FROM esc_estudios es,esc_instituciones i WHERE es.id_inst=i.id_inst AND es.id IN (:id)")
                        .setParameter("id", lid).getResultList()) {
                    Study es = ((Study) tmp.get(XUtil.intValue(r[0])));
                    Company pj = new Company();
                    pj.setBusinessName((String) r[2]);
                    es.setEntity(pj);
                }
            } catch (Exception ex) {
                for (Object[] r : (List<Object[]>) em.createQuery("SELECT e.id,p FROM Company p,Study e WHERE p.id=-e.entity.id AND e.id IN (:id)")
                        .setParameter("id", lid).getResultList()) {
                    ((Study) tmp.get(XUtil.intValue(r[0]))).setEntity((Company) r[1]);
                }
            }

        }
        return el;
    }

    @Override
    public Study find(Object id) {
        Study study = em.find(Study.class,id);
        People people = (People) ((Object[]) em.createQuery("SELECT p,d FROM People p JOIN p.document d WHERE p.id=:id")
                .setParameter("id", study.getPeopleId()).getSingleResult())[0];
        study.setPeople(people);
        return study;
    }

}
