
package forms;

import javax.validation.constraints.Min;

public class FeeForm {

	//Attributes
	private int		id;
	private double	amount;


	//Constructor
	public FeeForm() {
		super();
	}

	@Min(1)
	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
