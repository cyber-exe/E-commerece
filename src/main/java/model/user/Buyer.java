package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Buyer extends BaseModel {
    private String secondName;
    private int age;
    private Gender gender;
    private String phone;
    private String email;
    private String password;
}
