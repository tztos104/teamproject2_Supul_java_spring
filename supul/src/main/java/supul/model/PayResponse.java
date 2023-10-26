package supul.model;

import lombok.Data;

@Data
public class PayResponse {
	 private Integer code;
	 private String message;
	 private Payment  response;

}
