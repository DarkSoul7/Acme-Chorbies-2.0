
package forms;

import java.util.Collection;
import java.util.Date;

import domain.Invoice;

public class PeriodForm {

	//Attributes

	private Date				openPeriod;
	private Date				endPeriod;
	private Collection<Invoice>	invoices;


	//Constructor

	public PeriodForm() {
		super();
	}

	//Getter & setter

	public Date getOpenPeriod() {
		return this.openPeriod;
	}

	public void setOpenPeriod(final Date openPeriod) {
		this.openPeriod = openPeriod;
	}

	public Date getEndPeriod() {
		return this.endPeriod;
	}

	public void setEndPeriod(final Date endPeriod) {
		this.endPeriod = endPeriod;
	}

	public Collection<Invoice> getInvoices() {
		return this.invoices;
	}

	public void setInvoices(final Collection<Invoice> invoices) {
		this.invoices = invoices;
	}

}
