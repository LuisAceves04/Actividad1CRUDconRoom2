package com.fic.actividad1crudconroom.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fic.actividad1crudconroom.R;
import com.fic.actividad1crudconroom.model.Appdatabase;
import com.fic.actividad1crudconroom.model.Task;
import com.fic.actividad1crudconroom.model.Taskdao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class TaskFormActivity extends AppCompatActivity {
    private TextInputEditText etTaskTitle;
    private TextInputEditText etTaskDescription;
    private CheckBox cbIsCompleted;
    private Button btnSave;

    private Taskdao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        Appdatabase database = Appdatabase.getInstance(this);
        taskDao = database.taskDao();

        initViews();
        setupSaveButton();
    }

    private void initViews() {
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        cbIsCompleted = findViewById(R.id.cbIsCompleted);
        btnSave = findViewById(R.id.btnSave);
    }

    // Aquí le digo que hacer al botón cuando le haga click
    private void setupSaveButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        // obtengo los datos que el usuario escribió en los campos
        String taskTitle = etTaskTitle.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();
        boolean isCompleted = cbIsCompleted.isChecked();
        Date createdAt = new Date();

        // Validar que el título no esté vacío
        if (taskTitle.isEmpty()) {
            etTaskTitle.setError("El título es requerido");
            return;
        }

        Task newTask = new Task(taskTitle, taskDescription, createdAt, isCompleted);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskDao.insertTask(newTask);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskFormActivity.this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskFormActivity.this, "Error al guardar la tarea: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}