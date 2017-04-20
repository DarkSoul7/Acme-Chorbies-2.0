
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CachedTime;

@Repository
public interface CachedTimeRepository extends JpaRepository<CachedTime, Integer> {

	@Query("select c from CachedTime c")
	public CachedTime findUnique();
}
