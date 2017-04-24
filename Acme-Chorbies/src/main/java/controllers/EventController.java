
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Event;
import forms.EventForm;
import services.EventService;
import services.ManagerService;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	@Autowired
	private EventService	eventService;

	@Autowired
	private ManagerService	managerService;


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
		final Event event = this.eventService.findOne(eventId);

		this.eventService.unRegisterInEvent(event);
		result = new ModelAndView("redirect:/event/listRegister.do");

		return result;
	}

	//MANAGER
	@RequestMapping(value = "/listOfManager", method = RequestMethod.GET)
	public ModelAndView listEventOfManager() {
		final ModelAndView result;
		final Collection<Event> events = this.managerService.findByPrincipal().getEvents();

		result = new ModelAndView("event/list");
		result.addObject("events", events);
		result.addObject("requestURI", "event/manager/listOfManager.do");
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
			event = this.eventService.reconstruct(eventForm);
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
			event = this.eventService.reconstruct(eventForm);
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
