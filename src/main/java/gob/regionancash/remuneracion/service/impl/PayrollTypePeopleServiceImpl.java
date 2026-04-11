package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.remuneracion.service.PayrollTypePeopleFacadeLocal;
import gob.regionancash.remuneracion.model.PayrollTypePeople;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.isobit.util.AbstractFacade;
import org.isobit.util.XUtil;

public class PayrollTypePeopleServiceImpl implements PayrollTypePeopleFacadeLocal {

  public PayrollTypePeople load(PayrollTypePeople selected) {
     return null;
  }

  
  public List<PayrollTypePeople> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
    return null;
     /*Object payrollType = XUtil.isEmpty(filters.get("payrollType"), null);
     Object fullName = XUtil.isEmpty(filters.get("fullName"), null);
     List<Query> ql = new ArrayList<>();
    
     EntityManager em = getEntityManager(); String sql;
     ql.add(em.createQuery("SELECT o,pa,pe,do " + (sql = "FROM PayrollTypePeople o INNER JOIN Payroll pa ON pa.id=o.id.payrollId INNER JOIN People pe ON (1*pe.code)=o.id.peopleId LEFT JOIN pe.document do WHERE 1=1" + ((fullName != null) ? " AND UPPER(pe.fullName) LIKE :fullName" : "") + " ORDER BY pa.year DESC,pe.fullName")));

    
     if (pageSize > 0) {
       ((Query)ql.get(0)).setFirstResult(first).setMaxResults(pageSize);
       ql.add(em.createQuery("SELECT COUNT(o) " + sql));
    } 
     for (Query q : ql) {
       if (fullName != null) {
         q.setParameter("payrollType", payrollType);
      }
    } 
     if (pageSize > 0) {
       filters.put("size", ((Query)ql.get(1)).getSingleResult());
    }
     return getColumn(((Query)ql.get(0)).getResultList(), (AbstractFacade.RowAdapter)new Object(this));*/
    
  }
}