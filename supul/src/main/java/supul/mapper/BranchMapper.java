package supul.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import supul.model.Branch;

@Mapper
public interface BranchMapper {
	
    List<Branch> selectList();
    Branch selectById(int branchId); // ID로 지점 정보 조회
    void insertBranch(Branch branch); // 지점 정보 추가
    void updateBranch(Branch branch); // 지점 정보 수정
    void deleteBranch(int branchId); // 지점 정보 삭제
    
  //매장안내
    Branch selectByName(String branchName); // 이름으로 Branch 정보 조회
}
