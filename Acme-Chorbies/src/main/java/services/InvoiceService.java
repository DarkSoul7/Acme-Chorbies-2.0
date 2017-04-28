
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.InvoiceRepository;
import domain.Chorbi;
import domain.Invoice;
import forms.PeriodForm;

@Service
@Transactional
public class InvoiceService {

	//Managed repository

	@Autowired
	private InvoiceRepository	invoiceRepository;

	//Supported services

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private EventChorbiService	eventChorbiService;

	@Autowired
	private EventService		eventService;


	//Simple CRUD methods

	public Invoice findOne(final int invoiceId) {
		return this.invoiceRepository.findOne(invoiceId);
	}

	public Collection<Invoice> findAll() {
		return this.invoiceRepository.findAll();
	}

	public Invoice create() {
		final Invoice result = new Invoice();

		return result;
	}

	public Invoice save(final Invoice invoice) {
		Assert.notNull(invoice);
		final Invoice result = this.invoiceRepository.save(invoice);

		return result;
	}

	public void saveAll(final Collection<Invoice> invoices) {
		Assert.notNull(invoices);
		this.invoiceRepository.save(invoices);
	}

	//Other business methods

	public Invoice findInvoiceByParams(final Date openPeriod, final Date endPeriod, final Chorbi chorbi) {
		return this.invoiceRepository.findInvoiceByParams(openPeriod, endPeriod, chorbi.getId());
	}

	public Invoice findLastInvoiceByChorbi(final Chorbi chorbi) {
		return this.invoiceRepository.findLastInvoiceByChorbi(chorbi.getId());
	}

	/**
	 * Genera todas las facturas pendientes de todos los chobies del sistema
	 */
	public void generateAllInvoices() {
		final Collection<Invoice> result = new ArrayList<>();
		final Collection<Chorbi> chorbies = this.chorbiService.findAll();
		Invoice invoice;
		int months;
		final LocalDate now = new LocalDate();
		LocalDate openPeriod;

		for (final Chorbi chorbi : chorbies) {
			invoice = this.findLastInvoiceByChorbi(chorbi);
			if (invoice != null) {
				openPeriod = new LocalDate(invoice.getEndPeriod());
			} else {
				openPeriod = new LocalDate(this.eventService.getBeginningEventChorbiSubscribedDate(chorbi));
			}

			months = Months.monthsBetween(openPeriod, now).getMonths();
			result.addAll(this.generateChorbiInvoices(chorbi, months));
		}

		this.saveAll(result);
	}

	/***
	 * Genera las facturas que no habian sido generadas del chorbi en cuestión
	 * 
	 * @param chorbi
	 * @param months
	 * @return invoices de chorbi que no habian sido generadas hasta la fecha de hoy
	 */
	public Collection<Invoice> generateChorbiInvoices(final Chorbi chorbi, int months) {
		final Collection<Invoice> result = new ArrayList<>();
		while (months != 1) {
			DateTime openPeriod = new DateTime();
			DateTime endPeriod = new DateTime();

			if (openPeriod.getMonthOfYear() - (months) < 0) {
				openPeriod = new DateTime().withDate(openPeriod.getYear() - 1, 12 - months, 1);
				endPeriod = new DateTime().withDate(endPeriod.getYear() - 1, 12 - (months - 1), 1);

			} else if (openPeriod.getMonthOfYear() - months == 0) {
				openPeriod = new DateTime().withDate(openPeriod.getYear() - 1, 12, 1);
				endPeriod = new DateTime().withDate(endPeriod.getYear(), 1, 1);

			} else {
				openPeriod = new DateTime().withDate(openPeriod.getYear(), openPeriod.getMonthOfYear() - months, 1);
				endPeriod = new DateTime().withDate(endPeriod.getYear(), endPeriod.getMonthOfYear() - (months - 1), 1);
			}

			final Invoice invoice = this.create();
			invoice.setChorbi(chorbi);
			invoice.setOpenPeriod(openPeriod.toDate());
			invoice.setEndPeriod(endPeriod.toDate());
			final double amount = this.eventChorbiService.getMonhtlyFeeAmountByChorbiAndDate(openPeriod.toDate(), endPeriod.toDate(), chorbi);
			invoice.setAmount(amount);
			invoice.setPaid(false);
			if (amount != 0.0 && invoice.getEndPeriod().before(new Date())) {
				result.add(invoice);
			}

			months--;
		}
		return result;
	}

	public Collection<Invoice> getInvoiceByParamsAdmin(final Date openPeriod, final Date endPeriod) {
		return this.invoiceRepository.getInvoiceByParamsAdmin(openPeriod, endPeriod);
	}

	public Collection<Invoice> getInvoiceByParamsChorbi(final Date openPeriod, final Date endPeriod, final Chorbi chorbi) {
		return this.invoiceRepository.getInvoiceByParamsChorbi(openPeriod, endPeriod, chorbi.getId());
	}

	public void payInvoice(final Invoice invoice) {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		Assert.notNull(invoice);
		Assert.isTrue(chorbi.equals(invoice.getChorbi()));

		invoice.setPaid(true);
		invoice.setMomentPaid(new Date());
		this.save(invoice);
	}

	/* I PeriodForm */

	public PeriodForm createForm() {
		PeriodForm result;

		result = new PeriodForm();

		return result;
	}

	public PeriodForm createForm(final Date openPeriod, final Date endPeriod) {
		PeriodForm result;

		result = this.createForm();
		result.setOpenPeriod(openPeriod);
		result.setEndPeriod(endPeriod);

		return result;
	}

	/* F PeriodForm */
}
