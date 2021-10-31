package ru.itis.forms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordForm {
    private String oldPassword;
    private String newPassword;
    private String reNewPassword;
}
