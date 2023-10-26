package supul.model.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import supul.model.Branch;
import supul.model.User;

@Entity
@Table(name="board_qna")
@Data
public class QnaBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private int qnaId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id",  nullable = false)
    private Branch branch;
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    @Column(name = "title", nullable = false, length = 250)
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 999, message = "내용은 999자 이내여야 합니다.")
    @Column(name = "content", nullable = false, length = 999)
    private String content;
    
    @Column(name = "type", nullable = false, length = 250)
    private String type;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "modi_date")
    private LocalDateTime modiDate;
    
    // 나는 하나 // mappedBy JoinColumn의 name이 아니라 BoardQcomment에서 BoardQuestion의 변수 questionId;를 불러옴.
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL)
    private List<QnaComment> comment = new ArrayList<>();

}
