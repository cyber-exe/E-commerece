package model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategory extends BaseModel {
    private UUID categoryId;
}
