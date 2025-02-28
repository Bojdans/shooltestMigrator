package org.example.shooltest.Entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Тесты_вопросы")

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 2000)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    @ToString.Exclude
    private ShooltestTest test;
    @ManyToOne
    @JoinColumn(name = "rubric_id",nullable = false)
    private NewRubric rubric;

    public Question(String questionText, List<Answer> answers, NewRubric rubric) {
        this.questionText = questionText;
        this.answers = answers;
        this.rubric = rubric;
    }
}
