package dk.magenta.datafordeler.registerdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;

/**
 * Created by lars on 16-02-17.
 */
@Controller
public class DemoController {

    @Autowired
    EventSender eventSender;

    @Autowired
    PostnummerRepository postnummerRepository;

    @Autowired
    PostnummerRegistreringRepository postnummerRegistreringRepository;

    @Autowired
    SessionManager sessionManager;

    private void beginRequest() {
        this.sessionManager.beginSession();
    }

    private void endRequest() {
        this.sessionManager.endSession();
    }


    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/postnummer")
    public String list(Model model) {
        beginRequest();
        return "postnummerlist";
    }

    @GetMapping("/postnummer/create")
    public String createPostnummer(Model model) {
        beginRequest();
        Postnummer postnummer = new Postnummer();
        PostnummerRegistrering registrering = new PostnummerRegistrering();
        model.addAttribute("postnummer", postnummer);
        model.addAttribute("postnummerregistrering", registrering);
        model.addAttribute("cancel", "/postnummer");
        return "postnummerregistrering";
    }

    @PostMapping("/postnummer/create")
    public String editItemSubmit(Model model, @ModelAttribute Postnummer postnummer, @ModelAttribute PostnummerRegistrering postnummerRegistrering) {
        this.beginRequest();
        this.postnummerRepository.save(postnummer);
        postnummerRegistrering.setEntity(postnummer);
        this.postnummerRegistreringRepository.save(postnummerRegistrering);
        this.eventSender.sendPostnummerAddEvent(postnummer);
        this.eventSender.sendPostnummerRegistreringAddEvent(postnummerRegistrering);
        return "redirect:/postnummer/"+postnummer.getId();
    }

    @GetMapping("/postnummer/{id}")
    public String editItemVersion(Model model, @PathVariable Long id) {
        this.beginRequest();
        Postnummer postnummer = this.postnummerRepository.findOne(id);
        if (postnummer==null) {
            throw new ItemNotFoundException();
        }
        model.addAttribute("postnummer", postnummer);
        model.addAttribute("allRegistrations", this.postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(postnummer));
        return "postnummer";
    }

    @GetMapping("/postnummer/{id}/update")
    public String createPostnummerRegistrering(Model model, @PathVariable Long id) {
        beginRequest();
        Postnummer postnummer = this.postnummerRepository.findOne(id);
        PostnummerRegistrering registrering = new PostnummerRegistrering();
        Iterator<PostnummerRegistrering> iterator = postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(postnummer).iterator();
        if (iterator.hasNext()) {
            PostnummerRegistrering latestExisting = iterator.next();
            registrering.setNummer(latestExisting.getNummer());
            registrering.setBynavn(latestExisting.getBynavn());
        }
        model.addAttribute("postnummer", postnummer);
        model.addAttribute("postnummerregistrering", registrering);
        model.addAttribute("cancel", "/postnummer/" + id);
        return "postnummerregistrering";
    }

    @PostMapping("/postnummer/{id}/update")
    public String createPostnummerRegistreringSubmit(Model model, @PathVariable Long id, @ModelAttribute PostnummerRegistrering registrering) {
        beginRequest();
        Postnummer postnummer = this.postnummerRepository.findOne(id);
        registrering.setEntity(postnummer);
        this.postnummerRegistreringRepository.save(registrering);
        this.eventSender.sendPostnummerRegistreringAddEvent(registrering);
        return "redirect:/postnummer/"+id;
    }

    @ModelAttribute("allPostnummerItems")
    public Iterable<Postnummer> allPostnummerItems() {
        return postnummerRepository.findAll();
    }

}
