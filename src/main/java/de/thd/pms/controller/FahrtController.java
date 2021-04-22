package de.thd.pms.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.thd.pms.dto.FahrtBootPersonDTO;
import de.thd.pms.model.Boot;
import de.thd.pms.model.Fahrt;
import de.thd.pms.service.BootService;
import de.thd.pms.service.DaoException;
import de.thd.pms.service.FahrtService;
import de.thd.pms.service.PersonService;

@Controller
@RequestMapping("/fahrt")
public class FahrtController {
	private static Logger log = LoggerFactory.getLogger(FahrtController.class);
	@Autowired
    private FahrtService fahrtService;
	@Autowired
    private PersonService personService;
	@Autowired
    private BootService bootService;

	/**
     * <p>Searches for all {@link Fahrt} objects and returns them in a 
     * <code>Collection</code>.</p>
     * 
     * <p>Expected HTTP GET and request '/findAll'.</p>
     */
    @RequestMapping(value="/findAll", method=RequestMethod.GET)
    public ModelAndView findAll() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("title", "Fahrten");
		mv.addObject("message", "Alle Fahrten aller Boote im Verein.");
		mv.addObject("fahrten", fahrtService.findAllDTO());
		mv.setViewName("list-fahrten");
		return mv;
    }
    
    @RequestMapping(value="/findAdmin", method=RequestMethod.GET)
    public ModelAndView findAllVoll() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("title", "Alle Fahrten");
		mv.addObject("message", "Alle Fahrten aller Boote im Verein (Admin).");
		List<FahrtBootPersonDTO> fahrten = fahrtService.findAllVoll();
		mv.addObject("fahrten", fahrten);
		mv.addObject("unempty", Boolean.valueOf(fahrten.size() > 0));
		mv.setViewName("list-fahrten-voll");
		return mv;
    }

    /**
     * @param id der Primärschlüssel des {@link Boot}
     * @return
     */
    @RequestMapping(value="/beginne", method=RequestMethod.GET)
    public ModelAndView beginne(@RequestParam(required=true) Long id) {
    	ModelAndView mv = new ModelAndView();
		mv.addObject("title", "Fahrt eintragen");
		mv.addObject("message", "Tragen Sie hier die Teilnehmer der Fahrt ein.");
		mv.addObject("boot", bootService.findById(id));
		mv.addObject("personen", personService.findAll());
		mv.setViewName("edit-fahrt");
    	return mv;
    }

    /**
     * <p>Saves a Fahrt.</p>
     * 
     * <p>Expected HTTP POST and request '/save'.</p>
     * @param id of a Boot
     * @param sitz array of ids of Person objects. 
     * @return
     */
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public ModelAndView save(@RequestParam(required=true) Long id, Long[] sitz) {
    	log.info("save " + id + " " + Arrays.toString(sitz));
    	try {
			fahrtService.beginne(id, sitz);
			return new ModelAndView("redirect:aktuelle");
		} catch (DaoException e) {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("message", e.getMessage());
			return mv;
		}
    }

	/**
     * <p>Searches for {@link Fahrt} objects that are not yet terminated and returns them in a 
     * <code>Collection</code>.</p>
     * 
     * <p>Expected HTTP GET and request '/aktuelle'.</p>
     */
    @RequestMapping(value="/aktuelle", method=RequestMethod.GET)
    public ModelAndView findAktuelle() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("title", "Aktuelle Fahrten");
		mv.addObject("message", "Alle Fahrten die noch nicht beendet sind. Klicken Sie auf ein Boot um die Fahrt zu beenden.");
		mv.addObject("fahrten", fahrtService.findNichtBeendetDTO());
		mv.setViewName("list-fahrten");
		return mv;
    }

    /**
     * <p>Terminates a Fahrt.</p>
     * 
     * <p>Expected HTTP GET and request '/beende?id=5'.</p>
     * @return 
     */
    @RequestMapping(value="/beende", method=RequestMethod.GET)
    public String beende(@RequestParam(required=true) Long id) {
    	fahrtService.beende(id);
        return "redirect:aktuelle";
    }

}
