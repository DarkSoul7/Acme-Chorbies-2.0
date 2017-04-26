
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import domain.Fee;
import forms.FeeForm;
import repositories.FeeRepository;

@Service
@Transactional
public class FeeService {

	@Autowired
	private FeeRepository feeRepository;


	public FeeService() {
		super();
	}

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
		this.feeRepository.save(fee);
	}

	public void delete(final Fee fee) {
		this.feeRepository.delete(fee);
	}

	//Other business methods
	public Fee reconstruct(final FeeForm feeForm, final BindingResult binding) {

		final Fee result = this.findOne(feeForm.getId());
		result.setAmountManager(feeForm.getAmountManager());
		result.setAmountChorbi(feeForm.getAmountChorbi());

		return result;
	}

	public Fee getFee() {
		return this.feeRepository.getFee();
	}

}
