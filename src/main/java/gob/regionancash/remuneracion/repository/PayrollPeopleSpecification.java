package gob.regionancash.remuneracion.repository;

import org.springframework.data.jpa.domain.Specification;
import gob.regionancash.remuneracion.model.PayrollPeople;
import jakarta.persistence.criteria.Join;

public class PayrollPeopleSpecification {

    public static Specification<PayrollPeople> filterByFullName(String fullName) {
        return (root, query, cb) -> {

            if (fullName == null || fullName.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + fullName.toUpperCase().replaceAll("\\s+", "%") + "%";

            Join<Object, Object> people = root.join("people");
            return cb.like(cb.upper(people.get("fullName")), pattern);
        };
    }
}