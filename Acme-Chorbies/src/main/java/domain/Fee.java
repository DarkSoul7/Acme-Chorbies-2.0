
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

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

	public double getAmountManager() {
		return this.amountManager;
	}

	public void setAmountManager(final double amountManager) {
		this.amountManager = amountManager;
	}

	public double getAmountChorbi() {
		return this.amountChorbi;
	}

	public void setAmountChorbi(final double amountChorbi) {
		this.amountChorbi = amountChorbi;
	}

	//Getter & setter

}
