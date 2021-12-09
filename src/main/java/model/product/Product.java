package model.product;

import model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Product extends BaseModel {
    private String title;
    private String description;
    private BigDecimal price;
}
