package com.example.demo.ui.coursemenu;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class CourseMenuViewModel extends ViewModel {


    private final List<Student> studentList = new ArrayList<>();

    public CourseMenuViewModel() {
        initStudents();
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    //临时初始化数据使用
    private void initStudents() {
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
