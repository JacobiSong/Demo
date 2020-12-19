package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.entity.File;

import java.util.List;

public class CourseFileAdapter extends RecyclerView.Adapter<CourseFileAdapter.FileViewHolder> {

    private final List<File> fileList;

    public CourseFileAdapter(List<File> fileList) {
        super();
        this.fileList = fileList;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        private final TextView fileNameTextView;
        private final TextView authorTextView;
        private final TextView fileSize;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            this.fileNameTextView = itemView.findViewById(R.id.textFileName);
            this.authorTextView = itemView.findViewById(R.id.textFileAuthor);
            this.fileSize = itemView.findViewById(R.id.textFileSize);
        }
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        File file = this.fileList.get(position);
        holder.fileNameTextView.setText(file.getFilename());
        holder.authorTextView.setText(file.getAuthor());
        String fileSize = String.valueOf(file.getSize()) + "KB";
        holder.fileSize.setText(fileSize);
    }

    @Override
    public int getItemCount() {
        return this.fileList.size();
    }
}
