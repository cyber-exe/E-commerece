package model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseModel {
    private String title;
    private String description;
    private BigDecimal price;
    private long count;
}
