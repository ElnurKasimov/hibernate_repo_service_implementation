package com.softserve.itacademy.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "todos", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "owner_id"})})
public class ToDo {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "todo_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @NotBlank(message = "The title cannot be empty")
    @Column(nullable = false)
    private String title;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "toDo", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
    private List<User> collaborators;
    @Generated
    public long getId() {
        return id;
    }
    @Generated
    public void setId(long id) {
        this.id = id;
    }
    @Generated
    public String getTitle() {
        return title;
    }
    @Generated
    public void setTitle(String title) {
        this.title = title;
    }
    @Generated
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Generated
    public User getOwner() {
        return owner;
    }
    @Generated
    public void setOwner(User owner) {
        this.owner = owner;
    }
    @Generated
    public List<Task> getTasks() {
        return tasks;
    }
    @Generated
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    @Generated
    public List<User> getCollaborators() {
        return collaborators;
    }
    @Generated
    public void setCollaborators(List<User> users) {
        this.collaborators = users;
    }

    @Generated
    @Override
    public String toString() {
        return "ToDo {" +
                "id = " + id +
                ", title = '" + title + '\'' +
                ", createdAt = " + createdAt +
                "} ";
    }
}
