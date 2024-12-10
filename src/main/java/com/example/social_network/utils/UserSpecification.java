package com.example.social_network.utils;
import org.springframework.data.jpa.domain.Specification;
import com.example.social_network.entities.User;

public class UserSpecification {

    public static Specification<User> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern)
            );
        };
    }
}
