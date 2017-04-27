
package controllers;

import java.util.Collection;

import javax.validation.Valid;

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
	public ModelAndView list(@RequestParam(required = false) final String errorMessage, @RequestParam(required = false) final Collection<Invoice> invoices) {
		final PeriodForm periodForm = new PeriodForm();
		ModelAndView result;

		result = this.listInvoiceChorbi(periodForm);
		result.addObject("invoices", invoices);

		return result;
	}

	@RequestMapping(value = "/listSave", method = RequestMethod.POST, params = "save")
	public ModelAndView listFind(@Valid final PeriodForm periodForm, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.listInvoiceErrorChorbi(periodForm, "invoice.list.error");
		else {
			try {
				final Chorbi chorbi = this.chorbiService.findByPrincipal();
				Assert.isTrue(periodForm.getEndPeriod().after(periodForm.getOpenPeriod()));
				Collection<Invoice> invoices;

				if (periodForm.getEndPeriod() == null || periodForm.getEndPeriod() == null)
					invoices = chorbi.getInvoices();
				else
					invoices = this.invoiceService.getInvoiceByParamsChorbi(periodForm.getOpenPeriod(), periodForm.getEndPeriod(), chorbi);

				invoices = chorbi.getInvoices();
				result = new ModelAndView("redirect:/invoice/list.do");
				result.addObject("invoices", invoices);

			} catch (final Throwable oops) {
				result = this.listInvoiceErrorChorbi(periodForm, "invoice.list.error");
			}
			return result;
		}

		return result;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll(@RequestParam(required = false) final String errorMessage, @RequestParam(required = false) PeriodForm periodForm) {

		if (periodForm == null)
			periodForm = new PeriodForm();
		ModelAndView result;

		result = this.listInvoiceAdmin(periodForm);
		result.addObject("invoices", periodForm.getInvoices());

		return result;
	}

	@RequestMapping(value = "/listAllSave", method = RequestMethod.POST, params = "save")
	public ModelAndView listAllFind(@Valid final PeriodForm periodForm, final BindingResult binding) {

		ModelAndView result;
		if (binding.hasErrors())
			result = this.listInvoiceErrorAdmin(periodForm, "invoice.list.error");
		else
			try {
				Assert.isTrue(periodForm.getEndPeriod().after(periodForm.getOpenPeriod()));
				Collection<Invoice> invoices;
				if (periodForm.getEndPeriod() == null || periodForm.getOpenPeriod() == null)
					invoices = this.invoiceService.findAll();
				else
					invoices = this.invoiceService.getInvoiceByParamsAdmin(periodForm.getOpenPeriod(), periodForm.getEndPeriod());

				result = new ModelAndView("redirect:/invoice/listAll.do");
				periodForm.setInvoices(invoices);
				result.addObject("periodForm", periodForm);

			} catch (final Throwable oops) {
				result = this.listInvoiceErrorAdmin(periodForm, "invoice.list.error");
			}
		return result;
	}
	//Ancillary methods

	protected ModelAndView listInvoiceChorbi(final PeriodForm periodForm) {
		return this.listInvoiceErrorChorbi(periodForm, null);
	}

	protected ModelAndView listInvoiceErrorChorbi(final PeriodForm periodForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("invoice/list");
		result.addObject("periodForm", periodForm);
		result.addObject("errorMessage", message);
		result.addObject("RequestURI", "invoice/listSave.do");

		return result;
	}

	protected ModelAndView listInvoiceAdmin(final PeriodForm periodForm) {
		return this.listInvoiceErrorAdmin(periodForm, null);
	}

	protected ModelAndView listInvoiceErrorAdmin(final PeriodForm periodForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("invoice/listAll");
		result.addObject("periodForm", periodForm);
		result.addObject("errorMessage", message);
		result.addObject("RequestURI", "invoice/listAllSave.do");

		return result;
	}
}
