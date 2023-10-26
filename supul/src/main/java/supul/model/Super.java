package supul.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name="super")
@Data
public class Super {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "superId", unique = true)
    @NotEmpty(message = "아이디를 입력하세요.")
    private String superId;
	
	@Column(name = "superPw")
    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String superPw;
}
