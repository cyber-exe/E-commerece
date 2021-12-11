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
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String lastName;
    private int age;
}
