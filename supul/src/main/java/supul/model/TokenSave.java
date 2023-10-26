package supul.model;

import lombok.Data;

@Data
public class TokenSave {
	
	    private String imp_key;
	    private String imp_secret;

	    public TokenSave(String imp_key, String imp_secret) {
	        this.imp_key = imp_key;
	        this.imp_secret = imp_secret;
	    }
}
  