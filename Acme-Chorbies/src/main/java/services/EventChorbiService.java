
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventChorbiRepository;
import domain.Chorbi;
import domain.Event;
import domain.EventChorbi;

@Service
@Transactional
public class EventChorbiService {

	//Managed repository

	@Autowired
	private EventChorbiRepository	eventChorbiRepository;


	//Supported services

	//Simple CRUD methods

	public EventChorbi create() {
		final EventChorbi result = new EventChorbi();

		return result;
	}

	public EventChorbi save(final EventChorbi eventChorbi) {
		EventChorbi result;
		Assert.notNull(eventChorbi);
		eventChorbi.setMomentSubscribed(new Date());

		result = this.eventChorbiRepository.save(eventChorbi);

		return result;
	}

	public void delete(final EventChorbi eventChorbi) {
		Assert.notNull(eventChorbi);
		this.eventChorbiRepository.delete(eventChorbi);
	}

	//Other business methods

	public EventChorbi findEventChorbiByParameters(final Chorbi chorbi, final Event event) {
		final EventChorbi result = this.eventChorbiRepository.findEventChorbiByParameters(chorbi.getId(), event.getId());
		return result;
	}

	public Collection<Chorbi> findChorbiesInEvent(final Event event) {
		Collection<Chorbi> result;
		Assert.notNull(event);
		result = this.eventChorbiRepository.findChorbiesInEvent(event.getId());

		return result;
	}
}
