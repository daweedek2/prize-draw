package cz.kostka.prizedraw.repository;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrawResultRepository extends JpaRepository<DrawResult, Long> {
    List<DrawResult> findByPerson(Person person);
}