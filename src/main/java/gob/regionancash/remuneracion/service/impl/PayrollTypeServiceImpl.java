package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.Employee;
import gob.regionancash.remuneracion.service.PayrollTypeFacadeLocal;
import gob.regionancash.remuneracion.model.Concept;
import gob.regionancash.remuneracion.model.Payroll;
import gob.regionancash.remuneracion.model.PayrollAmount;
import gob.regionancash.remuneracion.model.PayrollConcept;
import gob.regionancash.remuneracion.model.PayrollGroup;
import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PayrollType;
import gob.regionancash.remuneracion.model.PayrollTypePeople;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.isobit.directory.model.People;
import org.isobit.util.AbstractFacade;
import org.isobit.util.XUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class PayrollTypeServiceImpl implements PayrollTypeFacadeLocal {

    private final EntityManager em;

    // Constructor para inyectar EntityManager
    public PayrollTypeServiceImpl(EntityManager em) {
        this.em = em;
    }

    EntityManager getEntityManager() {
      return em;
    }

  public List<PayrollType> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
     /*Object name = XUtil.isEmpty(filters.get("name"), null);
     List<Query> ql = new ArrayList<>();
    
     EntityManager em = getEntityManager(); String sql;
     ql.add(em.createQuery("SELECT o " + (sql = "FROM PayrollType o WHERE 1=1" + ((name != null) ? " AND UPPER(o.name) LIKE :name" : "") + " ORDER BY o DESC")));

    
     if (pageSize > 0) {
       ((Query)ql.get(0)).setFirstResult(first).setMaxResults(pageSize);
       ql.add(em.createQuery("SELECT COUNT(o) " + sql));
    } 
     for (Query q : ql) {
       if (name != null) {
         q.setParameter("name", "%" + name.toString().toLowerCase().replaceAll("\\s+", "%") + "%");
      }
    } 
     if (pageSize > 0) {
       filters.put("size", ((Query)ql.get(1)).getSingleResult());
    }
     List<PayrollType> l = ((Query)ql.get(0)).getResultList();
     l.forEach(e -> {
          if (e.getGroupId() != null) {
            PayrollGroup payrollGroup = (PayrollGroup)em.find(PayrollGroup.class, e.getGroupId());
            if (payrollGroup.getParentId() == null) {
              e.setMainGroup(payrollGroup);
            } else {
              e.setGroup(payrollGroup);
              e.setMainGroup((PayrollGroup)em.find(PayrollGroup.class, payrollGroup.getParentId()));
            } 
          } 
        });
     return l;*/
     return null;
  }

  
  public List<PayrollGroup> getPayrollGroupList() {
     List<PayrollGroup> l2 = new ArrayList();
     EntityManager em = getEntityManager();
     List<PayrollGroup> l = em.createQuery("SELECT pg FROM PayrollGroup pg").getResultList();
     l.forEach(e -> {
          if (e.getParentId() == null) {
            l2.add(e);
          }
        });
     Collections.reverse(l);
     l.forEach(e -> {
          if (e.getParentId() != null) {
            em.detach(e);
            e.setName("--- " + e.getName());
            l2.add(e.getParentId().intValue(), e);
          } 
        });
     return l2;
  }

  
  public PayrollType load(Integer id) {
     EntityManager em = getEntityManager();
     PayrollType payrollType = (PayrollType)em.find(PayrollType.class, id);
     /*List<PayrollTypePeople> persons = AbstractFacade.getColumn(em.createQuery("SELECT pp,pe,do FROM PayrollTypePeople pp JOIN People pe ON pe.code=pp.id.peopleId LEFT JOIN pe.document do WHERE pp.id.payrollTypeId=:payrollTypeId AND pp.id.peopleId>0 ORDER BY pe.fullName").setParameter("payrollTypeId", id)
         .getResultList(), (AbstractFacade.RowAdapter)new Object(this));
    
     payrollType.setPersons(persons);*/
     return payrollType;
  }
  
  public Object generate(Map params) {
     List payrollTypeList = (List)params.get("payrollTypeId");
     EntityManager em = getEntityManager();
     payrollTypeList.forEach(payrollTypeId -> {
          Payroll payroll;
          
          try {
            payroll = (Payroll)em.createQuery("SELECT p FROM Payroll p WHERE p.type.id=:payrollTypeId AND p.month=:month AND p.year=:year", Payroll.class).setParameter("payrollTypeId", payrollTypeId).setParameter("year", Integer.valueOf(XUtil.intValue(params.get("year")))).setParameter("month", Integer.valueOf(XUtil.intValue(params.get("month")))).getSingleResult();
           } catch (NoResultException nre) {
            payroll = new Payroll();
            
            //payroll.setYear(Integer.valueOf(XUtil.intValue(params.get("year"))));
            //payroll.setMonth(Integer.valueOf(XUtil.intValue(params.get("month"))));
          } 
          if (payroll.getId() == null) {
            em.persist(payroll);
          } else {
            em.merge(payroll);
          } 
          
          int payrollId = payroll.getId().intValue();
          
          em.createQuery("DELETE FROM PayrollPeople pc WHERE pc.id.payrollId=:payrollId").setParameter("payrollId", Integer.valueOf(payrollId)).executeUpdate();
          em.createQuery("""
            DELETE FROM PayrollConcept pc WHERE pc.id.payrollId=:payrollId
            """)
          .setParameter("payrollId", Integer.valueOf(payrollId))
          .executeUpdate();

          List<Object[]> peoples = em.createQuery("SELECT pp,pe,em,'' FROM Employee em JOIN em.people pe,PayrollTypePeople pp WHERE 1*pe.code=pp.peopleId AND pp.payrollTypeId=:payrollTypeId", Object[].class).setParameter("payrollTypeId", payrollTypeId).getResultList();
          Map<Object, Object[]> m = (Map)new HashMap<>();
          for (Object[] people : peoples) {
            PayrollTypePeople payrollTypePeople = (PayrollTypePeople)people[0];
            people[3] = null;
            m.put(payrollTypePeople.getPeopleId(), people);
          } 
          List<Contract> contracs = em.createQuery("""
            SELECT c FROM Contract c WHERE c.id IN (SELECT MAX(c.id) FROM Contract c WHERE c.active=1 AND (c.charge=0 OR c.charge IS NULL) AND (1*c.people.code) IN (SELECT pp.id.peopleId FROM PayrollTypePeople pp WHERE pp.payrollTypeId=:payroll) GROUP BY c.peopleId)
            """).setParameter("payroll", payrollTypeId)
            .getResultList();
          //contracs.forEach(());
          Map<Object, List> remunerativeLevelMap = new HashMap<>();
          Map<Object, List> pensionSystemMap = new HashMap<>();
          //peoples.forEach(());
          List<PayrollAmount> payrollAmountList = em.createQuery("""
            SELECT pa FROM PayrollAmount pa WHERE pa.payrollTypeId=:payrollTypeId OR pa.payrollTypeId IS NULL
          """).setParameter("payrollTypeId", payrollTypeList.get(0)).getResultList();
          //payrollAmountList.forEach(());
        });
     return params;
  }

}