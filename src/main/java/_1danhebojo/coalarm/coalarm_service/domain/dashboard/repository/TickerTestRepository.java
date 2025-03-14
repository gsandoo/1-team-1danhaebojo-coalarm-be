package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestId;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.TickerTestJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TickerTestRepository {
    private final TickerTestJpaRepository jpaRepository;
    private final TickerTestRepositoryImpl repositoryImpl;

    public TickerTestRepository(TickerTestJpaRepository jpaRepository, TickerTestRepositoryImpl repositoryImpl) {
        this.jpaRepository = jpaRepository;
        this.repositoryImpl = repositoryImpl;
    }

    public List<TickerTestEntity> findByCodeOrderedByUtcDateTime(String code) {
        return repositoryImpl.findByCodeOrderedByUtcDateTime(code);
    }

    public void save(TickerTestEntity entity) {
        jpaRepository.save(entity);
    }

    public void deleteById(TickerTestId id) {
        jpaRepository.deleteById(id);
    }
}
