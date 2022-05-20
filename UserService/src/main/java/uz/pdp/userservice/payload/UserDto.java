package uz.pdp.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.userservice.entity.District;

import javax.persistence.OneToOne;
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
