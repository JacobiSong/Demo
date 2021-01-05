package com.example.demo.ui.coursefile;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.File;

import java.util.ArrayList;
import java.util.List;

public class CourseFileViewModel extends ViewModel {

    private final List<File> files = new ArrayList<>();

    public CourseFileViewModel() {
        initData();
    }

    public List<File> getFiles() {
        return files;
    }

    private void initData() {
        this.files.add(new File(
                "作业1.pdf",
                "张老师",
                78.6
        ));
        this.files.add(new File(
                "单元测评2.docx",
                "张老师",
                107.3
        ));
    }
}
