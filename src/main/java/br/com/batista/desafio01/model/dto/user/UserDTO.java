package br.com.batista.desafio01.model.dto.user;

import br.com.batista.desafio01.model.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UserDTO {

    @JsonIgnore
    private long idUser;

    @NotNull(message = "{user.dto.name.notnull}")
    @Size(min = 2, max = 20, message = "{user.dto.name.size}")
    private String name;

    @NotNull(message = "{user.dto.password.notnull}")
    @Size(min = 6, max = 12, message = "{user.dto.password.size}")
    private String password;

    @NotNull(message = "{user.dto.document.notnull}")
    @Size(min = 9, max = 14, message = "{user.dto.document.size}")
    @Pattern(regexp = "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})", message="{user.dto.document.valid}")
    private String document;

    @NotNull(message = "{user.dto.email.notnull}")
    @Email(message = "{user.dto.email.valid}")
    private String email;


    BigDecimal moneyBalance;

    boolean isSendMoney;

    boolean isReceiveMoney;

    public UserDTO(){

    }

    public UserDTO(User user){
        if(user.getName() != null) {
            this.setName(user.getName());
        }
        if(user.getPassword() != null) {
            this.setPassword(user.getPassword());
        }
        if(user.getEmail() != null) {
            this.setEmail(user.getEmail());
        }
        if(user.getDocument() != null) {
            this.setDocument(user.getDocument());
        }
        if(user.getMoneyBalance() != null) {
            this.setMoneyBalance(user.getMoneyBalance());
        }

        this.setSendMoney(user.isSendMoney());
        this.setReceiveMoney(user.isReceiveMoney());

    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public boolean isReceiveMoney() {
        return isReceiveMoney;
    }

    public void setReceiveMoney(boolean receiveMoney) {
        isReceiveMoney = receiveMoney;
    }

    public boolean isSendMoney() {
        return isSendMoney;
    }

    public void setSendMoney(boolean sendMoney) {
        isSendMoney = sendMoney;
    }

}
