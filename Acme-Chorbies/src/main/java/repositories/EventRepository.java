
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where e.moment between ?1 and ?2 and (e.seatsNumber - e-chorbies.size) > 0")
	public Collection<Event> getEarlyEvents(Date currentDate, Date currentDatePlusMonth);

	@Query("select e from Event e where e.moment < current_date")
	public Collection<Event> getFinishedEvents();

	@Query("select e from Event e where e.moment > ?2 or e.moment between ?1 and ?2 and (e.seatsNumber - e-chorbies.size) = 0")
	public Collection<Event> getFutureEvents(Date currentDate, Date currentDatePlusMonth);

	@Query("select count(e) from Chorbi c join c.events e where e.id=?1 and c.id=?2")
	public Integer getExistChorbiInEvent(int idEvent, int idChorbi);
}
