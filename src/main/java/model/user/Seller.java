package model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Seller extends BaseModel {
    private String registrationNumber;
    private String companyName;
    private String email;
    private String holderName;
    private String holderSecondName;
    private int age;
    private String phoneNumber;
    private Gender gender;
    private String password;
    private long chatId;
    private long massageId;
    private String lan;
}
