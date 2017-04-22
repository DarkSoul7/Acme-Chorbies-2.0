
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class EventChorbi extends DomainEntity {

	//Attributes
	private Date	momentSubscribed;


	//Constructor 
	public EventChorbi() {
		super();
	}

	//Getter & setter

	@Past
	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMomentSubscribed() {
		return this.momentSubscribed;
	}

	public void setMomentSubscribed(final Date momentSubscribed) {
		this.momentSubscribed = momentSubscribed;
	}


	//RelationShips
	private Event	event;
	private Chorbi	chorbi;


	@Valid
	@ManyToOne(optional = false)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(final Event event) {
		this.event = event;
	}

	@Valid
	@ManyToOne(optional = false)
	public Chorbi getChorbi() {
		return this.chorbi;
	}

	public void setChorbi(final Chorbi chorbi) {
		this.chorbi = chorbi;
	}

}
