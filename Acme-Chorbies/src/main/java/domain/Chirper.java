
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Chirper extends Actor {

	//Attributes

	//Constructor

	public Chirper() {
		super();
	}


	//Getter & setter

	//RelationShips

	private Collection<Like>	receivedLikes;
	private Collection<Chirp>	sentChirps;


	@Valid
	@OneToMany(mappedBy = "receiver")
	public Collection<Like> getReceivedLikes() {
		return this.receivedLikes;
	}

	public void setReceivedLikes(final Collection<Like> receivedLikes) {
		this.receivedLikes = receivedLikes;
	}

	@Valid
	@OneToMany(mappedBy = "sender")
	public Collection<Chirp> getSentChirps() {
		return this.sentChirps;
	}

	public void setSentChirps(final Collection<Chirp> sentChirps) {
		this.sentChirps = sentChirps;
	}
}
