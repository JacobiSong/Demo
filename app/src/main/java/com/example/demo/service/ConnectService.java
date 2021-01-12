package com.example.demo.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectService extends Service {
    private final MyBinder mBinder = new MyBinder();
    private Channel channel;
    private Thread thread;
    private Bootstrap bootstrap;
    private EventLoopGroup eventExecutors;
    private static int cnt = 0;

    public class MyBinder extends Binder {
        public void connect() {
            if (channel != null && channel.isActive()) {
                return;
            }
            if (cnt > 5) {
                return;
            }
            cnt++;
            SharedPreferences sp = getSharedPreferences("com.example.demo_preferences", MODE_PRIVATE);
            final boolean manually = sp.getBoolean("server_manually", false);
            final String ip = manually ? sp.getString("server_ip", "81.70.170.127") : "81.70.170.127";
            final int port = manually ? Integer.parseInt(sp.getString("server_port", "8888")) : 8888;
            ChannelFuture channelFuture = bootstrap.connect(ip, port);
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    channel = future.channel();
                } else {
                    future.channel().eventLoop().schedule(this::connect, 1, TimeUnit.SECONDS);
                }
            });
        }

        public void login(String username, String password, int identity, long dbVersion) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (thread != null && !thread.isInterrupted()) {
                thread.interrupt();
            }
            cnt = 0;
            thread = new Thread(() -> {
                try {
                    eventExecutors = new NioEventLoopGroup();
                    bootstrap = new Bootstrap();
                    bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new ClientInitializer());
                    connect();
                    for (int i = 0; i < 10; i++) {
                        if (channel != null && channel.isActive()) {
                            channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                    DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.LOGIN)
                                            .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                                            .setLogin(DatagramProto.Login.newBuilder().setDbVersion(dbVersion).setPassword(password)
                                                    .setIdentity(identity).setUsername(username).build()
                                            ).build().toByteString()
                            ).build());
                            channel.closeFuture().sync();
                            break;
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    eventExecutors.shutdownGracefully();
                }
            });
            thread.start();
        }
        public void register(String username, String password, int identity) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (thread != null && !thread.isInterrupted()) {
                thread.interrupt();
            }
            cnt = 0;
            thread = new Thread(() -> {
                try {
                    eventExecutors = new NioEventLoopGroup();
                    bootstrap = new Bootstrap();
                    bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new ClientInitializer());
                    connect();
                    for (int i = 0; i < 30; i++) {
                        if (channel != null && channel.isActive()) {
                            channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                    DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.REGISTER)
                                            .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                                            .setRegister(DatagramProto.Register.newBuilder().setPassword(password).setIdentityValue(identity)
                                                    .setUsername(username).build()
                                            ).build().toByteString()
                            ).build());
                            channel.closeFuture().sync();
                            break;
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    eventExecutors.shutdownGracefully();
                }
            }
            );
            thread.start();
        }

        public void pushAll() {
            new Thread(() -> {
                if (channel != null && channel.isActive()) {
                    Cursor cursor = MyApplication.getDatabase().query("select id, type, id1 from t_push order by time");
                    while (cursor.moveToNext()) {
                        int temporary_id = cursor.getInt(0);
                        int type = cursor.getInt(1);
                        String courseId= cursor.getString(2);
                        switch (type) {
                            case 1: {
                                Cursor c = MyApplication.getDatabase().query("select sender_id, receiver_id, content, time from " + courseId + "_m where temporary_id = ?", temporary_id);
                                if (c.moveToFirst()) {
                                    channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                            DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.MESSAGE)
                                                    .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100).setToken(ClientHandler.getToken())
                                                    .setMessage(
                                                            DatagramProto.Message.newBuilder().setSenderId(c.getString(0)).setReceiverId(c.getString(1))
                                                                    .setTemporaryId(temporary_id).setContent(c.getString(2)).setTime(c.getLong(3)).build()
                                                    ).build().toByteString()
                                    ).build());
                                }
                                break;
                            }
                            case 2: {
                                Cursor c = MyApplication.getDatabase().query("select sender_id, receiver_id, title, content, time from " + courseId + "_n where temporary_id = ?", temporary_id);
                                if (c.moveToFirst()) {
                                    channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                            DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.NOTIFICATION)
                                                    .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100).setToken(ClientHandler.getToken())
                                                    .setNotification(
                                                            DatagramProto.Notification.newBuilder().setSenderId(c.getString(0)).setReceiverId(c.getString(1))
                                                                    .setTemporaryId(temporary_id).setContent(c.getString(3)).setTime(c.getLong(4))
                                                                    .setTitle(c.getString(2)).build()
                                                    ).build().toByteString()
                                    ).build());
                                }
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
            }).start();
        }

        public void logout() {
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                        DatagramProto.DatagramVersion1.newBuilder().setOk(100).setToken(ClientHandler.getToken())
                                .setType(DatagramProto.DatagramVersion1.Type.LOGOUT).setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST)
                                .build().toByteString()
                ).build());
            }
        }

        public void getUserInfo(String userId) {
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                        DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.USER)
                                .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setToken(ClientHandler.getToken())
                                .setUser(DatagramProto.User.newBuilder().setId(userId).setLastModified(0).build())
                                .setOk(100).build().toByteString()
                ).build());
            }
        }

        public void getCourses() {
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                        DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.COURSE)
                                .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                                .setToken(ClientHandler.getToken()).build().toByteString()
                ).build());
            }
        }

        public void addGroup(String courseId) {
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                        DatagramProto.DatagramVersion1.newBuilder().setToken(ClientHandler.getToken()).setOk(101)
                                .setType(DatagramProto.DatagramVersion1.Type.COURSE).setCourse(
                                DatagramProto.Course.newBuilder().setId(courseId).build()
                        )
                                .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).build().toByteString()
                ).build());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if (channel != null) {
            channel.close();
        }
        if (thread != null) {
            thread.interrupt();
        }
        super.onDestroy();
    }
}