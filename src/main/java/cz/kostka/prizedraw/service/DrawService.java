package cz.kostka.prizedraw.service;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.model.Person;
import cz.kostka.prizedraw.model.Prize;
import cz.kostka.prizedraw.repository.DrawResultRepository;
import cz.kostka.prizedraw.repository.PersonRepository;
import cz.kostka.prizedraw.repository.PrizeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DrawService {

    private final PersonRepository personRepository;
    private final PrizeRepository prizeRepository;
    private final DrawResultRepository drawResultRepository;
    private final Random random = new Random();

    public DrawService(PersonRepository personRepository,
                       PrizeRepository prizeRepository,
                       DrawResultRepository drawResultRepository) {
        this.personRepository = personRepository;
        this.prizeRepository = prizeRepository;
        this.drawResultRepository = drawResultRepository;
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    public List<Person> getPeopleWithoutPrize() {
        return personRepository.findAll().stream()
                .filter(p -> drawResultRepository.findByPerson(p).isEmpty())
                .toList();
    }

    public List<Prize> getAvailablePrizes() {
        return prizeRepository.findByAssignedFalseOrderByOrderIndexAsc();
    }

    public Optional<Prize> getNextAvailablePrize() {
        return prizeRepository.findFirstByAssignedFalseOrderByOrderIndexAsc();
    }

    public List<DrawResult> getResults() {
        return drawResultRepository.findAll();
    }

    // Losování: pro první dostupnou cenu, mezi lidmi bez výhry
    public Optional<DrawResult> previewSequentialFromAvailable() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> eligiblePeople = getPeopleWithoutPrize();

        if (nextPrizeOpt.isEmpty() || eligiblePeople.isEmpty()) return Optional.empty();

        Prize currentPrize = nextPrizeOpt.get();
        Person winner = eligiblePeople.get(random.nextInt(eligiblePeople.size()));

        return Optional.of(new DrawResult(winner, currentPrize));
    }

    // Losování ze všech: pro první dostupnou cenu, mezi všemi lidmi
    public Optional<DrawResult> previewSequentialFromAll() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> people = personRepository.findAll();

        if (nextPrizeOpt.isEmpty() || people.isEmpty()) return Optional.empty();

        Prize currentPrize = nextPrizeOpt.get();
        Person winner = people.get(random.nextInt(people.size()));

        return Optional.of(new DrawResult(winner, currentPrize));
    }

    @Transactional
    public Optional<DrawResult> confirmResult(String vyherce, String cena) {
        Optional<Person> personOpt = personRepository.findByJmeno(vyherce);
        Optional<Prize> prizeOpt = prizeRepository.findAll().stream()
                .filter(prize -> Objects.equals(prize.getNazev(), cena))
                .findFirst();

        if (personOpt.isEmpty() || prizeOpt.isEmpty()) {
            return Optional.empty();
        }

        Prize prize = prizeOpt.get();
        prize.setAssigned(true);
        prizeRepository.save(prize);

        DrawResult result = new DrawResult(personOpt.get(), prize);
        drawResultRepository.save(result);

        return Optional.of(result);
    }

    public List<String> getEligibleNames(boolean onlyWithoutPrize) {
        List<Person> src = onlyWithoutPrize ? getPeopleWithoutPrize() : personRepository.findAll();
        return src.stream().map(Person::getJmeno).toList();
    }

    public Optional<String> getNextPrizeName() {
        return getNextAvailablePrize().map(Prize::getNazev);
    }
}