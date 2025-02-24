package org.example.shooltest.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gasvr_terms")
@Data
@NoArgsConstructor
public class OldRubric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private int id;
    @Column(name = "name")
    private String name;

}
