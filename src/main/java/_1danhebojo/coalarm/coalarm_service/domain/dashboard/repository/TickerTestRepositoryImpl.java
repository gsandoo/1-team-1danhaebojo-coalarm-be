package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TickerTestRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TickerTestEntity> findByCodeOrderedByUtcDateTime(String code) {
        String jpql = "SELECT t FROM TickerTestEntity t WHERE t.id.code = :code ORDER BY t.id.utcDateTime ASC";
        TypedQuery<TickerTestEntity> query = entityManager.createQuery(jpql, TickerTestEntity.class);
        query.setParameter("code", code);
        return query.getResultList();
    }
}
