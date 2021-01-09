package com.example.demo.ui.courses.chat;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_RECEIVED = 0;
    private int TYPE_SEND = 1;
    private Context context;

    private final LiveData<List<DatagramProto.Message>> data; //所有消息

    public MessageAdapter(Context context, LiveData<List<DatagramProto.Message>> data) {
        super();
        this.data = data;
        this.context = context;
    }

    /**
     * 临时用于判断消息是发送还是接受，用于生成左右两种不同的消息UI
     */
    private boolean isSend(DatagramProto.Message message) {
        return message.getSenderId().equals(MyApplication.getUsername());
    }

    //两个Holder风别用于缓存item_msg_left.xml和item_msg_right.xml布局中的控件
    class LeftViewHolder extends RecyclerView.ViewHolder {

        private final TextView leftMsg;
        private final ImageView imageView;
        //private final TextView leftName;

        public LeftViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageLeft);
            leftMsg = view.findViewById(R.id.textLeftMsg);
            //this.leftName = view.findViewById(R.id.textLeftName);
        }
    }

    class RightViewHolder extends RecyclerView.ViewHolder {

        private final TextView rightMsg;
        //private final ImageView rightImage;

        public RightViewHolder(@NonNull View view) {
            super(view);
            this.rightMsg = view.findViewById(R.id.textRightMsg);
            //this.rightImage = view.findViewById(R.id.imageRight);
        }
    }

    @Override
    public int getItemViewType(int position) {
        DatagramProto.Message message = data.getValue().get(position);
        if (message.getSenderId().equals(MyApplication.getUsername())) {
            return this.TYPE_SEND;
        } else {
            return this.TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == this.TYPE_RECEIVED) {
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
        DatagramProto.Message message = data.getValue().get(position);
        if (holder.getClass().equals(LeftViewHolder.class)) {
            ((LeftViewHolder) holder).leftMsg.setText(message.getContent());
            ((LeftViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserInfoActivity.class).putExtra("id", data.getValue().get(position).getId()));
                }
            });
            //((LeftViewHolder) holder).leftName.setText(message.getSenderId());
        } else if (holder.getClass().equals(RightViewHolder.class)) {
            ((RightViewHolder) holder).rightMsg.setText(message.getContent());
        } else {
            assert false;
        }
    }

    @Override
    public int getItemCount() {
        return data.getValue().size();
    }
}
