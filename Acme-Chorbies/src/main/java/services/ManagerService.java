
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Manager;
import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ManagerService {

	//Managed repository
	@Autowired
	private ManagerRepository managerRepository;

	//Supported services


	//Constructor
	public ManagerService() {
		super();
	}

	//Simple CRUD methods

	public Manager findOne(final int managerId) {
		return this.managerRepository.findOne(managerId);
	}

	public Collection<Manager> findAll() {
		return this.managerRepository.findAll();
	}

	public Manager create() {
		return null;
	}

	// Other business methods

	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount);
		Assert.notNull(result, "Any chorbi with userAccountId=" + userAccount.getId() + "has be found");

		return result;
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		Manager result;
		int userAccountId;

		userAccountId = userAccount.getId();
		result = this.managerRepository.findByUserAccountId(userAccountId);

		return result;
	}

	public Manager findByUserName(final String username) {
		Assert.notNull(username);
		Manager result;

		result = this.managerRepository.findByUserName(username);

		return result;
	}

	//DASHBOARD

	public Collection<Manager> listOfManagerOrderByEvents() {
		return this.managerRepository.listOfManagerOrderByEvents();
	}

	public Collection<Object[]> listOfManagerAndFee() {
		return this.managerRepository.listOfManagerAndFee();
	}
}
