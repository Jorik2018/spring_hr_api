package gob.regionancash.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specificationRepository;

    // Constructor 1: solo repository
    public BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
        if (repository instanceof JpaSpecificationExecutor) {
            this.specificationRepository = (JpaSpecificationExecutor<T>) repository;
        } else {
            this.specificationRepository = null;
        }
    }

    // Constructor 2: repository + specificationRepository
    public BaseServiceImpl(JpaRepository<T, ID> repository, JpaSpecificationExecutor<T> specificationRepository) {
        this.repository = repository;
        this.specificationRepository = specificationRepository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public long count() {
        return repository.count();
    }

    // Método opcional para Specifications
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        if (specificationRepository == null) {
            throw new UnsupportedOperationException("SpecificationRepository no está disponible para este repositorio");
        }
        return specificationRepository.findAll(spec, pageable);
    }
}
