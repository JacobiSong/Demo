package com.example.demo.ui.courses.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.ui.mine.profile.UserProfileActivity;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private final LiveData<List<DatagramProto.Message>> data; //所有消息

    public MessageAdapter(Context context, LiveData<List<DatagramProto.Message>> data) {
        super();
        this.data = data;
        this.context = context;
    }

    //两个Holder风别用于缓存item_msg_left.xml和item_msg_right.xml布局中的控件
    private static class LeftViewHolder extends RecyclerView.ViewHolder {
        private final TextView leftMsg;
        private final CircleImageView imageView;
        private final TextView leftName;
        public LeftViewHolder(@NonNull View view) {
            super(view);
            leftName = view.findViewById(R.id.textLeftName);
            imageView = view.findViewById(R.id.imageLeft);
            leftMsg = view.findViewById(R.id.textLeftMsg);
        }
    }

    private static class RightViewHolder extends RecyclerView.ViewHolder {

        private final TextView rightMsg;
        private final CircleImageView imageView;
        public RightViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageRight);
            rightMsg = view.findViewById(R.id.textRightMsg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!data.getValue().get(viewType).getSenderId().equals(MyApplication.getUsername())) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_left, parent, false);
            return new LeftViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_right, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DatagramProto.Message message = Objects.requireNonNull(data.getValue()).get(position);
        if (holder.getClass().equals(LeftViewHolder.class)) {
            String senderId = message.getSenderId();
            Cursor cursor = MyApplication.getDatabase().query("select name from user where id = ?", senderId);
            if (cursor.moveToFirst()) {
                ((LeftViewHolder) holder).leftName.setText(cursor.getString(0));
            }
            ((LeftViewHolder) holder).leftMsg.setText(message.getContent());
            SharedPreferences sp = context.getSharedPreferences("user_" + senderId, Context.MODE_PRIVATE);
            String image = sp.getString("photo", "");
            if (image.isEmpty()) {
                ((LeftViewHolder) holder).imageView.setImageResource(R.drawable.account_circle_80);
            } else {
                byte[] bytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
                ((LeftViewHolder) holder).imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            ((LeftViewHolder) holder).imageView.setOnClickListener(v -> context.startActivity(new Intent(context, UserInfoActivity.class).putExtra("id", senderId)));
        } else if (holder.getClass().equals(RightViewHolder.class)) {
            ((RightViewHolder) holder).rightMsg.setText(message.getContent());
            SharedPreferences sp = context.getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
            String image = sp.getString("photo", "");
            if (image.isEmpty()) {
                ((RightViewHolder) holder).imageView.setImageResource(R.drawable.account_circle_80);
            } else {
                byte[] bytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
                ((RightViewHolder) holder).imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            ((RightViewHolder) holder).imageView.setOnClickListener(v -> context.startActivity(new Intent(context, UserProfileActivity.class)));
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(data.getValue()).size();
    }
}
