package model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.BaseModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Buyer extends BaseModel {
    private String secondName;
    private int age;
    private Gender gender;
    private String phone;
    private String email;
    private String password;
    private long chatId;
    private long massageId;
    private String lan;

    @Override
    public String toString() {
        return  "name: " + super.getName() +
                "\nsecondName: " + secondName +
                "\nage: " + age +
                "\ngender: " + gender +
                "\nphone: " + phone +
                "\nemail: " + email +
                "\npassword: " + password;
    }
}
