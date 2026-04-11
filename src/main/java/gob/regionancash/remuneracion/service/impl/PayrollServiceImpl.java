package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.PensionSystem;
import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.model.RemunerativeLevel;
import gob.regionancash.remuneracion.service.PayrollFacadeLocal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import gob.regionancash.remuneracion.model.Concept;
import gob.regionancash.remuneracion.model.Payroll;
import gob.regionancash.remuneracion.model.PayrollAmount;
import gob.regionancash.remuneracion.model.PayrollConcept;
import gob.regionancash.remuneracion.model.PayrollConceptPK;
import gob.regionancash.remuneracion.model.PayrollGroup;
import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import gob.regionancash.remuneracion.model.PayrollType;
import gob.regionancash.remuneracion.repository.PayrollPeopleRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.isobit.app.X;
import org.isobit.app.model.User;
import org.isobit.directory.service.PeopleFacade;
import org.isobit.app.service.SessionFacade;
import org.isobit.directory.model.Dependency;
import org.isobit.directory.model.People;
import org.isobit.util.AbstractFacade;
import org.springframework.data.domain.*;
import org.isobit.util.XDate;
import org.isobit.util.XFile;
import org.isobit.util.XUtil;
import org.isobit.util.SimpleException;
import lombok.*;

@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollFacadeLocal {

    private PeopleFacade peopleFacade;

    private PayrollPeopleRepository payrollPeopleRepository;

    private SessionFacade sessionBean;

    private final EntityManager em;

    public void edit(Payroll entity) {
        /*
         * if (entity.getId() == null) {
         * Date today = X.getServerDate();
         * entity.setYear(Integer.valueOf(XDate.getYear(today)));
         * }
         * super.save(entity);
         */
    }

    public Payroll load(Integer id) {
        Payroll p = null;// (Payroll) findById(id);
        /*
         * Map<Object, Object> ext = new HashMap<>();
         * EntityManager em = getEntityManager();
         * List<PayrollPeople> persons = AbstractFacade.getColumn(em.createQuery(
         * "SELECT pp,pe,do FROM PayrollPeople pp JOIN People pe ON pe.code=pp.id.peopleId LEFT JOIN pe.document do WHERE pp.id.payrollId=:payrollId AND pp.id.peopleId>0 ORDER BY pe.fullName"
         * )
         * .setParameter("payrollId", p.getId())
         * .getResultList(), row -> {
         * PayrollPeople payrollPeople = (PayrollPeople) row[0];
         * People people = (People) row[1];
         * payrollPeople.setExt(new Object[1]);// espacio para cargar un concepto por
         * defecto
         * payrollPeople.setPeople(people);
         * return payrollPeople;
         * });
         * Map<Object, Object> m = new HashMap<>();
         * persons.forEach(pp -> m.put(Integer.valueOf(pp.getId().getPeopleId()),
         * pp.getPeople()));
         * 
         * ext.put("persons", persons);
         * // Son montos fijos a personas
         * List<PayrollConcept> concepts = em
         * .createQuery("SELECT pp FROM PayrollConcept pp WHERE pp.id.payrollId=:payrollId AND pp.id.conceptId=0"
         * )
         * .setParameter("payrollId", p.getId()).getResultList();
         * concepts.forEach(pp -> {
         * PayrollPeople people = (PayrollPeople)
         * m.get(Integer.valueOf(pp.getId().getPeopleId()));
         * 
         * if (people != null) {
         * ((Object[]) people.getExt())[0] = pp.getAmount();
         * }
         * });
         * p.setExt(ext);
         * return p;
         */
        return null;
    }

    public Object getConceptList(Integer payroll, Integer concept) {
        return null;/*
                     * AbstractFacade.getColumn(getEntityManager().createQuery(
                     * """
                     * SELECT pc,pp.id.peopleId,pe.fullName,pp
                     * FROM PayrollPeople pp
                     * INNER JOIN People pe ON pe.code=pp.id.peopleId
                     * LEFT JOIN PayrollConcept pc ON pc.id.payrollId=:payroll AND
                     * pc.id.conceptId=:concept AND pc.id.peopleId=pp.id.peopleId
                     * WHERE pp.id.payrollId=:payroll AND (pp.id.peopleId>0 OR
                     * pp.id.peopleId<-10000000) ORDER BY pe.fullName
                     * """)
                     * 
                     * .setParameter("payroll", payroll)
                     * .setParameter("concept", concept).getResultList(),
                     * (AbstractFacade.RowAdapter) new Object(this));
                     */
    }

    public void addPerson(Integer id, List<PayrollPeople> persons) {
        for (PayrollPeople people : persons) {
            people.getId().setPayrollId(id.intValue());
            // payrollPeopleRepository..persist(people);
        }
    }

    public Object importFile(File f) {
        /*
         * List<Object[]> l;
         * try {
         * l = (List<Object[]>) ((Object[]) XFile.readObject(f))[0];
         * } catch (IOException ex) {
         * throw new RuntimeException(ex);
         * }
         * Object[] header = l.get(0);
         * if (XUtil.intValue(header[0]) == 2) {
         * importPayrollConcept(l);
         * return null;
         * }
         * EntityManager em = getEntityManager();
         * Query employeeQuery =
         * em.createQuery("SELECT e FROM Employee e WHERE e.people.code*1=:people").
         * setFirstResult(0)
         * .setMaxResults(1);
         * Query peopleQuery =
         * em.createQuery("SELECT e FROM People e WHERE e.code*1=:people").
         * setFirstResult(0)
         * .setMaxResults(1);
         * Query payrollQuery = em
         * .createQuery("SELECT p FROM Payroll p WHERE p.type.id=:payrollType AND p.month=7 AND p.year=2020"
         * )
         * .setFirstResult(0).setMaxResults(1);
         * for (int r = 2; r < l.size(); r++) {
         * Object[] row = l.get(r);
         * int dni = XUtil.intValue(row[0]);
         * System.out.println(X.gson.toJson(row));
         * Employee employee = null;
         * try {
         * employee = (Employee) employeeQuery.setParameter("people",
         * Integer.valueOf(dni)).getSingleResult();
         * } catch (NoResultException ex) {
         * employee = new Employee();
         * //User u = this.userFacade.getCurrentUser();
         * //if (u != null) {
         * // employee.setUserId(Integer.valueOf(XUtil.intValue(u.getIdDir())));
         * //}
         * employee.setTypeId(Short.valueOf((short) 1));
         * employee.setPeople((People) peopleQuery.setParameter("people",
         * Integer.valueOf(dni)).getSingleResult());
         * }
         * if (employee.getCondicion() == null) {
         * employee.setCondicion(Short.valueOf((short) 0));
         * }
         * if (employee.getStatusId() == null) {
         * employee.setStatusId(Short.valueOf((short) 1));
         * }
         * employee.setIncomeDate((Date) row[12]);
         * employee.setnPlaza(X.toText(row[5]));
         * employee.setOldCode(Integer.valueOf(XUtil.intValue(row[6])));
         * employee.setPensionSystem(
         * (PensionSystem) em.find(PensionSystem.class,
         * Integer.valueOf(XUtil.intValue(row[7]))));
         * employee.setNroBankAccount(X.toText(row[9]));
         * employee.setAutogenerado(X.toText(row[10]));
         * if (employee.getId() == null) {
         * em.persist(employee);
         * } else {
         * em.merge(employee);
         * }
         * Payroll payroll = null;
         * 
         * try {
         * payroll = (Payroll) payrollQuery.setParameter("payrollType",
         * Integer.valueOf(XUtil.intValue(row[4])))
         * .getSingleResult();
         * } catch (NoResultException ex) {
         * payroll = new Payroll();
         * payroll.setType((PayrollType) em.find(PayrollType.class,
         * Integer.valueOf(XUtil.intValue(row[4]))));
         * payroll.setYear(Integer.valueOf(2020));
         * payroll.setMonth(Integer.valueOf(7));
         * em.persist(payroll);
         * }
         * PayrollPeoplePK pk = new PayrollPeoplePK();
         * pk.setPayrollId(payroll.getId().intValue());
         * pk.setPeopleId(dni);
         * PayrollPeople pp = (PayrollPeople) em.find(PayrollPeople.class, pk);
         * if (pp == null) {
         * pp = new PayrollPeople();
         * }
         * pp.setRemunerativeLevelId(Integer.valueOf(XUtil.intValue(row[1])));
         * pp.setRemunerativeLevel(X.toText(row[2]));
         * pp.setPosition(X.toText(row[3]));
         * try {
         * pp.setDiasLaborados(Float.valueOf(Float.parseFloat("" + row[11])));
         * } catch (Exception exception) {
         * }
         * 
         * if (pp.getId() == null) {
         * pp.setId(pk);
         * em.persist(pp);
         * } else {
         * em.merge(pp);
         * }
         * }
         */
        return Boolean.valueOf(true);
    }

    public Object getSummary(Object payrollId0, Integer group) {
        /*
         * EntityManager em = getEntityManager();
         * Payroll payroll0 = (Payroll) em.find(Payroll.class, payrollId0);
         * List<Map<Object, Object>> result = new ArrayList();
         * 
         * List<Payroll> payrollList = (group.intValue() > 0) ? em.createQuery(
         * "SELECT p FROM Payroll p join p.type pt where p.year=:year AND p.month=:month AND pt.groupId=:groupId"
         * )
         * .setParameter("groupId",
         * payroll0.getType().getGroupId()).setParameter("year", payroll0.getYear())
         * .setParameter("month", payroll0.getMonth()).getResultList()
         * : Arrays.<Payroll>asList(new Payroll[] { payroll0 });
         * 
         * for (Payroll payroll : payrollList) {
         * Map<Object, Object> payrollMap = new HashMap<>();
         * payrollMap.put("payrollName", payroll.getType().getDescription());
         * PayrollGroup payrollGroup = (PayrollGroup) em.find(PayrollGroup.class,
         * payroll.getType().getGroupId());
         * PayrollGroup payrollGroup0 = (PayrollGroup) em.find(PayrollGroup.class,
         * payrollGroup.getParentId());
         * payrollMap.put("payrollDescription",
         * "RESUMEN DE PLANILLA " + payrollGroup0.getName() + " - " +
         * payrollGroup.getName() + " : "
         * + (new DateFormatSymbols()).getMonths()[payroll.getMonth().intValue() -
         * 1].toUpperCase()
         * + " DEL " + payroll.getYear());
         * payroll.setCode(String.format("%02d", new Object[] { payroll.getMonth() })
         * + payroll.getYear().toString().substring(2) + "-" + String.format("%04d", new
         * Object[] {
         * Integer.valueOf((payroll.getNumber() != null) ?
         * payroll.getNumber().intValue() : 0) }));
         * 
         * if (group.intValue() == 6) {
         * 
         * payrollMap.put("payrollName", "TOTAL GENERAL");
         * payrollMap.put("payrollCount", Integer.valueOf(payrollList.size()));
         * payrollMap.put("payrolls", String.join(", ", (List)
         * payrollList.stream().map(p -> {
         * p.setCode(String.format("%02d", new Object[] { p.getMonth() }) +
         * p.getYear().toString().substring(2)
         * + "-" + String.format("%04d", new Object[] {
         * Integer.valueOf((p.getNumber() != null) ? p.getNumber().intValue() : 0) }));
         * return p.getCode();
         * }).collect(Collectors.toList())));
         * em.detach(payroll);
         * payroll.setId(Integer.valueOf(0));
         * } else {
         * payrollMap.put("payrollCode", payroll.getCode());
         * }
         * payrollMap.put("payrollId", payroll.getId());
         * payrollMap.put("byPensionSystem", em.createNativeQuery(
         * "SELECT pc.id_tipomov,pc.concept,(CASE WHEN pc.concept like '%.AFP%' THEN ps.name ELSE '' END) afp,\nround(sum(amount),2) amount FROM rem_payroll_concept pc \nINNER JOIN rem_payroll_people pp ON pp.payroll_id=pc.payroll_id AND pp.people_id=pc.people_id\nINNER JOIN rem_payroll pa ON pa.id=pc.payroll_id \nINNER JOIN rem_payroll_type pt ON pt.id=pa.type_id\nINNER JOIN rh_pension_system ps ON ps.id=pp.pension_system_id\nWHERE (pc.payroll_id=:payrollId OR 0=:payrollId) AND pa.year=:year AND pa.month=:month AND pt.group_id=:groupId AND pc.id_tipomov>0\nGROUP BY pc.id_tipomov,pc.concept,3\norder by pc.id_tipomov,3"
         * )
         * 
         * .setParameter("payrollId", payroll.getId())
         * .setParameter("year", payroll.getYear())
         * .setParameter("month", payroll.getMonth())
         * .setParameter("groupId", payroll.getType().getGroupId())
         * .getResultList());
         * 
         * List<Object[]> data = em.createNativeQuery(
         * "SELECT pp.m_nemonico,count(distinct pp.people_id),\nround(sum(case WHEN pc.id_tipomov=1 THEN amount else 0 END),2) amount11,\nround(sum(0),2) amount14,\nround(sum(case WHEN pc.concept_id=80 THEN amount else 0 END),2) amount12,\nround(sum(case WHEN pc.concept_id=80 OR pc.id_tipomov=1 THEN amount else 0 END),2) total FROM rem_payroll_concept pc \nINNER JOIN rem_payroll_people pp ON pp.payroll_id=pc.payroll_id AND pp.people_id=pc.people_id\nINNER JOIN rem_payroll pa ON pa.id=pc.payroll_id \nINNER JOIN rem_payroll_type pt ON pt.id=pa.type_id\nWHERE (pc.payroll_id=:payrollId OR 0=:payrollId) AND pa.year=:year AND pa.month=:month AND pt.group_id=:groupId GROUP BY pp.m_nemonico\norder by 1,2"
         * )
         * .setParameter("payrollId", payroll.getId()).setParameter("year",
         * payroll.getYear())
         * .setParameter("month", payroll.getMonth()).setParameter("groupId",
         * payroll.getType().getGroupId())
         * .getResultList();
         * int t = 0;
         * for (Object[] e : data) {
         * t += XUtil.intValue(e[1]);
         * }
         * payrollMap.put("workerCount", Integer.valueOf(t));
         * payrollMap.put("byMnemonico", data);
         * result.add(payrollMap);
         * if (group.intValue() == 6) {
         * break;
         * }
         * }
         * return result;
         */
        return null;
    }

    public List<PayrollPeople> getPayrollPeopleList(Object payrollId0, Integer group) {
        /*
         * EntityManager em = getEntityManager();
         * int columns = 13;
         * Payroll payroll0 = (Payroll) em.find(Payroll.class, payrollId0);
         * List<PayrollPeople> result = new ArrayList<>();
         * List<Payroll> payrollList = (group.intValue() > 0) ? em.createQuery(
         * "SELECT p FROM Payroll p join p.type pt where p.year=:year AND p.month=:month AND pt.groupId=:groupId"
         * )
         * .setParameter("groupId",
         * payroll0.getType().getGroupId()).setParameter("year", payroll0.getYear())
         * .setParameter("month", payroll0.getMonth()).getResultList()
         * : Arrays.<Payroll>asList(new Payroll[] { payroll0 });
         * for (Payroll payroll : payrollList) {
         * payroll.setCode(String.format("%02d", new Object[] { payroll.getMonth() })
         * + payroll.getYear().toString().substring(2) + "-"
         * + String.format("%04d", new Object[] {
         * Integer.valueOf(XUtil.intValue(payroll.getNumber())) }));
         * payroll.setMonthName(
         * (new DateFormatSymbols()).getMonths()[payroll.getMonth().intValue() -
         * 1].toUpperCase());
         * 
         * List<Concept> conceptList = em.createQuery(
         * "SELECT DISTINCT c FROM PayrollConcept pc INNER JOIN Concept c ON c.id=pc.id.conceptId WHERE pc.id.payrollId=:payroll AND c.typeId>0 ORDER BY c.typeId,c.weight"
         * )
         * .setParameter("payroll", payroll.getId()).getResultList();
         * Map<Object, Concept> conceptMap = new HashMap<>();
         * Map<Object, Integer> positionMap = new HashMap<>();
         * int pos = 0;
         * Object[] header = null;
         * List<Object[]> headerList = new ArrayList();
         * List<BigDecimal[]> summaryList = new ArrayList();
         * for (Concept c : conceptList) {
         * conceptMap.put(c.getId(), c);
         * if (pos >= columns) {
         * pos = 0;
         * }
         * if (c.getTypeId().intValue() > 1 && headerList.size() < 3) {
         * pos = 0;
         * 
         * while (headerList.size() < 2) {
         * headerList.add(header = new Object[columns]);
         * summaryList.add(new BigDecimal[columns]);
         * }
         * }
         * 
         * if (pos == 0) {
         * headerList.add(header = new Object[columns]);
         * summaryList.add(new BigDecimal[columns]);
         * }
         * header[pos] = XUtil.isEmpty(c.getAbbreviation(), c.getName());
         * positionMap.put(c.getId(), Integer.valueOf((headerList.size() - 1) * columns
         * + pos++));
         * }
         * 
         * List<PayrollPeople> l0 = new ArrayList<>();
         * Map<Object, PayrollPeople> peopleMap = new HashMap<>();
         * for (Object[] oo : em.createQuery(
         * "SELECT pc,pe,em FROM PayrollPeople pc LEFT JOIN Employee em ON em.id=pc.employeeId LEFT JOIN People pe ON (1*pe.code)=pc.id.peopleId WHERE pc.id.payrollId=:payroll AND (pc.id.peopleId>0 OR pc.id.peopleId<-10000000) ORDER BY pe.fullName"
         * )
         * .setParameter("payroll", payroll.getId())
         * .getResultList()) {
         * PayrollPeople payrollPeople = (PayrollPeople) oo[0];
         * em.detach(payrollPeople);
         * payrollPeople.setPayroll(payroll);
         * payrollPeople.setPeople((People) oo[1]);
         * 
         * if (XUtil.intValue(payrollPeople.getJurisdiction()) > 0) {
         * String jurisdiction = String.format("%06d", new Object[] {
         * payrollPeople.getJurisdiction() });
         * try {
         * payrollPeople.setJurisdictionName((String) em
         * .createQuery("SELECT p.name FROM Province p WHERE p.code=:jurisdiction",
         * String.class)
         * .setParameter("jurisdiction", jurisdiction.substring(0, 4))
         * .setMaxResults(1).getSingleResult());
         * } catch (NoResultException noResultException) {
         * }
         * }
         * 
         * if (payrollPeople.getTotalDesc() == null) {
         * payrollPeople.setTotalDesc(BigDecimal.ZERO);
         * }
         * if (payrollPeople.getDependencyId() != null) {
         * Dependency dependency = (Dependency) em.find(Dependency.class,
         * payrollPeople.getDependencyId());
         * if (dependency.getParentId() != null && dependency.getmNemonico() == null) {
         * Dependency parent = (Dependency) em.find(Dependency.class,
         * dependency.getParentId());
         * if (parent.getmNemonico() != null) {
         * dependency = parent;
         * }
         * }
         * 
         * payrollPeople.setDependency(dependency);
         * }
         * Employee employee = (Employee) oo[2];
         * System.out.println("==========" + employee);
         * if (employee != null) {
         * 
         * payrollPeople.setEmployee(employee);
         * payrollPeople.setIncomeDate(employee.getIncomeDate());
         * }
         * HashMap<Object, Object> ext = new HashMap<>();
         * ext.put("header", headerList);
         * List<Object[]> amountList = new ArrayList();
         * for (int i = 0; i < headerList.size(); i++) {
         * amountList.add(new Object[columns]);
         * }
         * if (amountList.size() > 2) {
         * Object[] row = amountList.get(2);
         * for (int j = 0; j < row.length; j++) {
         * row[j] = BigDecimal.ZERO;
         * }
         * }
         * payrollPeople.setPayrollConceptCollection(new ArrayList());
         * ext.put("concept", amountList);
         * ext.put("concepts", new ArrayList());
         * ext.put("summary", summaryList);
         * payrollPeople.setExt(ext);
         * peopleMap.put(Integer.valueOf(payrollPeople.getId().getPeopleId()),
         * payrollPeople);
         * l0.add(payrollPeople);
         * }
         * 
         * List<PayrollConcept> l2 = em
         * .createQuery(
         * "SELECT pc FROM PayrollConcept pc WHERE pc.id.payrollId=:payroll ORDER BY pc.conceptTypeId"
         * )
         * .setParameter("payroll", payroll.getId()).getResultList();
         * 
         * for (PayrollConcept pc : l2) {
         * PayrollPeople pp = peopleMap.get(Integer.valueOf(pc.getId().getPeopleId()));
         * if (pp == null) {
         * continue;
         * }
         * 
         * Map ext = (Map) pp.getExt();
         * List<Object[]> l3 = (List<Object[]>) ext.get("concept");
         * 
         * ((List<PayrollConcept>) ext.get("concepts")).add(pc);
         * pp.getPayrollConceptCollection().add(pc);
         * 
         * if (pc.getId().getConceptId() == 80) {
         * pp.setEsSalud(pc.getAmount());
         * }
         * Integer po = positionMap.get(Integer.valueOf(pc.getId().getConceptId()));
         * if (po != null) {
         * ((Object[]) l3.get(Math.floorDiv(po.intValue(), columns)))[po.intValue() %
         * columns] = pc
         * .getAmount();
         * BigDecimal[] bd = summaryList.get(Math.floorDiv(po.intValue(), columns));
         * if (bd[po.intValue() % columns] == null) {
         * bd[po.intValue() % columns] = pc.getAmount();
         * continue;
         * }
         * bd[po.intValue() % columns].add(pc.getAmount());
         * }
         * }
         * 
         * while (headerList.size() < 5) {
         * headerList.add(new Object[columns]);
         * summaryList.add(new BigDecimal[columns]);
         * }
         * for (PayrollPeople pp : l0) {
         * List<Object[]> l3 = (List<Object[]>) ((Map) pp.getExt()).get("concept");
         * while (l3.size() < 5) {
         * l3.add(new Object[13]);
         * }
         * List<PayrollConcept> c = pp.getPayrollConceptCollection();
         * int maxc = (int) Math.ceil(c.size() / 4.0D);
         * short col = 0, k = 0;
         * for (PayrollConcept pc : c) {
         * pc.setCol(col);
         * k = (short) (k + 1);
         * if (k >= maxc) {
         * k = 0;
         * col = (short) (col + 1);
         * }
         * }
         * }
         * 
         * Collections.sort(l0, (Comparator<? super PayrollPeople>) new Object(this));
         * 
         * result.addAll(l0);
         * }
         * return result;
         */
        return null;
    }

    public void addConcept(Integer payrollId, List<PayrollConcept> persons) {
        /*
         * EntityManager em = getEntityManager();
         * 
         * for (PayrollConcept item : persons) {
         * if (item.getId() == null) {
         * Map ext = (Map) item.getExt();
         * PayrollConceptPK pk = new PayrollConceptPK(payrollId.intValue(),
         * XUtil.intValue(ext.get("peopleId")),
         * XUtil.intValue(ext.get("conceptId")));
         * PayrollConcept payrollConcept1 = (PayrollConcept)
         * em.find(PayrollConcept.class, pk);
         * if (payrollConcept1 == null) {
         * item.setId(pk);
         * Concept concept = (Concept) em.find(Concept.class,
         * Integer.valueOf(item.getId().getConceptId()));
         * item.setConcept(concept.getName());
         * item.setConceptTypeId(concept.getTypeId());
         * item.setIdTipomov(concept.getTypeId());
         * em.persist(item);
         * continue;
         * }
         * payrollConcept1.setAmount(item.getAmount());
         * em.merge(payrollConcept1);
         * continue;
         * }
         * PayrollConcept payrollConcept = (PayrollConcept)
         * em.find(PayrollConcept.class, item.getId());
         * payrollConcept.setAmount(item.getAmount());
         * em.merge(payrollConcept);
         * }
         */
    }

    public Object generate(Integer id) {
        /*
         * EntityManager em = getEntityManager();
         * Payroll payroll = (Payroll) em.find(Payroll.class, id);
         * if (XUtil.intValue(payroll.getNumber()) == 0) {
         * payroll.setNumber(Integer.valueOf(1 + XUtil.intValue(em
         * .createQuery("SELECT max(p.number) FROM Payroll p WHERE p.year=:year AND p.month=:month"
         * ,
         * Integer.class)
         * .setParameter("year", payroll.getYear())
         * .setParameter("month", payroll.getMonth())
         * .getSingleResult())));
         * }
         * em.createQuery(
         * """
         * DELETE FROM PayrollConcept pc WHERE pc.id.conceptId in (56,58,59,80) AND
         * pc.id.payrollId=:payrollId
         * """)
         * .setParameter("payrollId", id).executeUpdate();
         * List<Object[]> employeeList = em.createQuery(
         * "SELECT e,pp FROM Employee e,PayrollPeople pp WHERE (1*e.people.code)=pp.id.peopleId AND pp.id.payrollId=:id"
         * ,
         * Object[].class).setParameter("id", id).getResultList();
         * Map<Object, PayrollPeople> payrollPeopleMap = new HashMap<>();
         * for (Object[] emplo : employeeList) {
         * Employee employee = (Employee) emplo[0];
         * PayrollPeople payrollPeople = (PayrollPeople) emplo[1];
         * payrollPeople.setEmployeeId(employee.getId());
         * payrollPeople.setPensionSystem(employee.getPensionSystem());
         * payrollPeople.setCuspp(employee.getCuspp());
         * payrollPeopleMap.put(Integer.valueOf(payrollPeople.getId().getPeopleId()),
         * payrollPeople);
         * }
         * Calendar calendar = Calendar.getInstance();
         * calendar.set(1, payroll.getYear().intValue());
         * calendar.set(2, payroll.getMonth().intValue() - 1);
         * calendar.set(5, 1);
         * Date from = calendar.getTime();
         * calendar.add(2, 1);
         * calendar.add(5, -1);
         * Date to = calendar.getTime();
         * 
         * List<Contract> contracs = em.createQuery(
         * "SELECT c FROM Contract c WHERE c.canceled=0 AND  (1*c.people.code) IN (SELECT pp.id.peopleId FROM PayrollPeople pp WHERE pp.id.payrollId=:payroll) AND (c.fechaFin>=:from OR c.fechaFin IS NULL) AND c.fechaIni<=:to ORDER BY c.charge ASC,c.active DESC,c.fechaIni DESC"
         * )
         * .setParameter("from", from).setParameter("to", to).setParameter("payroll",
         * id).getResultList();
         * Map<Object, Object> mm = new HashMap<>();
         * contracs.forEach(contract -> {
         * Object oo =
         * mm.get(Integer.valueOf(Integer.parseInt(contract.getPeople().getCode())));
         * 
         * if (oo == null) {
         * mm.put(Integer.valueOf(Integer.parseInt(contract.getPeople().getCode())),
         * Boolean.valueOf(true));
         * } else {
         * return;
         * }
         * 
         * PayrollPeople payrollPeople = (PayrollPeople) payrollPeopleMap
         * .get(Integer.valueOf(Integer.parseInt(contract.getPeople().getCode())));
         * 
         * payrollPeople.setDependencyId(contract.getDependencyId());
         * 
         * Position position = contract.getPosition();
         * 
         * if (position != null) {
         * payrollPeople.setCargoFunc(position.getName());
         * }
         * 
         * if (payrollPeople.getDependencyId() != null) {
         * Dependency dependency = (Dependency) em.find(Dependency.class,
         * payrollPeople.getDependencyId());
         * 
         * if (dependency.getParentId() != null && dependency.getmNemonico() == null) {
         * Dependency parent = (Dependency) em.find(Dependency.class,
         * dependency.getParentId());
         * 
         * if (parent.getmNemonico() != null) {
         * dependency = parent;
         * }
         * }
         * 
         * payrollPeople.setmNemonico(dependency.getmNemonico());
         * 
         * payrollPeople.setDependency(dependency);
         * }
         * 
         * if (contract.getRemunerativeLevelId() != null) {
         * RemunerativeLevel remunerativeLevel = (RemunerativeLevel)
         * em.find(RemunerativeLevel.class,
         * contract.getRemunerativeLevelId());
         * 
         * payrollPeople.setRemunerativeLevelId(remunerativeLevel.getId());
         * 
         * payrollPeople.setRemunerativeLevel(remunerativeLevel.getName());
         * }
         * payrollPeople.setResolucion(contract.getDocument());
         * int j = XUtil.intValue(contract.getProvinceId());
         * if (j > 0) {
         * payrollPeople.setJurisdiction(Integer.valueOf(j));
         * }
         * if (contract.getPosition() != null) {
         * payrollPeople.setPosition(contract.getPosition().getName());
         * }
         * em.merge(payrollPeople);
         * });
         * Map<Object, BigDecimal> totalIngrMap = new HashMap<>();
         * for (Object[] row : em.createQuery(
         * "SELECT pc.id.peopleId,ROUND(SUM(pc.amount),2) FROM PayrollConcept pc INNER JOIN Concept co ON co.id=pc.id.conceptId WHERE pc.id.payrollId=:payrollId AND pc.idTipomov=1 GROUP BY pc.id.peopleId"
         * )
         * .setParameter("payrollId", id).getResultList()) {
         * totalIngrMap.put(row[0], (BigDecimal) row[1]);
         * }
         * 
         * System.out.println(totalIngrMap);
         * for (PayrollConcept payrollConcept : em.createQuery(
         * "SELECT pc FROM PayrollConcept pc WHERE pc.id.conceptId IN (55,63) AND pc.id.payrollId=:payroll"
         * ,
         * PayrollConcept.class)
         * .setParameter("payroll", id).getResultList()) {
         * PayrollPeople payrollPeople =
         * payrollPeopleMap.get(Integer.valueOf(payrollConcept.getId().getPeopleId()));
         * 
         * if (payrollPeople != null) {
         * payrollPeople.setTotalIngr(totalIngrMap.get(Integer.valueOf(payrollPeople.
         * getId().getPeopleId())));
         * if (payrollPeople != null
         * && !payrollPeopleMap.containsKey("p" + payrollConcept.getId().getPeopleId()))
         * {
         * payrollPeople.setMontoRem(payrollConcept.getAmount());
         * em.merge(payrollPeople);
         * if (payrollConcept.getId().getConceptId() == 63) {
         * payrollPeopleMap.put("p" + payrollConcept.getId().getPeopleId(),
         * payrollPeople);
         * }
         * }
         * }
         * }
         * List<PayrollAmount> amounts =
         * em.createQuery("SELECT pa FROM PayrollAmount pa").getResultList();
         * List<Integer> aux = new ArrayList();
         * for (Object[] employee : employeeList) {
         * PayrollPeople payrollPeople = (PayrollPeople) employee[1];
         * if (aux.contains(Integer.valueOf(payrollPeople.getId().getPeopleId()))) {
         * continue;
         * }
         * aux.add(Integer.valueOf(payrollPeople.getId().getPeopleId()));
         * 
         * PensionSystem pensionSystem = payrollPeople.getPensionSystem();
         * for (PayrollAmount amount : amounts) {
         * 
         * if ("PS".equals(amount.getType())) {
         * if (pensionSystem.getId() != null &&
         * amount.getTargetId().equals(pensionSystem.getId())) {
         * 
         * if (amount.getConceptId().intValue() == 59 ||
         * amount.getConceptId().intValue() == 79) {
         * if (amount.getConceptId().intValue() == 59) {
         * 
         * PayrollConceptPK payrollConceptPK = new PayrollConceptPK(
         * payrollPeople.getId().getPayrollId(), payrollPeople.getId().getPeopleId(),
         * amount.getConceptId().intValue());
         * PayrollConcept payrollConcept1 = (PayrollConcept)
         * em.find(PayrollConcept.class,
         * payrollConceptPK);
         * if (payrollConcept1 == null) {
         * payrollConcept1 = new PayrollConcept(payrollConceptPK);
         * }
         * payrollConcept1.setAmount(
         * ((payrollPeople.getTotalIngr() != null) ? payrollPeople.getTotalIngr()
         * : BigDecimal.ZERO).multiply(amount.getAmount()));
         * payrollConcept1.setConceptTypeId(Integer.valueOf(2));
         * payrollConcept1.setIdTipomov(Integer.valueOf(2));
         * payrollConcept1.setConcept(((Concept) em.find(Concept.class,
         * Integer.valueOf(payrollConcept1.getId().getConceptId()))).getName());
         * em.persist(payrollConcept1);
         * }
         * continue;
         * }
         * PayrollConceptPK pk = new
         * PayrollConceptPK(payrollPeople.getId().getPayrollId(),
         * payrollPeople.getId().getPeopleId(), amount.getConceptId().intValue());
         * PayrollConcept payrollConcept = (PayrollConcept)
         * em.find(PayrollConcept.class, pk);
         * if (payrollConcept == null) {
         * payrollConcept = new PayrollConcept(pk);
         * }
         * payrollConcept.setAmount(((payrollPeople.getTotalIngr() != null) ?
         * payrollPeople.getTotalIngr()
         * : BigDecimal.ZERO).multiply(amount.getAmount()));
         * payrollConcept.setConceptTypeId(Integer.valueOf(2));
         * payrollConcept.setIdTipomov(Integer.valueOf(2));
         * payrollConcept.setConcept(((Concept) em.find(Concept.class,
         * Integer.valueOf(payrollConcept.getId().getConceptId()))).getName());
         * em.persist(payrollConcept);
         * }
         * continue;
         * }
         * if ("TR".equals(amount.getType())) {
         * if (payrollPeople.getId().getPeopleId() == amount.getTargetId().intValue()) {
         * Concept concept = (Concept) em.find(Concept.class, amount.getConceptId());
         * 
         * PayrollConceptPK pk = new
         * PayrollConceptPK(payrollPeople.getId().getPayrollId(),
         * payrollPeople.getId().getPeopleId(), amount.getConceptId().intValue());
         * PayrollConcept payrollConcept = (PayrollConcept)
         * em.find(PayrollConcept.class, pk);
         * if (payrollConcept == null) {
         * payrollConcept = new PayrollConcept(pk);
         * payrollConcept.setAmount(BigDecimal.ZERO);
         * }
         * payrollConcept.setAmount(payrollConcept.getAmount().add(amount.getAmount()));
         * payrollConcept.setConceptTypeId(concept.getTypeId());
         * payrollConcept.setIdTipomov(concept.getTypeId());
         * payrollConcept.setConcept(concept.getName());
         * em.persist(payrollConcept);
         * }
         * continue;
         * }
         * if (amount.getType() == null) {
         * Concept concept = (Concept) em.find(Concept.class, amount.getConceptId());
         * 
         * PayrollConceptPK pk = new
         * PayrollConceptPK(payrollPeople.getId().getPayrollId(),
         * payrollPeople.getId().getPeopleId(), amount.getConceptId().intValue());
         * PayrollConcept payrollConcept = (PayrollConcept)
         * em.find(PayrollConcept.class, pk);
         * if (payrollConcept == null) {
         * payrollConcept = new PayrollConcept(pk);
         * payrollConcept.setAmount(BigDecimal.ZERO);
         * }
         * payrollConcept.setAmount(payrollConcept.getAmount().add(amount.getAmount()));
         * payrollConcept.setConceptTypeId(concept.getTypeId());
         * payrollConcept.setIdTipomov(concept.getTypeId());
         * payrollConcept.setConcept(concept.getName());
         * em.persist(payrollConcept);
         * }
         * }
         * }
         * BigDecimal essalud = BigDecimal.ZERO;
         * PayrollConcept pc = null;
         * for (PayrollConcept e : em
         * .createQuery("SELECT pc FROM PayrollConcept pc WHERE pc.id.conceptId=80 AND pc.id.payrollId=:payroll"
         * ,
         * PayrollConcept.class)
         * .setParameter("payroll", id).getResultList()) {
         * essalud = essalud.add(e.getAmount());
         * if (pc == null) {
         * pc = e;
         * }
         * }
         * 
         * BigDecimal truncated = essalud.setScale(0, 1);
         * if (essalud.compareTo(truncated) > 0 &&
         * pc != null) {
         * pc.setAmount(pc.getAmount().subtract(essalud.subtract(truncated)));
         * em.persist(pc);
         * }
         * 
         * em.createQuery(
         * "UPDATE PayrollPeople pp SET pp.totalDesc=(SELECT ROUND(SUM(pc.amount),2) FROM PayrollConcept pc INNER JOIN Concept co ON co.id=pc.id.conceptId WHERE pc.id.payrollId=pp.id.payrollId AND pc.id.peopleId=pp.id.peopleId AND pc.conceptTypeId=2) WHERE pp.id.payrollId=:payroll"
         * )
         * .setParameter("payroll", id).executeUpdate();
         * payroll.setGenerateDate(X.getServerDate());
         */
        return Integer.valueOf(1);
    }

    public List getPayrollSummary(Integer i) {
        return null;/*
                     * AbstractFacade.getColumn(getEntityManager().createQuery(
                     * "SELECT co,sum(pc.amount) FROM PayrollConcept pc INNER JOIN Concept co ON co.id=pc.id.conceptId GROUP BY co ORDER BY co.typeId"
                     * )
                     * .getResultList(), (AbstractFacade.RowAdapter) new Object(this));
                     */
    }

    private void importPayrollConcept(List<Object[]> l) {
        /*
         * EntityManager em = getEntityManager();
         * Object[] row = null;
         * Object[] header = l.get(0);
         * Query payrollQuery = em
         * .createQuery("SELECT p FROM Payroll p WHERE p.type.id=:payrollType AND p.month=7 AND p.year=2020"
         * )
         * .setFirstResult(0).setMaxResults(1);
         * 
         * for (int r = 2; r < l.size(); r++) {
         * 
         * row = l.get(r);
         * Payroll payroll = null;
         * 
         * payroll = (Payroll) payrollQuery.setParameter("payrollType",
         * Integer.valueOf(XUtil.intValue(row[1])))
         * .getSingleResult();
         * 
         * int dni = XUtil.intValue(row[0]);
         * 
         * for (int c = 2; c < row.length; c++) {
         * if (c < header.length) {
         * 
         * int conceptId = XUtil.intValue(header[c]);
         * 
         * Concept concept = (Concept) em.find(Concept.class,
         * Integer.valueOf(conceptId));
         * if (concept != null) {
         * BigDecimal amount = null;
         * try {
         * amount = (new BigDecimal(row[c].toString())).setScale(2, 6);
         * } catch (Exception exception) {
         * }
         * 
         * if (amount != null && amount.doubleValue() != 0.0D) {
         * PayrollConceptPK idpc = new PayrollConceptPK(payroll.getId().intValue(), dni,
         * concept.getId().intValue());
         * PayrollConcept pc = (PayrollConcept) em.find(PayrollConcept.class, idpc);
         * if (pc == null) {
         * pc = new PayrollConcept();
         * }
         * pc.setConcept((concept.getName().length() >= 90) ?
         * concept.getName().substring(0, 90)
         * : concept.getName());
         * pc.setConceptTypeId(concept.getTypeId());
         * pc.setIdTipomov(concept.getTypeId());
         * pc.setAmount(amount);
         * if (pc.getId() == null) {
         * pc.setId(idpc);
         * if (idpc.getPeopleId() > 0) {
         * em.persist(pc);
         * }
         * } else {
         * em.merge(pc);
         * }
         * }
         * }
         * }
         * }
         * }
         */
    }

    public Object copy(Integer payroll, Map m) {
        /*
         * EntityManager em = getEntityManager();
         * Payroll p = (Payroll) em.find(Payroll.class, payroll);
         * Payroll p2 = new Payroll();
         * p2.setType(p.getType());
         * p2.setYear(Integer.valueOf(XUtil.intValue(m.get("year"))));
         * p2.setMonth(Integer.valueOf(XUtil.intValue(m.get("month"))));
         * em.persist(p2);
         * 
         * List<PayrollPeople> l = em.
         * createQuery("SELECT pe FROM PayrollPeople pe WHERE pe.id.payrollId=:payroll")
         * .setParameter("payroll", payroll).getResultList();
         * for (PayrollPeople pp : l) {
         * PayrollPeople pp2 = new PayrollPeople();
         * pp2.setId(new PayrollPeoplePK(p2.getId().intValue(),
         * pp.getId().getPeopleId()));
         * 
         * em.persist(pp2);
         * }
         * 
         * List<PayrollConcept> pcl = em
         * .createQuery("SELECT pe FROM PayrollConcept pe WHERE pe.id.payrollId=:payroll AND pe.idTipomov=1"
         * )
         * .setParameter("payroll", payroll).getResultList();
         * for (PayrollConcept pp : pcl) {
         * PayrollConcept pp2 = new PayrollConcept();
         * pp2.setId(new PayrollConceptPK(p2.getId().intValue(),
         * pp.getId().getPeopleId(), pp.getId().getConceptId()));
         * pp2.setAmount(pp.getAmount());
         * pp2.setConcept(pp.getConcept());
         * pp2.setIdTipomov(pp.getIdTipomov());
         * em.persist(pp2);
         * }
         * 
         * return m;
         */
        return null;
    }

    public Object deletePeople(Integer payroll, Integer concept) {
        /*
         * EntityManager em = getEntityManager();
         * PayrollPeople pp = (PayrollPeople) em.find(PayrollPeople.class,
         * new PayrollPeoplePK(payroll.intValue(), concept.intValue()));
         * em.remove(pp);
         */
        return Boolean.valueOf(true);
    }

    // private UserFacade userFacadeLocal;

    EntityManager getEntityManager() {
        return em;
    }

    // private ParametroFacade parametroFacadeLocal;

    private BigDecimal uit;

    private BigDecimal rmv;

    public void initSiaf(List<Payroll> planilla, Map m) {
        if (XUtil.isEmpty(planilla)) {
            System.out.println("No hay planillas seleccionadas");
            return;
        }
        Payroll p = planilla.get(0);
        for (Payroll p2 : planilla) {
            if (!p2.getFuenteFinanc().equals(p.getFuenteFinanc()) || !p2.getModLaboral().equals(p.getModLaboral())
                    || !p2.getType().equals(p.getType())) {
                throw new RuntimeException(
                        "Hay planillas con diferentes fuentes de financiento, modalidad laboral o tipos");
            }
        }
        List l = getEntityManager().createQuery(
                "SELECT p.nombComp FROM Employee p WHERE p.cargo.idCargo=61 AND p.situacion='11' AND p.sector.codSect='00' ")
                .getResultList();
        if (!l.isEmpty()) {
            m.put("nombComp", l.get(0));
        }
    }

    public void update(Payroll p, Map params) {
        /*if ("4".equals(p.getModLaboral().getId()) && p.getPeriodoIni() == null)
            throw new RuntimeException("Ingresar fecha de inicio de la Payroll de Construccion Civil");
        if ("4".equals(p.getModLaboral().getId()) && p.getPeriodoFin() == null) {
            throw new RuntimeException("Ingresar fecha final de la Payroll de Construccion Civil");
        }*/
        if (params.containsKey("tareo")) {
            if (p.getPayrollPeopleCollection().isEmpty()) {
                throw new RuntimeException("Ingresar fecha final de la Payroll de Construccion Civil");
            }
            for (PayrollPeople t : p.getPayrollPeopleCollection()) {
                /*if ((!"4".equals(p.getModLaboral().getId()) && XUtil.doubleValue(t.getMontoRem()) <= 0.0D)
                        || (XUtil.intValue(t.getDiasTot()) < 0 && XUtil.intValue(t.getDiasVac()) < 0)) {
                    throw new RuntimeException("error");
                }*/
            }

            return;
        }
        // p.setTipoTrab(p.getModLaboral().getEmployeeType());
        Employee personal = (Employee) this.sessionBean.get("personal");

        EntityManager em = getEntityManager();
        List<String> error = new ArrayList();
        if (p.getEstado() == null) {
            String anoEje = "" + XDate.getYear(new Date());
            if (params.containsKey("tareo")) {
                int n = 0;
                try {
                    n = XUtil.intValue(em.createQuery(
                            "SELECT MAX(p.planillaPK.nExpediente) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.planillaPK.nExpediente,1,1)='0'")
                            .setParameter("anoEje", anoEje)
                            .getSingleResult());
                } catch (NoResultException noResultException) {
                }

                n++;
                //p.setPayrollPK(new PayrollPK(anoEje, String.format("%08d", new Object[] { Integer.valueOf(n) })));
                //p.setFechaExp(X.getServerDate());
                User u = (User) this.sessionBean.get("_USER");

                n = 0;
                for (PayrollPeople t : p.getPayrollPeopleCollection()) {
                    //t.setNOrden(Integer.valueOf(++n));

                    //if ("27".equals(t.getEmployeeType().getId()));

                }

            } else {

                //p.setFechaExp(X.getServerDate());
                //p.setFechaPlan(X.getServerDate());
                int n = 0;
                try {
                    n = XUtil.intValue(em.createQuery(
                            "SELECT MAX(SUBSTRING(p.nPayroll,2,7)) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.nPayroll,1,1)='0'")
                            .setParameter("anoEje", anoEje)
                            .getSingleResult());
                } catch (NoResultException noResultException) {
                }

                n++;
                //p.setNPayroll(String.format("%08d", new Object[] { Integer.valueOf(n) }));
                //p.setPayrollPK(new PayrollPK(anoEje, "1" + String.format("%07d", new Object[] { Integer.valueOf(n) })));
            }
            p.setEstado(Character.valueOf('2'));
            // create(p);
        } else if (p.getEstado().charValue() == '2') {
            // edit(p);
            if (params.containsKey("tareo")) {
                int n = 0;
                for (PayrollPeople t : p.getPayrollPeopleCollection()) {

                    //if ("27".equals(t.getEmployeeType().getId()))
                      //  ;

                }

            } else {

                /*
                 * Ext ex = (Ext) p.getExt();
                 * if (ex != null && !XUtil.isEmpty(ex.getRemovedList())) {
                 * em.
                 * createQuery("DELETE FROM PayrollDet d WHERE d.planillaTrab IN :planillaTrab")
                 * .setParameter("planillaTrab", ex.getRemovedList())
                 * .executeUpdate();
                 * ex.getRemovedList().stream().forEach(o -> em.remove(o));
                 * }
                 * 
                 * em.createQuery(
                 * "DELETE FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND NOT d.planillaDetPK.codigo='2010' AND (d.monto=0 OR d.monto IS NULL)"
                 * )
                 * 
                 * .setParameter("anoEje", p.getPayrollPK().getAnoEje())
                 * .setParameter("nExpediente", p.getPayrollPK().getNExpediente())
                 * .executeUpdate();
                 * Map<Object, Object[]> m = (Map) new HashMap<>();
                 * 
                 * for (Object[] r : em.createQuery(
                 * "SELECT t.planillaTrabPK.dni,SUM(t.diasNor+t.diasDom-t.diasFalt) FROM PayrollTrab t JOIN t.planilla p WHERE t.planillaTrabPK.anoEje=:anoEje AND p.mes=:mes AND p.estado>'2' AND NOT t.planillaTrabPK.nExpediente=:nExpediente GROUP BY t.planillaTrabPK.dni"
                 * )
                 * 
                 * .setParameter("anoEje", p.getPayrollPK().getAnoEje())
                 * //.setParameter("mes", p.getMes())
                 * .setParameter("nExpediente", p.getPayrollPK().getNExpediente())
                 * .getResultList()) {
                 * m.put(r[0], r);
                 * }
                 * int daysMonth =
                 * XDate.getDaysOfMonth(XUtil.intValue(p.getPayrollPK().getAnoEje()),
                 * XUtil.intValue(p.getMes().getMesEje()) - 1);
                 * for (PayrollTrab t : p.getPayrollTrabCollection()) {
                 * Object[] r = m.get(t.getPayrollTrabPK().getDni());
                 * if (r != null &&
                 * XUtil.intValue(r[1]) + t.getDiasNor().intValue() - t.getDiasFer().intValue()
                 * > daysMonth) {
                 * error.add("Numero documento - " + t.getPayrollTrabPK().getDni() + ": Tiene "
                 * + (XUtil.intValue(r[1]) + t.getDiasNor().intValue() -
                 * t.getDiasFer().intValue())
                 * + " dias laborados dentro del mes de " + daysMonth);
                 * }
                 * }
                 * 
                 * m.clear();
                 * 
                 * for (Object[] r : em.createQuery(
                 * "SELECT d.planillaDetPK.dni,SUM(CASE WHEN d.ind='1' THEN d.monto ELSE 0 END),SUM(CASE WHEN d.ind='2' THEN d.monto ELSE 0 END),SUM(CASE WHEN d.ind='3' THEN d.monto ELSE 0 END) FROM PayrollDet d JOIN d.planillaTrab t WHERE t.planilla=:planilla GROUP BY d.planillaDetPK.dni"
                 * )
                 * 
                 * .setParameter("planilla", p).getResultList()) {
                 * m.put(r[0], r);
                 * }
                 * for (PayrollTrab t : p.getPayrollTrabCollection()) {
                 * Object[] r = m.get(t.getPayrollTrabPK().getDni());
                 * if (r != null) {
                 * t.setTotalIngr((BigDecimal) r[1]);
                 * t.setTotalDesc((BigDecimal) r[2]);
                 * t.setTotalApor((BigDecimal) r[3]);
                 * if (t.getTotalIngr() == null || t.getTotalIngr() == BigDecimal.ZERO) {
                 * error.add("Numero documento - " + t.getPayrollTrabPK().getDni()
                 * + ": El trabajador no tiene ningun concepto de ingreso.");
                 * continue;
                 * }
                 * if (t.getTotalDesc() != null && t.getTotalDesc().compareTo(t.getTotalIngr())
                 * >= 0) {
                 * error.add("Numero documento - " + t.getPayrollTrabPK().getDni()
                 * + ": Sus descuentos del trabajador son iguales o superan sus ingresos.");
                 * }
                 * }
                 * 
                 * }
                 */
            }
        } else {

            // edit(p);
        }
    }

    public Page<Payroll> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        System.out.println("filters=" + filters);
        Object anoEje = filters.get("anoEje");
        Object dni = filters.get("dni");
        int bandeja = XUtil.intValue(filters.get("bandeja"));
        Object secFunc = filters.get("secFunc");
        int mes = XUtil.intValue(filters.get("mesEje"));
        Object dependency = XUtil.isEmpty(filters.get("dependency"), null);
        Object code = XUtil.isEmpty(filters.get("code"), null);
        Object company = XUtil.isEmpty(filters.get("company"), null);
        Object type = XUtil.isEmpty(filters.get("type"), null);
        Boolean active = Boolean.valueOf(XUtil.booleanValue(filters.get("contract:active")));
        Object position = XUtil.isEmpty(filters.get("position"), null);
        Object level = XUtil.isEmpty(filters.get("level"), null);
        int option = XUtil.intValue(filters.get("option"));
        /*
         * List<Query> ql = new ArrayList<>();
         * EntityManager em = getEntityManager();
         * String sql;
         * ql.add(em.createQuery("SELECT o,0 " + (sql = "FROM Payroll o WHERE 1=1"
         * + ((type != null) ? " AND UPPER(o.type.name) LIKE :type" : "") +
         * " ORDER BY o DESC")));
         * 
         * if (pageSize > 0) {
         * ((Query) ql.get(0)).setFirstResult(first).setMaxResults(pageSize);
         * ql.add(em.createQuery("SELECT COUNT(o) " + sql));
         * }
         * for (Query q : ql) {
         * if (level != null) {
         * q.setParameter("level", level);
         * }
         * if (code != null) {
         * q.setParameter("code", "%" + code + "%");
         * }
         * if (active.booleanValue()) {
         * q.setParameter("hoy", X.getServerDate());
         * }
         * if (type != null) {
         * q.setParameter("type", "%" + type.toString().toUpperCase().replace(" ", "%")
         * + "%");
         * }
         * }
         * if (pageSize > 0) {
         * filters.put("size", ((Query) ql.get(1)).getSingleResult());
         * }
         * List<Payroll> l = AbstractFacade.getColumn(((Query)
         * ql.get(0)).getResultList());
         * l.forEach(p -> {
         * PayrollType e = p.getType();
         * 
         * if (e != null && e.getGroupId() != null) {
         * PayrollGroup payrollGroup = (PayrollGroup) em.find(PayrollGroup.class,
         * e.getGroupId());
         * if (payrollGroup.getParentId() == null) {
         * e.setMainGroup(payrollGroup);
         * } else {
         * e.setGroup(payrollGroup);
         * e.setMainGroup((PayrollGroup) em.find(PayrollGroup.class,
         * payrollGroup.getParentId()));
         * }
         * }
         * });
         * return l;
         */
        Query q = getEntityManager()
                .createQuery("SELECT p FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje "
                        + ((bandeja == 1) ? " AND SUBSTRING(p.planillaPK.nExpediente,1,1)='0' AND p.estado='1' " : "")
                        + (filters.containsKey("tareo") ? " AND SUBSTRING(p.planillaPK.nExpediente,1,1)='0' " : "")
                        + ((mes > 0) ? " AND p.mes.mesEje=:mes" : "")
                        + ((secFunc != null) ? " AND p.secFunc LIKE :secFunc" : "")
                        + ((dni != null) ? " AND p.elaboradoPor LIKE :dni" : "")
                        + " ORDER BY p.planillaPK.nExpediente DESC")
                .setParameter("anoEje", "" + anoEje);
        if (mes > 0) {
            q.setParameter("mes", String.format("%02d", new Object[] { Integer.valueOf(mes) }));
        }
        if (dni != null) {
            q.setParameter("dni", "%" + dni + "%");
        }
        if (secFunc != null) {
            q.setParameter("secFunc", "%" + secFunc + "%");
        }
        List<Payroll> l = q.getResultList();
        l.stream().forEach(p -> p.setPayrollPeopleCollection(Collections.EMPTY_LIST));

        return null;
    }

    public void procesarCC(Payroll planilla) {
        if (planilla.getEstado().charValue() != '2') {
            throw new SimpleException("No se pueden procesar planillas que esten en estado de Procesado.");
        }

        //String nExpediente = planilla.getId().getNExpediente();
        //String anoEje = planilla.getId().getAnoEje();
        /*
         * Date fecha_planilla = XDate.getDate(XUtil.intValue(anoEje),
         * XUtil.intValue(planilla.getMes().getMesEje()) - 1, 28);
         * System.out.println(XDate.getSQLDate(fecha_planilla));
         * //this.uit = this.parametroFacadeLocal.getBigDecimal(1, fecha_planilla);
         * //this.rmv = this.parametroFacadeLocal.getBigDecimal(2, fecha_planilla);
         * EntityManager em = getEntityManager();
         * em.createQuery(
         * "DELETE FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.codigo IN (SELECT codigo FROM Concepto c where procesar = 'S')"
         * )
         * .setParameter("anoEje", planilla.getPayrollPK().getAnoEje())
         * .setParameter("nExpediente", planilla.getPayrollPK().getNExpediente())
         * .executeUpdate();
         */
    }

    public Payroll copiar(Payroll p, Map m) {
        Payroll np = new Payroll();
        Object mesEje = m.get("mesEje2");
        Object anoEje = m.get("anoEje2");
        EntityManager em = getEntityManager();
        //p.setFechaExp(X.getServerDate());
        //p.setFechaPlan(X.getServerDate());
        //if (anoEje == null) {
          //  anoEje = p.getId().getAnoEje();
        //}
        int nn = 0;
        try {
            nn = XUtil.intValue(em.createQuery(
                    "SELECT MAX(SUBSTRING(p.nPayroll,2,7)) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.nPayroll,1,1)='0'")
                    .setParameter("anoEje", anoEje)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
        }

        nn++;
        // np.setNPayroll(String.format("%08d", new Object[] { Integer.valueOf(nn) }));
        //np.setId(
          //      new PayrollPK(anoEje.toString(), "0" + String.format("%07d", new Object[] { Integer.valueOf(nn) })));
        // np.setMes((Mes) em.find(Mes.class, mesEje));
        //np.setElaboradoPor(p.getElaboradoPor());
        //np.setFuenteF(p.getFuenteF());
        np.setModLaboral(p.getModLaboral());

        return np;
    }

    public static void main(String[] args) {
        /*
         * try {
         * ClassPathUtil.load("D:\\proyecto\\bootstrap.json");
         * EntityManager em = JPA.getInstance().getEntityManager();
         * Payroll planilla = (Payroll) em.find(Payroll.class, new PayrollPK("2015",
         * "00000320"));
         * 
         * } catch (Exception e) {
         * e.printStackTrace();
         * } finally {
         * System.exit(0);
         * }
         */
    }

    public void procesarAguinaldo(Payroll planilla) {
        if (planilla.getEstado().charValue() != '2') {
            throw new SimpleException("No se pueden procesar planillas que esten en estado de Procesado.");
        }

        /*
         * Date fecha_planilla =
         * XDate.getDate(XUtil.intValue(planilla.getPayrollPK().getAnoEje()),
         * XUtil.intValue(planilla.getMes().getMesEje()) - 1, 28);
         * BigDecimal agui_julio = null;//this.parametroFacadeLocal.getBigDecimal(3,
         * fecha_planilla);
         * 
         * EntityManager em = getEntityManager();
         * em.createQuery(
         * "DELETE FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.codigo IN (SELECT c.codigo FROM Concepto c WHERE c.procesar='S')"
         * );
         * String nExpediente = planilla.getPayrollPK().getNExpediente();
         * String anoEje = planilla.getPayrollPK().getAnoEje();
         * for (PayrollTrab dw_2 : planilla.getPayrollTrabCollection()) {
         * String dni = dw_2.getPayrollTrabPK().getDni();
         * PayrollDet d = new PayrollDet(nExpediente, anoEje, dni, "1261");
         * d.setInd(Character.valueOf('1'));
         * d.setValor(agui_julio);
         * d.setMonto(agui_julio);
         * em.persist(d);
         * 
         * dw_2.setRemAseg((BigDecimal) XUtil.isEmpty(em
         * .createQuery(
         * "SELECT SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.dni=:dni AND d.concepto.calculo='S' AND SUBSTRING(d.planillaDetPK.codigo,1,1)='1'"
         * ,
         * BigDecimal.class)
         * .setParameter("anoEje", anoEje)
         * .setParameter("nExpediente", nExpediente)
         * 
         * .getSingleResult(), BigDecimal.ZERO));
         * 
         * dw_2.setTotalIngr((BigDecimal) XUtil.isEmpty(em
         * .createQuery(
         * "SELECT SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.dni=:dni AND SUBSTRING(d.planillaDetPK.codigo,1,1)='1'"
         * ,
         * BigDecimal.class)
         * .setParameter("anoEje", anoEje)
         * .setParameter("nExpediente", nExpediente)
         * 
         * .getSingleResult(), BigDecimal.ZERO));
         * 
         * dw_2.setTotalApor(new BigDecimal("0"));
         * }
         */
    }

    public BigDecimal calculaCAFAE(BigDecimal monto_total, int dias_falt, int min_falt, int dias_total) {
        System.out.println("monto_total=" + monto_total + ";dias_falt=" + dias_falt + ";min_falt=" + min_falt
                + ";dias_total=" + dias_total);
        double dias_x = 0.0D;
        if (min_falt > 0 && min_falt < 51) {
            dias_x = 0.5D;
        } else if (min_falt > 50 && min_falt < 81) {
            dias_x = 1.0D;
        } else if (min_falt > 80 && min_falt < 121) {
            dias_x = 2.0D;
        } else if (min_falt > 120 && min_falt < 181) {
            dias_x = 3.0D;
        } else if (min_falt > 180 && min_falt < 241) {
            dias_x = 4.5D;
        } else if (min_falt > 240 && min_falt < 1000) {
            dias_x = 6.0D;
        }
        return monto_total
                .divide(new BigDecimal("" + dias_total), 10, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("" + (dias_falt + dias_x))).setScale(2, RoundingMode.HALF_UP);
    }

    public int getDaysInMontn(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month);
        return calendar.getActualMaximum(5);
    }

    private BigDecimal calculaEsSalud(String anoEje, String nExpediente, String mes, String dni,
            BigDecimal remAsegurable) {
        BigDecimal id_ess_calc = null;

        EntityManager em = getEntityManager();
        BigDecimal id_essalud = (BigDecimal) XUtil.isEmpty(em.createQuery(
                "SELECT SUM(C.monto) FROM PayrollTrab B, PayrollDet C WHERE B.planilla.estado>'2' AND B.planillaTrabPK.anoEje=:anoEje AND B.planilla.mes.mesEje=:mes AND B.planillaTrabPK.dni=:dni AND NOT B.planillaTrabPK.nExpediente=:nExpediente AND C.planillaDetPK.anoEje=B.planillaTrabPK.anoEje AND C.planillaDetPK.nExpediente=B.planillaTrabPK.nExpediente AND C.planillaDetPK.dni=B.planillaTrabPK.dni AND C.planillaDetPK.codigo='3010'",
                BigDecimal.class)
                .setParameter("dni", dni)
                .setParameter("anoEje", anoEje)
                .setParameter("mes", mes)
                .setParameter("nExpediente", nExpediente)
                .getSingleResult(), BigDecimal.ZERO);
        BigDecimal id_rem_aseg = (BigDecimal) XUtil.isEmpty(em.createQuery(
                "SELECT SUM(C.monto) FROM PayrollTrab B, PayrollDet C WHERE B.planilla.estado>'2' AND B.planillaTrabPK.anoEje=:anoEje AND B.planilla.mes.mesEje=:mes AND B.planillaTrabPK.dni=:dni AND NOT B.planillaTrabPK.nExpediente=:nExpediente AND C.planillaDetPK.anoEje=B.planillaTrabPK.anoEje AND C.planillaDetPK.nExpediente=B.planillaTrabPK.nExpediente AND C.planillaDetPK.dni=B.planillaTrabPK.dni AND C.concepto.calculo='S' AND C.ind='1'",
                BigDecimal.class)
                .setParameter("dni", dni)
                .setParameter("anoEje", anoEje)
                .setParameter("mes", mes)
                .setParameter("nExpediente", nExpediente)
                .getSingleResult(), BigDecimal.ZERO);
        id_rem_aseg = id_rem_aseg.add(remAsegurable);
        if (id_rem_aseg.doubleValue() < this.rmv.doubleValue()) {
            id_ess_calc = this.rmv.multiply(new BigDecimal("0.09")).setScale(2, RoundingMode.HALF_UP);
        } else {
            id_ess_calc = id_rem_aseg.multiply(new BigDecimal("0.09")).setScale(2, RoundingMode.HALF_UP);
        }
        if (id_ess_calc.doubleValue() < id_essalud.doubleValue()) {
            return new BigDecimal("0");
        }
        return id_ess_calc.subtract(id_essalud);
    }

    public void procesarNombrado(Payroll planilla) {
        BigDecimal ld_monto_tot = null, ld_rem_aseg = null, ld_5ta = null;
        //String anoEje = planilla.getPayrollPK().getAnoEje();
        //String mes = planilla.getMes().getMesEje();
        //Date fecha_planilla = XDate.getDate(XUtil.intValue(anoEje), XUtil.intValue(planilla.getMes().getMesEje()), 28);
        // this.uit = this.parametroFacadeLocal.getBigDecimal(1, fecha_planilla);
        // this.rmv = this.parametroFacadeLocal.getBigDecimal(2, fecha_planilla);

        /*String nExpediente = planilla.getPayrollPK().getNExpediente();
        EntityManager em = getEntityManager();
        em.createQuery(
                "DELETE FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.codigo IN (SELECT c.codigo FROM Concepto c WHERE c.procesar='S')")
                .setParameter("anoEje", anoEje)
                .setParameter("nExpediente", nExpediente)
                .executeUpdate();*/
        for (PayrollPeople planillaTrab : planilla.getPayrollPeopleCollection()) {
/* 
            int n_dias = planillaTrab.getDiasNor().intValue();
            if (n_dias == getDaysInMontn(XUtil.intValue(mes), XUtil.intValue(anoEje))) {
                n_dias = 30;
            }
            String dni = planillaTrab.getPayrollTrabPK().getDni();

            if (planilla.getType().getId().intValue() != 8 && planilla.getType().getId().intValue() != 9) {
                PayrollDet d;

                switch (XUtil.intValue(planilla.getModLaboral().getId())) {
                    case 1:
                    case 2:
                    case 8:
                        for (Object[] pd : em.createQuery(
                                "SELECT pd.personalDsctoPK.codigo,pd.monto FROM EmployeeDscto pd WHERE pd.personalDsctoPK.dni=:dni AND pd.estado = 'A' AND SUBSTRING(pd.personalDsctoPK.codigo,1,1)= '1' AND NOT pd.personalDsctoPK.codigo='1200' AND NOT pd.personalDsctoPK.codigo='1220'",
                                Object[].class)
                                .setParameter("dni", dni)
                                .getResultList()) {
                            PayrollDet planillaDet = new PayrollDet(nExpediente, anoEje, dni,
                                    Integer.parseInt(pd[0].toString()));
                            planillaDet.setInd(Character.valueOf('1'));
                            planillaDet.setValor((BigDecimal) pd[1]);
                            planillaDet.setMonto(((BigDecimal) pd[1]).multiply(new BigDecimal("" + n_dias))
                                    .divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP));
                            em.persist(planillaDet);
                        }
                        break;
                    case 5:
                        d = new PayrollDet(nExpediente, anoEje, dni, 1150);
                        d.setInd(Character.valueOf('1'));
                        d.setValor(planillaTrab.getMontoRem().multiply(new BigDecimal("" + n_dias)).divide(
                                new BigDecimal("30"), 2,
                                RoundingMode.HALF_UP));
                        d.setMonto(d.getValor());
                        em.persist(d);
                        break;
                    case 9:
                        d = new PayrollDet(nExpediente, anoEje, dni, 1000);
                        d.setInd(Character.valueOf('1'));
                        d.setValor(planillaTrab.getMontoRem().multiply(new BigDecimal("" + n_dias)).divide(
                                new BigDecimal("30"), 2,
                                RoundingMode.HALF_UP));
                        d.setMonto(d.getValor());
                        em.persist(d);
                        break;
                }

                ld_rem_aseg = (BigDecimal) XUtil.isEmpty(em.createQuery(
                        "SELECT SUM(pd.monto) FROM PayrollDet pd WHERE pd.planillaDetPK.anoEje=:anoEje AND pd.planillaDetPK.nExpediente=:nExpediente AND pd.planillaDetPK.dni=:dni AND pd.concepto.calculo='S'  AND SUBSTRING(pd.planillaDetPK.codigo,1,1) = '1'",
                        BigDecimal.class)
                        .setParameter("anoEje", anoEje)
                        .setParameter("nExpediente", nExpediente)
                        .setParameter("dni", dni)
                        .getSingleResult(), BigDecimal.ZERO);

                ld_monto_tot = (BigDecimal) XUtil.isEmpty(em.createQuery(
                        "SELECT SUM(pd.monto) FROM PayrollDet pd WHERE pd.planillaDetPK.anoEje=:anoEje AND pd.planillaDetPK.nExpediente=:nExpediente AND pd.planillaDetPK.dni=:dni AND SUBSTRING(pd.planillaDetPK.codigo,1,1)='1'",
                        BigDecimal.class)
                        .setParameter("anoEje", anoEje)
                        .setParameter("nExpediente", nExpediente)
                        .setParameter("dni", dni)
                        .getSingleResult(), BigDecimal.ZERO);

                BigDecimal ld_cafae = calculaCAFAE(ld_monto_tot, planillaTrab.getDiasFalt().intValue(),
                        XUtil.intValue(planillaTrab.getMinTard()), n_dias);
                System.out.println("valor Cafae=" + ld_cafae);
                if (ld_cafae.doubleValue() > 0.0D) {
                    d = new PayrollDet(nExpediente, anoEje, dni, 2090);
                    d.setInd(Character.valueOf('2'));
                    d.setValor(ld_cafae);
                    d.setMonto(ld_cafae);
                    em.persist(d);
                    ld_rem_aseg = ld_rem_aseg.subtract(ld_cafae);
                }
            }

            planillaTrab.setRemAseg(ld_rem_aseg);
            planillaTrab.setTotalIngr(ld_monto_tot);
            if (!planilla.getType().getId().equals("8") && !planilla.getType().getId().equals("9")) {
                BigDecimal ld_essalud;

                PayrollPeople d;

                if (XUtil.intValue(planilla.getModLaboral().getId()) == 8) {
                    BigDecimal ld_onp = ld_rem_aseg.multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);
                    if (ld_onp.doubleValue() > 0.0D) {
                        PayrollDet planillaDet = new PayrollDet(nExpediente, anoEje, dni, 2021);
                        planillaDet.setInd(Character.valueOf('2'));
                        planillaDet.setPorcen(new BigDecimal("4"));
                        planillaDet.setMonto(ld_onp);
                        em.persist(planillaDet);
                    }
                }

                BigDecimal ld_aporte_trab = (BigDecimal) em.createQuery(
                        "SELECT SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente = :nExpediente AND d.planillaDetPK.dni=:dni AND SUBSTRING(d.concepto.codPDT,1,2) = '06'",
                        BigDecimal.class).setParameter("anoEje", anoEje).setParameter("nExpediente", nExpediente)
                        .setParameter("dni", dni).getSingleResult();
                int n = 0;
                X.log("INSERTAR DESCUENTOS JUDICIALES");

                switch (Integer.valueOf(planilla.getModLaboral().getId())) {
                    case 1:
                    case 2:
                    case 9:*/
                        /*
                         * ld_essalud = calculaEsSalud(anoEje, nExpediente,
                         * planilla.getMes().getMesEje(), dni, ld_rem_aseg);
                         * if (ld_essalud.doubleValue() > 0.0D) {
                         * PayrollDet planillaDet = new PayrollDet(nExpediente, anoEje, dni, "3010");
                         * planillaDet.setInd(Character.valueOf('3'));
                         * planillaDet.setPorcen(new BigDecimal("9"));
                         * planillaDet.setMonto(ld_essalud);
                         * em.persist(planillaDet);
                         * planillaTrab.setTotalApor(ld_essalud);
                         * }
                         */
                       /*  break;
                    case 5:
                        if (ld_rem_aseg.doubleValue() < this.rmv.doubleValue()) {
                            ld_essalud = this.rmv.multiply(new BigDecimal("0.09")).setScale(n_dias,
                                    RoundingMode.HALF_UP);
                        } else {
                            BigDecimal aux = this.uit.multiply(new BigDecimal("0.3"));
                            if (ld_rem_aseg.doubleValue() > aux.doubleValue()) {
                                ld_essalud = aux.multiply(new BigDecimal("0.09")).setScale(2, RoundingMode.HALF_UP);
                            } else {
                                ld_essalud = ld_rem_aseg.multiply(new BigDecimal("0.09")).setScale(2,
                                        RoundingMode.HALF_UP);
                            }
                        }
                        d = new PayrollDet(nExpediente, anoEje, dni, 3010);
                        d.setInd(Character.valueOf('3'));
                        d.setPorcen(new BigDecimal("9"));
                        d.setMonto(ld_essalud);
                        em.persist(d);
                        planillaTrab.setTotalApor(ld_essalud);
                        break;
                }
            }
            planillaTrab.setTotalDesc((BigDecimal) em.createQuery(
                    "SELECT SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente = :nExpediente AND d.planillaDetPK.dni = :dni AND SUBSTRING(d.planillaDetPK.codigo,1,1) = '2'",
                    BigDecimal.class)
                    .setParameter("anoEje", anoEje)
                    .setParameter("dni", dni)
                    .setParameter("nExpediente", nExpediente)
                    .getSingleResult());
            planillaTrab.setTotalApor((BigDecimal) em.createQuery(
                    "SELECT SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente = :nExpediente AND d.planillaDetPK.dni = :dni AND SUBSTRING(d.planillaDetPK.codigo,1,1) = '3'",
                    BigDecimal.class)
                    .setParameter("anoEje", anoEje)
                    .setParameter("dni", dni)
                    .setParameter("nExpediente", nExpediente)
                    .getSingleResult());
            System.out.println("ACTUALIZA PLANILLA TRAB");
            em.merge(planillaTrab);*/
        }
    }

    public List getResultList(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Payroll p = (Payroll) filters.get("planilla");
        if (p != null) {
            getEntityManager().createQuery("SELECT t.planilla.fuenteFinanc FROM PayrollTrab t");

            /*List<Object[]> l = getEntityManager().createQuery(
                    "SELECT '01',t.planillaTrabPK.dni,t.planilla.fuenteFinanc,d.ind,d.planillaDetPK.codigo,'pen',d.monto FROM PayrollTrab t,PayrollDet d WHERE t.planillaTrabPK.anoEje=:anoEje AND t.planillaTrabPK.nExpediente=:nExpediente AND t.planillaTrabPK.anoEje=d.planillaDetPK.anoEje AND t.planillaTrabPK.nExpediente=d.planillaDetPK.nExpediente AND t.planillaTrabPK.dni=d.planillaDetPK.dni ")
                    .setParameter("anoEje", p.getPayrollPK().getAnoEje())
                    .setParameter("nExpediente", p.getPayrollPK().getNExpediente()).getResultList();

            Object[] head = { "001102", p.getPayrollPK().getAnoEje(), p.getMes().getMesEje(), "01", "01", "01",
                    Integer.valueOf(l.size()), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
                    new BigDecimal("0"),
                    new BigDecimal("0"), new BigDecimal("0") };
            for (Object[] v : l) {
                int ind = XUtil.intValue(v[3]);
                if (ind > 0 && ind < 7) {
                    head[6 + ind] = ((BigDecimal) head[6 + ind]).add(new BigDecimal(v[6].toString()));
                }
            }
            l.add(0, head);
            return l;*/
        }
        return Collections.EMPTY_LIST;
    }

    public void prepare(Payroll p) {
        EntityManager em = getEntityManager();
        String anoEje = "" + XDate.getYear(new Date());
        String mesEje = String.format("%02d", new Object[] { Integer.valueOf(XDate.getMonth(new Date())) });
        int n = 0;
        try {
            n = XUtil.intValue(em.createQuery(
                    "SELECT MAX(SUBSTRING(p.nPayroll,2,7)) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.nPayroll,1,1)='0'")
                    .setParameter("anoEje", anoEje)
                    .getSingleResult());
        } catch (NoResultException noResultException) {
        }

        n++;
        p.setPayrollPeopleCollection(new ArrayList());
        //p.setNPayroll(String.format("%08d", new Object[] { Integer.valueOf(n) }));
        //p.setPayrollPK(new PayrollPK(anoEje, "1" + String.format("%07d", new Object[] { Integer.valueOf(n) })));

        // p.setMes(new Mes("01", mesEje));

        //p.setFechaPlan(X.getServerDate());
    }

    private BigDecimal getRetenida5ta(String anoEje, String nExpediente, int mes, String dni) {
        return (BigDecimal) XUtil.isEmpty(getEntityManager().createQuery(
                "SELECT sum(c.monto) FROM PayrollTrab b JOIN b.planilla a, PayrollDet c where b.planillaTrabPK.anoEje = c.planillaDetPK.anoEje AND b.planillaTrabPK.nExpediente = c.planillaDetPK.nExpediente AND b.planillaTrabPK.dni = c.planillaDetPK.dni AND b.planillaTrabPK.anoEje = :anoEje AND a.mes.nMes <= :mes AND a.estado >= '3' AND a.planillaPK.nExpediente <> :nExpediente AND b.planillaTrabPK.dni =:dni AND c.planillaDetPK.codigo = '2010'",
                BigDecimal.class)
                .setParameter("anoEje", anoEje)
                .setParameter("dni", dni)
                .setParameter("mes", Integer.valueOf(mes))
                .setParameter("nExpediente", nExpediente)
                .getSingleResult(), BigDecimal.ZERO);
    }

    private BigDecimal calcula5ta(String anoEje, String nExpediente, int mes, String dni, BigDecimal monto_basico,
            BigDecimal monto_neto) {
        BigDecimal Impuesto5ta;
        int mes_calc = 0, mes_div = 0;

        BigDecimal BC = monto_basico.multiply(new BigDecimal("" + (12 - mes))).add(monto_neto);
        BC = BC.add(bc5ta(anoEje, nExpediente, mes, dni))
                .subtract(this.uit.multiply(new BigDecimal("7")).setScale(2, RoundingMode.HALF_UP));
        switch (mes) {
            case 1:
            case 2:
            case 3:
                mes_calc = 0;
                mes_div = 12;
            case 4:
                mes_calc = 3;
                mes_div = 9;
            case 5:
            case 6:
            case 7:
                mes_calc = 4;
                mes_div = 8;
            case 8:
                mes_calc = 7;
                mes_div = 5;
            case 9:
            case 10:
            case 11:
                mes_calc = 8;
                mes_div = 4;
            case 12:
                mes_calc = 12;
                mes_div = 1;
                break;
        }
        BigDecimal Impuesto5taRet = getRetenida5ta(anoEje, nExpediente, mes_calc, dni);
        if (BC.doubleValue() > 0.0D) {
            BC = BC.subtract(Impuesto5taRet);
            if (BC.doubleValue() > 0.0D) {
                Impuesto5ta = BC
                        .multiply(
                                (new BigDecimal("0.15")).divide(new BigDecimal("" + mes_div), 2, RoundingMode.HALF_UP));
            } else {
                Impuesto5ta = new BigDecimal("0");
            }
        } else {
            Impuesto5ta = new BigDecimal("0");
        }
        return Impuesto5ta;
    }

    private BigDecimal bc5ta(String anoEje, String nExpediente, int mes, String dni) {
        EntityManager em = getEntityManager();

        BigDecimal monto_ingreso = ((BigDecimal) XUtil.isEmpty(em.createQuery(
                "SELECT sum(c.monto) FROM PayrollTrab b JOIN b.planilla a, PayrollDet c where b.planillaTrabPK.anoEje =c.planillaDetPK.anoEje AND b.planillaTrabPK.nExpediente = c.planillaDetPK.nExpediente AND b.planillaTrabPK.dni = c.planillaDetPK.dni AND b.planillaTrabPK.anoEje = :anoEje AND a.mes.nMes <= :mes AND a.estado >= '3' AND a.planillaPK.nExpediente <> :nExpediente AND b.planillaTrabPK.dni=:dni AND SUBSTRING(c.planillaDetPK.codigo,1,1) = '1' AND b.tipoTrabajador.id <> '67' AND c.planillaDetPK.codigo not in ('1022')",
                BigDecimal.class).setParameter("anoEje", anoEje).setParameter("dni", dni)
                .setParameter("nExpediente", nExpediente).setParameter("mes", Integer.valueOf(mes)).getSingleResult(),
                BigDecimal.ZERO))
                .subtract((BigDecimal) XUtil.isEmpty(em.createQuery(
                        "SELECT sum(c.monto) FROM PayrollTrab b JOIN b.planilla a, PayrollDet c WHERE b.planillaTrabPK.anoEje = c.planillaDetPK.anoEje AND b.planillaTrabPK.nExpediente = c.planillaDetPK.nExpediente AND b.planillaTrabPK.dni = c.planillaDetPK.dni AND a.planillaPK.anoEje = :anoEje AND a.mes.nMes <= :mes AND a.estado >= '3' AND a.planillaPK.nExpediente<>:nExpediente AND b.planillaTrabPK.dni = :dni AND c.planillaDetPK.codigo in ('2090', '2091') AND b.tipoTrabajador.id <> '67'",
                        BigDecimal.class)
                        .setParameter("anoEje", anoEje)
                        .setParameter("dni", dni)
                        .setParameter("mes", Integer.valueOf(mes))
                        .setParameter("nExpediente", nExpediente)
                        .getSingleResult(), BigDecimal.ZERO));
        return (monto_ingreso.doubleValue() > 0.0D) ? monto_ingreso : BigDecimal.ZERO;
    }

    public void autorizar(Payroll planilla, String action, String pass) {
        if ("liberar".equals(action)) {
            /*
             * if (!this.userFacadeLocal.confirm(pass)) {
             * throw new
             * SimpleException("La clave de autorización está errada, porfavor vuelva a intentarlo."
             * );
             * }
             */
            planilla.setEstado(Character.valueOf('3'));
        } else {
            if (planilla.getPayrollPeopleCollection().isEmpty())
                throw new SimpleException("No hay trabajadores registrados en la Payroll");
            if (planilla.getEstado().charValue() != '2')
                throw new SimpleException("El estado es incorrecto para validar la planilla a estado procesado");
            /*
             * if (!this.userFacadeLocal.confirm(pass)) {
             * throw new
             * SimpleException("La clave de autorización está errada, porfavor vuelva a intentarlo."
             * );
             * }
             */
            //planilla.setFechaExp(X.getServerDate());
            //planilla.setEstado(Character.valueOf('3'));
        }
        getEntityManager().persist(planilla);
    }

    public void procesar(Payroll planilla) {
        /*if ("3".equals(planilla.getType().getId())) {
            procesarAguinaldo(planilla);
        } else {
            switch (XUtil.intValue(planilla.getModLaboral().getId())) {
                case 3:
                case 4:
                    procesarCC(planilla);
                    break;
                case 1:
                case 2:
                    procesarNombrado(planilla);
                    break;
                case 5:
                    procesarNombrado(planilla);
                    break;
                case 9:
                    procesarNombrado(planilla);
                    break;
                case 8:
                    procesarNombrado(planilla);
                    break;
            }
        }*/
    }

    public Payroll load(Payroll planilla) {
        if (planilla != null) {
            Payroll p = (Payroll) getEntityManager().find(Payroll.class, planilla.getId());
            p.getPayrollPeopleCollection().size();
            planilla = p;
        }
        return planilla;
    }

    public void add(Payroll planilla, List personalList, boolean tareo) {
        if (planilla.getEstado() != null && planilla.getEstado().charValue() >= '3') {
            throw new SimpleException("No se puede agregar datos de planilla que estén en estado Procesado.");
        }
        EntityManager em = getEntityManager();
        Map<Object, Object> dniCount = new HashMap<>();

        User u = (User) this.sessionBean.get("usuario");
        PayrollPeople planillaTrab = null;
        if (personalList.get(0) instanceof Employee) {
            if (tareo) {
                /*
                 * em.createQuery(
                 * "SELECT t.planillaTrabPK.dni,COUNT(t.planillaTrabPK.dni) FROM PayrollTrab t WHERE t.planillaTrabPK.anoEje=:anoEje AND t.personal IN :personal AND t.planillaTrabPK.nExpediente IN (SELECT p.planillaPK.nExpediente FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND p.mes=:mes AND p.tipoTrab<>:tipoTrab and (p.tipoPayroll.tipoPayroll='1' OR p.tipoPayroll.tipoPayroll='4')) GROUP BY t.planillaTrabPK.dni"
                 * )
                 * .setParameter("anoEje", planilla.getPayrollPK().getAnoEje())
                 * .setParameter("personal", personalList)
                 * .setParameter("mes", planilla.getMes())
                 * .setParameter("tipoTrab", planilla.getTipoTrab())
                 * .getResultList().stream().forEach(row -> dniCount.put(row[0], row));
                 */
            }

            /*
             * for (Employee personal : personalList) {
             * planillaTrab = null;
             * for (PayrollTrab planillaTrab1 : planilla.getPayrollTrabCollection())
             * ;
             * 
             * planillaTrab.setFechaModi(X.getServerDate());
             * 
             * if (u != null)
             * ;
             * 
             * if (!tareo)
             * ;
             * 
             * }
             */

        } else {

            planillaTrab = null;
            /*
             * for (Employee escEmployee : personalList) {
             * People pn = escEmployee.getPeople();
             * planilla.getPayrollTrabCollection()
             * .add(planillaTrab = new PayrollTrab(planilla.getPayrollPK().getNExpediente(),
             * planilla.getPayrollPK().getAnoEje(), pn.getCode()));
             * Employee personal = new Employee();
             * 
             * Object[] HEADER_LIST = (Object[]) planilla.getExt();
             * Object[][] d = new Object[HEADER_LIST.length + 1][3];
             * 
             * d[0][1] = escEmployee.getId();
             * planillaTrab.setExt(d);
             * }
             */
        }
    }

    public Payroll generate(Payroll planilla) {
        planilla = load(planilla);
        /*int np = XUtil.intValue(getEntityManager().createQuery(
                "SELECT MAX(SUBSTRING(p.nPayroll,2,7)) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.nPayroll,1,1)")
                .setParameter("anoEje", planilla.getPayrollPK().getAnoEje())
                .getSingleResult()) + 1;

        planilla.setNPayroll(String.format("%08d", new Object[] { Integer.valueOf(np) }));
        planilla.setFechaPlan(X.getServerDate());*/
        return planilla;
    }

    public void generateAFPNet(Payroll planilla, Map<String, List> mm) {
        System.out.println("generateAFPNet>mm=" + mm);

        List<Object[]> msg = new ArrayList();
        mm.put("msg", msg);
        List afpNetList = mm.get("afpNetList");
        Collection<PayrollPeople> planillaTrabList = planilla.getPayrollPeopleCollection();

        EntityManager em = getEntityManager();
        if (afpNetList.contains("C")) {
            if ("EX".equals(mm.get("action"))) {
                for (int i = 1; i <= planillaTrabList.size() / 100 + 1; i++) {
                    List<Object[]> cupssList = new ArrayList();
                    for (int k = 100 * (i - 1) + 1; k <= i * 100 &&
                            k <= planillaTrabList.size(); k++)
                        ;

                    String docname = "cupss_" + String.format("%02d", new Object[] { Integer.valueOf(i) }) + ".xls";
                    msg.add(new Object[] {
                            "SE GENERO " + docname + ", con " + cupssList.size() + " datos de trabajador(es)",
                            docname, cupssList });
                }

            } else if ("IN".equals(mm.get("action"))) {
                List<Employee> updateList = new ArrayList();
                File f = (File) mm.get(File.class);
                if (f != null && f.getName().toLowerCase().matches(".*cuspp.*[.]csv")) {
                    /*
                     * List<Object[]> cupssList = (List<Object[]>) Traslator.get("csv").read(f,
                     * new XMap(new Object[] { "hasHeader", Boolean.valueOf(true) }));
                     * for (Object[] cupss : cupssList) {
                     * Employee p = (Employee) em.find(Employee.class,
                     * String.format("%08d", new Object[] {
                     * Integer.valueOf(XUtil.intValue(cupss[1])) }));
                     * if (p != null) {
                     * if (XUtil.isEmpty(cupss[5])) {
                     * msg.add(p + "  se cambió a SNP, verifique si el número de DNI es correcto.");
                     * 
                     * updateList.add(p);
                     * for (int m = 1; m <= planillaTrabList.size(); m++)
                     * ;
                     * 
                     * continue;
                     * }
                     * 
                     * msg.add(p + " Nombres AFPNet: " + cupss[6] + " " + cupss[7] + " " +
                     * cupss[8]);
                     * }
                     * }
                     * 
                     * if (!updateList.isEmpty()) {
                     * updateList.stream().forEach(u -> em.merge(u));
                     * 
                     * msg.add("Se actualizó los CUPSS del Maestro de Employee satisfactoriamente."
                     * );
                     * } else {
                     * msg.add("No se han encontrado incosistencias para actualizar.");
                     * }
                     */
                } else {
                    // msg.add(
                    // "No se ha recibido archivo de cuspp de la planilla de trabajadores, debe
                    // cumplir el patron '%cuspp%csv'");
                }
            }
        }
        if (afpNetList.contains("P")) {
            if ("EX".equals(mm.get("action"))) {
                List<Object[]> ds_afpnet = new ArrayList();
                for (int i = 1; i < planillaTrabList.size(); i++)
                    ;

                /*String docname = planilla.getNPayroll() + "-planilla_afp.xls";
                msg.add(
                        new Object[] {
                                "SE GENERO " + docname + ", con " + ds_afpnet.size()
                                        + " datos de trabajador(es) en el SPP.",
                                docname, ds_afpnet });*/
            } else if ("IN".equals(mm.get("action"))) {

                File f = (File) mm.get(File.class);

                if (f != null && f.getName().endsWith(".txt")) {
                    ArrayList updateList = new ArrayList();
                    /*
                     * ((List) (new TraslatorCSV()).read(f, new XMap(new Object[] { "simple",
                     * Boolean.valueOf(true) }))).stream()
                     * .forEach(linea -> {
                     * 
                     * });
                     */

                    if (!updateList.isEmpty()) {
                        updateList.stream().forEach(t -> em.merge(t));

                        // msg.add("Se actualizó el SPP del Maestro de Employee satisfactoriamente.");
                    } else {
                        // msg.add("No se han encontrado incosistencias para actualizar.");
                    }
                }
            }
        }
    }

    private void f_inserta_afp(PayrollPeople pt, BigDecimal aporte_afp, BigDecimal comision_afp, BigDecimal prima_afp) {
        Payroll p = pt.getPayroll();
        EntityManager em = getEntityManager();
        em.createQuery(
                "DELETE FROM PayrollDet d WHERE d.planillaTrab=:planillaTrab AND d.planillaDetPK.codigo IN ('2040', '2041', '2042')")
                .setParameter("planillaTrab", pt)
                .executeUpdate();

        /*PayrollPeople d = new PayrollPeople(pt.getId().getNExpediente(), pt.getPayrollTrabPK().getAnoEje(),
                pt.getPayrollTrabPK().getDni(), 2040);
        d.setInd(Character.valueOf('2'));
        d.setValor(aporte_afp);
        d.setMonto(aporte_afp);
        em.persist(d);

        d = new PayrollPeople(pt.getPayrollTrabPK().getNExpediente(), pt.getPayrollTrabPK().getAnoEje(),
                pt.getPayrollTrabPK().getDni(), 2041);
        d.setInd(Character.valueOf('2'));
        d.setValor(comision_afp);
        d.setMonto(comision_afp);
        em.persist(d);

        d = new PayrollDet(pt.getPayrollTrabPK().getNExpediente(), pt.getPayrollTrabPK().getAnoEje(),
                pt.getPayrollTrabPK().getDni(), 2042);
        d.setInd(Character.valueOf('2'));
        d.setValor(prima_afp);
        d.setMonto(prima_afp);
        em.persist(d);*/
    }

    private void validaRegPension(Payroll planilla, List error) {
        /*
         * Map<Object, Object> m = new HashMap<>();
         * EntityManager em = getEntityManager();
         * for (Object[] r : em.createQuery(
         * "SELECT d.planillaDetPK.dni,SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.codigo IN ('2030','2031') GROUP BY d.planillaDetPK.dni"
         * )
         * .setParameter("anoEje", planilla.getPayrollPK().getAnoEje())
         * .setParameter("nExpediente", planilla.getPayrollPK().getNExpediente())
         * .getResultList()) {
         * m.put(r[0], r[1]);
         * }
         * for (Object[] r : em.createQuery(
         * "SELECT d.planillaDetPK.dni,SUM(d.monto) FROM PayrollDet d WHERE d.planillaDetPK.anoEje=:anoEje AND d.planillaDetPK.nExpediente=:nExpediente AND d.planillaDetPK.codigo IN ('2040', '2041', '2042') GROUP BY d.planillaDetPK.dni"
         * )
         * .setParameter("anoEje", planilla.getPayrollPK().getAnoEje())
         * .setParameter("nExpediente", planilla.getPayrollPK().getNExpediente())
         * .getResultList()) {
         * m.put("AFP-" + r[0], r[1]);
         * }
         * for (PayrollTrab t : planilla.getPayrollTrabCollection()) {
         * Object onp = m.get(t.getPayrollTrabPK().getDni());
         * Object afp = m.get("AFP-" + t.getPayrollTrabPK().getDni());
         * String str = t.getPayrollTrabPK().getDni();
         * }
         */
    }

    public List validar(Payroll planilla, ArrayList<String> error) {
        Collection<PayrollPeople> planillaTrabList = planilla.getPayrollPeopleCollection();
        if (planillaTrabList.size() == 0) {
            error.add("No hay trabajadores registrados en la Payroll");
        }
        if (planilla.getEstado() == null || planilla.getEstado().charValue() != '2') {
            error.add("El estado es incorrecto para validar la planilla a estado procesado");
        }
        HashMap<Object, Object[]> m = (HashMap) new HashMap<>();
        EntityManager em = getEntityManager();
        /*
         * for (Object[] r : em.createQuery(
         * "SELECT d.planillaDetPK.dni,SUM(CASE WHEN d.ind='1' THEN d.monto ELSE 0 END),SUM(CASE WHEN d.ind='2' THEN d.monto ELSE 0 END),SUM(CASE WHEN d.ind='3' THEN d.monto ELSE 0 END) FROM PayrollDet d JOIN d.planillaTrab t WHERE t.planilla=:planilla GROUP BY d.planillaDetPK.dni"
         * )
         * .setParameter("planilla", planilla).getResultList()) {
         * m.put(r[0], r);
         * }
         * for (PayrollTrab t : planilla.getPayrollTrabCollection()) {
         * Object[] r = m.get(t.getPayrollTrabPK().getDni());
         * if (r != null) {
         * if (r[1] == null || BigDecimal.ZERO.equals(r[1])) {
         * error.add("Numero documento - " + t.getPayrollTrabPK().getDni()
         * + ": El trabajador no tiene ningun concepto de ingreso.");
         * continue;
         * }
         * if (r[2] != null && ((BigDecimal) r[2]).compareTo((BigDecimal) r[1]) >= 0) {
         * error.add("Numero documento - " + t.getPayrollTrabPK().getDni()
         * + ": Sus descuentos del trabajador son iguales o superan sus ingresos.");
         * }
         * }
         * }
         */

        validaRegPension(planilla, error);
        return error;
    }

    public int getMax(String anoEje, boolean tareo) {
        if (tareo) {
            return XUtil.intValue(getEntityManager().createQuery(
                    "SELECT MAX(p.planillaPK.nExpediente) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND SUBSTRING(p.planillaPK.nExpediente,1,1)")
                    .setParameter("anoEje", anoEje)
                    .getSingleResult());
        }
        return XUtil.intValue(
                getEntityManager()
                        .createQuery("SELECT MAX(p.nPayroll) FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje")
                        .setParameter("anoEje", anoEje)
                        .getSingleResult());
    }

    public void validateTareo(Payroll p, Map<String, List> m) {
        /*
         * String htareoText = (String) m.get("htareoText");
         * if (htareoText != null) {
         * List error = new ArrayList();
         * User u = (User) this.sessionBean.get("usuario");
         * List<PayrollTrab> l = p.getPayrollTrabCollection();
         * Object[] dh = (Object[]) m.get("dh");
         * List<String> dni = new ArrayList();
         * dni.add("");
         * for (String s : htareoText.split("[|]")) {
         * String[] arrayOfString = s.split(";");
         * }
         * 
         * Map<Object, Object> m2 = new HashMap<>();
         * for (Object[] r : getEntityManager().createQuery(
         * "SELECT t.planillaTrabPK.dni,COUNT(t.planillaTrabPK.dni) FROM PayrollTrab t WHERE t.planillaTrabPK.anoEje=:anoEje AND t.planillaTrabPK.dni IN :dni AND t.planillaTrabPK.nExpediente IN (SELECT p.planillaPK.nExpediente FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND p.mes.mesEje=:mesEje AND p.tipoPayroll.tipoPayroll='1') GROUP BY t.planillaTrabPK.dni"
         * )
         * .setParameter("anoEje", p.getPayrollPK().getAnoEje())
         * .setParameter("dni", dni)
         * .setParameter("mesEje", p.getMes().getMesEje())
         * .getResultList()) {
         * m2.put(r[0], r);
         * }
         * String sss = "";
         * for (int i = 0; i < 31; i++) {
         * sss = sss + ",SUM(CASE WHEN t.d" + String.format("%02d", new Object[] {
         * Integer.valueOf(i + 1) })
         * + " IS NULL THEN 0 ELSE 1 END)";
         * }
         * for (Object[] r :
         * getEntityManager().createQuery("SELECT t.planillaTrabPK.dni" + sss
         * +
         * " FROM PayrollTrab t WHERE t.planillaTrabPK.anoEje=:anoEje AND t.planillaTrabPK.dni IN :dni AND t.planillaTrabPK.nExpediente IN (SELECT p.planillaPK.nExpediente FROM Payroll p WHERE p.planillaPK.anoEje=:anoEje AND p.mes.mesEje=:mesEje AND p.tipoPayroll.tipoPayroll<>'7' AND p.planillaPK.nExpediente<>:nExpediente)  GROUP BY t.planillaTrabPK.dni"
         * )
         * 
         * .setParameter("nExpediente", p.getPayrollPK().getNExpediente())
         * .setParameter("anoEje", p.getPayrollPK().getAnoEje())
         * .setParameter("dni", dni)
         * .setParameter("mesEje", p.getMes().getMesEje())
         * .getResultList()) {
         * m2.put("D" + r[0], r);
         * }
         * for (String s : htareoText.split("[|]")) {
         * String[] ss = s.split(";");
         * PayrollTrab t = l.get(XUtil.intValue(ss[0]));
         * 
         * System.out.println(t);
         * char[] days = ss[1].toCharArray();
         * Character[] nd = new Character[days.length];
         * 
         * int DT = 0, DF = 0, DFT = 0, DD = 0, DM = 0, DV = 0;
         * int dd = 0;
         * for (int j = 0; j < days.length; j++) {
         * Object[] r = (Object[]) m2.get(t.getPayrollTrabPK().getDni());
         * if (r != null && XUtil.intValue(r[1]) == 0) {
         * Object[] rd = (Object[]) m2.get("D" + t.getPayrollTrabPK().getDni());
         * if (rd != null &&
         * XUtil.intValue(rd[j + 1]) != 0) {
         * dd++;
         * 
         * continue;
         * }
         * }
         * Character c = Character.valueOf(days[j]);
         * int d = ((Integer) ((Object[]) dh[j])[0]).intValue();
         * switch (c.charValue()) {
         * case 'X':
         * if (d == -1) {
         * c = Character.valueOf('F');
         * DF++;
         * break;
         * }
         * if (d == 1) {
         * c = Character.valueOf('d');
         * DD++;
         * break;
         * }
         * DT++;
         * break;
         * 
         * case 'x':
         * if (d == -1 || d == 1) {
         * DFT++;
         * if (d == 1) {
         * DD++;
         * } else {
         * DF++;
         * }
         * c = Character.valueOf('x');
         * break;
         * }
         * c = null;
         * break;
         * 
         * case 'D':
         * DM++;
         * break;
         * case 'V':
         * DV++;
         * break;
         * case 'd':
         * DD++;
         * break;
         * case 'F':
         * DF++;
         * break;
         * default:
         * c = null;
         * break;
         * }
         * nd[j] = c;
         * switch (j) {
         * case 0:
         * t.setD01(c);
         * break;
         * case 1:
         * t.setD02(c);
         * break;
         * case 2:
         * t.setD03(c);
         * break;
         * case 3:
         * t.setD04(c);
         * break;
         * case 4:
         * t.setD05(c);
         * break;
         * case 5:
         * t.setD06(c);
         * break;
         * case 6:
         * t.setD07(c);
         * break;
         * case 7:
         * t.setD08(c);
         * break;
         * case 8:
         * t.setD09(c);
         * break;
         * case 9:
         * t.setD10(c);
         * break;
         * case 10:
         * t.setD11(c);
         * break;
         * case 11:
         * t.setD12(c);
         * break;
         * case 12:
         * t.setD13(c);
         * break;
         * case 13:
         * t.setD14(c);
         * break;
         * case 14:
         * t.setD15(c);
         * break;
         * case 15:
         * t.setD16(c);
         * break;
         * case 16:
         * t.setD17(c);
         * break;
         * case 17:
         * t.setD18(c);
         * break;
         * case 18:
         * t.setD19(c);
         * break;
         * case 19:
         * t.setD20(c);
         * break;
         * case 20:
         * t.setD21(c);
         * break;
         * case 21:
         * t.setD22(c);
         * break;
         * case 22:
         * t.setD23(c);
         * break;
         * case 23:
         * t.setD24(c);
         * break;
         * case 24:
         * t.setD25(c);
         * break;
         * case 25:
         * t.setD26(c);
         * break;
         * case 26:
         * t.setD27(c);
         * break;
         * case 27:
         * t.setD28(c);
         * break;
         * case 28:
         * t.setD29(c);
         * break;
         * case 29:
         * t.setD30(c);
         * break;
         * case 30:
         * t.setD31(c);
         * break;
         * }
         * continue;
         * }
         * t.setDiasNor(Integer.valueOf(DT));
         * t.setDiasFer(Integer.valueOf(DF));
         * t.setDiasFtrab(Integer.valueOf(0));
         * t.setExtraHours(Integer.valueOf(DFT * 8));
         * t.setDiasDom(Integer.valueOf(DD));
         * t.setDiasDm(Integer.valueOf(DM));
         * t.setDiasVac(Integer.valueOf(DV));
         * t.setDiasTot(Integer.valueOf(DT + DD + DM));
         * t.setFechaModi(X.getServerDate());
         * if (u != null) {
         * //People personaNatural = (People) u.getExt();
         * //t.setDniModi(personaNatural.getCode());
         * }
         * }
         * m.put("error", error);
         * }
         * m.put("error", Collections.EMPTY_LIST);
         */
    }

    public void actualizar(Payroll p) {
        if (p.getEstado() != null && p.getEstado().charValue() != '0')
            throw new RuntimeException("No se puede actualizar datos de planilla que estén en estado Procesado.");
        if (p.getPayrollPeopleCollection().isEmpty()) {
            throw new RuntimeException("No hay trabajadores en la planilla para actualizar sus datos.");
        }
        EntityManager em = getEntityManager();
        em.createQuery(
                "SELECT p FROM Employee p WHERE p.situacion='11' AND p.modLaboral=:modLaboral AND p.secFun=:secFun AND p.fuenteF=:fuenteF",
                Employee.class)

                .setParameter("modLaboral", p.getModLaboral())
                // .setParameter("secFun", p.getSecFunc())
                .setParameter("fuenteF", p.getFuenteFinanc())
                .getResultList();
        for (PayrollPeople t : p.getPayrollPeopleCollection()) {
            Employee pe = (Employee) em.find(Employee.class, t.getId().getEmployeeId());
            if (pe != null)
                ;
        }
    }
}