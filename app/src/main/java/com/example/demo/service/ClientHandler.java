package com.example.demo.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramProto.Datagram msg) throws Exception {
        // 获取报文版本
        final int version = msg.getVersion();
        if (version == 1) { // 处理版本1的报文
            version1Handler(ctx, DatagramProto.DatagramVersion1.parseFrom(msg.getDatagram()));
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
            }
            case LOGIN: { // 登录响应
                switch (msg.getOk()) {
                    case 100: // 登录成功
                        token = msg.getToken();
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.login"));
                        break;
                    case 200: // 账号或密码错误
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
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
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "注册成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 200:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "账号已被注册", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 201:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "注册失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        Looper.myLooper().quit();
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
                    Looper.prepare();
                    Toast.makeText(MyApplication.getInstance(), "登出成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Looper.myLooper().quit();
                }
                break;
            }
            case COURSE: {
                switch (msg.getOk()) {
                    case 100:
                        MyApplication.getInstance().sendBroadcast(new Intent().setAction("com.example.demo.course").putExtra("courses", msg.getCourses().toByteArray()));
                        break;
                    case 101:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "添加成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 200:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "该课程群已被注册", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 201:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "创建失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    default:
                        break;
                }
                break;
            }
            case USER: {
                switch (msg.getOk()) {
                    case 100:
                        DatagramProto.User user = msg.getUser();
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
                    case 101:
                    case 102:
                    case 103:
                    case 105:
                    case 104:
                    case 107:
                    case 106:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "修改成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 200:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "无此用户", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 201:
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 206:
                    case 207:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "修改失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 210:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "您没有该权限", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
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
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "消息发送失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
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
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "通知发送成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                        break;
                    case 200:
                        Looper.prepare();
                        Toast.makeText(MyApplication.getInstance(), "通知发送失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Looper.myLooper().quit();
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
        final DatagramProto.DatagramVersion1.Type type = msg.getType();
        switch (type) {
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
                    MyApplication.getDatabase().update(receiver_id + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues, "temporary_id = " + temporary_id);
                } else {
                    MyApplication.getDatabase().insert(receiver_id + "_m", SQLiteDatabase.CONFLICT_REPLACE, messageValues);
                }
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
                    MyApplication.getDatabase().update(receiver_id + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues, "temporary_id = " + temporary_id);
                } else {
                    MyApplication.getDatabase().insert(receiver_id + "_n", SQLiteDatabase.CONFLICT_REPLACE, notificationValues);
                }
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
                SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("db_version", createTime);
                editor.apply();
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
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        token = "";
        // TODO : 断线重连
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // 捕获异常, 关闭连接
        token = "";
        Log.d("SYQ", "exceptionCaught: " + "异常退出" + cause.getMessage());
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