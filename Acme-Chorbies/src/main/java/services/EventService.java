
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventRepository;
import domain.Chorbi;
import domain.Event;
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

	public Collection<Event> getFinishedEvents() {
		return this.eventRepository.getFinishedEvents();
	}

	public Collection<Event> getFutureEvents() {
		final DateTime now = new DateTime();
		final DateTime aMonthLater = now.plusMonths(1);
		final Collection<Event> result = this.eventRepository.getFutureEvents(now.toDate(), aMonthLater.toDate());

		return result;
	}

	public Event reconstruct(final EventForm eventForm) throws CheckDigitException {
		Assert.notNull(eventForm);
		final Manager manager = this.managerService.findByPrincipal();

		Event result = new Event();
		if (eventForm.getId() != 0) {
			result = this.findOne(eventForm.getId());
			Assert.isTrue(result.getManager().equals(manager));
		} else
			result.setManager(manager);
		result.setDescription(eventForm.getDescription());
		result.setMoment(eventForm.getMoment());
		result.setPicture(eventForm.getPicture());
		result.setSeatsNumber(eventForm.getSeatsNumber());
		result.setTitle(eventForm.getTitle());
		result.setAmount(this.feeService.getFee().getAmount());
		return result;
	}

	public Integer getExistChorbiInEvent(final int idEvent, final int idChorbi) {
		return this.eventRepository.getExistChorbiInEvent(idEvent, idChorbi);
	}

	public double getChorbiFeeMonthlyAmount(final Chorbi chorbi) {
		Assert.notNull(chorbi);
		this.administratorService.findByPrincipal();
		final DateTime now = new DateTime();
		final Calendar limitDateMinusMonth = Calendar.getInstance();

		//1-(month - 1)-year
		if (now.getMonthOfYear() == 1)
			limitDateMinusMonth.set(now.getYear() - 1, 12, 1);
		else
			limitDateMinusMonth.set(now.getYear(), now.getMonthOfYear() - 1, 1);

		//1-month-year
		final Calendar limitDate = Calendar.getInstance();
		limitDate.set(now.getYear(), now.getMonthOfYear(), 1);

		final Collection<Event> events = this.eventRepository.getEventsNotFinishedByChorbi(chorbi.getId(), limitDateMinusMonth.getTime(), limitDate.getTime());
		double result = 0.0;

		for (final Event event : events)
			result += event.getAmount();

		return result;
	}

	public Collection<Event> getEventsRegister() {
		return this.chorbiService.findByPrincipal().getEvents();
	}

	public void registerInEvent(final Event event) {

		final Chorbi chorbi = this.chorbiService.findByPrincipal();

		Assert.notNull(event);
		Assert.isTrue(event.getSeatsNumber() > event.getChorbies().size());
		Assert.isTrue(this.getExistChorbiInEvent(event.getId(), chorbi.getId()) == 0);

		event.getChorbies().add(chorbi);

		this.eventRepository.save(event);
	}

	public void unRegisterInEvent(final Event event) {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();

		Assert.notNull(event);
		Assert.isTrue(this.getExistChorbiInEvent(event.getId(), chorbi.getId()) == 0);

		event.getChorbies().remove(chorbi);

		this.eventRepository.save(event);
	}

}
