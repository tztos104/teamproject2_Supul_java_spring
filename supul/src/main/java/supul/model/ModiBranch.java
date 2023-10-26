package supul.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import supul.model.board.NoticeBoard;
import supul.model.board.QnaBoard;
import supul.model.board.ReviewBoard;
import supul.model.board.QnaComment;


@Entity
@Table(name ="modiBranch")
@Data
public class ModiBranch {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rqbranch_id")
	int modibranchId;
	int originbranchId;
	String originbranchname;
	String phone;

}
 