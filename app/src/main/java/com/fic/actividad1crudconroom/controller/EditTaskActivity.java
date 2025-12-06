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

public class EditTaskActivity extends AppCompatActivity {

    private TextInputEditText etTaskTitle;
    private TextInputEditText etTaskDescription;
    private CheckBox cbIsCompleted;
    private Button btnUpdate, btnCancel;
    private Taskdao taskDao;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Conectar con la base de datos
        Appdatabase database = Appdatabase.getInstance(this);
        taskDao = database.taskDao();

        // Conectar los elementos del XML
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        cbIsCompleted = findViewById(R.id.cbIsCompleted);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        cargarDatos();

        // Configurar botones
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarTarea();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar esta pantalla
            }
        });
    }

    private void cargarDatos() {
        // Obtener los datos que mandamos desde la lista
        String titulo = getIntent().getStringExtra("task_title");
        String descripcion = getIntent().getStringExtra("task_description");
        boolean completada = getIntent().getBooleanExtra("task_completed", false);
        taskId = getIntent().getIntExtra("task_id", -1);

        // Poner los datos en los campos
        etTaskTitle.setText(titulo);
        etTaskDescription.setText(descripcion);
        cbIsCompleted.setChecked(completada);
    }

    private void actualizarTarea() {
        // Obtener los nuevos datos
        String nuevoTitulo = etTaskTitle.getText().toString();
        String nuevaDescripcion = etTaskDescription.getText().toString();
        boolean estaCompletada = cbIsCompleted.isChecked();

        // Validar que el título no esté vacío
        if (nuevoTitulo.isEmpty()) {
            etTaskTitle.setError("El título es requerido");
            return;
        }

        // Crear la tarea actualizada
        Task tareaActualizada = new Task(nuevoTitulo, nuevaDescripcion, new java.util.Date(), estaCompletada);
        tareaActualizada.setId(taskId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                taskDao.updateTask(tareaActualizada);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditTaskActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }).start();
    }
}