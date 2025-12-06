package cz.kostka.prizedraw.controller;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.model.Person;
import cz.kostka.prizedraw.model.Prize;
import cz.kostka.prizedraw.repository.DrawResultRepository;
import cz.kostka.prizedraw.repository.PersonRepository;
import cz.kostka.prizedraw.repository.PrizeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonRepository personRepository;
    private final PrizeRepository prizeRepository;
    private final DrawResultRepository drawResultRepository;

    public AdminController(PersonRepository personRepository, PrizeRepository prizeRepository,
                           final DrawResultRepository drawResultRepository) {
        this.personRepository = personRepository;
        this.prizeRepository = prizeRepository;
        this.drawResultRepository = drawResultRepository;
    }

    @GetMapping("/people")
    public String peopleAdmin(Model model) {
        model.addAttribute("people", personRepository.findAll());
        model.addAttribute("newPerson", new Person());
        return "person_admin";
    }

    @PostMapping("/people")
    public String addPerson(@ModelAttribute("newPerson") Person person) {
        if (person.getJmeno() != null && !person.getJmeno().isBlank()) {
            personRepository.save(person);
        }
        return "redirect:/admin/people";
    }

    @PostMapping("/people/delete/{id}")
    public String deletePerson(@PathVariable Long id) {
        personRepository.deleteById(id);
        return "redirect:/admin/people";
    }

    @GetMapping("/prizes")
    public String prizesAdmin(Model model) {
        model.addAttribute("prizes", prizeRepository.findAllByOrderByOrderIndexAsc());
        model.addAttribute("newPrize", new Prize());
        return "prize_admin";
    }

    @PostMapping("/prizes")
    public String addPrize(@ModelAttribute("newPrize") Prize prize) {
        if (prize.getNazev() != null && !prize.getNazev().isBlank()) {
            prize.setAssigned(false);
            if (prize.getOrderIndex() == null) prize.setOrderIndex(0);
            prizeRepository.save(prize);
        }
        return "redirect:/admin/prizes";
    }

    @PostMapping("/prizes/delete/{id}")
    public String deletePrize(@PathVariable Long id) {
        prizeRepository.deleteById(id);
        return "redirect:/admin/prizes";
    }

    @PostMapping("/prizes/reset/{id}")
    public String resetPrize(@PathVariable Long id) {
        prizeRepository.findById(id).ifPresent(p -> {
            p.setAssigned(false);
            prizeRepository.save(p);
        });
        drawResultRepository.findAll()
                .stream()
                .filter(result -> Objects.equals(result.getPrize().getId(), id))
                .map(DrawResult::getId)
                .forEach(drawResultRepository::deleteById);
        return "redirect:/admin/prizes";
    }

    @PostMapping("/prizes/update/{id}")
    public String updatePrize(@PathVariable Long id,
                              @RequestParam("nazev") String nazev,
                              @RequestParam(value = "orderIndex", required = false) Integer orderIndex) {
        prizeRepository.findById(id).ifPresent(p -> {
            if (nazev != null && !nazev.isBlank()) {
                p.setNazev(nazev.trim());
            }
            p.setOrderIndex(orderIndex == null ? 0 : orderIndex);
            prizeRepository.save(p);
        });
        return "redirect:/admin/prizes";
    }
}