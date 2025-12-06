package com.fic.actividad1crudconroom.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.actividad1crudconroom.R;
import com.fic.actividad1crudconroom.controller.TaskFormActivity;
import com.fic.actividad1crudconroom.model.Appdatabase;
import com.fic.actividad1crudconroom.model.Taskdao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.fic.actividad1crudconroom.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAddTask;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private Taskdao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        fabAddTask = findViewById(R.id.fabAddTask);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Inicializar base de datos PRIMERO
        Appdatabase database = Appdatabase.getInstance(this);
        taskDao = database.taskDao();

        // Configurar RecyclerView DESPUÉS
        setupRecyclerView();

        loadTasks();

        // Configurar botón
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskFormActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        taskAdapter = new TaskAdapter(new ArrayList<>(), taskDao, recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);
    }

    private void loadTasks() {
        new Thread(() -> {
            List<Task> tasks = taskDao.getAllTasks();
            runOnUiThread(() -> {
                taskAdapter = new TaskAdapter(tasks, taskDao, recyclerViewTasks);
                recyclerViewTasks.setAdapter(taskAdapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}