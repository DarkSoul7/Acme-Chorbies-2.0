
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	public Manager findByUserAccountId(int id);

	@Query("select m from Manager m where m.userAccount.username = ?1")
	public Manager findByUserName(String username);

	//C1
	@Query("select m from Manager m order by m.events.size")
	public Collection<Manager> listOfManagerOrderByEvents();

	//C2
	@Query("select e.manager,sum(e.amount) from Event e group by e.manager")
	public Collection<Object[]> listOfManagerAndFee();

}
