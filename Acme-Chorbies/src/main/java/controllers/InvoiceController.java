
package controllers;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import services.InvoiceService;
import domain.Chorbi;
import domain.Invoice;
import forms.PeriodForm;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends AbstractController {
	
	//Related services
	
	@Autowired
	private InvoiceService	invoiceService;
	
	@Autowired
	private ChorbiService	chorbiService;
	
	
	//Constructor
	
	public InvoiceController() {
		super();
	}
	
	//List invoices
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) String errorMessage, @RequestParam(required = false) String openPeriod,
		@RequestParam(required = false) String endPeriod) {
		PeriodForm periodForm;
		ModelAndView result;
		Date openPeriodDate = null;
		Date endPeriodDate = null;
		
		if (openPeriod != null) {
			if (openPeriod.equals("-1")) {
				openPeriodDate = new Date(0L);
			} else {
				openPeriodDate = new Date(Long.valueOf(openPeriod));
			}
		}
		
		if (endPeriod != null) {
			if (endPeriod.equals("-1")) {
				endPeriodDate = new Date();
			} else {
				endPeriodDate = new Date(Long.valueOf(endPeriod));
			}
		}
		
		periodForm = this.invoiceService.createForm(openPeriodDate, endPeriodDate);
		result = this.listInvoiceChorbi(periodForm);
		
		return result;
	}
	@RequestMapping(value = "/listSave", method = RequestMethod.POST, params = "save")
	public ModelAndView listFind(@Valid PeriodForm periodForm, BindingResult binding) {
		ModelAndView result;
		String openPeriod = "-1";
		String endPeriod = "-1";
		
		if (binding.hasErrors())
			result = this.listInvoiceChorbi(periodForm, "invoice.list.error");
		else {
			try {
				if (periodForm.getOpenPeriod() != null) {
					openPeriod = String.valueOf(periodForm.getOpenPeriod().getTime());
				}
				
				if (periodForm.getEndPeriod() != null) {
					endPeriod = String.valueOf(periodForm.getEndPeriod().getTime());
				}
				
				result = new ModelAndView(String.format("redirect:/invoice/list.do?openPeriod=%s&endPeriod=%s", openPeriod, endPeriod));
				
			} catch (Throwable oops) {
				result = this.listInvoiceChorbi(periodForm, "invoice.list.error");
			}
			return result;
		}
		
		return result;
	}
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll(@RequestParam(required = false) String errorMessage, @RequestParam(required = false) String openPeriod,
		@RequestParam(required = false) String endPeriod) {
		PeriodForm periodForm;
		ModelAndView result;
		Date openPeriodDate = null;
		Date endPeriodDate = null;
		
		if (openPeriod != null) {
			if (openPeriod.equals("-1")) {
				openPeriodDate = new Date(0L);
			} else {
				openPeriodDate = new Date(Long.valueOf(openPeriod));
			}
		}
		
		if (endPeriod != null) {
			if (endPeriod.equals("-1")) {
				endPeriodDate = new Date();
			} else {
				endPeriodDate = new Date(Long.valueOf(endPeriod));
			}
		}
		
		periodForm = this.invoiceService.createForm(openPeriodDate, endPeriodDate);
		result = this.listInvoiceAdmin(periodForm);
		
		return result;
	}
	@RequestMapping(value = "/listAllSave", method = RequestMethod.POST, params = "save")
	public ModelAndView listAllFind(@Valid PeriodForm periodForm, BindingResult binding) {
		ModelAndView result;
		String openPeriod = "-1";
		String endPeriod = "-1";
		
		if (binding.hasErrors())
			result = this.listInvoiceAdmin(periodForm, "invoice.list.error");
		else
			try {
				if (periodForm.getOpenPeriod() != null) {
					openPeriod = String.valueOf(periodForm.getOpenPeriod().getTime());
				}
				
				if (periodForm.getEndPeriod() != null) {
					endPeriod = String.valueOf(periodForm.getEndPeriod().getTime());
				}
				
				result = new ModelAndView(String.format("redirect:/invoice/listAll.do?openPeriod=%s&endPeriod=%s", openPeriod, endPeriod));
				
			} catch (Throwable oops) {
				result = this.listInvoiceAdmin(periodForm, "invoice.list.error");
			}
		return result;
	}
	//Ancillary methods
	
	protected ModelAndView listInvoiceChorbi(PeriodForm periodForm) {
		return this.listInvoiceChorbi(periodForm, null);
	}
	
	protected ModelAndView listInvoiceChorbi(PeriodForm periodForm, String message) {
		ModelAndView result;
		Chorbi chorbi;
		Collection<Invoice> invoices = null;
		
		try {
			chorbi = this.chorbiService.findByPrincipal();
			
			// El único caso en el que alguna de las fechas pueda venir a null es cuando hay un error. En ese caso no se muestra ninguna factura
			if (StringUtils.isBlank(message)) {
				if (periodForm.getEndPeriod() == null && periodForm.getOpenPeriod() == null) {
					invoices = chorbi.getInvoices();
				} else {
					Assert.isTrue(periodForm.getEndPeriod().after(periodForm.getOpenPeriod()));
					invoices = this.invoiceService.getInvoiceByParamsChorbi(periodForm.getOpenPeriod(), periodForm.getEndPeriod(), chorbi);
				}
			}
			
			result = new ModelAndView("invoice/list");
			result.addObject("periodForm", periodForm);
			result.addObject("invoices", invoices);
			result.addObject("errorMessage", message);
			result.addObject("RequestURI", "invoice/listSave.do");
		} catch (Throwable oops) {
			result = new ModelAndView("invoice/list");
			result.addObject("periodForm", periodForm);
			result.addObject("errorMessage", "invoice.list.error");
			result.addObject("RequestURI", "invoice/listSave.do");
		}
		
		return result;
	}
	
	protected ModelAndView listInvoiceAdmin(PeriodForm periodForm) {
		return this.listInvoiceAdmin(periodForm, null);
	}
	
	protected ModelAndView listInvoiceAdmin(PeriodForm periodForm, String message) {
		ModelAndView result;
		Collection<Invoice> invoices = null;
		
		try {
			// El único caso en el que alguna de las fechas pueda venir a null es cuando hay un error. En ese caso no se muestra ninguna factura
			if (StringUtils.isBlank(message)) {
				if (periodForm.getEndPeriod() == null && periodForm.getOpenPeriod() == null) {
					invoices = this.invoiceService.findAll();
				} else {
					Assert.isTrue(periodForm.getEndPeriod().after(periodForm.getOpenPeriod()));
					invoices = this.invoiceService.getInvoiceByParamsAdmin(periodForm.getOpenPeriod(), periodForm.getEndPeriod());
				}
			}
			
			result = new ModelAndView("invoice/listAll");
			result.addObject("periodForm", periodForm);
			result.addObject("invoices", invoices);
			result.addObject("errorMessage", message);
			result.addObject("RequestURI", "invoice/listAllSave.do");
		} catch (Throwable oops) {
			result = new ModelAndView("invoice/listAll");
			result.addObject("periodForm", periodForm);
			result.addObject("errorMessage", "invoice.list.error");
			result.addObject("RequestURI", "invoice/listAllSave.do");
		}
		
		return result;
	}
}
