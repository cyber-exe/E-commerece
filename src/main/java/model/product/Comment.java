package model.product;

import model.BaseModel;

import java.util.UUID;

public class Comment extends BaseModel {
    private UUID productId;
    private String text;
}
