package org.example.shooltest.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "Предметы")
@Data
public class NewRubric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Код")
    private Integer id;
    @Column(name = "Учебныедисциплины")
    private String name;
}
