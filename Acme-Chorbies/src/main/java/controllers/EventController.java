package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Event;
import services.EventService;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController{

	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Event> events;

		events = this.eventService.getEarlyEvents();

		result = new ModelAndView("event/list");
		result.addObject("events", events);
		result.addObject("listForm", true);
		result.addObject("requestURI", "event/list.do");

		return result;
	}
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		Collection<Event> events;

		events = this.eventService.findAll();

		result = new ModelAndView("event/list2");
		result.addObject("events", events);
		result.addObject("requestURI", "event/listAll.do");

		return result;
	}
	
	@RequestMapping(value = "/listRegister", method = RequestMethod.GET)
	public ModelAndView listRegister() {
		ModelAndView result;
		Collection<Event> events;

		events = this.eventService.getEventsRegister();

		result = new ModelAndView("event/list");
		result.addObject("events", events);
		result.addObject("chorbiRegister", true);
		result.addObject("requestURI", "event/list.do");

		return result;
	}
	
	@RequestMapping(value = "/unregister", method = RequestMethod.GET)
	public ModelAndView unregister(@RequestParam(required = true) final int eventId) {
		ModelAndView result;
		Event event = this.eventService.findOne(eventId);

		this.eventService.unRegisterInEvent(event);
		result = new ModelAndView("redirect:/event/listRegister.do");

		return result;
	}
}
