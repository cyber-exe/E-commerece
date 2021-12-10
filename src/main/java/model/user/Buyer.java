package model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Buyer extends BaseModel {
    private String secondName;
    private int age;
    private Gender gender;
    private String phone;
    private String email;
    private String password;
}
