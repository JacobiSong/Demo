package com.example.demo.ui.coursemenber;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class MemberViewModel extends ViewModel {

    private List<Student> studentList = new ArrayList<>();

    public MemberViewModel() {
        initData();
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    private void initData() {
        this.studentList = new ArrayList<>();
        this.studentList.add(new Student(
                "123456234",
                "小明",
                "13999999999",
                "xxx@gmail.com",
                0,
                "qwer1234",
                "计算机科学与技术学院",
                "软件工程",
                11111
        ));
    }
}
