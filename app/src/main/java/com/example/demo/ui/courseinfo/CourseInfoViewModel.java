package com.example.demo.ui.courseinfo;

import androidx.lifecycle.ViewModel;

import com.example.demo.entity.Course;

public class CourseInfoViewModel extends ViewModel {

    private Course course;

    public CourseInfoViewModel() {
        initData();
    }

    public Course getCourse() {
        return course;
    }

    private void initData() {
        this.course = new Course(
                "cs_10001",
                "数据结构",
                "正心716",
                "[2-15周]周一12节、周三12节",
                true,
                "先导课程：C语言程序设计",
                3
        );
    }
}
