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

    public record EligiblePersonDTO(Long id, String name) {}
    public record PreviewDTO(Long personId, String name, String prizeName) {}

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

    public List<Person> getAllPeople() { return personRepository.findAll(); }

    public List<Person> getPeopleWithoutPrize() {
        return personRepository.findAll().stream()
                .filter(p -> drawResultRepository.findByPerson(p).isEmpty())
                .toList();
    }

    public List<EligiblePersonDTO> getEligiblePairs(boolean onlyWithoutPrize) {
        List<Person> src = onlyWithoutPrize ? getPeopleWithoutPrize() : personRepository.findAll();
        return src.stream().map(p -> new EligiblePersonDTO(p.getId(), p.getJmeno())).toList();
    }

    public List<Prize> getAvailablePrizes() {
        return prizeRepository.findByAssignedFalseOrderByOrderIndexDesc();
    }

    public Optional<Prize> getNextAvailablePrize() {
        return prizeRepository.findFirstByAssignedFalseOrderByOrderIndexDesc();
    }

    public List<DrawResult> getResults() { return drawResultRepository.findAll(); }

    // Serverem řízený preview
    public Optional<PreviewDTO> previewAvailableServerDriven() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> eligiblePeople = getPeopleWithoutPrize();
        if (nextPrizeOpt.isEmpty() || eligiblePeople.isEmpty()) return Optional.empty();
        Prize currentPrize = nextPrizeOpt.get();
        Person winner = eligiblePeople.get(random.nextInt(eligiblePeople.size()));
        return Optional.of(new PreviewDTO(winner.getId(), winner.getJmeno(), currentPrize.getDisplayName()));
    }

    public Optional<PreviewDTO> previewAllServerDriven() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> people = personRepository.findAll();
        if (nextPrizeOpt.isEmpty() || people.isEmpty()) return Optional.empty();
        Prize currentPrize = nextPrizeOpt.get();
        Person winner = people.get(random.nextInt(people.size()));
        return Optional.of(new PreviewDTO(winner.getId(), winner.getJmeno(), currentPrize.getDisplayName()));
    }

    // Fallback preview (původní logika) — doplněno, aby controller kompiloval
    public Optional<DrawResult> previewSequentialFromAvailable() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> eligiblePeople = getPeopleWithoutPrize();
        if (nextPrizeOpt.isEmpty() || eligiblePeople.isEmpty()) return Optional.empty();
        Prize currentPrize = nextPrizeOpt.get();
        Person winner = eligiblePeople.get(random.nextInt(eligiblePeople.size()));
        return Optional.of(new DrawResult(winner, currentPrize));
    }

    public Optional<DrawResult> previewSequentialFromAll() {
        Optional<Prize> nextPrizeOpt = getNextAvailablePrize();
        List<Person> people = personRepository.findAll();
        if (nextPrizeOpt.isEmpty() || people.isEmpty()) return Optional.empty();
        Prize currentPrize = nextPrizeOpt.get();
        Person winner = people.get(random.nextInt(people.size()));
        return Optional.of(new DrawResult(winner, currentPrize));
    }

    @Transactional
    public Optional<DrawResult> confirmResult(Long vyherceId, Long cenaId) {
        Optional<Person> personOpt = personRepository.findById(vyherceId);
        Optional<Prize> prizeOpt = prizeRepository.findById(cenaId);

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

    public Optional<Person> findPersonById(Long id, boolean onlyWithoutPrize) {
        if (id == null) return Optional.empty();
        Optional<Person> pOpt = personRepository.findById(id);
        if (pOpt.isEmpty()) return Optional.empty();
        Person p = pOpt.get();
        if (onlyWithoutPrize) {
            boolean hasWin = !drawResultRepository.findByPerson(p).isEmpty();
            if (hasWin) return Optional.empty();
        }
        return Optional.of(p);
    }

    public List<String> getEligibleNames(boolean onlyWithoutPrize) {
        List<Person> src = onlyWithoutPrize ? getPeopleWithoutPrize() : personRepository.findAll();
        return src.stream().map(Person::getJmeno).toList();
    }

    public Optional<String> getNextPrizeName() {
        return getNextAvailablePrize().map(Prize::getDisplayName);
    }
}