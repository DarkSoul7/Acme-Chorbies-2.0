
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChirperRepository;
import security.LoginService;
import security.UserAccount;
import domain.Chirper;

@Service
@Transactional
public class ChirperService {

	//Managed repository

	@Autowired
	private ChirperRepository	chirperRepository;


	//Supported services;

	//Constructor

	public ChirperService() {
		super();
	}

	//Simple CRUD methods

	public Chirper create() {
		return new Chirper();
	}

	public Collection<Chirper> findAll() {
		return this.chirperRepository.findAll();
	}

	public Chirper findOne(final int chirperId) {
		return this.chirperRepository.findOne(chirperId);

	}

	public void save(final Chirper chirper) {
		this.chirperRepository.save(chirper);
	}

	public void delete(final Chirper chirper) {
		this.chirperRepository.delete(chirper);
	}

	//Other business methods

	public Chirper findByPrincipal() {
		Chirper result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount);
		Assert.notNull(result, "Any chirper with userAccountId=" + userAccount.getId() + "has be found");

		return result;
	}

	public Chirper findByUserAccount(final UserAccount userAccount) {
		Chirper result;
		int userAccountId;

		userAccountId = userAccount.getId();
		result = this.chirperRepository.findByUserAccountId(userAccountId);

		return result;
	}

	public Chirper findByUserName(final String username) {
		Assert.notNull(username);
		Chirper result;

		result = this.chirperRepository.findByUserName(username);

		return result;
	}

}
