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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import supul.model.board.NoticeBoard;
import supul.model.board.QnaBoard;
import supul.model.board.ReviewBoard;
import supul.model.board.QnaComment;


@Entity
@Table(name ="branch")
@Data
public class Branch {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="branch_id")
	int branchId; 
	@NotEmpty(message = "지점명을 입력하세요.")
    @Pattern(regexp="[가-힣]{2,15}", message = "한글 2~15자 입니다.")
    String name;
	
    @NotEmpty(message = "번호를 입력하세요.")
    @Pattern(regexp = "^(\\d{2,4})-(\\d{3,4})-(\\d{3,4})$", message = "올바른 전화번호 형식이 아닙니다. (예: 031-1234-5678)")
    String phone;
     
    @NotEmpty(message = "주소를 입력하세요.")
    @Pattern(regexp = "^[A-Za-z가-힣0-9 ()-]+$", message = "주소는 한글, 숫자, 띄어쓰기만 허용됩니다.")
    @Size(min= 5, max = 50, message = "주소는 5자 이상 50자 이내로 입력하세요.")
    String address;
    
    @NotEmpty(message = "좌표를 입력하세요.")
    @Pattern(regexp = "^\\d{1,3}\\.\\d{1,10}\\s*,\\s*\\d{1,3}\\.\\d{1,10}$", message = "올바른 좌표 형식이 아닙니다. (예: 35.229573,129.087266)")
    String coordinate;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<Thema> tm;
	
	@OneToMany(mappedBy = "branch")
	List<Reservation> rv;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<QnaBoard> qna;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<ReviewBoard> review;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	List<QnaComment> comment;


	

	@Override
	public String toString() {
		return name;
	}


	
	
}
 