package com.fic.actividad1crudconroom.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tasks")
public class Task {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int id;

    private String taskTitle;
    private String taskDescription;
    private Date createdAt;
    private boolean isCompleted;

    //Constructor
    public Task(@NonNull String taskTitle, String taskDescription, Date createdAt, boolean isCompleted) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.createdAt = createdAt;
        this.isCompleted = isCompleted;

    }
    // Getters y Setters
    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTaskTitle() {

        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {

        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {

        this.createdAt = createdAt;
    }

    public boolean isCompleted() {

        return isCompleted;
    }

    public void setCompleted(boolean completed) {

        isCompleted = completed;
    }

}
