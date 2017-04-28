
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
public class Fee extends DomainEntity {

	//Attributes

	private double	amountManager;
	private double	amountChorbi;


	//Constructor

	public Fee() {
		super();
	}

	@Min(1)
	public double getAmountManager() {
		return this.amountManager;
	}

	public void setAmountManager(final double amountManager) {
		this.amountManager = amountManager;
	}

	@Min(1)
	public double getAmountChorbi() {
		return this.amountChorbi;
	}

	public void setAmountChorbi(final double amountChorbi) {
		this.amountChorbi = amountChorbi;
	}

}
