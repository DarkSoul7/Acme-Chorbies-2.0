
package forms;

public class LikeForm {
	
	int				id;
	private String	comment;
	private int		idReceiver;
	private Integer	stars;
	
	
	public LikeForm() {
		super();
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(final int id) {
		this.id = id;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(final String comment) {
		this.comment = comment;
	}
	
	public int getIdReceiver() {
		return this.idReceiver;
	}
	
	public void setIdReceiver(final int idReceiver) {
		this.idReceiver = idReceiver;
	}
	
	public Integer getStars() {
		return this.stars;
	}
	
	public void setStars(final Integer stars) {
		this.stars = stars;
	}
	
}
