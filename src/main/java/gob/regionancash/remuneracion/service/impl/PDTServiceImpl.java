package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.jr.QuintaRowReport;
import gob.regionancash.remuneracion.service.PDTFacadeLocal;
import jakarta.persistence.EntityManager;
import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PdtQuinta;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.isobit.util.XUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class PDTServiceImpl  implements PDTFacadeLocal {

    	private final EntityManager em;

    // Constructor para inyectar EntityManager
    public PDTServiceImpl(EntityManager em) {
        this.em = em;
    }

    EntityManager getEntityManager() {
      return em;
    }

  public List<QuintaRowReport> loadReport(int first, int pageSize, String sortField, Map<String, Object> filters) {
     int anio = 0;
     String q = null;
     if (filters != null) {
       anio = XUtil.intValue(filters.get("anoEje"));
       q = (String)filters.get("q");
    } 
     if (XUtil.isEmpty(q)) { q = "___"; } else { q = "%" + q.toUpperCase() + "%"; }

    
     List<Employee> l = getEntityManager().createQuery("SELECT p FROM Employee p WHERE (p.drtPersonaNatural.nombreCompleto LIKE :q OR p.drtPersonaNatural.numeroPndid LIKE :q)").setParameter("q", q).getResultList();
     ArrayList<QuintaRowReport> nl = new ArrayList<>();
     for (Employee employee : l) {      
       List<PdtQuinta> l2 = getEntityManager().createQuery("""
        SELECT q FROM PdtQuinta q WHERE q.pdtQuintaPK.dni LIKE :dni AND q.pdtQuintaPK.anio=:anio ORDER BY q.pdtQuintaPK.anio,q.pdtQuintaPK.mes
      """)
       .setParameter("dni", "%" + employee.getPeople().getCode() + "%")
       .setParameter("anio", Integer.valueOf(anio))
       .getResultList();
       List<PayrollPeople> l3 = getEntityManager().createQuery("SELECT d FROM PerDetallepla d WHERE d.perPlanilla.perPeriodoPla.anio=:anio AND d.perDetalleplaPK.idEsc=:idEsc").setParameter("idEsc", employee.getId()).setParameter("anio", Short.valueOf((short)anio)).getResultList();
       System.out.println(employee.getId() + "___" + employee.getPeople() + " l2=" + l2.size());
       for (PdtQuinta q2 : l2) {
         QuintaRowReport qt = new QuintaRowReport();
         for (PayrollPeople payrollPeople : l3);
         qt.setPdtQuinta(q2);
         qt.setEmployee(employee);
         nl.add(qt);
      } 
    } 
     return nl;
  }

  public void clear(int anio, int mes) {
     getEntityManager().createQuery("DELETE FROM PdtQuinta q WHERE q.pdtQuintaPK.anio=:anio AND q.pdtQuintaPK.mes=:mes")
       .setParameter("anio", Integer.valueOf(anio))
       .setParameter("mes", Integer.valueOf(mes))
       .executeUpdate();
  }

}