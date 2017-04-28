
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FeeRepository;
import domain.Fee;
import forms.FeeForm;

@Service
@Transactional
public class FeeService {

	//Managed repository

	@Autowired
	private FeeRepository			feeRepository;

	//Supported services
	@Autowired
	private AdministratorService	administratorService;


	//Constructor
	public FeeService() {
		super();
	}

	//Simple CRUD methods
	@Deprecated
	public Fee create() {
		return new Fee();
	}

	public Collection<Fee> findAll() {
		return this.feeRepository.findAll();
	}

	public Fee findOne(final int feeId) {
		return this.feeRepository.findOne(feeId);

	}

	public void save(final Fee fee) {
		this.administratorService.findByPrincipal();

		this.feeRepository.save(fee);
	}

	@Deprecated
	public void delete(final Fee fee) {
		this.feeRepository.delete(fee);
	}


	//Other business methods

	@Autowired
	private Validator	validator;


	public Fee reconstruct(final FeeForm feeForm, final BindingResult binding) {

		final Fee result = this.findOne(feeForm.getId());
		result.setAmountManager(feeForm.getAmountManager());
		result.setAmountChorbi(feeForm.getAmountChorbi());

		this.validator.validate(result, binding);

		return result;
	}

	public Fee getFee() {
		return this.feeRepository.getFee();
	}

}
