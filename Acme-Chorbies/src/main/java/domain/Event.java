
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Access(AccessType.PROPERTY)
@Entity
@Table(name = "\"Event\"")
public class Event extends DomainEntity {
	
	//Attributes
	
	private String	title;
	private Date	eventMoment;
	private String	description;
	private String	picture;
	private Integer	seatsNumber;
	private Double	amount;
	
	
	//Constructor
	
	public Event() {
		super();
	}
	
	//Getter & setter
	
	@NotBlank
	@SafeHtml
	@Size(min = 10, max = 50)
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEventMoment() {
		return this.eventMoment;
	}
	
	public void setEventMoment(final Date eventMoment) {
		this.eventMoment = eventMoment;
	}
	
	@NotBlank
	@SafeHtml
	@Size(min = 20, max = 200)
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	@NotBlank
	@SafeHtml
	@URL
	public String getPicture() {
		return this.picture;
	}
	
	public void setPicture(final String picture) {
		this.picture = picture;
	}
	
	@Min(1)
	@NotNull
	public Integer getSeatsNumber() {
		return this.seatsNumber;
	}
	
	public void setSeatsNumber(final Integer seatsNumber) {
		this.seatsNumber = seatsNumber;
	}
	
	@NotNull
	public Double getAmount() {
		return this.amount;
	}
	
	public void setAmount(final Double amount) {
		this.amount = amount;
	}
	
	
	//RelationShips
	
	private Manager					manager;
	private Collection<EventChorbi>	eventChorbies;
	
	
	@Valid
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}
	
	public void setManager(final Manager manager) {
		this.manager = manager;
	}
	
	@Valid
	@OneToMany(mappedBy = "event")
	public Collection<EventChorbi> getEventChorbies() {
		return this.eventChorbies;
	}
	
	public void setEventChorbies(final Collection<EventChorbi> eventChorbies) {
		this.eventChorbies = eventChorbies;
	}
	
}
