package supul.model.board;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import supul.model.Admin;
import supul.model.Branch;

@Entity
@Table(name = "comment")
@Data
public class QnaComment {
	
	@Id
	@Column(name = "comment_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commentId; 
	
	@ManyToOne(fetch = FetchType.LAZY)					// 내가 다수 
	@JoinColumn(name = "qna_id", nullable = false)	// 부모 엔티티와 자식 엔티티 사이의 관계를 매핑할 때 // BoardQuestion JoinColumn(연관관계를 나타냄)을 사용해야됨. // @ManyToOne, @OneToMany, @OneToOne, @ManyToMany 과 함께 사용됨 //외래 키(Foreign Key)로 매핑될 때 사용
	QnaBoard qna;	// BoardQeustion에서 mappedBy 와 동일하게 지정
	 
	@ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

	private String writer;
	@NotBlank(message = "내용을 입력해주세요.")
	@Size(max =999, message = "내용은 999자 이내여야 합니다.")
	@Column(name = "content") 
	private String content;
	 
	@Column(name = "reg_date")
	private LocalDateTime regDate;
	
	@Column(name = "modi_date")
	private LocalDateTime modiDate;

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", content=" + content + ", regDate="
				+ regDate + ", modiDate=" + modiDate + "]";
	}
	
	

}
