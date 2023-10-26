package supul.repository;





import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;




import supul.model.Ranking;


public interface RankingRepository extends JpaRepository<Ranking, Integer> {

	List <Ranking> findByThemaName(String themaName );
	List <Ranking> findByThemaThemaId(int themaId );
	
	
	
	
}
  