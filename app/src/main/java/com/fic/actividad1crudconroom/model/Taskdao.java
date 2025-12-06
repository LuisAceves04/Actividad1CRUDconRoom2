package com.fic.actividad1crudconroom.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Taskdao {
    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTaskById(int id);
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> getAllTasks();
    @Update
    void updateTask(Task task);
    @Insert
    void insertTask(Task task);
    @Delete
    void deleteTask(Task task);
}
