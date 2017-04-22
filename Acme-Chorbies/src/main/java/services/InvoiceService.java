
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.InvoiceRepository;
import domain.Chorbi;
import domain.Invoice;

@Service
@Transactional
public class InvoiceService {

	//Managed repository

	@Autowired
	private InvoiceRepository	invoiceRepository;

	//Supported services

	@Autowired
	private ChorbiService		chorbiService;


	//Simple CRUD methods

	//Other business methods

	public Invoice findInvoiceByParams(final Date openPeriod, final Date endPeriod, final Chorbi chorbi) {
		return this.invoiceRepository.findInvoiceByParams(openPeriod, endPeriod, chorbi.getId());
	}

	public Invoice findLastInvoiceByChorbi(final Chorbi chorbi) {
		return this.invoiceRepository.findLastInvoiceByChorbi(chorbi.getId());
	}

	//	public Collection<Invoice> generateInvoices(){
	//		Collection<Invoice> result;
	//		Collection<Chorbi> chorbies = chorbiService.findAll();
	//		Calendar now = Calendar.getInstance();
	//		for(Chorbi chorbi: chorbies){
	//			
	//		}
	//	}
}
