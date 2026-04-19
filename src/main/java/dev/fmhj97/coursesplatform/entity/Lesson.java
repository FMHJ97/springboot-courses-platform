package dev.fmhj97.coursesplatform.entity;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id, order_index"})
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * JPA Constructor
     */
    protected Lesson() {}

    /**
     * Constructor with args
     * @param title
     * @param content
     * @param orderIndex
     * @param course
     */
    public Lesson(String title, String content, int orderIndex, Course course) {
        this.title = title;
        this.content = content;
        this.orderIndex = orderIndex;
        this.course = course;
    }

    // --- Getters/Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public Course getCourse() {
        return course;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
