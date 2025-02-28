package org.example.shooltest.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "oldposts")
public class OldEntityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "post_author", nullable = false)
    private Long postAuthor;

    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    @Column(name = "post_date_gmt", nullable = false)
    private LocalDateTime postDateGmt;

    @Column(name = "post_title", columnDefinition = "TEXT")
    private String postTitle;

    @Column(name = "post_excerpt", columnDefinition = "TEXT")
    private String postExcerpt;

    @Column(name = "post_status", length = 20, nullable = false)
    private String postStatus;

    @Column(name = "comment_status", length = 20, nullable = false)
    private String commentStatus;

    @Column(name = "ping_status", length = 20, nullable = false)
    private String pingStatus;

    @Column(name = "post_password", length = 255)
    private String postPassword;

    @Column(name = "post_name", length = 200)
    private String postName;

    @Column(name = "to_ping", columnDefinition = "TEXT")
    private String toPing;

    @Column(name = "pinged", columnDefinition = "TEXT")
    private String pinged;

    @Column(name = "post_modified", nullable = false)
    private LocalDateTime postModified;

    @Column(name = "post_modified_gmt", nullable = false)
    private LocalDateTime postModifiedGmt;

    @Column(name = "post_content_filtered", columnDefinition = "TEXT")
    private String postContentFiltered;

    @Column(name = "post_content", columnDefinition = "TEXT")
    @ToString.Exclude
    private String postContent;

    @Column(name = "post_parent", nullable = false)
    private Long postParent;

    @Column(name = "guid", length = 255)
    private String guid;

    @Column(name = "menu_order", nullable = false)
    private Integer menuOrder;

    @Column(name = "post_type", length = 20, nullable = false)
    private String postType;

    @Column(name = "post_mime_type", length = 100)
    private String postMimeType;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @ManyToOne
    @JoinTable(
            name = "gasvr_term_relationships",
            joinColumns = @JoinColumn(name = "object_id"),
            inverseJoinColumns = @JoinColumn(name = "term_taxonomy_id")
    )
    private OldRubric term;
}
