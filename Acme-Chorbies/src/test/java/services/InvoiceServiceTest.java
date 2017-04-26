
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Chorbi;
import domain.Invoice;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class InvoiceServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private InvoiceService	invoiceService;

	// Other related services ------------------------------------------------

	@Autowired
	private EventService	eventService;

	@Autowired
	private ChorbiService	chorbiService;


	// Tests ------------------------------------------------------------------

	/***
	 * Update chorbies's monthly fees
	 * Testing cases:
	 * 1º Good test -> expected: monthly fees returned
	 * 2º Bad test; an unauthenticated actor cannot generate monthly fees. expected -> IllegalArgumentException
	 * 3º Bad test; the dates are incorrect. expected -> IllegalArgumentException
	 */

	@Test
	public void updateFeesDriver() {

		final Object testingData[][] = {
			//principal, open period, end period, expected exception
			{
				"admin", "2017-04-01", "2017-05-01", null
			}, {
				null, "2017-04-01", "2017-05-01", IllegalArgumentException.class
			}, {
				"admin", "2017-07-01", "2017-05-01", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.updateFeesTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void updateFeesTemplate(final String principal, final String openPeriod, final String endPeriod, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final DateTime date1 = DateTime.parse(openPeriod);
			final DateTime date2 = DateTime.parse(endPeriod);

			final Collection<Chorbi> chorbies = this.chorbiService.findAll();
			for (final Chorbi chorbi : chorbies) {
				final Double amount = this.eventService.getChorbiFeeMonthlyAmount(chorbi, date1.toDate(), date2.toDate());

				final Invoice invoice = this.invoiceService.create();
				invoice.setChorbi(chorbi);
				invoice.setOpenPeriod(date1.toDate());
				invoice.setEndPeriod(date2.toDate());
				invoice.setPaid(false);
				invoice.setAmount(amount);

				this.invoiceService.save(invoice);
			}

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

}
