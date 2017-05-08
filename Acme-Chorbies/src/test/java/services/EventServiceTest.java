
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
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
	 * 2º Bad test; the method not returns a null list -> expected: NullPointerException
	 * 3º Bad test; the expected list size is 0 -> expected: IllegalArgumentException
	 */

	@Test
	public void browseEarlyEventDriver() {
		final Object[][] testingData = {
			//list size, expected exception
			{
				2, null
			}, {
				null, NullPointerException.class
			}, {
				7, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.browseEarlyEventTemplate((Integer) testingData[i][0], (Class<?>) testingData[i][1]);
		}
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
	 * 2º Bad test; the method not returns a null list -> expected: NullPointerException
	 * 3º Bad test; dates are too old -> expected: IllegalArgumentException
	 */

	@Test
	public void browseHighlightedEventDriver() {
		final Object[][] testingData = {
			//size greyList, size highlightedList, size non highlightedList, date1, date2, expected exception
			{
				1, 4, 0, "2017-01-01", "2018-01-01", null
			}, {
				null, null, null, "2017-01-01", "2017-01-01", NullPointerException.class
			}, {
				4, 2, 0, "2001-01-01", "2004-01-01", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.browseHighlightedEventTemplate((Integer) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		}
	}

	protected void browseHighlightedEventTemplate(final Integer greyList, final Integer highlightedList, final Integer nonHighlightedList, final String date1, final String date2, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			final DateTime time1 = DateTime.parse(date1);
			final DateTime time2 = DateTime.parse(date2);
			final Collection<EventForm> eventsGreyList = this.eventService.getFinishedEvents();
			final Collection<EventForm> eventsHighlightedList = this.eventService.getFutureHighlighted(time1.toDate(), time2.toDate());
			final Collection<EventForm> eventsNonHighlightedList = this.eventService.nonHighlighted(time1.toDate(), time2.toDate());

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

		for (int i = 0; i < testingData.length; i++) {
			this.listEventTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
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

		for (int i = 0; i < testingData.length; i++) {
			this.registerEventTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
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

			final DateTime time = new DateTime();
			eventForm.setEventMoment(time.plusDays(1).toDate());

			final Event event = this.eventService.reconstruct(eventForm, null);

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
				"manager1", 94, null
			}, {
				"chorbi1", 95, IllegalArgumentException.class
			}, {
				"manager3", 96, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.editEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
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
				"manager1", 94, null
			}, {
				"chorbi1", 95, IllegalArgumentException.class
			}, {
				"manager3", 96, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.deleteEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
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

	/***
	 * Register to an event
	 * Testing cases:
	 * 1º Good test -> expected: chorbi registered in event
	 * 2º Bad test; an actor who is not chorbi cannot register to an event -> expected: IllegalArgumentException
	 * 3º Bad test; an event whose seats available is 0 cannot permit subscriptions -> expected: IllegalArgumentException
	 */

	@Test
	public void subscribeEventDriver() {
		final Object[][] testingData = {
			//chorbi, event id, expected exception
			{
				"chorbi1", 98, null
			}, {
				"admin", 95, IllegalArgumentException.class
			}, {
				"chorbi2", 98, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.subscribeEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void subscribeEventTemplate(final String principal, final int eventId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Event event = this.eventService.findOne(eventId);

			if (principal.equals("chorbi2")) {
				event.setSeatsNumber(event.getSeatsNumber() - 1);
			}

			this.eventService.registerInEvent(event);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Unregister to an event
	 * Testing cases:
	 * 1º Good test -> expected: chorbi unregistered
	 * 2º Bad test; an actor who is not chorbi cannot unregister from an event -> expected: IllegalArgumentException
	 * 3º Bad test; a chorbi who has not been registered to the event in question cannot unregister from it -> expected: IllegalArgumentException
	 */

	@Test
	public void unsubscribeEventDriver() {
		final Object[][] testingData = {
			//chorbi, event id, expected exception
			{
				"chorbi1", 94, null
			}, {
				"admin", 95, IllegalArgumentException.class
			}, {
				"chorbi2", 98, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.unsubscribeEventTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void unsubscribeEventTemplate(final String principal, final int eventId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Event event = this.eventService.findOne(eventId);

			this.eventService.unRegisterInEvent(event);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Browse the list of events a chorbi has been registered
	 * Testing cases:
	 * 1º Good test -> expected: list returned
	 * 2º Bad test; an authenticated actor who is not have no event list -> expected: IllegalArgumentException
	 * 3º Bad test; an unauthenticated actor have no event list -> expected: IllegalArgumentException
	 */

	@Test
	public void browseEventListDriver() {
		final Object[][] testingData = {
			//chorbi, event id, expected exception
			{
				"chorbi1", null
			}, {
				"admin", IllegalArgumentException.class
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.browseEventListTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void browseEventListTemplate(final String principal, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Event> events = this.eventService.getEventsRegister();

			Assert.notNull(events);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
