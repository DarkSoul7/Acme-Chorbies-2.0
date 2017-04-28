
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
	private Date					moment;
	private String					description;
	private String					picture;
	private Integer					seatsNumber;
	private Boolean					highlighted;
	private Collection<EventChorbi>	eventChorbies;
	
	
	//Constructor
	public EventForm() {
		super();
	}
	
	public EventForm(final Event event, final Boolean highlighted) {
		super();
		
		this.setId(event.getId());
		this.description = event.getDescription();
		this.moment = event.getMoment();
		this.picture = event.getPicture();
		this.seatsNumber = event.getSeatsNumber();
		this.title = event.getTitle();
		this.highlighted = highlighted;
		this.eventChorbies = event.getEventChorbies();
	}
	
	public EventForm(final Event event) {
		super();
		
		this.setId(event.getId());
		this.description = event.getDescription();
		this.moment = event.getMoment();
		this.picture = event.getPicture();
		this.seatsNumber = event.getSeatsNumber();
		this.title = event.getTitle();
		this.highlighted = null;
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
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMoment() {
		return this.moment;
	}
	
	public void setMoment(final Date moment) {
		this.moment = moment;
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
	
	public void setEventChorbies(Collection<EventChorbi> eventChorbies) {
		this.eventChorbies = eventChorbies;
	}
	
}
