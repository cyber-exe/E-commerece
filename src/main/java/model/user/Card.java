package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class Card extends BaseModel {
    private String cardNum;
    private LocalDate expireDate;
    private BigDecimal balance;
    private UUID ownerId;
}
