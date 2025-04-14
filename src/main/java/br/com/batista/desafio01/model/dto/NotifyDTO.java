package br.com.batista.desafio01.model.dto;

public class NotifyDTO {

    String status;
    String message;

    public NotifyDTO(String status, String message) {
        this.status = status;
        this.message = message;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
