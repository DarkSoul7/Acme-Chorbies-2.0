
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chorbi;
import domain.Like;
import forms.LikeForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LikeServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private LikeService		likeService;

	// Other related services ------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Tests ------------------------------------------------------------------

	/***
	 * Like another chorbi
	 * Testing cases:
	 * 1� Good test -> expected: like registered
	 * 2� Bad test; a chorbi cannot gives a re-like to the same chorbi who have already a like from him -> expected: IllegalArgumentException
	 * 3� Bad test; an unauthenticated actor cannot gives likes -> expected: IllegalArgumentException
	 */

	@Test
	public void likeChorbiDriver() {

		final Object testingData[][] = {
			//principal, chorbi id, expected exception
			{
				"chorbi1", 42, null
			}, {
				"chorbi1", 41, IllegalArgumentException.class
			}, {
				null, 42, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.likeChorbiTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void likeChorbiTemplate(final String principal, final int chorbiId, final Class<?> expectedException) {

		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Chorbi receiver = this.chorbiService.findOne(chorbiId);
			final LikeForm likeForm = this.likeService.create();

			likeForm.setComment("Test");
			likeForm.setIdReceiver(receiver.getId());

			final Like result = this.likeService.reconstruct(likeForm, null);

			this.likeService.save(result);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Cancel like another chorbi
	 * Testing cases:
	 * 1� Good test -> expected: like cancelled
	 * 2� Bad test; a chorbi cannot gives cancel a like if he/she has not give the like before -> expected: IllegalArgumentException
	 * 3� Bad test; an unauthenticated actor cannot cancel likes -> expected: IllegalArgumentException
	 */

	@Test
	public void cancelLikeChorbiDriver() {

		final Object testingData[][] = {
			//principal, chorbi id, expected exception
			{
				"chorbi1", 41, null
			}, {
				"chorbi2", 42, IllegalArgumentException.class
			}, {
				null, 42, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.cancelLikeChorbiTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void cancelLikeChorbiTemplate(final String principal, final int chorbiId, final Class<?> expectedException) {

		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Chorbi sender = this.chorbiService.findByPrincipal();
			final Chorbi receiver = this.chorbiService.findOne(chorbiId);

			final Like like = this.likeService.findLikeFromChorbies(sender, receiver);
			this.likeService.delete(like);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Browse the catalogue of chorbies who have liked him or her as long as he or she has registered a valid credit card
	 * 
	 * Testing cases:
	 * 1� Good test -> expected: chorbi list returned
	 * 2� Bad test; To get the chorbi list the chorbi in question must have a valid credit card registered -> expected: IllegalArgumentException
	 * 3� Bad test; an unauthenticated have no like list-> expected: IllegalArgumentException
	 */

	@Test
	public void listChorbiesYouLikeDriver() {

		final Object testingData[][] = {
			//principal, expected exception
			{
				"chorbi1", null
			}, {
				"chorbi4", IllegalArgumentException.class
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.listChorbiesYouLikeTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void listChorbiesYouLikeTemplate(final String principal, final Class<?> expectedException) {

		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Chorbi chorbi = this.chorbiService.findByPrincipal();

			final Collection<Chorbi> chorbies = this.chorbiService.findChorbisFromAuthor(chorbi);
			Assert.notNull(chorbies);
			Assert.notNull(chorbi.getCreditCard());

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
