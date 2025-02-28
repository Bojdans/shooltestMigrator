package org.example.shooltest.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Тесты")
public class ShooltestTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "rubric_id", nullable = false)
    private NewRubric rubric;

    @Column
    private String redirectUrl;

    @JoinColumn(name = "oldPost_id")
    @OneToOne
    private OldEntityPost oldEntityPost;
}