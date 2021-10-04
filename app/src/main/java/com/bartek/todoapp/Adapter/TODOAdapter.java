package com.bartek.todoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bartek.todoapp.Database.DBHandler;
import com.bartek.todoapp.MainActivity;
import com.bartek.todoapp.Model.TODOModel;
import com.bartek.todoapp.R;

import java.util.List;

public class TODOAdapter extends RecyclerView.Adapter<TODOAdapter.ViewHolder> implements View.OnClickListener {

    private List<TODOModel> todoList;
    private MainActivity activity;
    private DBHandler db;
    private onCL onClickListener;

    public TODOAdapter(MainActivity activity, onCL onCL){
        this.onClickListener = onCL;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);

        return new ViewHolder(itemView, onClickListener);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        TODOModel item = todoList.get(position);
        holder.text.setText(item.getText());
        holder.data.setText(item.getData());
        holder.category.setText(item.getCategory());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTasks(List<TODOModel> todoList){
        this.todoList = todoList;
    }

    public void deleteTask(int id){
        TODOModel task = todoList.get(id);
        db = new DBHandler(getContext());
        db.openDatabase();
        db.deleteTask(task.getId());
        todoList.remove(id);
        notifyItemRemoved(id);
    }

    public Context getContext() {
        return activity;
    }

    @Override
    public void onClick(View view) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text, data, category;
        onCL onCL;

        ViewHolder (View view, onCL onCL) {
            super(view);
            text = view.findViewById(R.id.textMain);
            data = view.findViewById(R.id.textData);
            category = view.findViewById(R.id.textCategory);
            this.onCL = onCL;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCL.onClick(getAbsoluteAdapterPosition());
        }
    }

    public interface onCL{
        void onClick(int position);
    }
}