package supul.model;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


import lombok.Data;

@Entity
@Data
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wishThema_id")
	int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "thema_id")
	Thema thema;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	User user;
	
	
	 String wChkStr;
	    
	    public String getWChkStr() {

	    	String res = thema.getThemaId()+"_"+user.getId();
	    	
	    	return res;
	    	
	    	
	    }

		@Override
		public String toString() {
			return "WishList [id=" + id + "]";
		}

		
	    
}
  