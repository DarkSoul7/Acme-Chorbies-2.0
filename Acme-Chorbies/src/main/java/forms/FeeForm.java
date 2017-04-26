
package forms;

import javax.validation.constraints.Min;

public class FeeForm {

	//Attributes
	private int		id;
	private double	amountManager;
	private double	amountChorbi;


	//Constructor
	public FeeForm() {
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

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
