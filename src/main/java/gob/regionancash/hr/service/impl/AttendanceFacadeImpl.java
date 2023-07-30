package gob.regionancash.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.isobit.app.X;
import org.isobit.app.service.UserFacade;
import org.isobit.directory.model.Dependency;
import org.isobit.directory.model.People;
import org.isobit.util.XDate;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

import gob.regionancash.hr.dto.AssistPeople;
import gob.regionancash.hr.dto.AssistSummary;
import gob.regionancash.hr.dto.DaySummary;
import gob.regionancash.hr.model.Attendance;
import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.DevicePeople;
import gob.regionancash.hr.model.Holiday;
import gob.regionancash.hr.model.License;
import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.service.AttendanceFacade;
import gob.regionancash.zk.LogData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class AttendanceFacadeImpl implements AttendanceFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserFacade userFacade;

    class AtendanseDetail {

    }

    class TimeTable {

        int[] enterTime = new int[]{7, 45, 11};

        int[] breakTime = new int[]{13, 0};//new int[]{13, 40};

        int[] breakTimeLimit = new int[]{14, 15, 6};

        int[] outTime = new int[]{17, 0};

    }

    public static boolean isSameDay(Date date1, Date date2) {
        Instant instant1 =(date1 instanceof java.sql.Date?((java.sql.Date) date1).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant():date1.toInstant())
                .truncatedTo(ChronoUnit.DAYS);
        Instant instant2 = (date2 instanceof java.sql.Date?((java.sql.Date) date2).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant():date2.toInstant())
                .truncatedTo(ChronoUnit.DAYS);
        return instant1.equals(instant2);

    }

    @Override
    public List load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object date = XUtil.isEmpty(filters.get("date"), null);
        Object people = XUtil.isEmpty(filters.get("people"), null);

        Object code2 = XUtil.isEmpty(filters.get("code"), null);
        List<Query> ql = new ArrayList();
        String sql;
        //Solo recupera las personas y fechas
        ql.add(em.createQuery("SELECT DISTINCT "
                + "(CASE WHEN o.peopleId=0 THEN o.code ELSE o.peopleId END),"
                + "o.date,"
                + "p "
                + (sql = "FROM Attendance o JOIN People p ON o.peopleId=p.id WHERE 1=1 "
                + (date != null ? " AND DATE(o.date)=DATE(:date) " : "")
                + (people != null ? " AND UPPER(p.fullName) LIKE :people" : "")
                + (code2 != null ? " AND UPPER(p.code) LIKE :code" : "")
                + "  ORDER by DATE(o.date) DESC,p.fullName")));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        Date today = new Date();
        for (Query q : ql) {
            if (people != null) {
                q.setParameter("people", "%" + people.toString().toUpperCase().replace(" ", "%") + "%");
            }
            if (code2 != null) {
                q.setParameter("code", "%" + code2.toString().toUpperCase().replace(" ", "%") + "%");
            }
            if (date != null) {
                q.setParameter("date", date);
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List attendanceItemList = new ArrayList();
        int key = 0;
        Calendar c = Calendar.getInstance();
        List peopleList = new ArrayList();
        Date from = null;
        Date to = null;
        if (first == 0) {
            to = new Date();
        }
        HashMap mm = new HashMap();
        int i = 0;
        TimeTable timeTable = new TimeTable();
        for (Object[] row : (List<Object[]>) ql.get(0).getResultList()) {
            AssistPeople assistPeople = new AssistPeople();
            Long code = (Long) row[0];
            //Attendance attendance = (Attendance) row[1];
            //ap.setCode(""+attendance.getCode());
            if (row[2] != null) {
                People pe = (People) row[2];
                assistPeople.setIdDir(pe.getId());
                assistPeople.setCode(pe.getCode());
                assistPeople.setFullName(pe.getFullName());
                code = (long) pe.getId();
            }
            assistPeople.setTimes(new ArrayList());
            //Save each people for recover the asistence only of the people in the page result
            if (!peopleList.contains(row[0])) {
                peopleList.add(row[0]);
            }
            from = (Date) row[1];
            //Limit date o page result
            if (to == null) {
                to = from;
            }

            assistPeople.setDate(from);
            assistPeople.setTurno(0);
            //Para evitar que se repita codigo-fecha
            mm.put(code + "-" + XDate.toString(from), assistPeople);
            assistPeople.setId(++i);
            attendanceItemList.add(assistPeople);
        }
        int[] timeControl = timeTable.breakTime;
        //System.out.println("mm=" + mm);
        String[] dayName = {"D", "L", "M", "M", "J", "V", "S"};
        if (!attendanceItemList.isEmpty()) {

            List<Attendance> l = em.createQuery("SELECT o FROM Attendance o WHERE ((NOT o.peopleId=0 AND (o.peopleId+0) IN :people) "
                    //aqui puede pasar que 
                    //+ "OR (o.peopleId=0 AND LENGTH(o.code)>2 AND o.code IN :people)"
                    + ") AND DATE(o.date)>=DATE(:from) AND DATE(o.date)<=DATE(:to) ORDER by DATE(o.date) DESC,o.peopleId,o.time ASC")
                    .setParameter("people", peopleList)
                    .setParameter("to", to)
                    .setParameter("from", from)
                    .getResultList();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            c.set(2021, 10, 17, 0, 0, 0);
            Date timeTo17112021 = c.getTime();
            for (Attendance assist : l) {
                Long code = assist.getCode();
                if (assist.getPeopleId() > 0) {
                    code = (long) assist.getPeopleId();
                }
                AssistPeople assistPeople = (AssistPeople) mm.get(code + "-" + XDate.toString(assist.getDate()));
                if (assistPeople != null) {
                    assistPeople.setDate(assist.getDate());
                    assistPeople.getTimes().add(assist);
                    c.setTime(assist.getDate());
                    assistPeople.setDayName(dayName[c.get(Calendar.DAY_OF_WEEK) - 1]);
                }
            }
            long seconds;

            for (AssistPeople assistPeople : (List<AssistPeople>) attendanceItemList) {
                System.out.println(formatDate.format(assistPeople.getDate())+" = "+formatDate.format(today));
                if (isSameDay(assistPeople.getDate(), today)) {

                    timeControl = timeTable.enterTime;
                    c.setTime(today);
                    c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                    c.set(Calendar.MINUTE, timeControl[1]);
                    c.set(Calendar.SECOND, 0);
                    
                    System.out.println(formatDate.format(assistPeople.getDate())+" = "+formatDate.format(today));
                
                    if (today.before(c.getTime())) {
                        
                        assistPeople.setEntryDelay("");
                        assistPeople.setBreakDelay("");
                    } else {
                        timeControl = timeTable.breakTimeLimit;
                        c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                        c.set(Calendar.MINUTE, timeControl[1]);
                        c.set(Calendar.SECOND, 0);
                        if (today.before(c.getTime())) {
                            assistPeople.setBreakDelay("");
                        } else {
                            timeControl = timeTable.outTime;
                            c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                            c.set(Calendar.MINUTE, timeControl[1]);
                            c.set(Calendar.SECOND, 0);
                            if (today.before(c.getTime())) {
                                assistPeople.setOutDelaySeconds(0L);
                            }
                        }
                    }

                }

                for (Attendance assist : assistPeople.getTimes()) {

                    c.setTime(assist.getTime());
                    timeControl = timeTable.breakTime;
                    c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                    c.set(Calendar.MINUTE, timeControl[1]);
                    c.set(Calendar.SECOND, 0);
                    
                    if (assist.getTime().before(c.getTime())) {
                        if (assistPeople.getEntryTime() == null) {
                            assistPeople.setTime(assist.getTime());
                            assistPeople.setEntryTime(assist.getTime());
                            timeControl = timeTable.enterTime;

                            c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                            c.set(Calendar.MINUTE, timeControl[1]);
                            c.set(Calendar.SECOND, 0);
                            seconds = TimeUnit.MILLISECONDS.toSeconds(assistPeople.getTime().getTime() - c.getTimeInMillis());

                            if (seconds > 59) {
                                assistPeople.setEntryDelaySeconds(seconds);
                                assistPeople.setEntryDelay(
                                        (seconds > 60 ? (String.format("%02d", seconds / 60) + "m ") : "")
                                        + ((seconds = (seconds % 60)) > 0 ? (String.format("%02d", seconds) + "s") : "")
                                );
                            }
                        }
                    } else {
                        timeControl = timeTable.outTime;
                        c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                        c.set(Calendar.MINUTE, timeControl[1]);
                        c.set(Calendar.SECOND, 0);
                        if (assist.getTime().before(c.getTime())) {
                            if (assistPeople.getBreakTime() == null) {
                                assistPeople.setBreakTime(assist.getTime());
                                timeControl = timeTable.breakTimeLimit;
                                c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                c.set(Calendar.MINUTE, timeControl[1]);
                                c.set(Calendar.SECOND, 0);
                                seconds = TimeUnit.MILLISECONDS.toSeconds(assist.getTime().getTime() - c.getTimeInMillis());
                                if (seconds > 59) {
                                    assistPeople.setBreakDelaySeconds(seconds);
                                    assistPeople.setBreakDelay(
                                            (seconds > 60 ? (String.format("%02d", seconds / 60) + "m ") : "")
                                            + ((seconds = (seconds % 60)) > 0 ? (String.format("%02d", seconds) + "s") : "")
                                    );
                                }
                            }
                        } else if (assistPeople.getOutTime() == null) {
                            assistPeople.setOutTime(assist.getTime());
                        }
                    }
                }
                if (assistPeople.getEntryTime() == null) {
                    assistPeople.setEntryDelay("No marcó");
                    assistPeople.setEntryDelaySeconds(1000L);
                }
                if (assistPeople.getBreakTime() == null && assistPeople.getBreakDelay() == null) {
                    assistPeople.setBreakDelay("No marcó");
                    assistPeople.setBreakDelaySeconds(1000L);
                }
                if (assistPeople.getOutTime() == null) {
                    assistPeople.setOutDelaySeconds(1000L);
                }
            }

            c.set(2021, 10, 17, 0, 0, 0);
            List<License> licenseList = em.createQuery("SELECT o FROM License o WHERE o.peopleId IN :peoples")
                    .setParameter("peoples", peopleList.stream().map((e) -> Integer.parseInt(e.toString())).collect(Collectors.toList()))
                    .getResultList();
            for (License license : licenseList) {
                boolean is = false;
                for (AssistPeople assistPeople : (List<AssistPeople>) attendanceItemList) {
                    if (license.getPeopleId() == assistPeople.getIdDir()) {

                        if (compare(license.getFecIni(), assistPeople.getDate()) <= 0
                                && compare(license.getFecFin(), assistPeople.getDate()) >= 0) {
                            assistPeople.setJustification(license.getType() + " " + license.getDetalle() + " " + license.getFecIni() + "->" + license.getFecFin());
                            is = true;
                        }
                    }
                }
                if (!is && compare(license.getFecFin(), from) >= 0
                        && compare(license.getFecIni(), to) <= 0) {
                    AssistPeople assistPeople = new AssistPeople();
                    People people2 = em.find(People.class, license.getPeopleId());
                    if (people2 != null) {
                        assistPeople.setFullName(people2.getFullName());
                        assistPeople.setCode(people2.getCode());
                        assistPeople.setId(i++);
                        assistPeople.setDate(license.getFecIni());
                        assistPeople.setJustification(license.getType() + " " + license.getDetalle() + " " + license.getFecIni() + "->" + license.getFecFin());
                        attendanceItemList.add(assistPeople);
                    }
                }
            }
            for (Holiday license : em.createQuery("SELECT o FROM Holiday o", Holiday.class).getResultList()) {
                for (AssistPeople assistPeople : (List<AssistPeople>) attendanceItemList) {
                    /*if (!license.getFecIni().before(assistPeople.getDate()) && !license.getFecFin().after(assistPeople.getDate())) {

                        assistPeople.setJustification(license.getType() + " " + license.getDetalle());
                    }*/
                }
            }
        }
        attendanceItemList.sort(new Comparator<AssistPeople>() {
            @Override
            public int compare(AssistPeople o1, AssistPeople o2) {
                int c = o1.getFullName().compareTo(o2.getFullName());
                if (c == 0) {
                    c = -o1.getDate().compareTo(o2.getDate());
                }
                return c;
            }
        });
        return attendanceItemList;
    }

    private int compare(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear()) {
            return d1.getYear() - d2.getYear();
        }
        if (d1.getMonth() != d2.getMonth()) {
            return d1.getMonth() - d2.getMonth();
        }
        return d1.getDate() - d2.getDate();
    }

    @Override
    public Object importFile(File file) {
        ArrayList data = new ArrayList();
        try {
            FileReader fr = new FileReader(file);   //reads the file  
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream 
            String line;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while ((line = br.readLine()) != null) {
                String s[] = line.split("\t", -1);
                Attendance a = new Attendance();
                a.setInOutMode(0);
                a.setPeopleId((long) XUtil.intValue(s[0]));
                //a.setDateTime(formatter.parse(s[1]));
                a.setCreateDate(new Date());
                em.persist(a);
            }
            fr.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public void bulk(List<LogData> list) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        //El error es cuando 01000 y 1000 son codigos diferentes pero de valor igual
        TypedQuery<Number> peopleQuery = em.createQuery("SELECT id FROM People a WHERE a.document.id=4 AND CONCAT('',1*a.code)=:code", Number.class);

        TypedQuery<Number> peopleQuery2 = em.createQuery("SELECT peopleId FROM DevicePeople a WHERE CONCAT('',1*a.enrollNumber)=:code", Number.class).setMaxResults(1);
        TypedQuery<Number> q = em.createQuery("SELECT COUNT(a) FROM Attendance a WHERE a.code=:code AND a.date=:date AND a.time=:time", Number.class);
        for (LogData logData : list) {
            if (logData.getTimestamp() != null) {
                try {
                    calendar.setTime(formatter.parse(logData.getTimestamp()));
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                calendar.set(Calendar.YEAR, logData.getYear());
                calendar.set(Calendar.MONTH, logData.getMonth() - 1);
                calendar.set(Calendar.DAY_OF_MONTH, logData.getDay());
                calendar.set(Calendar.HOUR_OF_DAY, logData.getHour());
                calendar.set(Calendar.MINUTE, logData.getMinute());
                calendar.set(Calendar.SECOND, logData.getSecond());
            }
            //System.out.println(logData.getHour()+":"+logData.getMinute()+":"+logData.getSecond());
            long enrollNumber = logData.getEnrollNumber();
            long peopleId = 0;
            Date dateTime = calendar.getTime();

            if (q.setParameter("code", enrollNumber)
                    .setParameter("date", dateTime)
                    .setParameter("time", dateTime)
                    .getSingleResult().intValue() == 0) {
                Attendance a = new Attendance();
                a.setCode(enrollNumber);
                try {
                    if (enrollNumber < 2000) {
                        peopleId = peopleQuery2.setParameter("code", "" + enrollNumber).getSingleResult().intValue();
                    } else {
                        //Aqui se genera un error
                        peopleId = peopleQuery.setParameter("code", "" + enrollNumber).getSingleResult().intValue();
                    }
                } catch (NonUniqueResultException e) {
                    System.out.println("Ya se encontro marca el " + formatter.format(dateTime) + " para enrollNumber=" + enrollNumber);
                    continue;
                } catch (NoResultException e) {
                    System.out.println("No se encontro el directorio de enrollNumber=" + enrollNumber);
                }
                a.setPeopleId(peopleId);
                a.setDate(calendar.getTime());
                a.setTime(calendar.getTime());
                a.setInOutMode(logData.getInOutMode());
                a.setMachineNumber(logData.getMachineNumber());
                a.setVerifyMode(logData.getVerifyMode());
                a.setCreateDate(X.getServerDate());
                em.persist(a);
                //System.out.println("INSERTED " + a);
            }
        }
    }

    @Override
    public List getPeople(List<Integer> list) {
        List l = new ArrayList();
        List<Object[]> l0 = em.createQuery("SELECT d,p.code,p.fullName FROM DevicePeople d JOIN People p ON p.id=d.peopleId WHERE p.id=d.peopleId AND d.id IN :id")
                .setParameter("id", list).getResultList();
        for (Object[] o : l0) {
            DevicePeople d = (DevicePeople) o[0];
            d.setCode(XUtil.intValue(o[1]));
            d.setFullName((String) o[2]);
            l.add(d);
        }
        return l;
    }

    @Override
    public void save(DevicePeople user) {
        DevicePeople n = em.find(DevicePeople.class, user.getEnrollNumber());
        if (n == null) {
            em.persist(user);
        } else {
            user.setPeopleId(user.getPeopleId());
            em.merge(user);
        }
    }

    @Override
    public Object getReport(Integer dependencyId, Date from, Date to, Map params) {
        Date today = new Date();
        String boss = null;
        try {
            Object[] row = em.createQuery("SELECT p.fullName,d FROM Contract c JOIN c.people p,Dependency d WHERE c.active=1 AND d.positionId=c.positionId AND c.dependencyId=d.id", Object[].class)
                    .setMaxResults(1)
                    .getSingleResult();
            boss = (row[0] + " / " + ((Dependency) row[1]).getFullName()).toUpperCase();
        } catch (NoResultException nre) {

        }
        
        Map licenseTypeMap = new HashMap();
        TimeTable timeTable = new TimeTable();
        licenseTypeMap.put("O", "ONOMASTICO");
        licenseTypeMap.put("P", "PAPELETA");
        licenseTypeMap.put("LS", "LICENCIA SINDICAL");
        licenseTypeMap.put("DMC", "DESCANSO MEDICO COVID");
        licenseTypeMap.put("DM", "DESCANSO MEDICO");

        int option = XUtil.intValue(params.get("option"));
        String groups = (String) params.get("groups");
        Object laborRegimeId = params.get("laborRegimeId");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        //List<AssistSummary> assistPeopleSummaryList = new ArrayList();
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        //int currentMonth = c.get(Calendar.MONTH);
        ArrayList header = new ArrayList();
        char[] dayName = new char[]{'D', 'L', 'M', 'M', 'J', 'V', 'S'};
        Map<Integer, Integer> dayIndex = new HashMap();
        int i = 0, monthLimit = 0, dayOfWeek;
        do {
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek > 1 && dayOfWeek < 7) {
                header.add(dayName[dayOfWeek - 1] + "\n" + c.get(Calendar.DAY_OF_MONTH));
                dayIndex.put(c.get(Calendar.DAY_OF_MONTH), i);
                i++;
                monthLimit++;
            }
            c.add(Calendar.DAY_OF_YEAR, 1);
        } while (!c.getTime().after(to));
        while (24 > header.size()) {
            header.add(' ');
        }
        /*for (i = 0; i < dayIndex.length; i++) {
            if (dayIndex[i] == null) {
                dayIndex[i] = 0;
            }
        }*/
        int DAY_SUMMARY = 15;
        String workMode = (String) params.get("workMode");
        if ("T".equals(workMode)) {
            workMode = null;
        }
        Object people = params.get("people");
        
        System.out.println("people==============="+people);
        Map<Object, AssistSummary> assistMainSummaryMap = new HashMap();
        //Si no hay contrato no saldra el trabajador en el reporte
        Query q = em.createQuery(/*people != null?"SELECT DISTINCT pn,po,lr.name,de,co,em.workModality FROM People pn "
                + "LEFT JOIN Employee em ON em.peopleId=pn.id "
                + "LEFT JOIN Contract co ON co.people=pn "
                + "LEFT JOIN co.position po "
                + "LEFT JOIN co.dependency de "
                + "LEFT JOIN de.type td "
                
                //+ "JOIN DevicePeople dp ON dp.peopleId=em.peopleId "
                + "LEFT JOIN LaborRegime lr ON lr.id=em.laborRegimeId "
                + "WHERE "
                + " co.canceled=0 AND em.canceled=0 "
                + "AND co.fechaIni<=:to "
                + "AND ((co.fechaFin is null AND co.active=1) OR co.fechaFin>=:from) "
                + " AND co.peopleIdLong IN :peopleId "
                + "ORDER BY pn.fullName":*/
                
                
                "SELECT DISTINCT pn,po,lr.name,de,co,em.workModality FROM Employee em "
                + "JOIN People pn ON em.peopleId=pn.id "
                + "LEFT JOIN LaborRegime lr ON lr.id=em.laborRegimeId "
                + "LEFT JOIN Contract co ON co.people=pn "
                + "LEFT JOIN co.position po "
                + "LEFT JOIN co.dependency de "
                + "LEFT JOIN de.type td "
                + "WHERE em.canceled=0 "
                        
                + "AND ("+(people != null ?" co.id is null OR":"")+" (co.canceled=0 AND co.fechaIni<=:to "
                + "AND ((co.fechaFin is null AND co.active=1) OR co.fechaFin>=:from))) "
                        
                + (people != null ? " AND em.peopleIdLong IN :peopleId " : 
                        
                        ("AND not em.workModality IS null "
                        + (workMode != null ? " AND em.workModality=:workMode " : "")
                        + (laborRegimeId != null ? " AND em.laborRegimeId=:laborRegimeId " : "")
                        + "AND NOT em.laborRegimeId=12 "))
                + "ORDER BY pn.fullName"
        ).setParameter("from", from).setParameter("to", to);
        List peopleIdList = new ArrayList();
        if (people != null) {
            peopleIdList.add((long) XUtil.intValue(people));
            q.setParameter("peopleId", peopleIdList);
        } else {
            if (laborRegimeId != null) {
                q.setParameter("laborRegimeId", (short) XUtil.intValue(laborRegimeId));
            }
            if (workMode != null) {
                q.setParameter("workMode", workMode);
            }
        }
        List<Object[]> resultList = (List<Object[]>) q.getResultList();
        c.setTime(to);
        long mainPeriod = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH);
        if (!resultList.isEmpty()) {
            int year, month;
            Integer dayMonth;
            int[] timeControl;
            long period;
            peopleIdList.clear();
            List<Holiday> holidays;
            for (Holiday holiday : (holidays = em.createQuery("SELECT h FROM Holiday h WHERE h.date BETWEEN :from AND :to", Holiday.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList())) {
                if (option < 3) {
                    c.setTime(holiday.getDate());
                    dayMonth = c.get(Calendar.DAY_OF_MONTH);
                    dayMonth = dayIndex.get(dayMonth);
                    if (dayMonth != null) {
                        holiday.setIndex(dayMonth);
                    }
                }
            }
            HashMap<Integer, String> dayNameMap = new HashMap();
            dayNameMap.put(2, "L");
            dayNameMap.put(3, "M");
            dayNameMap.put(4, "M");
            dayNameMap.put(5, "J");
            dayNameMap.put(6, "V");
            List remoteEmployees = new ArrayList();
            for (Object[] o : resultList) {
                People pe = (People) o[0];
                if (peopleIdList.contains(pe.getId().longValue())) {
                    continue;
                }
                //System.out.println("people=" + pe);
                String laborRegime = (String) o[2];
                Contract contract = (Contract) o[4];
                Object workModality = o[5];
                if (workModality != null && "RM".contains(workModality.toString())) {
                    remoteEmployees.add(pe.getId().longValue());
                }
                c.setTime(to);
                while (c.getTime().after(from)) {
                    dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    period = year * 100 + month;
                    AssistSummary assistMainSummary = assistMainSummaryMap.get(option == 3 ? pe.getId().longValue() : period);
                    if (assistMainSummary == null) {
                        assistMainSummaryMap.put(option == 3 ? pe.getId().longValue() : period, assistMainSummary = new AssistSummary());
                    }
                    AssistSummary assistSummary = (AssistSummary) assistMainSummary.get(option == 3 ? period : pe.getId().longValue());
                    if (assistSummary == null) {
                        assistMainSummary.put(option == 3 ? period : pe.getId().longValue(), assistSummary = new AssistSummary());
                        assistSummary.setData(new ArrayList());
                        assistSummary.setBoss(boss);
                        assistSummary.setPeopleId(pe.getId());
                        assistSummary.setCode(pe.getCode());
                        assistSummary.setFullName(pe.getFullName().toUpperCase());
                        Position position = (Position) o[1];
                        if (position != null) {
                            assistSummary.setPosition(position.getName().toUpperCase());
                        }
                    }

                    if (option == 3) {
                        assistMainSummary.setFullName(pe.getFullName());
                        if (dayOfWeek > 1 && dayOfWeek < 7) {
                            DaySummary daySummary = new DaySummary();
                            daySummary.setFullName(pe.getFullName());
                            daySummary.setDay(c.get(Calendar.DAY_OF_MONTH));
                            daySummary.setDayName(dayNameMap.get(dayOfWeek));
                            daySummary.setDate(c.getTime());
                            daySummary.setPeriod(year * 100 + c.get(Calendar.MONTH));
                            assistSummary.getData().add(daySummary);
                        }
                    } else {
                        Date tmp = c.getTime();
                        String[] monthStatus = new String[31];
                        assistSummary.setTimes(new Long[31][3]);
                        assistSummary.setStatus(monthStatus);
                        assistSummary.setLaborRegime(laborRegime);
                        assistSummary.setFullName(pe.getFullName());
                        dependencyId = contract.getDependencyId();
                        if (dependencyId != null) {
                            Dependency dependency = em.find(Dependency.class, dependencyId);
                            if (dependency.getParentId() != null && dependency.getMNemonico() == null) {
                                Dependency parent = em.find(Dependency.class, dependency.getParentId());
                                if (parent.getMNemonico() != null) {
                                    dependency = parent;
                                }
                            }
                            assistSummary.setDependencyId(dependencyId);
                            assistSummary.setDependency(dependency);
                        }
                        if (contract.getFechaIni().after(from)) {
                            c.setTime(contract.getFechaIni());
                            while (c.getTime().after(from)) {
                                dayMonth = c.get(Calendar.DAY_OF_MONTH);
                                dayMonth = dayIndex.get(dayMonth);
                                try {

                                    if (dayMonth != null) {
                                        monthStatus[dayMonth] = " ";
                                    }
                                    c.add(Calendar.DAY_OF_MONTH, -1);
                                } catch (Exception e) {
                                    System.out.println("dayIndex=" + dayIndex);
                                    System.out.println("dayMonth=" + dayMonth);
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        if (contract.getFechaFin() != null) {
                            c.setTime(contract.getFechaFin());
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            while (c.getTime().before(to)) {
                                dayMonth = c.get(Calendar.DAY_OF_MONTH);
                                Integer k = dayIndex.get(dayMonth);
                                if (k != null) {
                                    monthStatus[k] = " ";
                                }
                                c.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        c.setTime(tmp);
                        c.add(Calendar.MONTH, -1);
                        break;
                    }
                    c.add(Calendar.DAY_OF_YEAR, -1);
                }

                /*Date birthdate = pe.getBirthdate();
                if (birthdate != null) {
                    c.setTime(birthdate);
                    dayMonth = c.get(Calendar.DAY_OF_MONTH);
                    AssistSummary assistMonthSummary = (AssistSummary) assistMainSummary.get(c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH));
                    if (assistMonthSummary != null) {
                        detailAsist = assistMonthSummary.getData();
                        for (Object[] detail : detailAsist) {
                            if (detail[1].equals(dayMonth)) {
                                detail[6] = "ONOMASTICO";
                                break;
                            }
                        }
                    }
                }*/
                System.out.println("pe=" + pe);
                peopleIdList.add(pe.getId().longValue());
            }
            for (Holiday holiday : holidays) {
                c.setTime(holiday.getDate());
                if (option == 3) {
                    period = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH);
                    dayMonth = c.get(Calendar.DAY_OF_MONTH);
                    for (AssistSummary assistMainSummary : assistMainSummaryMap.values()) {
                        AssistSummary assistSummary = (AssistSummary) assistMainSummary.get(period);
                        if (assistSummary != null) {
                            for (DaySummary daySummary : (Collection<DaySummary>) assistSummary.getData()) {
                                if (daySummary.getDay() == dayMonth) {
                                    daySummary.setDecision(XUtil.isEmpty(holiday.getDescription()) ? "FERIADO" : holiday.getDescription());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    //System.out.println("perod=" + mainPeriod);
                    AssistSummary assistMainSummary = assistMainSummaryMap.get(mainPeriod);
                    for (AssistSummary assistSummary : (Collection<AssistSummary>) assistMainSummary.values()) {
                        String[] status = assistSummary.getStatus();
                        if (holiday.getIndex() != null) {
                            status[holiday.getIndex()] = XUtil.isEmpty(holiday.getType()) ? "FE" : holiday.getType();
                        }
                    }
                }
            }

            if (!remoteEmployees.isEmpty()) {
                for (Object[] remote : (List<Object[]>) em.createQuery("SELECT s.peopleIdLong,s.loginTime,s.logoutTime,s.accessDate "
                        + "FROM RhSession s WHERE s.accessDate>=:from AND s.accessDate<=:to AND s.peopleIdLong IN :people ORDER BY s.peopleId")
                        .setParameter("from", from)
                        .setParameter("to", to)
                        .setParameter("people", remoteEmployees)
                        .getResultList()) {
                    c.setTime((Date) remote[3]);
                    period = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH);
                    AssistSummary assistMainSummary = assistMainSummaryMap.get(option == 3 ? remote[0] : mainPeriod);
                    if (assistMainSummary != null) {
                        AssistSummary assistSummary = (AssistSummary) assistMainSummary.get(option == 3 ? period : remote[0]);
                        if (assistSummary != null) {
                            dayMonth = c.get(Calendar.DAY_OF_MONTH);
                            if (option == 3) {
                                List<DaySummary> detailAsist = assistSummary.getData();
                                for (int j = 0; j < detailAsist.size(); j++) {
                                    DaySummary detail = detailAsist.get(j);
                                    if (detail.getDay() == dayMonth) {
                                        detail.setEntry((detail.getEntry() + "\n" + (Date) remote[1]).trim());
                                        detail.setBreaking("---");
                                        detail.setOut((detail.getOut() + "\n" + (Date) remote[2]).trim());
                                        break;
                                    }
                                }
                            } else {
                                dayMonth = dayIndex.get(dayMonth);
                                if (dayMonth != null) {
                                    String status[] = assistSummary.getStatus();
                                    if (status[dayMonth] != null && status[dayMonth].startsWith("O")) {
                                        status[dayMonth] = status[dayMonth] + ",TR";
                                    } else {
                                        status[dayMonth] = "TR";
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Object[] assist : (List<Object[]>) em.createQuery("SELECT distinct s.peopleId,s.date,s.time "
                    + "FROM Attendance s WHERE s.date>=:from AND s.date<=:to AND s.peopleId IN :people "
                    + "ORDER BY s.peopleId,s.date,s.time")
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("people", peopleIdList)
                    .getResultList()) {
                c.setTime((Date) assist[1]);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                period = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH);
                AssistSummary assistMainSummary = assistMainSummaryMap.get(option == 3 ? ((Number) assist[0]).longValue() : mainPeriod);
                if (assistMainSummary != null) {
                    AssistSummary assistSummary = (AssistSummary) assistMainSummary.get(option == 3 ? period : ((Number) assist[0]).longValue());

                    if (assistSummary != null) {
                        dayMonth = c.get(Calendar.DAY_OF_MONTH);
                        Date mark = (Date) assist[2];
                        c.setTime(mark);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayMonth);
                        c.set(Calendar.MILLISECOND, 0);
                        mark = c.getTime();

                        if (option == 3) {
                            List<DaySummary> detailAsist = assistSummary.getData();
                            int k = -1;
                            for (int j = 0; j < detailAsist.size(); j++) {
                                if (detailAsist.get(j).getDay() == dayMonth) {
                                    k = j;
                                    break;
                                }
                            }
                            if (k >= 0) {
                                //get the row day
                                DaySummary detail = (DaySummary) detailAsist.get(k);
                                timeControl = timeTable.breakTimeLimit;
                                c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                c.set(Calendar.MINUTE, timeControl[1]);
                                c.set(Calendar.SECOND, 0);
                                Date breakTimeLimit = c.getTime();
                                if (XUtil.isEmpty(detail.getBreaking()) && today.before(breakTimeLimit)) {
                                    detail.setBreaking("---");
                                }
                                timeControl = timeTable.outTime;
                                c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                c.set(Calendar.MINUTE, timeControl[1]);
                                c.set(Calendar.SECOND, 0);
                                Date outTime = c.getTime();
                                if (XUtil.isEmpty(detail.getOut()) && today.before(outTime)) {
                                    detail.setOut("---");
                                }
                                int[] tt = timeTable.breakTime;
                                c.set(Calendar.HOUR_OF_DAY, tt[0]);
                                c.set(Calendar.MINUTE, tt[1]);
                                c.set(Calendar.SECOND, 0);
                                long markTimeMillis = mark.getTime();
                                if (mark.before(c.getTime())) {
                                    if (XUtil.isEmpty(detail.getEntry())) {
                                        tt = timeTable.enterTime;
                                        c.set(Calendar.HOUR_OF_DAY, tt[0]);
                                        c.set(Calendar.MINUTE, tt[1]);
                                        c.set(Calendar.SECOND, 0);
                                        Long seconds = TimeUnit.MILLISECONDS.toSeconds(markTimeMillis - c.getTimeInMillis());
                                        detail.setEntry(stf.format(mark) + (seconds > 59 ? ("\n" + (seconds > 60 ? ((seconds / 60)
                                                + "m ") : "") + (seconds % 60) + "s") : ""));
                                        detail.setEntryDelaySeconds(seconds);
                                    }
                                } else {
                                    timeControl = timeTable.outTime;
                                    c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                    c.set(Calendar.MINUTE, timeControl[1]);
                                    c.set(Calendar.SECOND, 0);
                                    if (mark.before(c.getTime())) {
                                        if (XUtil.isEmpty(detail.getBreaking())) {
                                            Long seconds = TimeUnit.MILLISECONDS.toSeconds(markTimeMillis - breakTimeLimit.getTime());
                                            detail.setBreaking(stf.format(mark)
                                                    + (seconds > 59 ? ("\n" + (seconds > 60 ? ((seconds / 60) + "m ") : "") + (seconds % 60) + "s") : ""));
                                            detail.setBreakDelaySeconds(seconds);
                                        }
                                    } else if (XUtil.isEmpty(detail.getOut())) {//out
                                        detail.setOut(stf.format(mark));
                                    }
                                }

                            }
                        } else {

                            if (assistSummary.getFullName().contains("PINEDO ER")) {
                                System.out.println(assistSummary.getFullName());
                                System.out.println("dayMonth=" + dayMonth);
                            }
                            dayMonth = dayIndex.get(dayMonth);
                            if (assistSummary.getFullName().contains("PINEDO ER")) {
                                System.out.println("dayIndex=" + dayMonth);
                                System.out.println("mark=" + sdtf.format(mark));
                            }
                            if (dayMonth != null) {
                                Long timesMonth[][] = assistSummary.getTimes();
                                //dayMonth = dayMonth - 1;
                                Long[] times = timesMonth[dayMonth];
                                int[] tt = timeTable.breakTime;
                                c.set(Calendar.HOUR_OF_DAY, tt[0]);
                                c.set(Calendar.MINUTE, tt[1]);
                                c.set(Calendar.SECOND, 0);
                                if (mark.before(c.getTime())) {
                                    if (times[0] == null) {
                                        tt = timeTable.enterTime;
                                        c.set(Calendar.HOUR_OF_DAY, tt[0]);
                                        c.set(Calendar.MINUTE, tt[1]);
                                        c.set(Calendar.SECOND, 0);
                                        Long seconds = TimeUnit.MILLISECONDS.toSeconds(mark.getTime() - c.getTimeInMillis());
                                        times[0] = seconds;
                                    }
                                } else {
                                    timeControl = timeTable.outTime;
                                    c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                    c.set(Calendar.MINUTE, timeControl[1]);
                                    c.set(Calendar.SECOND, 0);
                                    if (mark.before(c.getTime())) {
                                        if (times[1] == null) {
                                            timeControl = timeTable.breakTimeLimit;
                                            c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                            c.set(Calendar.MINUTE, timeControl[1]);
                                            c.set(Calendar.SECOND, 0);
                                            times[1] = TimeUnit.MILLISECONDS.toSeconds(mark.getTime() - c.getTimeInMillis());
                                        }
                                    } else if (times[2] == null) {//out
                                        timeControl = timeTable.outTime;
                                        c.set(Calendar.HOUR_OF_DAY, timeControl[0]);
                                        c.set(Calendar.MINUTE, timeControl[1]);
                                        c.set(Calendar.SECOND, 0);
                                        times[2] = TimeUnit.MILLISECONDS.toSeconds(mark.getTime() - c.getTimeInMillis());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AssistSummary assistSummary;
            for (License license : (List<License>) em.createQuery("SELECT s FROM License s WHERE s.fecIni<=:to AND s.fecFin>=:from AND s.peopleIdLong IN :people ORDER BY s.peopleId")
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("people", peopleIdList)
                    .getResultList()) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                AssistSummary assistMainSummary = assistMainSummaryMap.get(option == 3 ? license.getPeopleIdLong() : (mainPeriod));

                Date end = to.after(license.getFecFin()) ? license.getFecFin() : to;
                LocalDateTime endOfDate
                        = Instant.ofEpochMilli(end.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);

                c.setTime(Date.from(endOfDate
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
                end = c.getTime();

                c.setTime(license.getFecIni().before(from) ? from : license.getFecIni());
                if (assistMainSummary != null) {
                    do {
                        dayMonth = c.get(Calendar.DAY_OF_MONTH);
                        if (option == 3) {
                            year = c.get(Calendar.YEAR);
                            month = c.get(Calendar.MONTH);
                            period = year * 100 + month;
                            assistSummary = (AssistSummary) assistMainSummary.get(period);
                            if (assistSummary != null) {
                                for (DaySummary row : (List<DaySummary>) assistSummary.getData()) {
                                    if (row.getDay() == dayMonth) {
                                        List exceptions = row.getExceptions();
                                        if (exceptions == null) {
                                            row.setExceptions(exceptions = new ArrayList());
                                        }
                                        exceptions.add(license);
                                        break;
                                    }
                                }
                            }
                        } else {
                            assistSummary = (AssistSummary) assistMainSummary.get(license.getPeopleIdLong());

                            dayMonth = dayIndex.get(dayMonth);
                            String status[] = assistSummary.getStatus();
//                            if (assistSummary.getFullName().contains("OLORTEGU")) {
//                                System.out.println("p=" + assistSummary.getFullName());
//                                System.out.println("end=" + sdf.format(end));
//                                System.out.println("dayMonth=" + dayMonth);
//                                System.out.println(sdf.format(c.getTime()) + "   for " + (license.getFecIni() != null ? sdf.format(license.getFecIni()) : "null") + "=>" + (license.getFecFin() != null ? sdf.format(license.getFecFin()) : "null"));
//                            }
                            if (dayMonth != null) {
                                String licenseType = (String) licenseTypeMap.get(license.getType());
                                if ("V".equals(licenseType)) {
                                    licenseType = "";
                                }
                                if ("O".equals(license.getType())) {
                                    for (int k = 0; k < status.length; k++) {
                                        if (status[k] != null && status[k].startsWith("O")) {
                                            String[] oldBirthdayStatus = status[k].split(",");
                                            if (oldBirthdayStatus.length == 1) {
                                                status[k] = null;
                                            } else {
                                                status[k] = oldBirthdayStatus[oldBirthdayStatus.length - 1];
                                            }
                                        }
                                    }
                                }
                                status[dayMonth] = license.getType();
                                //considrar el tiempo de asunto particular
                                if (license.getAuthorizationType() != null && license.getAuthorizationType() == 2) {
                                    status[dayMonth] = "AP";
                                    assistSummary.setPp(assistSummary.getPp() + 1);
                                    if (license.getTimeEnd() != null && license.getTimeStart() != null) {
                                        assistSummary.setParticularTime(
                                                assistSummary.getParticularTime()
                                                + (int) ((license.getTimeEnd().getTime() - license.getTimeStart().getTime()) / 1000.0)
                                        );
                                    }
                                    long minutes = assistSummary.getParticularTime() / 60;
                                    long hours = minutes / 60;
                                    minutes = minutes % 60;
                                    assistSummary.setDelay(
                                            assistSummary.getPp() + " - "
                                            + (String.format("%02d", hours))
                                            + ":" + String.format("%02d", minutes));
                                }

                            }
                        }
                        c.add(Calendar.DAY_OF_MONTH, 1);
                    } while (!c.getTime().after(end));
                }
            }
        }
        List dataList = new ArrayList();
        final List<Comparator> comparatorList = new ArrayList();
        Map<String, Comparator<AssistSummary>> comparatorMap = new HashMap();
        comparatorMap.put("LR", (AssistSummary o1, AssistSummary o2) -> {
            String d1 = o1.getLaborRegime();
            String d2 = o2.getLaborRegime();
            String dn1 = d1 != null ? d1 : "";
            return dn1.compareTo(d2 != null ? d2 : "");
        });
        comparatorMap.put("DE", (AssistSummary o1, AssistSummary o2) -> {
            Dependency d1 = o1.getDependency();
            Dependency d2 = o2.getDependency();
            String dn1 = d1 != null ? d1.getFullName() : "";
            return dn1.compareTo(d2 != null ? d2.getFullName() : "");
        });
        if (option != 3 && groups != null && groups.length() > 0) {

            for (String s : groups.split(",")) {
                comparatorList.add(comparatorMap.get(s));
            }
            groups = groups.split(",")[0];
        }
        comparatorList.add((Comparator<AssistSummary>) (AssistSummary o1, AssistSummary o2) -> o1.getFullName().compareTo(o2.getFullName()));
        Comparator<AssistSummary> comparator = (AssistSummary o1, AssistSummary o2) -> {
            int j = 0;
            for (Comparator<AssistSummary> co : comparatorList) {
                j = co.compare(o1, o2);
                if (j != 0) {
                    return j;
                }
            }
            return j;
        };
        List<AssistSummary> list0 = new ArrayList(assistMainSummaryMap.values());
        if (option == 3) {
            list0.sort(comparator);
        }
        for (AssistSummary assistPeopleSummary : list0) {
            List<AssistSummary> list = new ArrayList(assistPeopleSummary.values());
            if (option != 3) {
                list.sort(comparator);
            }
            for (AssistSummary assistSummary : list) {
                if (option == 3) {
                    long particularTime = 0, tolerance = 0;
                    List<DaySummary> data = assistSummary.getData();
                    int falta = 0;
                    for (DaySummary dayRow : data) {
                        if (assistSummary.getFullName().contains("SOLANO H")) {
                            //System.out.println(dayRow[1]);
                        }
                        List exceptions = dayRow.getExceptions();
                        if (exceptions != null) {
                            for (Object o : exceptions) {
                                if (o instanceof License) {
                                    License license = (License) o;
                                    String licenseType = (String) licenseTypeMap.get(license.getType());
                                    dayRow.setDecision((licenseType != null
                                            ? licenseType
                                            : license.getType())
                                            + (license.getAuthorizationType() != null ? "("
                                            + License.AUTHORIZACTION_TYPE.get(license.getAuthorizationType()) + ")" : "")
                                            + " " + license.getDetalle() + " " + license.getNroSoli());
                                    if ("P".equals(license.getType())) {
                                        dayRow.setDecision(dayRow.getDecision() + " " + license.getTimeStart() + "->" + license.getTimeEnd());
                                        if (license.getAuthorizationType() != null && license.getAuthorizationType() == 2) {
                                            particularTime += (int) ((license.getTimeEnd().getTime() - license.getTimeStart().getTime()) / 1000.0);
                                            assistSummary.setPp(assistSummary.getPp() + 1);
                                            if (license.getTimeEnd() != null && license.getTimeStart() != null) {
                                                assistSummary.setParticularTime(
                                                        assistSummary.getParticularTime()
                                                        + (int) ((license.getTimeEnd().getTime() - license.getTimeStart().getTime()) / 1000.0)
                                                );
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if ((XUtil.isEmpty(dayRow.getEntry()) || XUtil.isEmpty(dayRow.getBreaking()) || XUtil.isEmpty(dayRow.getOut())) && XUtil.isEmpty(dayRow.getDecision())) {
                            dayRow.setDecision("FALTA");
                        } else {
                            dayRow.setRemark(dayRow.getDecision());
                            dayRow.setDecision(null);
                        }
                        if (XUtil.isEmpty(dayRow.getDecision())) {
                            if (dayRow.getEntryDelaySeconds() >= 11 * 60) {
                                dayRow.setDecision("FALTA");
                            } else if (dayRow.getEntryDelaySeconds() > 59) {
                                tolerance += dayRow.getEntryDelaySeconds();
                            }
                            if (dayRow.getBreakDelaySeconds() >= 6 * 60) {
                                dayRow.setDecision("FALTA");
                            } else if (dayRow.getBreakDelaySeconds() > 59) {
                                tolerance += dayRow.getBreakDelaySeconds();
                            }
                        }
                        if ("FALTA".equals(dayRow.getDecision())) {
                            falta++;
                        }
                    }
                    for (DaySummary dayRow : data) {
                        dayRow.setExceptions(null);
                        if (falta > 0) {
                            dayRow.setFoul("" + falta);
                        }
                        if (assistSummary.getPp() > 0) {
                            particularTime = assistSummary.getParticularTime();
                            long minutes = particularTime / 60;
                            long hours = minutes / 60;
                            minutes = minutes % 60;
                            dayRow.setParticular(assistSummary.getPp() + " - "
                                    + (hours > 0 ? (String.format("%02d", hours) + "h ") : "")
                                    + (minutes > 0 ? (String.format("%02d", minutes) + "m") : ""));
                        }
                        if (tolerance > 0) {
                            long minutes = tolerance / 60;
                            long hours = minutes / 60;
                            minutes = minutes % 60;
                            dayRow.setTolerance((hours > 0 ? (String.format("%02d", hours) + "h ") : "")
                                    + (minutes > 0 ? (String.format("%02d", minutes) + "m") : ""));
                        }
                    }
                    dataList.addAll(data);
                } else {
                    int blank = 0;
                    int falta = 0;
                    int tr = 0;
                    int dmc = 0;
                    int dm = 0;
                    int tp = 0;
                    int v = 0;
                    int ll = 0;
                    String[] statusMonth = assistSummary.getStatus();
                    Long[][] timesMonth = assistSummary.getTimes();
                    for (i = 0; i < monthLimit; i++) {
                        //dia sin vacacion o permiso

                        if (statusMonth[i] == null) {

                            if (timesMonth[i][0] == null || timesMonth[i][1] == null || timesMonth[i][2] == null) {
                                statusMonth[i] = "F";
                                falta++;
                                //tiene q evaluarse si hay permiso q considere dia completo o horas q cubran 7:45-7:55 2:10-2-15 5:00
                            } else {
                                Long seconds = timesMonth[i][0], enterSeconds = (long) 0, breakSeconds = (long) 0;
                                String status = "A";
                                //enterTime
                                if (seconds > 59) {
                                    enterSeconds = seconds;
                                    if (seconds >= 11 * 60) {
                                        status = "F";
                                    }
                                }
                                //breakTime
                                if ((seconds = timesMonth[i][1]) > 59) {
                                    breakSeconds = seconds;
                                    if (seconds >= 6 * 60) {
                                        status = "F";
                                    }
                                }
                                if ("F".equals(status)) {
                                    falta++;
                                } else {
                                    assistSummary.setAccumulatedSeconds(enterSeconds + breakSeconds + (assistSummary.getAccumulatedSeconds() != null ? assistSummary.getAccumulatedSeconds() : 0));

                                }
                                timesMonth[i][0] = enterSeconds;
                                statusMonth[i] = status + (enterSeconds > 59 || breakSeconds > 59 ? ("\n" + (enterSeconds / 60) + "/" + (breakSeconds / 60)) : "");
                            }
                        } else if (statusMonth[i].equals(" ")) {
                            blank++;
                        } else if (statusMonth[i].startsWith("O")) {
                            String[] oll = statusMonth[i].split(",");
                            if (oll.length == 1) {
                                statusMonth[i] = oll[0];
                            } else {
                                statusMonth[i] = oll[oll.length - 1];
                            }
                        } else if (statusMonth[i].equals("TR")) {
                            tr++;
                        } else if (statusMonth[i].equals("V")) {
                            v++;
                        } else if (statusMonth[i].equals("DMC")) {
                            dmc++;
                        } else if (statusMonth[i].equals("DM")) {
                            dm++;
                        }
                    }
                    assistSummary.setF(falta);
                    assistSummary.setTp(tp);
                    assistSummary.setDm(dm);
                    assistSummary.setDmc(dmc);
                    assistSummary.setTr(tr);
                    assistSummary.setV(v);
                    assistSummary.setL(ll);
                    assistSummary.setWorkedDays(30 - falta - blank);
                    assistSummary.setHours(8 * falta);
                    long seconds = XUtil.intValue(assistSummary.getAccumulatedSeconds());
                    if (option != 2 || true) {
                        dataList.add(assistSummary);
                        if (seconds > 0) {
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            minutes = minutes % 60;
                            assistSummary.setTolerance(String.format("%d:%02d", hours, minutes));
                        }
                        seconds = seconds - 60 * 60 * 2;
                        if (seconds < 0) {
                            seconds = 0;
                        }
                        seconds = (seconds / 60) + assistSummary.getF() * 8 * 60 + assistSummary.getParticularTime() / 60;
                        long hours = seconds / 60;
                        long minutes = seconds % 60;
                        assistSummary.setTd(String.format("%d:%02d", hours, minutes));
                    }
                }
            }
        }

        String month = new SimpleDateFormat("MMMM").format(to).toUpperCase();
        String prevMonth = new SimpleDateFormat("MMMM").format(from).toUpperCase();
        if (!prevMonth.equals(month)) {
            month = prevMonth + " - " + month;
        }
        return new XMap(
                "data", dataList,
                "HEADER", header,
                "GROUPING", groups,
                "DO_NOT_PROCESS", "HEADER",
                "FECHA_INI", sdf.format(from),
                "FECHA_FIN", sdf.format(to),
                "MONTH", month,
                "YEAR", "" + c.get(Calendar.YEAR));
    }

}
