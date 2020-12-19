package com.example.demo.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<List<NotificationProfile>> notifications;

    public NotificationsViewModel() {
        notifications = new MutableLiveData<>();
        notifications.setValue(new ArrayList<>());
    }

    public LiveData<List<NotificationProfile>> getNotifications() {
        return notifications;
    }
}