package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.entity.Msg;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Msg> msgList; //所有消息

    public MsgAdapter(List<Msg> msgList) {
        super();
        this.msgList = msgList;
    }

    //两个Holder风别用于缓存item_msg_left.xml和item_msg_right.xml布局中的控件
    class LeftViewHolder extends RecyclerView.ViewHolder {

        private final TextView leftMsg;
        private final ImageView imageView;
        private final TextView leftName;

        public LeftViewHolder(@NonNull View view) {
            super(view);
            this.imageView = view.findViewById(R.id.imageLeft);
            this.leftMsg = view.findViewById(R.id.textLeftMsg);
            this.leftName = view.findViewById(R.id.textLeftName);
        }
    }

    class RightViewHolder extends RecyclerView.ViewHolder {

        private final TextView rightMsg;
        private final ImageView rightImage;

        public RightViewHolder(@NonNull View view) {
            super(view);
            this.rightMsg = view.findViewById(R.id.textRightMsg);
            this.rightImage = view.findViewById(R.id.imageRight);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Msg msg = this.msgList.get(position);
        return msg.getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Msg.TYPE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_left_b, parent, false);
            return new LeftViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_right_b, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if (holder.getClass().equals(LeftViewHolder.class)) {
            ((LeftViewHolder) holder).leftMsg.setText(msg.getContent());
            ((LeftViewHolder) holder).leftName.setText(msg.getName());
        } else if (holder.getClass().equals(RightViewHolder.class)) {
            ((RightViewHolder) holder).rightMsg.setText(msg.getContent());
        } else {
            assert false;
        }
    }

    @Override
    public int getItemCount() {
        return this.msgList.size();
    }
}
