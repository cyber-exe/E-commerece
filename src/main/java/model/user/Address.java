package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class Address extends BaseModel {
    private String country;
    private String region;
    private String state;
    private String district;
    private String street;
    private String house;
}
