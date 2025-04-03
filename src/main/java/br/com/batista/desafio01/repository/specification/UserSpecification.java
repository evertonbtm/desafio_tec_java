package br.com.batista.desafio01.repository.specification;

import br.com.batista.desafio01.model.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> withName(String name) {
        return (root, query, criteriaBuilder) -> {
            return name == null || name.isEmpty()
                    ? criteriaBuilder.isTrue(criteriaBuilder.literal(true))
                    : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    public static Specification<User> withEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            return email == null || email.isEmpty()
                    ? criteriaBuilder.isTrue(criteriaBuilder.literal(true))
                    : criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.trim().toLowerCase() + "%");
        };
    }

    public static Specification<User> withDocument(String document) {
        return (root, query, criteriaBuilder) -> {
            return document == null || document.isEmpty()
                    ? criteriaBuilder.isTrue(criteriaBuilder.literal(true))
                    : criteriaBuilder.like(criteriaBuilder.lower(root.get("document")), "%" + document.trim().toLowerCase() + "%");
        };
    }

    public static Specification<User> withDeleted(boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("deleted"), deleted);
        };
    }
}
