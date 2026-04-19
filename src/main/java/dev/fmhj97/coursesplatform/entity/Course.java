package dev.fmhj97.coursesplatform.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    /**
     * JPA Constructor
     */
    protected Course() {}

    /**
     * Constructor with args
     * @param title
     * @param description
     * @param price
     * @param instructor
     */
    public Course(String title, String description, BigDecimal price, Instructor instructor) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.instructor = instructor;
    }

    // ---

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
