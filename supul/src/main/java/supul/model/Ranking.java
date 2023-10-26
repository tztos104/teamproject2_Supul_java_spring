package supul.model;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Ranking {

   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name="rank_id")
   int rankId;
  
   @ManyToOne
   @JoinColumn(name = "thema_id")
   Thema thema;
   
   @ManyToOne
   @JoinColumn(name = "id")
   User user;
   
   int people;
   int minutes;
   int seconds;
    
   @Transient
   Duration time = Duration.ofMinutes(minutes).plusSeconds(seconds);
   int themaRank;
   
   String date;
   String themaName;
      
   String branchName;
   
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "pay_id")
   Pay pay;
   
	public void calcRank(List<Ranking> rankTheme) {
		// 시간을 기준으로 랭킹 정렬
		

		// 랭킹 순서를 업데이트
		int themaRank = 0;
		for (Ranking ranking : rankTheme) {
			themaRank++;
			ranking.setThemaRank(themaRank);
		}
	}

@Override
public String toString() {
	return "Ranking [rankId=" + rankId + ", people=" + people + ", time=" + time + ", date=" + date + ", themaName="
			+ themaName + ", branchName=" + branchName + "]";
}


   
}

