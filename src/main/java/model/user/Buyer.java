package model.user;

import bot.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.BaseModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Buyer extends BaseModel {
    private String secondName;
    private int age;
    private Gender gender;
    private String phone;
    private String email;
    private String password;
    private long chatId;
    private long massageId;
    private String lan;
    private State state;

    public Buyer(long chatId, String name) {
        super.setName(name);
        this.chatId = chatId;
        this.state = State.START;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "secondName='" + secondName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", chatId=" + chatId +
                ", massageId=" + massageId +
                ", lan='" + lan + '\'' +
                ", state=" + state +
                '}';
    }
}
