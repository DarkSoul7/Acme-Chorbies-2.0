
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Event;
import forms.EventForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private EventService	eventService;


	// Other related services ------------------------------------------------

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
	 * Manage events
	 * Testing cases:
	 * 1º Good test -> expected: list manager's events
	 * 2º Good test -> expected: register manager's event
	 * 3º Good test -> expected: edit manager's events
	 * 4º Good test -> expected: delete manager's events
	 * 5º Bad test; A manager cannot edit an event that is not his -> expected: IllegalArgumentException
	 * 6º Bad test; A manager cannot delete an event that is not his -> expected: IllegalArgumentException
	 * 7º Bad test; An unauthenticated actor cannot register an event -> expected: IllegalArgumentException
	 * 8º Bad test; An unauthenticated actor cannot delete an event -> expected: IllegalArgumentException
	 * 9º Bad test; A chorbi cannot register an event -> expected: IllegalArgumentException
	 * 10º Bad test; A chorbi cannot delete an event -> expected: IllegalArgumentException
	 */

	//TODO: test de los 10 casos de prueba
	@Test
	public void manageEventDriver() {
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

	protected void manageEventTemplate(final Integer greyList, final Integer highlightedList, final Integer nonHighlightedList, final Class<?> expectedException) {
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
}
