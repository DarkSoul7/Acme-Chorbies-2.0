
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

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
	
	
	//Simple CRUD methods
	
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
		
		for (final Chorbi chorbi : chorbies) {
			invoice = this.findLastInvoiceByChorbi(chorbi);
			final LocalDate openPeriod = new LocalDate(invoice.getOpenPeriod());
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
	@SuppressWarnings("static-access")
	public Collection<Invoice> generateChorbiInvoices(final Chorbi chorbi, int months) {
		final Collection<Invoice> result = new ArrayList<>();
		while (months != 1) {
			final Calendar openPeriod = Calendar.getInstance();
			final Calendar endPeriod = Calendar.getInstance();
			
			if (openPeriod.MONTH - (months) <= 0)
				openPeriod.set(openPeriod.YEAR - 1, 12 - months, 1);
			else
				openPeriod.set(openPeriod.YEAR, openPeriod.MONTH - months, 1);
			if (openPeriod.MONTH - (months - 1) <= 0)
				endPeriod.set(endPeriod.YEAR - 1, 12 - (months - 1), 1);
			
			final Invoice invoice = this.create();
			invoice.setChorbi(chorbi);
			invoice.setOpenPeriod(openPeriod.getTime());
			invoice.setEndPeriod(endPeriod.getTime());
			final double amount = this.eventChorbiService.getMonhtlyFeeAmountByChorbiAndDate(openPeriod.getTime(), endPeriod.getTime(), chorbi);
			invoice.setAmount(amount);
			invoice.setPaid(false);
			
			result.add(invoice);
			
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
	
	/* I PeriodForm */
	
	public PeriodForm createForm() {
		PeriodForm result;
		
		result = new PeriodForm();
		
		return result;
	}
	
	public PeriodForm createForm(Date openPeriod, Date endPeriod) {
		PeriodForm result;
		
		result = this.createForm();
		result.setOpenPeriod(openPeriod);
		result.setEndPeriod(endPeriod);
		
		return result;
	}
	
	/* F PeriodForm */
}
