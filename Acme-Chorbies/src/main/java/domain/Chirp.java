
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "parentChirp_id" }, name = "UniqueParentChirp") })
public class Chirp extends DomainEntity {
	
	//Attributes
	private String	subject;
	private String	text;
	private Date	moment;
	private String	attachments;
	private Boolean	original;
	
	
	//Constructor
	public Chirp() {
		super();
	}
	
	//Getters and setters
	
	@NotBlank
	@SafeHtml
	@Size(min = 0, max = 80)
	public String getSubject() {
		return this.subject;
	}
	
	public void setSubject(final String subject) {
		this.subject = subject;
	}
	
	@NotBlank
	@SafeHtml
	@Size(min = 0, max = 250)
	public String getText() {
		return this.text;
	}
	
	public void setText(final String text) {
		this.text = text;
	}
	
	@Past
	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	
	public void setMoment(final Date moment) {
		this.moment = moment;
	}
	
	@SafeHtml
	public String getAttachments() {
		return this.attachments;
	}
	
	public void setAttachments(final String attachments) {
		this.attachments = attachments;
	}
	
	@NotNull
	public Boolean getOriginal() {
		return this.original;
	}
	
	public void setOriginal(final Boolean original) {
		this.original = original;
	}
	
	
	//RellationShips
	
	private Actor	sender;
	private Actor	receiver;
	private Chirp	childChirp;
	private Chirp	parentChirp;
	
	
	@Valid
	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}
	
	public void setSender(final Actor sender) {
		this.sender = sender;
	}
	
	@Valid
	@ManyToOne(optional = false)
	public Actor getReceiver() {
		return this.receiver;
	}
	
	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}
	
	@Valid
	@OneToOne(mappedBy = "parentChirp", optional = true)
	public Chirp getChildChirp() {
		return this.childChirp;
	}
	
	public void setChildChirp(final Chirp childChirp) {
		this.childChirp = childChirp;
	}
	
	@Valid
	@OneToOne(optional = true)
	public Chirp getParentChirp() {
		return this.parentChirp;
	}
	
	public void setParentChirp(final Chirp parentChirp) {
		this.parentChirp = parentChirp;
	}
	
}
