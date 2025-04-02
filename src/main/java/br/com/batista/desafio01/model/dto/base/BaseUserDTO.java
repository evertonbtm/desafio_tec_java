package br.com.batista.desafio01.model.dto.base;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public abstract class BaseUserDTO {

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 20, message = "O nome deve ter entre 2 e 20 caracteres")
    private String name;

    @NotNull(message = "A senha não pode ser nula")
    @Size(min = 6, max = 12, message = "A senha deve ter entre 6 e 12 caracteres")
    private String password;

    @NotNull(message = "O documento não pode ser nulo")
    @Size(min = 9, max = 14, message = "O documento deve ter entre 9 e 14 caracteres")
    @Pattern(regexp = "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})", message="CPF ou CPNJ invalido")
    private String document;

    @NotNull(message = "O e-mail não pode ser nulo")
    @Email(message = "O e-mail deve ser válido")
    private String email;


    BigDecimal moneyBalance;

    boolean isSendMoney;

    boolean isReceiveMoney;

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
