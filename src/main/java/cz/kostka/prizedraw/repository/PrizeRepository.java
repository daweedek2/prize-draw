package cz.kostka.prizedraw.repository;

import cz.kostka.prizedraw.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrizeRepository extends JpaRepository<Prize, Long> {
    List<Prize> findByAssignedFalseOrderByOrderIndexDesc();
    List<Prize> findAllByOrderByOrderIndexDesc();
    Optional<Prize> findFirstByAssignedFalseOrderByOrderIndexDesc();
}