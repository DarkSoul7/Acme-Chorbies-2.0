
package domain;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Chirper {

	//Attributes

	private String		companyName;
	private String		vatNumber;
	private CreditCard	creditCard;
	private Boolean		validCreditCard;


	//Constructor
	public Manager() {
		super();
	}

	//Getter & setter

	@NotBlank
	@SafeHtml
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@NotBlank
	@SafeHtml
	public String getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final String vatNumber) {
		this.vatNumber = vatNumber;
	}

	@NotNull
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Transient
	@AssertTrue
	public Boolean getValidCreditCard() {
		return this.validCreditCard;
	}

	public void setValidCreditCard(final Boolean validCreditCard) {
		this.validCreditCard = validCreditCard;
	}


	//RelationShips

	private Collection<Event>	events;


	@Valid
	@OneToMany(mappedBy = "manager")
	public Collection<Event> getEvents() {
		return this.events;
	}

	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}
}
