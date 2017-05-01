
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Chirp;
import domain.Chorbi;
import domain.Manager;
import forms.ChirpForm;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ChirpService	chirpService;

	// Other related services ------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;
	
	@Autowired
	private ManagerService managerService;


	// Tests ------------------------------------------------------------------

	/***
	 * Chirp to another chorbi
	 * Testing cases:
	 * 1º Good test -> expected: chirp sent
	 * 2º Bad test; an unauthenticated actor cannot send a chirp -> expected: IllegalArgumentException
	 */

	@Test
	public void sendChirpDriver() {

		final Object testingData[][] = {
			//principal, chorbi receiver id, expected exception
			{
				"chorbi1", 43, null
			}, {
				null, 41, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void sendChirpTemplate(final String principal, final int receiverId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Chorbi receiver = this.chorbiService.findOne(receiverId);
			final ChirpForm chirpForm = this.chirpService.create();

			chirpForm.setReceiver(receiver);
			chirpForm.setSubject("Test");
			chirpForm.setText("Testing");

			final Chirp chirp = this.chirpService.reconstruct(chirpForm);
			this.chirpService.save(chirp);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Reply chirp
	 * Testing cases:
	 * 1º Good test -> expected: chirp replied
	 * 2º Bad test; an unauthenticated actor cannot reply a chirp -> expected: IllegalArgumentException
	 */

	@Test
	public void replyChirpDriver() {

		final Object testingData[][] = {
			//principal, reply chirp, expected exception
			{
				"chorbi1", 52, null
			}, {
				null, 52, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.replyChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void replyChirpTemplate(final String principal, final int replyId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final ChirpForm chirpForm = this.chirpService.create();
			final Chirp reply = this.chirpService.findOne(replyId);

			chirpForm.setParentChirpId(replyId);
			chirpForm.setReceiver(reply.getSender());
			chirpForm.setSubject("Test");
			chirpForm.setText("Testing");

			final Chirp chirp = this.chirpService.reconstruct(chirpForm);
			this.chirpService.save(chirp);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Resend chirp
	 * Testing cases:
	 * 1º Good test -> expected: chirp replied
	 * 2º Bad test; an unauthenticated actor cannot reply a chirp -> expected: IllegalArgumentException
	 */

	@Test
	public void resendChirpDriver() {

		final Object testingData[][] = {
			//principal, receiver id, chirp id, expected exception
			{
				"chorbi1", 42, 52, null
			}, {
				null, 41, 52, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.resendChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void resendChirpTemplate(final String principal, final int receiverId, final int chirpId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Chirp originalChirp = this.chirpService.findOne(chirpId);

			final ChirpForm resend = this.chirpService.toFormObject(originalChirp, false);

			resend.setText("Resend");

			final Chirp result = this.chirpService.reconstruct(resend);
			this.chirpService.save(result);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Erase chirp
	 * Testing cases:
	 * 1º Good test -> expected: chirp deleted
	 * 2º Bad test; an unauthenticated actor cannot delete a chirp -> expected: IllegalArgumentException
	 * 3º Bad test; A chorbi cannot delete a chirp that is not his -> expected: IllegalArgumentException
	 */

	@Test
	public void eraseChirpDriver() {

		final Object testingData[][] = {
			//principal, chirp id, expected exception
			{
				"chorbi3", 49, null
			}, {
				null, 47, IllegalArgumentException.class
			}, {
				"chorbi2", 52, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.eraseChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void eraseChirpTemplate(final String principal, final int chirpId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Chirp chirp = this.chirpService.findOne(chirpId);

			this.chirpService.delete(chirp);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
	
	/***
	 * Broadcast chirp
	 * Testing cases:
	 * 1º Good test -> expected: chirp sent to all chorbies that manager have.
	 * 2º Bad test; an unauthenticated actor cannot broadcast chirp -> expected: IllegalArgumentException
	 * 3º Bad test; Attachment incorrect -> expected: IllegalArgumentException
	 */
	
	@Test
	public void broadcastChirp() {

		final Object testingData[][] = {
			//principal, chorbi receiver id, expected exception
			{
				"manager1", "Subject test", "Text test", null, null
			}, {
				null, "Subject test", "Text test", null, IllegalArgumentException.class
			},
			{
				"manager1", "Subject test", "Text test", "Attachment incorrect", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.broadcastChirpTemplate((String) testingData[i][0],(String) testingData[i][1],(String) testingData[i][2],
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	
	protected void broadcastChirpTemplate(final String principal, final String subject, final String text, final String attachments, 
			final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			Manager manager = managerService.findByPrincipal();
			final ChirpForm chirpForm = this.chirpService.create();
			chirpForm.setReceiver(manager);
			chirpForm.setEventId(manager.getEvents().iterator().next().getId());
			chirpForm.setAttachments(attachments);
			chirpForm.setText(text);
			chirpForm.setSubject(subject);
			chirpService.broadcastChirp(chirpForm);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
