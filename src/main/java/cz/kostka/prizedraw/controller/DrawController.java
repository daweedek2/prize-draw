package cz.kostka.prizedraw.controller;

import cz.kostka.prizedraw.model.DrawResult;
import cz.kostka.prizedraw.service.DrawService;
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
        model.addAttribute("vysledky", drawService.getResults());
        model.addAttribute("ceny", drawService.getAvailablePrizes());
        model.addAttribute("lide", drawService.getAllPeople());

        // Data pro kolo štěstí
        model.addAttribute("eligibleWithoutPrize", drawService.getEligibleNames(true));
        model.addAttribute("eligibleAll", drawService.getEligibleNames(false));
        model.addAttribute("nextPrize", drawService.getNextPrizeName().orElse(null));
        return "overview";
    }

    @PostMapping("/losovat")
    public String draw(Model model) {
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

    @PostMapping("/losovat-ze-vsech")
    public String drawFromAll(Model model) {
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
        return "overview";
    }

    @PostMapping("/losovat-znovu")
    public String redraw() {
        return "redirect:/";
    }
}