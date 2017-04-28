
package forms;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import domain.Actor;

public class ChirpForm {

	//Attributes
	private String	subject;
	private String	text;
	private String	attachments;
	private Actor	receiver;
	private Integer	parentChirpId;
	private Integer eventId;


	//Constructor
	public ChirpForm() {
		super();
	}

	//Getters and setters

	@NotBlank
	@SafeHtml
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	@SafeHtml
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@SafeHtml
	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final String attachments) {
		this.attachments = attachments;
	}

	@Valid
	@NotNull
	public Actor getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}

	@Min(0)
	public Integer getParentChirpId() {
		return this.parentChirpId;
	}

	public void setParentChirpId(final Integer parentChirpId) {
		this.parentChirpId = parentChirpId;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	
	

}
