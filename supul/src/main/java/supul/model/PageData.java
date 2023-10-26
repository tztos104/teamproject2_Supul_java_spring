package supul.model;

import org.springframework.web.multipart.MultipartFile;


import lombok.Data;

@Data
public class PageData {
	 String msg, goUrl;
	 MultipartFile mmff;
}
