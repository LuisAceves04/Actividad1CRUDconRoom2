package com.fic.actividad1crudconroom.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.actividad1crudconroom.R;
import com.fic.actividad1crudconroom.controller.EditTaskActivity;
import com.fic.actividad1crudconroom.model.Task;
import com.fic.actividad1crudconroom.model.Taskdao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Taskdao taskDao;
    private ViewGroup parentViewGroup;

    public TaskAdapter(List<Task> taskList, Taskdao taskDao, ViewGroup parent) {
        this.taskList = taskList;
        this.taskDao = taskDao;
        this.parentViewGroup = parent;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    // Clase ViewHolder
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvDate, tvStatus;
        private Button btnDelete, btnEdit;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            // Configurar clic del botón eliminar
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task taskToDelete = taskList.get(position);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    taskDao.deleteTask(taskToDelete);
                                    itemView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            taskList.remove(position);
                                            notifyItemRemoved(position);
                                            Toast.makeText(itemView.getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    itemView.post(new Runnable() {
                                        @Override
                                        public void run() {Toast.makeText(itemView.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
            });

            //Configurar clic del botón editar
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task taskToEdit = taskList.get(position);

                        // Crear Intent para abrir EditTaskActivity
                        android.content.Intent intent = new android.content.Intent(itemView.getContext(), EditTaskActivity.class
                        );

                        // Pasar los datos de la tarea
                        intent.putExtra("task_id", taskToEdit.getId());
                        intent.putExtra("task_title", taskToEdit.getTaskTitle());
                        intent.putExtra("task_description", taskToEdit.getTaskDescription());
                        intent.putExtra("task_completed", taskToEdit.isCompleted());

                        // Abrir la actividad de editar
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Task task) {
            tvTitle.setText(task.getTaskTitle());
            tvDescription.setText(task.getTaskDescription());

            // Formatear fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = dateFormat.format(task.getCreatedAt());
            tvDate.setText("Creada: " + fechaFormateada);

            // Mostrar estado
            if (task.isCompleted()) {
                tvStatus.setText("COMPLETADA");
                tvStatus.setBackgroundColor(0xFF4CAF50);
            } else {
                tvStatus.setText("PENDIENTE");
                tvStatus.setBackgroundColor(0xFF808080);
            }
        }
    }
}