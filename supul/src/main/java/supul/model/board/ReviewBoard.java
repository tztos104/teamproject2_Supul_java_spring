package supul.model.board;


import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import supul.model.Branch;
import supul.model.Thema;
import supul.model.User;


@Entity
@Table(name="board_review")
@Data 
public class ReviewBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private int reviewId;    
    
    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "thema_id", nullable = false)
    private Thema thema;
 
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;    
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    @Column(name = "title", nullable = false)
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 9999, message = "내용은 999자 이내여야 합니다.")
    @Column(name = "content", nullable = false, length = 9999)
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "modi_date")
    private LocalDateTime modiDate;

    @Column(name = "cnt")
    private int cnt = 0;
    
    @Size(max = 10, message = "최대 10자까지 입력 가능합니다.")
    @Column(name = "esc_time")
    private String escTime;

    @Column(name = "clear")
    private boolean clear;

    @Column(name = "grade")
    private double grade;
 
    @Column(name = "file_name")
    private String fileName;

    @Transient
    private String filePath;

    @Transient
    private MultipartFile file;
}
