
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;
import forms.EventForm;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select ec.event from EventChorbi ec where ec.chorbi.id = ?1")
	public Collection<Event> getEventsRegister(int chorbiId);

	@Query("select e from Event e where e.moment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<Event> getEarlyEvents(Date currentDate, Date currentDatePlusMonth);

	// Req C2 --------
	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.moment < current_date) then false end) from Event e where e.moment < current_date")
	public Collection<EventForm> getFinishedEvents();

	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.moment between ?1 and ?2 and (e2.seatsNumber - e2.eventChorbies.size) > 0) then true end) from Event e where e.moment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<EventForm> getFutureHighlighted(Date currentDate, Date currentDatePlusMonth);

	@Query("select new forms.EventForm(e) from Event e where e.moment > ?2 or e.moment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) = 0")
	public Collection<EventForm> nonHighlighted(Date currentDate, Date currentDatePlusMonth);
	//----------------

	@Query("select e from Event e where e.moment > ?2 or e.moment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<Event> getFutureEvents(Date currentDate, Date currentDatePlusMonth);

	@Query("select count(ec) from Chorbi c join c.eventChorbies ec where ec.event.id=?1 and c.id=?2")
	public Integer getExistChorbiInEvent(int idEvent, int idChorbi);

	@Query("select e from Event e join e.eventChorbies ec where e.moment between ?2 and ?3 and ec.chorbi.id = ?1")
	public Collection<Event> getEventsNotFinishedByChorbi(int chorbiId, Date openPeriod, Date endPerior);
}
