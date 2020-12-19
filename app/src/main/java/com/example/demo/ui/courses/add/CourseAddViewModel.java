package com.example.demo.ui.courses.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseAddViewModel extends ViewModel {
    private MutableLiveData<List<CourseAddProfile>> courses;

    public CourseAddViewModel() {
        courses = new MutableLiveData<>();
        courses.setValue(new ArrayList<>());
    }

    public LiveData<List<CourseAddProfile>> getCourses() {
        return courses;
    }
}