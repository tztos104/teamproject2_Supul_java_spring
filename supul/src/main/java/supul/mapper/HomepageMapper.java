package supul.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import supul.model.HomepageDTO;

@Mapper
public interface HomepageMapper {

   List<HomepageDTO> selectHomepageIntro();   
   HomepageDTO selectHomepageByTitle(String title);
    void updateHomepage(HomepageDTO homepage);
    
    
    

}