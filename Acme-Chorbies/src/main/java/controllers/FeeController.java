/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FeeService;
import domain.Fee;
import forms.FeeForm;

@Controller
@RequestMapping("/fee")
public class FeeController extends AbstractController {

	//Related services

	@Autowired
	private FeeService	feeService;


	// Constructors -----------------------------------------------------------

	public FeeController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Collection<Fee> fee = this.feeService.findAll();
		final FeeForm feeForm = new FeeForm();
		feeForm.setAmountManager(fee.iterator().next().getAmountManager());
		feeForm.setAmountChorbi(fee.iterator().next().getAmountChorbi());
		feeForm.setId(fee.iterator().next().getId());

		result = this.createEditModelAndView(feeForm);

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final FeeForm feeForm, final BindingResult binding) {
		ModelAndView result;
		Fee fee = null;

		try {
			fee = this.feeService.reconstruct(feeForm, binding);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(feeForm, "fee.commit.error");
		}
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(feeForm);
		} else {
			try {
				this.feeService.save(fee);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(feeForm, "fee.commit.error");
			}
		}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final FeeForm feeForm) {
		final ModelAndView result = this.createEditModelAndView(feeForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final FeeForm feeForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("fee/edit");
		result.addObject("feeForm", feeForm);
		result.addObject("message", message);

		result.addObject("RequestURI", "fee/save.do");

		return result;
	}
}
