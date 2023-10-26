package supul.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import supul.model.Ranking;
import supul.model.User;

@Mapper
public interface UserMapper {

    List<User> userList(User ud);

    User getUserById(String user_id);
	
	// 랭크 추가
	int insertRank(Ranking rd);
	
	// 랭크 데이터 전체 리스트
	List<Ranking> rankList(Ranking rd);
	
	// 아이디로 랭크데이터 검색
	Ranking rankId(int rankId);
	
	// 랭크 삭제
	int rankdel(Ranking rd);
	int rankModify(Ranking rd);
	
	
}
