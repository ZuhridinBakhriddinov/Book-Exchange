package uz.pdp.clients.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String universityName;
    private String password;
    private String telegramUsername;


    private Integer districtId;
}
