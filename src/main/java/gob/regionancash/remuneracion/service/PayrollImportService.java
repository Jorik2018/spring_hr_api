package gob.regionancash.remuneracion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.uns.remuneracion.model.PayrollPeriod;
import edu.uns.remuneracion.model.PerPlanilla;
import edu.uns.remuneracion.model.PlanillaPeople;
import edu.uns.remuneracion.model.PlanillaConcept;
import edu.uns.remuneracion.repository.PerPeriodoPlaRepository;
import edu.uns.remuneracion.repository.PlanillaRepository;
import edu.uns.remuneracion.repository.PlanillaPeopleRepository;
import edu.uns.remuneracion.repository.PlanillaConceptRepository;
import gob.regionancash.remuneracion.model.PayrollConcept;
import gob.regionancash.remuneracion.model.PayrollPeople;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollImportService {

    private final PerPeriodoPlaRepository periodRepository;
    private final PlanillaRepository planillaRepository;
    private final PlanillaPeopleRepository payrollPeopleRepository;
    private final PlanillaConceptRepository conceptRepository;

    @Transactional
    public PayrollPeriod savePeriodo(PayrollPeriod periodo, boolean confirmDelete) {
        Optional<PayrollPeriod> existing = periodRepository.findById(periodo.getId());
        if (existing.isPresent()) {
            if (confirmDelete) {
                deletePeriodo(existing.get());
            } else {
                throw new RuntimeException("Periodo ya existe, enviar confirmDelete=true para reemplazarlo");
            }
        }
        System.out.println("periodo=" + periodo);
        return periodRepository.save(periodo);
    }

    @Transactional
    public void deletePeriodo(PayrollPeriod period) {
        List<PerPlanilla> planillas = planillaRepository.findByPeriod(period);
        for (PerPlanilla planilla : planillas) {
            payrollPeopleRepository.deleteAllByPayrollId(planilla.getId());
            conceptRepository.deleteAllByPayrollId(planilla.getId());
        }
        planillaRepository.deleteAll(planillas);
        periodRepository.delete(period);
    }

    @Transactional
    public PerPlanilla savePlanilla(PerPlanilla planilla, Integer periodId, boolean confirmDelete) {
        PayrollPeriod periodo = periodRepository.findById(periodId)
        .orElseThrow(() -> new RuntimeException(""));
        planilla.setPeriod(periodo);

        return planillaRepository.findById(planilla.getId())
                .orElseGet(() -> planillaRepository.save(planilla));
    }

    @Transactional
    public PlanillaPeople savePayrollPeople(PlanillaPeople pp) {
        return payrollPeopleRepository.findById(pp.getId())
                .orElseGet(() -> payrollPeopleRepository.save(pp));
    }

    @Transactional
    public PlanillaConcept savePayrollConcept(PlanillaConcept pc) {
        return conceptRepository.findById(pc.getId())
                .orElseGet(() -> conceptRepository.save(pc));
    }
}