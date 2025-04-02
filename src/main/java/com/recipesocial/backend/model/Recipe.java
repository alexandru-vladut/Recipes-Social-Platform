package com.recipesocial.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String ingredients;

    private String instructions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
}
