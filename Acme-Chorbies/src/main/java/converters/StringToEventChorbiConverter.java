
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.EventChorbiRepository;
import domain.EventChorbi;

@Component
@Transactional
public class StringToEventChorbiConverter implements Converter<String, EventChorbi> {

	@Autowired
	private EventChorbiRepository	eventChorbiRepository;


	@Override
	public EventChorbi convert(final String text) {
		EventChorbi result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.eventChorbiRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
