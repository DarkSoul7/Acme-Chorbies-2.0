
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Chorbi;
import domain.Event;
import domain.EventChorbi;
import domain.Manager;
import forms.EventForm;

@Service
@Transactional
public class EventService {
	
	//Managed repository
	@Autowired
	private EventRepository			eventRepository;
	
	//Supported services
	
	@Autowired
	private ManagerService			managerService;
	
	@Autowired
	private ChorbiService			chorbiService;
	
	@Autowired
	private ChirpService			chirpService;
	
	@Autowired
	private FeeService				feeService;
	
	@Autowired
	private AdministratorService	administratorService;
	
	@Autowired
	private EventChorbiService		eventChorbiService;
	
	
	//Constructor
	
	public EventService() {
		super();
	}
	
	//Simple CRUD methods
	
	public Event findOne(final int eventId) {
		final Event result = this.eventRepository.findOne(eventId);
		return result;
	}
	
	public Collection<Event> findAll() {
		return this.eventRepository.findAll();
	}
	
	public EventForm create() {
		
		final EventForm result = new EventForm();
		
		return result;
	}
	
	public Event save(final Event event) throws CheckDigitException {
		Assert.notNull(event);
		final Manager manager = this.managerService.findByPrincipal();
		Event result = null;
		Assert.isTrue(event.getManager().equals(manager));
		
		//Checking Manager's credit card
		final Boolean creditCard = this.chorbiService.checkCreditCard(manager.getCreditCard());
		Assert.isTrue(creditCard);
		
		if (event.getId() != 0)
			try {
				result = this.eventRepository.save(event);
				this.chirpService.createDefaultEventChirp(true, event);
				
			} catch (final Throwable e) {
				System.err.println(e);
			}
		else
			result = this.eventRepository.save(event);
		return result;
	}
	
	public void delete(final Event event) {
		Assert.notNull(event);
		final Manager manager = this.managerService.findByPrincipal();
		Assert.isTrue(event.getManager().equals(manager));
		
		try {
			this.chirpService.createDefaultEventChirp(false, event);
			final Collection<EventChorbi> eventChorbies = event.getEventChorbies();
			this.eventChorbiService.deleteAll(eventChorbies);
			this.eventRepository.delete(event);
		} catch (final Throwable e) {
			System.err.println(e);
		}
		
	}
	
	//Other business methods
	
	public Collection<Event> getEarlyEvents() {
		final DateTime now = new DateTime();
		final DateTime aMonthLater = now.plusMonths(1);
		
		final Collection<Event> result = this.eventRepository.getEarlyEvents(now.toDate(), aMonthLater.toDate());
		return result;
	}
	
	public Collection<EventForm> getFinishedEvents() {
		return this.eventRepository.getFinishedEvents();
	}
	
	public Collection<Event> getFutureEvents() {
		final DateTime now = new DateTime();
		final DateTime aMonthLater = now.plusMonths(1);
		final Collection<Event> result = this.eventRepository.getFutureEvents(now.toDate(), aMonthLater.toDate());
		
		return result;
	}
	
	
	@Autowired
	private Validator	validator;
	
	
	public Event reconstruct(final EventForm eventForm, final BindingResult binding) throws CheckDigitException {
		Assert.notNull(eventForm);
		final Manager manager = this.managerService.findByPrincipal();
		
		Event result = new Event();
		if (eventForm.getId() != 0) {
			result = this.findOne(eventForm.getId());
			Assert.isTrue(result.getManager().equals(manager));
		} else {
			final Collection<EventChorbi> eventsChorbi = new ArrayList<EventChorbi>();
			result.setManager(manager);
			result.setEventChorbies(eventsChorbi);
		}
		result.setDescription(eventForm.getDescription());
		result.setEventMoment(eventForm.getEventMoment());
		result.setPicture(eventForm.getPicture());
		result.setSeatsNumber(eventForm.getSeatsNumber());
		result.setTitle(eventForm.getTitle());
		result.setAmount(this.feeService.getFee().getAmountManager());
		
		this.validator.validate(result, binding);
		
		if (result.getEventMoment() != null) {
			if (!result.getEventMoment().after(new Date())) {
				String[] codigos = { "javax.validation.constraints.Future.message" };
				FieldError error = new FieldError("eventForm", "eventMoment", result.getEventMoment(), false, codigos, null, "");
				binding.addError(error);
			}
		}
		
		return result;
	}
	
	public EventForm toFormObject(final Event event) {
		final EventForm result = this.create();
		result.setSeatsNumber(event.getSeatsNumber());
		result.setDescription(event.getDescription());
		result.setTitle(event.getTitle());
		result.setEventMoment(event.getEventMoment());
		result.setId(event.getId());
		result.setPicture(event.getPicture());
		return result;
	}
	
	public Integer getExistChorbiInEvent(final int idEvent, final int idChorbi) {
		return this.eventRepository.getExistChorbiInEvent(idEvent, idChorbi);
	}
	
	public double getChorbiFeeMonthlyAmount(final Chorbi chorbi, final Date openPeriod, final Date endPeriod) {
		Assert.notNull(chorbi);
		Assert.isTrue(openPeriod.before(endPeriod));
		this.administratorService.findByPrincipal();
		final DateTime now = new DateTime();
		final Calendar limitDateMinusMonth = Calendar.getInstance();
		Double result;
		
		//1-(month - 1)-year
		if (now.getMonthOfYear() == 1)
			limitDateMinusMonth.set(now.getYear() - 1, 12, 1);
		else
			limitDateMinusMonth.set(now.getYear(), now.getMonthOfYear() - 1, 1);
		
		//1-month-year
		final Calendar limitDate = Calendar.getInstance();
		limitDate.set(now.getYear(), now.getMonthOfYear(), 1);
		
		result = this.eventRepository.getChorbisFeeAmountFromEventsBetweenDates(chorbi.getId(), limitDateMinusMonth.getTime(), limitDate.getTime());
		
		if (result == null)
			result = 0.0;
		
		return result;
	}
	
	public Collection<Event> getEventsRegister() {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		return this.eventRepository.getEventsRegister(chorbi.getId());
	}
	
	public void registerInEvent(final Event event) {
		
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		
		Assert.notNull(event);
		Assert.isTrue(event.getSeatsNumber() > event.getEventChorbies().size());
		Assert.isTrue(this.getExistChorbiInEvent(event.getId(), chorbi.getId()) == 0);
		
		final EventChorbi eventChorbi = this.eventChorbiService.create();
		eventChorbi.setChorbi(chorbi);
		eventChorbi.setEvent(event);
		
		this.eventChorbiService.save(eventChorbi);
	}
	
	public void unRegisterInEvent(final Event event) {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		
		Assert.notNull(event);
		Assert.isTrue(this.getExistChorbiInEvent(event.getId(), chorbi.getId()) == 1);
		
		final EventChorbi eventChorbi = this.eventChorbiService.findEventChorbiByParameters(chorbi, event);
		
		this.eventChorbiService.delete(eventChorbi);
	}
	
	public Collection<EventForm> getFutureHighlighted(final Date currentDate, final Date currentDatePlusMonth) {
		final DateTime past = new DateTime("2016-01-01");
		Assert.isTrue(currentDate.after(past.toDate()));
		Assert.isTrue(currentDatePlusMonth.after(past.toDate()));
		
		return this.eventRepository.getFutureHighlighted(currentDate, currentDatePlusMonth);
	}
	
	public Collection<EventForm> nonHighlighted(final Date currentDate, final Date currentDatePlusMonth) {
		return this.eventRepository.nonHighlighted(currentDate, currentDatePlusMonth);
	}
	
}
