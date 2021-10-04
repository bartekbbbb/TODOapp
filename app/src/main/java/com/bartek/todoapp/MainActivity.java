package com.bartek.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bartek.todoapp.Adapter.TODOAdapter;
import com.bartek.todoapp.Database.DBHandler;
import com.bartek.todoapp.Model.TODOModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TODOAdapter.onCL {

    private FloatingActionButton addButton;
    private RecyclerView tasksRecyclerView;
    private TODOAdapter tasksAdapter;
    private List<TODOModel> taskList;
    private DBHandler db;
    private RecyclerView.ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);
        db.openDatabase();

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTask();
            }
        });

        tasksRecyclerView = findViewById(R.id.RVS);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter = new TODOAdapter(this, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        checkRVS();

        tasksAdapter.setTasks(taskList);
    }

    public void openAddTask(){
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);
    }

    private void checkRVS(){
        TextView emptyRVS = findViewById(R.id.emptyRVS);
        if(taskList.isEmpty())
            emptyRVS.setText("Brak zadań do wyświetlenia");
        else
            emptyRVS.setText("");
    }

    @Override
    public void onClick(int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Chcesz usunąć zadanie?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tasksAdapter.deleteTask(position);
                        checkRVS();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }
}