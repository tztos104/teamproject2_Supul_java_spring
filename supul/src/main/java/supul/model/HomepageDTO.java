package supul.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Alias("hDTO")
@Entity
@Table(name="homepage_intro")
@Data
public class HomepageDTO {
   
	@Id
    private String title1;
    private String content1;
    private String title2;
    private String title3;
    private String subtitle1;
    private String content2;
    private String content3;
    private String content4;
    private String content5;
    private String content6;   
 
}