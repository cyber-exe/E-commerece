package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;
import model.product.Product;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class History extends BaseModel {
    private Product product;
    private Buyer buyer;
}
