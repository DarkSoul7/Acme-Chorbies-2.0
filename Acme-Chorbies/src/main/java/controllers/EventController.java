
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Chorbi;
import domain.Event;
import domain.EventChorbi;
import forms.EventForm;
import security.LoginService;
import services.ChorbiService;
import services.EventService;
import services.ManagerService;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	@Autowired
	private EventService eventService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ChorbiService chorbiService;

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
		final DateTime now = new DateTime();
		final DateTime aMonthLater = now.plusMonths(1);
		final Collection<EventForm> eventsGreyList = this.eventService.getFinishedEvents();
		final Collection<EventForm> eventsHighlightedList = this.eventService.getFutureHighlighted(now.toDate(),
				aMonthLater.toDate());
		final Collection<EventForm> eventsNonHighlightedList = this.eventService.nonHighlighted(now.toDate(),
				aMonthLater.toDate());

		final Collection<EventForm> allEvents = new ArrayList<EventForm>();
		allEvents.addAll(eventsGreyList);
		allEvents.addAll(eventsHighlightedList);
		allEvents.addAll(eventsNonHighlightedList);

		List<Integer> listChorbiJoinEventYet = new ArrayList<Integer>();
		if (LoginService.isAuthenticated()) {
			Chorbi chorbi = chorbiService.findByPrincipal();
			for(EventChorbi eventChorbi : chorbi.getEventChorbies()){
				for (EventForm eventForm : allEvents) {
					if (eventChorbi.getEvent().getId() == eventForm.getId()) {
						listChorbiJoinEventYet.add(eventForm.getId());
					}
					
				}
			}
		}

		result = new ModelAndView("event/listAll");
		result.addObject("events", allEvents);
		result.addObject("listChorbiJoinEventYet", listChorbiJoinEventYet);
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
		final Event event = this.eventService.findOne(eventId);

		this.eventService.unRegisterInEvent(event);
		result = new ModelAndView("redirect:/event/listRegister.do");

		return result;
	}

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(@RequestParam(required = true) final int eventId) {
		ModelAndView result;
		final Event event = this.eventService.findOne(eventId);

		this.eventService.registerInEvent(event);
		result = new ModelAndView("redirect:/event/listAll.do");

		return result;
	}

	// MANAGER
	@RequestMapping(value = "/listOfManager", method = RequestMethod.GET)
	public ModelAndView listEventOfManager() {
		final ModelAndView result;
		final Collection<Event> events = this.managerService.findByPrincipal().getEvents();

		result = new ModelAndView("event/list");
		result.addObject("events", events);
		result.addObject("requestURI", "event/listOfManager.do");
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final EventForm eventForm = this.eventService.create();

		result = this.createModelAndView(eventForm);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final EventForm eventForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView();
		Event event = new Event();

		try {
			event = this.eventService.reconstruct(eventForm, binding);
		} catch (final Exception e) {
			result = this.createModelAndView(eventForm, "event.commit.error");
		}
		if (binding.hasErrors())
			result = this.createModelAndView(eventForm);
		else
			try {
				this.eventService.save(event);
				result = new ModelAndView("redirect:/event/listOfManager.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(eventForm, "event.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@Valid final EventForm eventForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView();
		Event event = new Event();

		try {
			event = this.eventService.reconstruct(eventForm, binding);
		} catch (final Exception e) {
			result = this.createEditModelAndView(eventForm, "event.commit.error");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(eventForm);
		else
			try {
				this.eventService.save(event);
				result = new ModelAndView("redirect:/event/listOfManager.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(eventForm, "event.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int eventId) {
		ModelAndView result;
		final Event event = this.eventService.findOne(eventId);
		final EventForm eventForm = this.eventService.toFormObject(event);
		result = this.createEditModelAndView(eventForm);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int eventId) {
		ModelAndView result;
		final Event event = this.eventService.findOne(eventId);

		try {
			this.eventService.delete(event);
			result = new ModelAndView("redirect:/event/listOfManager.do");
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:/event/listOfManager.do");
			result.addObject("errorMessage", "event.delete.error");
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final EventForm eventForm) {
		final ModelAndView result = this.createEditModelAndView(eventForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final EventForm eventForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("event/edit");
		result.addObject("eventForm", eventForm);
		result.addObject("message", message);
		result.addObject("requestURI", "event/edit.do");

		return result;
	}

	protected ModelAndView createModelAndView(final EventForm eventForm) {
		final ModelAndView result = this.createModelAndView(eventForm, null);
		return result;
	}

	protected ModelAndView createModelAndView(final EventForm eventForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("event/register");
		result.addObject("eventForm", eventForm);
		result.addObject("message", message);
		result.addObject("requestURI", "event/register.do");

		return result;
	}

}
