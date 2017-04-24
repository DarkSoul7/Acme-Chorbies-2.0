
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.EventChorbi;

@Component
@Transactional
public class EventChorbiToStringConverter implements Converter<EventChorbi, String> {

	@Override
	public String convert(final EventChorbi eventChorbi) {
		String result;

		if (eventChorbi == null)
			result = null;
		else
			result = String.valueOf(eventChorbi.getId());

		return result;
	}
}
