package supul.service;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class PasswordChange {

    String currentPassword;
    String newPassword;
    String confirmNewPassword;

}
