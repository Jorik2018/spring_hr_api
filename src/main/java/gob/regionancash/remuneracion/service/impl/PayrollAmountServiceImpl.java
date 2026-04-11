package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.model.PensionSystem;
import gob.regionancash.remuneracion.service.PayrollAmountFacadeLocal;
import gob.regionancash.remuneracion.model.Concept;
import gob.regionancash.remuneracion.model.PayrollAmount;
import gob.regionancash.remuneracion.repository.PayrollAmountRepository;
import gob.regionancash.util.BaseServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.isobit.app.X;
import org.isobit.util.AbstractFacade;
import org.springframework.stereotype.Service;

//@Service
public class PayrollAmountServiceImpl extends BaseServiceImpl<PayrollAmount, Integer> implements PayrollAmountFacadeLocal {

  public PayrollAmountServiceImpl(PayrollAmountRepository repository) {
    super(repository);
  }

  public void edit(PayrollAmount entity) {
     Concept concept = entity.getConcept();
     if (concept != null) {
       entity.setConceptId(concept.getId());
    }
     if (entity.getIniDate() == null) {
       entity.setIniDate(X.getServerDate());
    }
     super.save(entity);
  }

  
  public PayrollAmount load(Object id) {
     PayrollAmount payrollAmount = (PayrollAmount)findById((Integer)id);
     //payrollAmount.setTarget(em.find(PensionSystem.class, id));
     return payrollAmount;
  }

  
  public List<PayrollAmount> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
     return null;/*(List<PayrollAmount>)getEntityManager().createQuery("SELECT c,co,ps.name,rl.name,pe.fullName FROM PayrollAmount c LEFT JOIN Concept co ON co.id=c.conceptId LEFT JOIN PensionSystem ps ON ps.id=c.targetId LEFT JOIN RemunerativeLevel rl ON rl.id=c.targetId LEFT JOIN People pe ON pe.code=c.targetId WHERE 1=1 ORDER BY c.id DESC", Object[].class)
       .getResultList().stream().map(e -> {
          PayrollAmount payrollAmount = (PayrollAmount)e[0];
          payrollAmount.setConcept((Concept)e[1]);
          if ("TR".equals(payrollAmount.getType())) {
            payrollAmount.setTargetName((String)e[4]);
          } else if ("RL".equals(payrollAmount.getType())) {
            payrollAmount.setTargetName((String)e[3]);
          } else {
            payrollAmount.setTargetName((String)e[2]);
          } 
          return payrollAmount;
         }).collect(Collectors.toList());*/
  }
}