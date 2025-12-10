package cz.kostka.prizedraw.controller;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.model.Person;
import cz.kostka.prizedraw.model.Prize;
import cz.kostka.prizedraw.service.DrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class DrawController {

    private final DrawService drawService;

    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @GetMapping("/")
    public String overview(Model model) {
        addOverviewDataToModel(model);
        return "overview";
    }

    private void addOverviewDataToModel(final Model model) {
        model.addAttribute("vysledky", drawService.getResults());
        model.addAttribute("ceny", drawService.getAvailablePrizes());
//        model.addAttribute("lide", drawService.getAllPeople());
        model.addAttribute("eligibleWithoutPrizePairs", drawService.getEligiblePairs(true));
        model.addAttribute("eligibleAllPairs", drawService.getEligiblePairs(false));
    }

    @GetMapping("/api/preview")
    @ResponseBody
    public ResponseEntity<?> apiPreview(@RequestParam("mode") String mode,
                                        @RequestParam("prizeId") Long prizeId) {
        boolean onlyWithoutPrize = "available".equals(mode);
        Optional<DrawService.PreviewDTO> dto = drawService.previewForPrize(prizeId, onlyWithoutPrize);
        return dto.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("No preview available"));
    }

    @PostMapping("/losovat")
    public String draw(@RequestParam(value = "previewPersonId", required = false) Long previewPersonId,
                       @RequestParam(value = "selectedPrizeId", required = false) Long selectedPrizeId,
                       Model model) {
        Optional<Prize> prizeOpt = drawService.findPrizeById(selectedPrizeId);
        if (prizeOpt.isPresent() && previewPersonId != null) {
            Optional<Person> personOpt = drawService.findPersonById(previewPersonId, true);
            if (personOpt.isPresent()) {
                DrawResult r = new DrawResult(personOpt.get(), prizeOpt.get());
                model.addAttribute("vyherce", r.getClovek());
                model.addAttribute("cena", r.getCena());
                model.addAttribute("moznaJmena", drawService.getEligibleNames(true));
                model.addAttribute("vyherceId", personOpt.get().getId());
                model.addAttribute("cenaId", prizeOpt.get().getId());
                model.addAttribute("vysledky", drawService.getResults());
                return "winnerPage";
            }
        }

        Optional<DrawResult> preview = drawService.previewSequentialFromAvailable();
        model.addAttribute("moznaJmena", drawService.getEligibleNames(true));
        model.addAttribute("vyherce", preview.map(DrawResult::getClovek).orElse("N/A"));
        model.addAttribute("cena", preview.map(DrawResult::getCena).orElse("N/A"));
        // když je fallback, person/prize ID neznáme – winnerPage pošle starý způsob (nebo nedovolí potvrzení)
        return "winnerPage";
    }

    @PostMapping("/losovat-ze-vsech")
    public String drawFromAll(@RequestParam(value = "previewPersonId", required = false) Long previewPersonId,
                              @RequestParam(value = "selectedPrizeId", required = false) Long selectedPrizeId,
                              Model model) {
        Optional<Prize> prizeOpt = drawService.findPrizeById(selectedPrizeId);
        if (prizeOpt.isPresent() && previewPersonId != null) {
            Optional<Person> personOpt = drawService.findPersonById(previewPersonId, false);
            if (personOpt.isPresent()) {
                DrawResult r = new DrawResult(personOpt.get(), prizeOpt.get());
                model.addAttribute("vyherce", r.getClovek());
                model.addAttribute("cena", r.getCena());
                model.addAttribute("moznaJmena", drawService.getEligibleNames(false));
                model.addAttribute("vyherceId", personOpt.get().getId());
                model.addAttribute("cenaId", prizeOpt.get().getId());
                model.addAttribute("vysledky", drawService.getResults());
                return "winnerPage";
            }
        }

        Optional<DrawResult> preview = drawService.previewSequentialFromAll();
        model.addAttribute("moznaJmena", drawService.getEligibleNames(false));
        model.addAttribute("vyherce", preview.map(DrawResult::getClovek).orElse("N/A"));
        model.addAttribute("cena", preview.map(DrawResult::getCena).orElse("N/A"));
        return "winnerPage";
    }

    // NOVÉ: potvrzení výsledku podle ID
    @PostMapping("/potvrdit")
    public String confirmByIds(@RequestParam("personId") Long personId,
                               @RequestParam("prizeId") Long prizeId,
                               Model model) {
        drawService.confirmResultByIds(personId, prizeId);
        addOverviewDataToModel(model);
        return "overview";
    }

    @PostMapping("/losovat-znovu")
    public String redraw() {
        return "redirect:/";
    }
}