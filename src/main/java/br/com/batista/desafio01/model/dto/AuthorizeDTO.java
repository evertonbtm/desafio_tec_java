package br.com.batista.desafio01.model.dto;

public class AuthorizeDTO {

    private String status;
    private String message;

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

    public AuthorizeDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthorizeDTO() {
    }
}
