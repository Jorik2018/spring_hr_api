package gob.regionancash.hr.service.impl;

import java.util.List;
//import org.isobit.db.DBFacadeLocal;
//import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MarcacionFacadeImpl {
    
//    @Autowired
//    private DBFacadeLocal dBFacade;
    @PersistenceContext
    private EntityManager em;


    public List getMarcacionList(String dni){
        return null;// dBFacade.executeQuery("access", "SELECT t.Date_Time FROM TA_Record_Info t,HR_Personnel p WHERE t.Per_Code=p.Per_Code AND p.Code_Str='"+dni+"' ORDER BY t.Date_Time");
    }
    
    
}
