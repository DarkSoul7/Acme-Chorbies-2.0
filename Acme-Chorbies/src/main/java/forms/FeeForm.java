
package forms;


public class FeeForm {

	//Attributes
	private int		id;
	private double	amountManager;
	private double	amountChorbi;


	//Constructor
	public FeeForm() {
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

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
