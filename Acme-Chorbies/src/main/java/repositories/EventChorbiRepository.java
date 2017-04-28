
package repositories;

import java.util.Collection;
import java.util.Date;

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

	@Query("select sum(ec.event.amount) from EventChorbi ec where ec.momentSubscribed between ?1 and ?2 and ec.chorbi.id = ?3 and ec.event.eventMoment > current_date or ec.momentSubscribed < ?1 and ec.chorbi.id = ?3 and ec.event.eventMoment > current_date")
	public double getMonhtlyFeeAmountByChorbiAndDate(Date openPeriod, Date endPeriod, int chorbiId);
}
