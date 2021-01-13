package com.example.demo.ui.mine.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<String> gender;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> email;
    private MutableLiveData<String> department;
    private MutableLiveData<String> major;
    private MutableLiveData<String> classNo;

    public UserProfileViewModel() {

    }

    public MutableLiveData<String> getGender() {
        return gender;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getDepartment() {
        return department;
    }

    public MutableLiveData<String> getMajor() {
        return major;
    }

    public MutableLiveData<String> getClassNo() {
        return classNo;
    }
}
