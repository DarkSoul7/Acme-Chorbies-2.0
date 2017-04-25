
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Event;
import domain.Manager;
import forms.EventForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class EventServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private EventService	eventService;

	// Other related services ------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	// Tests ------------------------------------------------------------------

	/***
	 * Browse event that are going to be organised in less than one month and have seats available
	 * Testing cases:
	 * 1º Good test -> expected: event list returned (size = 0)
	 * 2º Bad test; the method not returns a null list -> expected: IllegalArgumentException
	 * 3º Bad test; the expected list size is 0 -> expected: IllegalArgumentException
	 */

	@Test
	public void browseEarlyEventDriver() {
		final Object[][] testingData = {
			//list size, expected exception
			{
				0, null
			}, {
				null, NullPointerException.class
			}, {
				7, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.browseEarlyEventTemplate((Integer) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void browseEarlyEventTemplate(final Integer listSize, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			final Collection<Event> events = this.eventService.getEarlyEvents();
			Assert.isTrue(events.size() == listSize);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Browse list highlighted events
	 * Testing cases:
	 * 1º Good test -> expected: lists return expected sizes
	 * 2º Bad test; the method not returns a null list -> expected: IllegalArgumentException
	 * 3º Bad test; the expected list size is not correct -> expected: IllegalArgumentException
	 */

	@Test
	public void browseHighlightedEventDriver() {
		final Object[][] testingData = {
			//size greyList, size highlightedList, size non highlightedList, expected exception
			{
				0, 1, 2, null
			}, {
				null, null, null, NullPointerException.class
			}, {
				4, 2, 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.browseHighlightedEventTemplate((Integer) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void browseHighlightedEventTemplate(final Integer greyList, final Integer highlightedList, final Integer nonHighlightedList, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			final Calendar date1 = Calendar.getInstance();
			final Calendar date2 = Calendar.getInstance();
			date1.set(2017, 1, 01);
			date2.set(2018, 01, 01);
			final Collection<EventForm> eventsGreyList = this.eventService.getFinishedEvents();
			final Collection<EventForm> eventsHighlightedList = this.eventService.getFutureHighlighted(date1.getTime(), date2.getTime());
			final Collection<EventForm> eventsNonHighlightedList = this.eventService.nonHighlighted(date1.getTime(), date2.getTime());

			Assert.isTrue(eventsGreyList.size() == greyList);
			Assert.isTrue(eventsHighlightedList.size() == highlightedList);
			Assert.isTrue(eventsNonHighlightedList.size() == nonHighlightedList);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * List events
	 * Testing cases:
	 * 1º Good test -> expected: list manager's events
	 * 2º Bad test; an unauthenticated actor have no events -> expected: IllegalArgumentException
	 * 3º Bad test; a chorbi have no events -> expected: IllegalArgumentException
	 */

	@Test
	public void listEventDriver() {
		final Object[][] testingData = {
			//manager, expected exception
			{
				"manager1", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"chorbi1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEventTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void listEventTemplate(final String principal, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Manager manager = this.managerService.findByPrincipal();
			Assert.notNull(manager.getEvents());

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Register events
	 * Testing cases:
	 * 1º Good test -> expected: event registered
	 * 2º Bad test; an unauthenticated actor cannot register events -> expected: IllegalArgumentException
	 * 3º Bad test; a chorbi cannot register events -> expected: IllegalArgumentException
	 */

	@Test
	public void registerEventDriver() {
		final Object[][] testingData = {
			//manager, expected exception
			{
				"manager1", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"chorbi1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerEventTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void registerEventTemplate(final String principal, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final EventForm eventForm = this.eventService.create();
			eventForm.setDescription("This is a test description to get the 20 minimum characters");
			eventForm.setPicture("http://testing.com");
			eventForm.setSeatsNumber(80);
			eventForm.setTitle("This is a Test title");
			eventForm.setMoment(new Date());

			final Event event = this.eventService.reconstruct(eventForm);

			this.eventService.save(event);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Edit events
	 * Testing cases:
	 * 1º Good test -> expected: event edited
	 * 2º Bad test; an actor who is not a manager cannot edit events -> expected: IllegalArgumentException
	 * 3º Bad test; a manager cannot edit an event that not his -> expected: IllegalArgumentException
	 */

	@Test
	public void editEventDriver() {
		final Object[][] testingData = {
			//manager, event id, expected exception
			{
				"manager1", 92, null
			}, {
				"chorbi1", 93, IllegalArgumentException.class
			}, {
				"manager3", 94, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void editEventTemplate(final String principal, final int eventId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Event event = this.eventService.findOne(eventId);
			event.setTitle("The event has been edited");
			this.eventService.save(event);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Delete events
	 * Testing cases:
	 * 1º Good test -> expected: event deleted
	 * 2º Bad test; an actor who is not a manager cannot delete events -> expected: IllegalArgumentException
	 * 3º Bad test; a manager cannot delete an event that not his -> expected: IllegalArgumentException
	 */

	@Test
	public void deleteEventDriver() {
		final Object[][] testingData = {
			//manager, event id, expected exception
			{
				"manager1", 92, null
			}, {
				"chorbi1", 93, IllegalArgumentException.class
			}, {
				"manager3", 94, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteEventTemplate(final String principal, final int eventId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Event event = this.eventService.findOne(eventId);
			this.eventService.delete(event);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
