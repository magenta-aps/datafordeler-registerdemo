package dk.magenta.datafordeler.registerdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

/**
 * Created by lars on 16-02-17.
 */
@Controller
@RequestMapping("/postnummer")
public class PostnummerController {

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

    private static final boolean EVENT_SEND_ONLY_REFERENCES = true;

    @GetMapping("")
    public String list(Model model) {
        beginRequest();
        return "postnummerlist";
    }

    @GetMapping("/create")
    public String createPostnummer(Model model) {
        beginRequest();
        Postnummer postnummer = new Postnummer();
        PostnummerRegistrering registrering = new PostnummerRegistrering(1);
        model.addAttribute("postnummer", postnummer);
        model.addAttribute("postnummerregistrering", registrering);
        model.addAttribute("cancel", "/postnummer");
        return "postnummerregistrering";
    }

    @PostMapping("/create")
    public String createPostnummer(Model model, @ModelAttribute Postnummer postnummer, @ModelAttribute PostnummerRegistrering postnummerRegistrering) {
        this.beginRequest();
        this.postnummerRepository.save(postnummer);
        this.createPostnummerRegistrering(postnummer, postnummerRegistrering);
        return "redirect:/postnummer/"+postnummer.getId();
    }

    @GetMapping("/{id}")
    public String viewPostnummer(Model model, @PathVariable Long id) {
        this.beginRequest();
        Postnummer postnummer = this.postnummerRepository.findOne(id);
        if (postnummer==null) {
            throw new ItemNotFoundException();
        }
        model.addAttribute("postnummer", postnummer);
        model.addAttribute("allRegistrations", this.postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(postnummer));
        return "postnummer";
    }

    @GetMapping("/{id}/update")
    public String updatePostnummer(Model model, @PathVariable Long id) {
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

    @PostMapping("/{id}/update")
    public String updatePostnummer(Model model, @PathVariable Long id, @ModelAttribute PostnummerRegistrering postnummerRegistrering) {
        beginRequest();
        Postnummer postnummer = this.postnummerRepository.findOne(id);
        this.createPostnummerRegistrering(postnummer, postnummerRegistrering);
        return "redirect:/postnummer/"+id;
    }

    @ModelAttribute("allPostnummerItems")
    public Iterable<Postnummer> allPostnummerItems() {
        return postnummerRepository.findAll();
    }

    private PostnummerRegistrering createPostnummerRegistrering(Postnummer postnummer, PostnummerRegistrering registrering) {
        registrering.setEntity(postnummer);
        PostnummerRegistrering latest = null;
        for (PostnummerRegistrering l : this.postnummerRegistreringRepository.findByEntityOrderByRegisterFrom(postnummer)) {
            latest = l;
        }
        int nextSequenceNumber = latest != null ? latest.sequenceNumber : 1;
        registrering.setSequenceNumber(nextSequenceNumber);
        this.postnummerRegistreringRepository.save(registrering);
        this.eventSender.sendPostnummerRegistreringAddEvent(registrering, EVENT_SEND_ONLY_REFERENCES);
        return registrering;
    }

    @RequestMapping(value="/{checksum}/get", method=RequestMethod.GET)
    public @ResponseBody PostnummerRegistrering getRegistrering(@PathVariable String checksum, Model model) {
        beginRequest();
        PostnummerRegistrering registrering = postnummerRegistreringRepository.findOneByChecksum(checksum);
        endRequest();
        return registrering;
    }

}
