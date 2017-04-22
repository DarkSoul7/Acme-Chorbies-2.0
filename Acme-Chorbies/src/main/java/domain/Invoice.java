
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Invoice extends DomainEntity {

	//Attributes

	private Date	openPeriod;
	private Date	endPeriod;
	private double	amount;
	private boolean	paid;


	//Constructor

	public Invoice() {
		super();
	}

	//Getter & setter

	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getOpenPeriod() {
		return this.openPeriod;
	}

	public void setOpenPeriod(final Date openPeriod) {
		this.openPeriod = openPeriod;
	}

	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getEndPeriod() {
		return this.endPeriod;
	}

	public void setEndPeriod(final Date endPeriod) {
		this.endPeriod = endPeriod;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public boolean isPaid() {
		return this.paid;
	}

	public void setPaid(final boolean paid) {
		this.paid = paid;
	}


	//Relationships

	private Chorbi	chorbi;


	@Valid
	@ManyToOne(optional = false)
	public Chorbi getChorbi() {
		return this.chorbi;
	}

	public void setChorbi(final Chorbi chorbi) {
		this.chorbi = chorbi;
	}

}
