package gob.regionancash.util;

import java.util.List;

public interface BaseService<T, ID> {

    T save(T entity);

    void delete(T entity);

    T findById(ID id);

    List<T> findAll();

    long count();
}