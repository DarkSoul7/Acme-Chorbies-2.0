
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.EventChorbi;

@Repository
public interface EventChorbiRepository extends JpaRepository<EventChorbi, Integer> {

	@Query("select ec from EventChorbi ec where ec.chorbi.id = ?1 and ec.event.id = ?2")
	public EventChorbi findEventChorbiByParameters(int chorbiId, int eventId);

	@Query("select ec.chorbi from EventChorbi ec where ec.event.id = ?1")
	public Collection<Chorbi> findChorbiesInEvent(int eventId);
}
