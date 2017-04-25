
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirper;

@Repository
public interface ChirperRepository extends JpaRepository<Chirper, Integer> {

	@Query("select c from Chirper c where c.userAccount.id = ?1")
	Chirper findByUserAccountId(int id);

	@Query("select c from Chirper c where c.userAccount.username = ?1")
	Chirper findByUserName(String username);
}
