package cz.kostka.prizedraw.repository;

import cz.kostka.prizedraw.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByJmeno(String jmeno);
    List<Person> findAllByOrderByIdAsc();
}