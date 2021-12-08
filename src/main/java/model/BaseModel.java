package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data

public abstract class BaseModel {
    private UUID id;
    private String name;
    private boolean isActive;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    {
        this.id = UUID.randomUUID();
        this.isActive = true;
    }
}
