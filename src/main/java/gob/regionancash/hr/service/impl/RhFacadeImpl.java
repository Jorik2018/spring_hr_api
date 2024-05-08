package gob.regionancash.hr.service.impl;
import org.isobit.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import gob.regionancash.hr.service.RhFacade;

public class RhFacadeImpl implements RhFacade{

    @Autowired
    private UserService userFacade;

    enum Perm {
        ACCESS_RH,
        ADMIN_RH,
        ACCESS_RH_CONTRACT,
        ADMIN_RH_CONTRACT,
        ACCESS_RH_POSITION,
        ADMIN_RH_POSITION,
        ACCESS_RH_PERPAPELETA,
        ADMIN_RH_PERPAPELETA,
        ACCESS_RH_PERLICENCIA,
        ADMIN_RH_PERLICENCIA,
        ACCESS_RH_EMPLOYEE,
        ADMIN_RH_EMPLOYEE,
        ACCESS_RH_PENSIONSYSTEM,
        ADMIN_RH_PENSIONSYSTEM,
        ADMIN_RH_MERIT,
        ACCESS_RH_ESCDEMERITO,
        ADMIN_RH_ESCDEMERITO,
        ADMIN_RH_TRAINING
    };
    /* 
    @Override
    public Object getBlock(HttpServletRequest request, String op, Object delta) {
        if ("list".equals(op)) {
            Map blocks = (Map) request.getAttribute("#blocks");
            blocks.put("positions", new XMap("info", "Rh"));
            return blocks;
        } else if ("view".equals(op)) {
            if (userFacade.access(Perm.ACCESS_RH)) {
                return new XMap("title", "Rhs", "src", "/rh/block/Block.xhtml");
            }
        }
        return null;

    }
*/
    /*@PostMappingConstruct
    public void init() {
        add(this);
        add(new UserFacade.UserModule() {
            @Override
            public User login(String name, String pass, Map m) {
                return null;
            }

            @Override
            public User authenticateFinalize(User user) {
                ((Map) user.getExt()).put("dependency", em.createQuery("SELECT DISTINCT d FROM Contract c JOIN c.dependency d WHERE c.people.id=:people")
                        .setParameter("people", user.getIdDir()).getResultList());
                return user;
            }

            @Override
            public User password(Map m) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void loadPerm(User user, List perms) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public User logout(User user) {
                return user;
            }
        });
    }*/

}
