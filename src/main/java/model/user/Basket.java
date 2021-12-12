package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)

public class Basket extends BaseModel {
    private UUID ownerId;
    private UUID productId;
}
