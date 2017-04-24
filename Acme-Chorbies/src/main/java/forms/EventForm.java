
package forms;

import java.util.Date;

import domain.Event;

public class EventForm {

	//Attributes

	private int		id;
	private String	title;
	private Date	moment;
	private String	description;
	private String	picture;
	private int		seatsNumber;
	private Boolean	highlighted;


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

	public int getSeatsNumber() {
		return this.seatsNumber;
	}

	public void setSeatsNumber(final int seatsNumber) {
		this.seatsNumber = seatsNumber;
	}

	public Boolean getHighlighted() {
		return this.highlighted;
	}

	public void setHighlighted(final Boolean highlighted) {
		this.highlighted = highlighted;
	}

}
