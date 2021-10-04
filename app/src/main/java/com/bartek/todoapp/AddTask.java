package com.bartek.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bartek.todoapp.Database.DBHandler;
import com.bartek.todoapp.Model.TODOModel;

public class AddTask extends AppCompatActivity {

    private CheckBox workCheckbox, shopCheckbox, otherCheckbox;
    private Button addButton, cancelButton;
    private EditText temp;
    private String textM, textD, cat;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        workCheckbox = findViewById(R.id.workCheckbox);
        shopCheckbox = findViewById(R.id.shopCheckbox);
        otherCheckbox = findViewById(R.id.otherCheckbox);

        db = new DBHandler(this);
        db.openDatabase();

        addButton = findViewById(R.id.buttAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TODOModel task = new TODOModel();
                temp = findViewById(R.id.taskName);
                textM = temp.getText().toString();
                task.setText(textM);
                temp = findViewById(R.id.taskData);
                textD = temp.getText().toString();
                task.setData(textD);
                task.setCategory(cat);
                if (textM.equals("") || textD.equals("") || (!workCheckbox.isChecked() && !shopCheckbox.isChecked() && !otherCheckbox.isChecked())){
                    Toast.makeText(getApplicationContext(), "Wprowadź wszystkie dane!", Toast.LENGTH_LONG).show();
                }
                else {
                    if(task == null){
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Błąd zapisu. Chcesz powtórzyć?")
                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        openMain();
                                    }
                                })
                                .show();
                    }
                    else{
                        db.insertTask(task);
                        Toast.makeText(getApplicationContext(), "Zadanie poprawnie dodane!", Toast.LENGTH_LONG).show();
                        openMain();
                    }
                }
            }
        });
        cancelButton = findViewById(R.id.buttCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        switch(view.getId()) {
            case R.id.workCheckbox:
                shopCheckbox.setChecked(false);
                otherCheckbox.setChecked(false);
                cat = "Praca";
                break;

            case R.id.shopCheckbox:
                otherCheckbox.setChecked(false);
                workCheckbox.setChecked(false);
                cat = "Zakupy";;
                break;

            case R.id.otherCheckbox:
                workCheckbox.setChecked(false);
                shopCheckbox.setChecked(false);
                cat = "Inne";
                break;

            default: cat = "Inne";
        }
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}