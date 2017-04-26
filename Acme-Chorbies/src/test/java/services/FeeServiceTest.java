
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Fee;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FeeServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private FeeService	feeService;


	// Other related services ------------------------------------------------

	// Tests ------------------------------------------------------------------

	/***
	 * Change the manager's and chorbi's event fee
	 * Testing cases:
	 * 1º Good test -> expected: fee changed
	 * 2º Bad test; an unauthenticated actor cannot change the fee -> expected: IllegalArgumentException
	 * 3º Bad test; a chorbi cannot change the fee -> expected: IllegalArgumentException
	 */

	@Test
	public void changeFeeDriver() {

		final Object testingData[][] = {
			//principal, expected exception
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"chorbi1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFeeTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void changeFeeTemplate(final String principal, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Fee fee = this.feeService.getFee();

			fee.setAmountChorbi(5.2);
			fee.setAmountManager(7.6);

			this.feeService.save(fee);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
