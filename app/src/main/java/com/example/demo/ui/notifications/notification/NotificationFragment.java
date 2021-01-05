package com.example.demo.ui.notifications.notification;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NotificationFragment extends Fragment {

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notification_fragment, container, false);
        TextView title = root.findViewById(R.id.notification_activity_title);
        TextView subtitle = root.findViewById(R.id.notification_activity_subtitle);
        TextView content = root.findViewById(R.id.notification_activity_content);
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        String receiverId = getActivity().getIntent().getStringExtra("receiver_id");
        long id = getActivity().getIntent().getLongExtra("id", 0);
        QueryObservable queryObservable = MyApplication.getDatabase().createQuery(receiverId + "_n", "select sender_id, title, content, time from " + receiverId + "_n where id = ?", id);
        queryObservable.subscribe(new Consumer<SqlBrite.Query>() {
            @Override
            public void accept(SqlBrite.Query query) throws Exception {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    Cursor c = MyApplication.getDatabase().query("select name from course where id = ?", receiverId);
                    if (c.moveToFirst()) {
                        title.setText(cursor.getString(1));
                        subtitle.setText(c.getString(0));
                        content.setText(cursor.getString(2));
                    }
                }
            }
        });
        queryObservable.unsubscribeOn(Schedulers.io());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}