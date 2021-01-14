package com.example.demo.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHandler extends SimpleChannelInboundHandler<DatagramProto.Datagram> {
    // 保存当前连接的token
    private static String token = "";

    public static String getToken() {
        return token;
    }

    public ClientHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramProto.Datagram msg) throws Exception {
        // 获取报文版本
        final int version = msg.getVersion();
        if (version == 1) { // 处理版本1的报文
            version1Handler(ctx, DatagramProto.DatagramVersion1.parseFrom(msg.getDatagram()));
        }
    }

    /*
     * 处理版本1的报文
     */
    private void version1Handler(ChannelHandlerContext ctx, DatagramProto.DatagramVersion1 msg) {
        Log.d("Datagram", "version1Handler: " + msg);
        // 检查token是否合法
        if (msg.getType() != DatagramProto.DatagramVersion1.Type.LOGIN && msg.getType() != DatagramProto.DatagramVersion1.Type.REGISTER
                && !token.equals(msg.getToken())) {
            return;
        }
        // 获取报文子类型
        final DatagramProto.DatagramVersion1.Subtype subtype = msg.getSubtype();
        switch (subtype) {
            case RESPONSE: // 处理服务器的Response响应报文
                version1Response(ctx, msg);
                break;
            case PUSH: // 处理服务器的Push推送报文
                version1Push(ctx, msg);
                break;
            default:
                break;
        }
    }

    /*
     * 处理服务器发来的版本1的Response响应报文
     */
    private void version1Response(ChannelHandlerContext ctx, DatagramProto.DatagramVersion1 msg) {
        final DatagramProto.DatagramVersion1.Type type = msg.getType();
        switch (type) {
            case KEEP_ALIVE: {
                MyApplication.getServer().pushAll();
                break;
            }
            case LOGIN: { // 登录响应
                switch (msg.getOk()) {
                    case 100: // 登录成功
                        token = msg.getToken();
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.login"));
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "登录成功"));
                        break;
                    case 200: // 账号或密码错误
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "账号或密码错误"));
                        break;
                    default:
                        break;
                }
                break;
            }
            case REGISTER: {
                switch (msg.getOk()) {
                    case 100:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.register"));
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "注册成功"));
                        break;
                    case 200:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "账号已注册"));
                        break;
                    case 201:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "注册失败"));
                        break;
                    default:
                        break;
                }
                ctx.close();
                break;
            }
            case LOGOUT: {
                if (msg.getOk() == 100) {
                    token = "";
                    ctx.close();
                    MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.logout"));
                    MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "登出成功"));
                }
                break;
            }
            case COURSE: {
                switch (msg.getOk()) {
                    case 100:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.course").putExtra("courses", msg.getCourses().toByteArray()));
                        break;
                    case 101:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "添加成功"));
                        break;
                    case 200:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "该课程群已被注册"));
                        break;
                    case 201:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "创建失败"));
                        break;
                    default:
                        break;
                }
                break;
            }
            case USER: {
                switch (msg.getOk()) {
                    case 100: {
                        DatagramProto.User user = msg.getUser();

                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + user.getId(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                            editor.putString("photo", new String(Base64.encode(msg.getUser().getPhoto().toByteArray(), Base64.DEFAULT)));
                        }
                        editor.apply();

                        ContentValues userValues = new ContentValues();
                        userValues.put("phone", user.getPhone());
                        userValues.put("email", user.getEmail());
                        userValues.put("gender", user.getGenderValue());
                        userValues.put("last_modified", user.getLastModified());
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", user.getId());
                        switch (user.getIdentityValue()) {
                            case 0: {
                                ContentValues studentValues = new ContentValues();
                                studentValues.put("department", user.getStudent().getDepartment());
                                studentValues.put("major", user.getStudent().getMajor());
                                studentValues.put("class_no", user.getStudent().getClassNo());
                                MyApplication.getDatabase().update("student", SQLiteDatabase.CONFLICT_REPLACE, studentValues, "id = ?", user.getId());
                                break;
                            }
                            case 1: {
                                ContentValues teacherValues = new ContentValues();
                                teacherValues.put("department", user.getTeacher().getDepartment());
                                MyApplication.getDatabase().update("teacher", SQLiteDatabase.CONFLICT_REPLACE, teacherValues, "id = ?", user.getId());
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    case 101: {
                        ContentValues userValues = new ContentValues();
                        userValues.put("phone", msg.getUser().getPhone());
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 102: {
                        ContentValues userValues = new ContentValues();
                        userValues.put("email", msg.getUser().getEmail());
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 103: {
                        ContentValues userValues = new ContentValues();
                        userValues.put("gender", msg.getUser().getGenderValue());
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 104: {
                        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).edit();
                        editor.putString("password", msg.getUser().getPassword());
                        editor.apply();
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 105: {
                        int identity = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).getInt("identity", 0);
                        if (identity == 0) {
                            ContentValues studentValues = new ContentValues();
                            studentValues.put("department", msg.getUser().getStudent().getDepartment());
                            MyApplication.getDatabase().update("student", SQLiteDatabase.CONFLICT_REPLACE, studentValues, "id = ?", MyApplication.getUsername());
                        } else {
                            ContentValues teacherValues = new ContentValues();
                            teacherValues.put("department", msg.getUser().getTeacher().getDepartment());
                            MyApplication.getDatabase().update("teacher", SQLiteDatabase.CONFLICT_REPLACE, teacherValues, "id = ?", MyApplication.getUsername());
                        }
                        ContentValues userValues = new ContentValues();
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 106: {
                        ContentValues studentValues = new ContentValues();
                        studentValues.put("major", msg.getUser().getStudent().getMajor());
                        MyApplication.getDatabase().update("student", SQLiteDatabase.CONFLICT_REPLACE, studentValues, "id = ?", MyApplication.getUsername());
                        ContentValues userValues = new ContentValues();
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 107: {
                        ContentValues studentValues = new ContentValues();
                        studentValues.put("class_no", msg.getUser().getStudent().getClassNo());
                        MyApplication.getDatabase().update("student", SQLiteDatabase.CONFLICT_REPLACE, studentValues, "id = ?", MyApplication.getUsername());
                        ContentValues userValues = new ContentValues();
                        long last_modified = msg.getUser().getLastModified();
                        userValues.put("last_modified", last_modified);
                        MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", MyApplication.getUsername());
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long time = sp.getLong("db_version", 0);
                        if (last_modified > time) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putLong("db_version", last_modified);
                            editor.apply();
                        }
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改成功"));
                        break;
                    }
                    case 108: {
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        long last_modified = msg.getUser().getLastModified();
                        long time = sp.getLong("db_version", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        if (last_modified > time) {
                            editor.putLong("db_version", last_modified);

                        }
                        editor.putString("photo", new String(Base64.encode(msg.getUser().getPhoto().toByteArray(),Base64.DEFAULT)));
                        editor.apply();
                        MyApplication.getDatabase().executeAndTrigger("user", "drop table if exists _m");
                        break;
                    }
                    case 200:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "无此用户"));
                        break;
                    case 201:
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 206:
                    case 207:
                    case 208:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "修改失败"));
                        break;
                    case 210:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "您没有该权限"));
                        break;
                    default:
                        break;
                }
                break;
            }
            case MESSAGE: {
                switch (msg.getOk()) {
                    case 100:
                        MyApplication.getDatabase().delete("t_push", "id = " + msg.getMessage().getTemporaryId() + " and type = 1");
                        break;
                    case 200:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "消息发送失败"));
                        break;
                    default:
                        break;
                }
                break;
            }
            case NOTIFICATION: {
                switch (msg.getOk()) {
                    case 100:
                        MyApplication.getDatabase().delete("t_push", "id = " + msg.getNotification().getTemporaryId() + " and type = 2");
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "通知发送成功"));
                        break;
                    case 200:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.toast").putExtra("text", "通知发送失败"));
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    /*
     * 处理服务器发来的版本1的Push推送报文
     */
    private void version1Push(ChannelHandlerContext ctx, DatagramProto.DatagramVersion1 msg) {
        while(MyApplication.getDatabase() == null) {
            Log.d("ClientHandler", "loading database...");
        }
        switch (msg.getOk()) {
            case 100: {
                switch (msg.getType()) {
                    case GROUP: {
                        DatagramProto.Group group = msg.getGroup();
                        DatagramProto.Course course = group.getCourse();
                        long lastModified = course.getLastModified();
                        ContentValues courseValues = new ContentValues();
                        courseValues.put("id", course.getId());
                        courseValues.put("name", course.getName());
                        courseValues.put("classroom", course.getClassroom());
                        courseValues.put("time", course.getTime());
                        courseValues.put("semester", course.getSemester());
                        courseValues.put("remarks", course.getRemarks());
                        courseValues.put("last_modified", lastModified);
                        MyApplication.getDatabase().insert("course", SQLiteDatabase.CONFLICT_REPLACE, courseValues);
                        for (DatagramProto.User user : group.getUsers().getUsersList()) {
                            ContentValues joinValues = new ContentValues();
                            joinValues.put("user_id", user.getId());
                            joinValues.put("course_id", course.getId());
                            MyApplication.getDatabase().insert("t_join", SQLiteDatabase.CONFLICT_REPLACE, joinValues);
                            String name = user.getName();
                            if (!name.isEmpty()) {
                                int identity = user.getIdentityValue();
                                ContentValues userValues = new ContentValues();
                                userValues.put("id", user.getId());
                                switch (identity) {
                                    case 0:
                                        MyApplication.getDatabase().insert("student", SQLiteDatabase.CONFLICT_IGNORE, userValues);
                                        break;
                                    case 1:
                                        MyApplication.getDatabase().insert("teacher", SQLiteDatabase.CONFLICT_IGNORE, userValues);
                                        break;
                                    default:
                                        break;
                                }
                                userValues.put("last_modified", user.getCreateTime());
                                userValues.put("name", name);
                                userValues.put("identity", identity);
                                MyApplication.getDatabase().insert("user", SQLiteDatabase.CONFLICT_IGNORE, userValues);
                            }
                        }
                        MyApplication.getDatabase().execute("create table if not exists " + course.getId() + "_m (" +
                                "id bigint, " +
                                "sender_id varchar(10) not null, " +
                                "receiver_id varchar(10) not null, " +
                                "content varchar(1000) not null, " +
                                "time bigint, " +
                                "temporary_id int, " +
                                "constraint un1 unique(id, temporary_id))");
                        MyApplication.getDatabase().execute("create table if not exists " + course.getId() + "_n (" +
                                "id bigint, " +
                                "sender_id varchar(10) not null, " +
                                "receiver_id varchar(10) not null, " +
                                "title varchar(20) not null, " +
                                "content varchar(1000) not null, " +
                                "time bigint, " +
                                "temporary_id int, " +
                                "constraint un1 unique(id, temporary_id))");
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("db_version", lastModified);
                        editor.apply();
                        // 发送Ack报文, 返回正确码100
                        ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.GROUP)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.ACK).setToken(token).setOk(100)
                                        .setPush(msg.getPush()).build().toByteString()
                        ).build());
                        break;
                    }
                    case MESSAGE: {
                        DatagramProto.Message message = msg.getMessage();
                        String sender_id = message.getSenderId();
                        String receiver_id = message.getReceiverId();
                        int temporary_id = message.getTemporaryId();
                        long time = message.getTime();
                        ContentValues messageValues = new ContentValues();
                        messageValues.put("id", message.getId());
                        messageValues.put("sender_id", sender_id);
                        messageValues.put("receiver_id", receiver_id);
                        messageValues.put("content", message.getContent());
                        messageValues.put("time", time);
                        messageValues.putNull("temporary_id");
                        if (sender_id.equals(MyApplication.getUsername())) {
                            Cursor cursor = MyApplication.getDatabase().query("select count(1) from " + receiver_id + "_m where temporary_id = ?", temporary_id);
                            if (cursor.moveToFirst() && cursor.getInt(0) != 0) {
                                MyApplication.getDatabase().update(receiver_id + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues, "temporary_id = " + temporary_id);
                            } else if(cursor.getInt(0) == 0) {
                                MyApplication.getDatabase().insert(receiver_id + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues);
                            }
                        } else {
                            MyApplication.getDatabase().insert(receiver_id + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues);
                        }
                        MyApplication.getDatabase().executeAndTrigger("course", "drop table if exists _m");
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("db_version", time);
                        editor.apply();
                        // 发送Ack报文, 返回正确码100
                        ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.MESSAGE)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.ACK).setToken(token).setOk(100)
                                        .setPush(msg.getPush()).build().toByteString()
                        ).build());
                        break;
                    }
                    case NOTIFICATION: {
                        DatagramProto.Notification notification = msg.getNotification();
                        String sender_id = notification.getSenderId();
                        String receiver_id = notification.getReceiverId();
                        int temporary_id = notification.getTemporaryId();
                        long time = notification.getTime();
                        ContentValues notificationValues = new ContentValues();
                        notificationValues.put("id", notification.getId());
                        notificationValues.put("sender_id", sender_id);
                        notificationValues.put("receiver_id", receiver_id);
                        notificationValues.put("title", notification.getTitle());
                        notificationValues.put("content", notification.getContent());
                        notificationValues.put("time", notification.getTime());
                        notificationValues.putNull("temporary_id");
                        if (sender_id.equals(MyApplication.getUsername())) {
                            Cursor cursor = MyApplication.getDatabase().query("select count(1) from " + receiver_id + "_n where temporary_id = ?", temporary_id);
                            if (cursor.moveToFirst() && cursor.getInt(0) != 0) {
                                MyApplication.getDatabase().update(receiver_id + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues, "temporary_id = " + temporary_id);
                            } else {
                                MyApplication.getDatabase().insert(receiver_id + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues);
                            }
                        } else {
                            MyApplication.getDatabase().insert(receiver_id + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues);
                        }
                        MyApplication.getDatabase().executeAndTrigger("course", "drop table if exists _n");
                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("db_version", time);
                        editor.apply();
                        // 发送Ack报文, 返回正确码100
                        ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.NOTIFICATION)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.ACK).setToken(token).setOk(100)
                                        .setPush(msg.getPush()).build().toByteString()
                        ).build());
                        break;
                    }
                    case USER: {
                        DatagramProto.User user = msg.getUser();
                        long createTime = user.getCreateTime();
                        int identity = user.getIdentityValue();

                        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("db_version", createTime);
                        editor.apply();

                        ContentValues userValues = new ContentValues();
                        userValues.put("id", user.getId());
                        switch (identity) {
                            case 0:
                                MyApplication.getDatabase().insert("student", SQLiteDatabase.CONFLICT_REPLACE, userValues);
                                break;
                            case 1:
                                MyApplication.getDatabase().insert("teacher", SQLiteDatabase.CONFLICT_REPLACE, userValues);
                                break;
                            default:
                                break;
                        }
                        userValues.put("name", user.getName());
                        userValues.put("identity", identity);
                        userValues.put("last_modified", createTime);
                        MyApplication.getDatabase().insert("user", SQLiteDatabase.CONFLICT_REPLACE, userValues);
                        // 发送Ack报文, 返回正确码100
                        ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.USER)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.ACK).setToken(token).setOk(100)
                                        .setPush(msg.getPush()).build().toByteString()
                        ).build());
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            case 101: {
                if (msg.getType() == DatagramProto.DatagramVersion1.Type.USER) {
                    
                    DatagramProto.User user = msg.getUser();
                    SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putLong("db_version", user.getLastModified());
                    if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                        editor.putString("photo", new String(Base64.encode(msg.getUser().getPhoto().toByteArray(), Base64.DEFAULT)));
                    }
                    editor.apply();
                    
                    String id = MyApplication.getUsername();
                    ContentValues userValues = new ContentValues();
                    userValues.put("phone", user.getPhone());
                    userValues.put("email", user.getEmail());
                    userValues.put("gender", user.getGenderValue());
                    userValues.put("last_modified", user.getLastModified());
                    MyApplication.getDatabase().update("user", SQLiteDatabase.CONFLICT_REPLACE, userValues, "id = ?", id);
                    switch (user.getIdentityValue()) {
                        case 0: {
                            ContentValues studentValues = new ContentValues();
                            studentValues.put("department", user.getStudent().getDepartment());
                            studentValues.put("major", user.getStudent().getMajor());
                            studentValues.put("class_no", user.getStudent().getClassNo());
                            MyApplication.getDatabase().update("student", SQLiteDatabase.CONFLICT_REPLACE, studentValues, "id = ?", id);
                            break;
                        }
                        case 1: {
                            ContentValues teacherValues = new ContentValues();
                            teacherValues.put("department", user.getTeacher().getDepartment());
                            MyApplication.getDatabase().update("teacher", SQLiteDatabase.CONFLICT_REPLACE, teacherValues, "id = ?", id);
                            break;
                        }
                        default:
                            break;
                    }
                    // 发送Ack报文, 返回正确码100
                    ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                            DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.USER)
                                    .setSubtype(DatagramProto.DatagramVersion1.Subtype.ACK).setToken(token).setOk(100)
                                    .setPush(msg.getPush()).build().toByteString()
                    ).build());
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        token = "";
        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.offline"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // 捕获异常, 关闭连接
        token = "";
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent && !token.isEmpty()) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) { // 一段时间内连接不活跃, 发送保活报文
                ctx.channel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                        DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.KEEP_ALIVE)
                                .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setToken(token).build().toByteString()
                ).build());
            }
        }
    }
}