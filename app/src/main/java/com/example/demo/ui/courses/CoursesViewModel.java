package com.example.demo.ui.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CoursesViewModel extends ViewModel {

    private MutableLiveData<List<CourseProfile>> courses;

    public CoursesViewModel() {
        courses = new MutableLiveData<>();
        courses.setValue(new ArrayList<>());
    }

    public LiveData<List<CourseProfile>> getCourses() {
        return courses;
    }
}