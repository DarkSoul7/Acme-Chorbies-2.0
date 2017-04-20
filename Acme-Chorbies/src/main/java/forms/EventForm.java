
package forms;

import java.util.Date;

public class EventForm {

	//Attributes

	private int		id;
	private String	title;
	private Date	moment;
	private String	description;
	private String	picture;
	private int		seatsNumber;


	//Constructor
	public EventForm() {
		super();
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

}
