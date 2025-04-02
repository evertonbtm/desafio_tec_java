package br.com.batista.desafio01.model.dto.user;

import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.repository.specification.UserSpecification;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.domain.Specification;

public class UserSearchDTO {

    @Schema(description = "user.searchdto.hint.name")
    private String name;

    @Schema(description = "user.searchdto.hint.document")
    private String document;

    @Schema(description = "user.searchdto.hint.email")
    private String email;

    public UserSearchDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specification<User> getSpecification() {
        return Specification
                .allOf(UserSpecification.withName(name))
                .and(UserSpecification.withEmail(email))
                .and(UserSpecification.withDocument(document));
    }
}
