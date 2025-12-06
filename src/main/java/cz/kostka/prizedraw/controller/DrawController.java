package cz.kostka.prizedraw.controller;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.model.Person;
import cz.kostka.prizedraw.model.Prize;
import cz.kostka.prizedraw.service.DrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class DrawController {

    private final DrawService drawService;

    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @GetMapping("/")
    public String overview(Model model) {
        model.addAttribute("vysledky", drawService.getResults());
        model.addAttribute("ceny", drawService.getAvailablePrizes());
        model.addAttribute("lide", drawService.getAllPeople());

        addWheelData(model);
        return "overview";
    }

    private void addWheelData(final Model model) {
        // Párová data pro kolo štěstí
        model.addAttribute("eligibleWithoutPrizePairs", drawService.getEligiblePairs(true));
        model.addAttribute("eligibleAllPairs", drawService.getEligiblePairs(false));
        model.addAttribute("nextPrize", drawService.getNextPrizeName().orElse(null));
    }

    // REST preview endpoint: server vybere vítěze
    @GetMapping("/api/preview")
    @ResponseBody
    public ResponseEntity<?> apiPreview(@RequestParam("mode") String mode) {
        Optional<DrawService.PreviewDTO> dto = switch (mode) {
            case "available" -> drawService.previewAvailableServerDriven();
            case "all" -> drawService.previewAllServerDriven();
            default -> Optional.empty();
        };
        return dto.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("No preview available"));
    }

    // Losovat: použije ID z kola
    @PostMapping("/losovat")
    public String draw(@RequestParam(value = "previewPersonId", required = false) Long previewPersonId,
                       Model model) {
        Optional<Prize> nextPrizeOpt = drawService.getNextAvailablePrize();
        if (nextPrizeOpt.isPresent() && previewPersonId != null) {
            Optional<Person> personOpt = drawService.findPersonById(previewPersonId, true);
            if (personOpt.isPresent()) {
                DrawResult r = new DrawResult(personOpt.get(), nextPrizeOpt.get());
                model.addAttribute("vyherce", r.getClovek());
                model.addAttribute("cena", r.getCena());
                model.addAttribute("moznaJmena", drawService.getEligibleNames(true));
                return "winnerPage";
            }
        }

        Optional<DrawResult> preview = drawService.previewSequentialFromAvailable();
        model.addAttribute("moznaJmena", drawService.getEligibleNames(true));
        model.addAttribute("cena", drawService.getNextPrizeName().orElse("N/A"));

        if (preview.isEmpty()) {
            model.addAttribute("vyherce", "N/A");
            return "winnerPage";
        }
        DrawResult r = preview.get();
        model.addAttribute("vyherce", r.getClovek());
        return "winnerPage";
    }

    // Losovat ze všech: použije ID z kola
    @PostMapping("/losovat-ze-vsech")
    public String drawFromAll(@RequestParam(value = "previewPersonId", required = false) Long previewPersonId,
                              Model model) {
        Optional<Prize> nextPrizeOpt = drawService.getNextAvailablePrize();
        if (nextPrizeOpt.isPresent() && previewPersonId != null) {
            Optional<Person> personOpt = drawService.findPersonById(previewPersonId, false);
            if (personOpt.isPresent()) {
                DrawResult r = new DrawResult(personOpt.get(), nextPrizeOpt.get());
                model.addAttribute("vyherce", r.getClovek());
                model.addAttribute("cena", r.getCena());
                model.addAttribute("moznaJmena", drawService.getEligibleNames(false));
                return "winnerPage";
            }
        }

        Optional<DrawResult> preview = drawService.previewSequentialFromAll();
        model.addAttribute("moznaJmena", drawService.getEligibleNames(false));
        model.addAttribute("cena", drawService.getNextPrizeName().orElse("N/A"));

        if (preview.isEmpty()) {
            model.addAttribute("vyherce", "N/A");
            return "winnerPage";
        }
        DrawResult r = preview.get();
        model.addAttribute("vyherce", r.getClovek());
        return "winnerPage";
    }

    @PostMapping("/potvrdit")
    public String confirm(@RequestParam("vyherce") String vyherce,
                          @RequestParam("cena") String cena,
                          Model model) {
        drawService.confirmResult(vyherce, cena);
        model.addAttribute("vysledky", drawService.getResults());
        model.addAttribute("ceny", drawService.getAvailablePrizes());
        model.addAttribute("lide", drawService.getAllPeople());

        addWheelData(model);

        return "overview";
    }

    @PostMapping("/losovat-znovu")
    public String redraw() {
        return "redirect:/";
    }
}