
package forms;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import domain.Event;
import domain.EventChorbi;

public class EventForm {
	
	//Attributes
	
	private int						id;
	private String					title;
	private Date					eventMoment;
	private String					description;
	private String					picture;
	private Integer					seatsNumber;
	private Boolean					highlighted;
	private Collection<EventChorbi>	eventChorbies;
	private int						seatsAvailable;
	private Double					amount;
	private Boolean					joined;
	
	
	//Constructor
	public EventForm() {
		super();
	}
	
	public EventForm(final Event event, final Boolean highlighted, final int seatsAvailable, final Boolean joined) {
		super();
		
		this.setId(event.getId());
		this.description = event.getDescription();
		this.setEventMoment(event.getEventMoment());
		this.picture = event.getPicture();
		this.seatsNumber = event.getSeatsNumber();
		this.title = event.getTitle();
		this.highlighted = highlighted;
		this.eventChorbies = event.getEventChorbies();
		this.seatsAvailable = seatsAvailable;
		this.amount = event.getAmount();
		this.joined = joined;
	}
	
	public EventForm(final Event event, final int seatsAvailable, final Boolean joined) {
		super();
		
		this.setId(event.getId());
		this.description = event.getDescription();
		this.setEventMoment(event.getEventMoment());
		this.picture = event.getPicture();
		this.seatsNumber = event.getSeatsNumber();
		this.title = event.getTitle();
		this.highlighted = null;
		this.seatsAvailable = seatsAvailable;
		this.joined = joined;
	}
	
	//Getter & setter
	
	public int getId() {
		return this.id;
	}
	
	public void setId(final int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public int getSeatsAvailable() {
		return this.seatsAvailable;
	}
	
	public void setSeatsAvailable(final int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEventMoment() {
		return this.eventMoment;
	}
	
	public void setEventMoment(final Date eventMoment) {
		this.eventMoment = eventMoment;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public String getPicture() {
		return this.picture;
	}
	
	public void setPicture(final String picture) {
		this.picture = picture;
	}
	
	public Integer getSeatsNumber() {
		return this.seatsNumber;
	}
	
	public void setSeatsNumber(final Integer seatsNumber) {
		this.seatsNumber = seatsNumber;
	}
	
	public Boolean getHighlighted() {
		return this.highlighted;
	}
	
	public void setHighlighted(final Boolean highlighted) {
		this.highlighted = highlighted;
	}
	
	public Collection<EventChorbi> getEventChorbies() {
		return this.eventChorbies;
	}
	
	public void setEventChorbies(final Collection<EventChorbi> eventChorbies) {
		this.eventChorbies = eventChorbies;
	}
	
	public Double getAmount() {
		return this.amount;
	}
	
	public void setAmount(final Double amount) {
		this.amount = amount;
	}
	
	public Boolean getJoined() {
		return this.joined;
	}
	
	public void setJoined(Boolean joined) {
		this.joined = joined;
	}
	
}
