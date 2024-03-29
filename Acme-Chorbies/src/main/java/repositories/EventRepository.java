
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
	
	@Query("select new domain.Event(ec.event, ec.event.seatsNumber - ec.event.eventChorbies.size) from EventChorbi ec where ec.chorbi.id = ?1")
	public Collection<Event> getEventsRegister(int chorbiId);
	
	@Query("select new domain.Event(e, e.seatsNumber - e.eventChorbies.size) from Event e where e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<Event> getEarlyEvents(Date currentDate, Date currentDatePlusMonth);
	
	// Req C2 --------
	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.eventMoment < current_date) then false end, e.seatsNumber - e.eventChorbies.size, case when 1=1 then true end) from Event e where e.eventMoment < current_date")
	public Collection<EventForm> getFinishedEvents();
	
	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.eventMoment < current_date) then false end, e.seatsNumber - e.eventChorbies.size, case when exists(select 1 from EventChorbi ec where ec.event.id = e.id and ec.chorbi.id = ?1) then true else false end) from Event e where e.eventMoment < current_date")
	public Collection<EventForm> getFinishedEventsByChorbi(int chorbiId);
	
	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.eventMoment between ?1 and ?2 and (e2.seatsNumber - e2.eventChorbies.size) > 0) then true end, e.seatsNumber - e.eventChorbies.size, case when 1=1 then true end) from Event e where e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<EventForm> getFutureHighlighted(Date currentDate, Date currentDatePlusMonth);
	
	@Query("select new forms.EventForm(e, case when exists(select 1 from Event e2 where e2.eventMoment between ?1 and ?2 and (e2.seatsNumber - e2.eventChorbies.size) > 0) then true end, e.seatsNumber - e.eventChorbies.size, case when exists(select 1 from EventChorbi ec where ec.event.id = e.id and ec.chorbi.id = ?3) then true else false end) from Event e where e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<EventForm> getFutureHighlightedByChorbi(Date currentDate, Date currentDatePlusMonth, int chorbiId);
	
	@Query("select new forms.EventForm(e, e.seatsNumber - e.eventChorbies.size, case when 1=1 then true end) from Event e where e.eventMoment > ?2 or e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) = 0")
	public Collection<EventForm> nonHighlighted(Date currentDate, Date currentDatePlusMonth);
	
	@Query("select new forms.EventForm(e, e.seatsNumber - e.eventChorbies.size, case when exists(select 1 from EventChorbi ec where ec.event.id = e.id and ec.chorbi.id = ?3) then true else false end) from Event e where e.eventMoment > ?2 or e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) = 0")
	public Collection<EventForm> nonHighlightedByChorbi(Date currentDate, Date currentDatePlusMonth, int chorbiId);
	//----------------
	
	@Query("select e from Event e where e.eventMoment > ?2 or e.eventMoment between ?1 and ?2 and (e.seatsNumber - e.eventChorbies.size) > 0")
	public Collection<Event> getFutureEvents(Date currentDate, Date currentDatePlusMonth);
	
	@Query("select count(ec) from Chorbi c join c.eventChorbies ec where ec.event.id=?1 and c.id=?2")
	public Integer getExistChorbiInEvent(int idEvent, int idChorbi);
	
	@Query("select e from Event e join e.eventChorbies ec where e.eventMoment between ?2 and ?3 and ec.chorbi.id = ?1")
	public Collection<Event> getEventsNotFinishedByChorbi(int chorbiId, Date openPeriod, Date endPerior);
	
	@Query("select sum(ec.amount) from EventChorbi ec where ec.momentSubscribed between ?2 and ?3 and ec.chorbi.id = ?1")
	public Double getChorbisFeeAmountFromEventsBetweenDates(int chorbiId, Date openPeriod, Date endPerior);
	
	@Query("select distinct e.momentSubscribed from EventChorbi e where e.momentSubscribed <= (select min(e2.momentSubscribed) from EventChorbi e2) and chorbi.id = ?1")
	public Date getBeginningEventChorbiSubscribedDate(int chorbiId);
}
