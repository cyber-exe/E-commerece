package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Admin extends BaseModel{
    private String secondName;
    private int age;
    private String phone;
    private String email;
}
