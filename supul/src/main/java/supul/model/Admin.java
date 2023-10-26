package supul.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import supul.model.board.NoticeBoard;
import supul.model.board.QnaComment;

@Entity
@Table(name = "admin")
@Data
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_no")
	int no;

	@NotEmpty(message = "아이디를 입력하세요.")
	@Pattern(regexp = "^[a-zA-Z0-9]{3,10}$", message = "아이디는 영문 또는 숫자 3~10자 입니다.")
	@Column(name = "admin_id")
	String adminId;

	@NotEmpty(message = "비밀번호를 입력하세요.")
	@Pattern(regexp = "^.*(?=^.{6,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 영문자+숫자+특수문자(~ 포함) 6~12자입니다.")
	@Column(name = "admin_pw", nullable = false)
	String adminPw;

	
	@Transient
	String adminPw1;

	@NotEmpty(message = "이름을 입력하세요.")
	@Pattern(regexp = "[가-힣]{2,10}", message = "한글 2~10자 입니다.")
	@Column(name = "name", nullable = false)
	String name;

	@NotEmpty(message = "연락처를 입력하세요.")
	@Column(name = "phone", nullable = false)
	String phone;

	@NotEmpty(message = "이메일을 입력하세요.")
	@Column(name = "email", nullable = false)
	String email;

	@NotEmpty(message = "생년월일을 입력하세요.")
	@Column(name = "birth", nullable = false)
	String birth;

	@Column(name = "reg_date")
	LocalDateTime regDate;

	String branchName;




 
	
	
	public Admin() {
		this.regDate = LocalDateTime.now(); // 현재 시간으로 초기화
	}

	@Override
	public String toString() {
		return "Admin [no=" + no + ", adminId=" + adminId + ", name=" + name + "]";
	}

}