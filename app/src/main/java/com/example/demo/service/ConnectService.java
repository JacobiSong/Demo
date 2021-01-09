package com.example.demo.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.demo.MyApplication;
import com.example.demo.datagram.DatagramProto;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.resolver.dns.LoggingDnsQueryLifeCycleObserverFactory;

public class ConnectService extends Service {
    public class MyBinder extends Binder {
        public void login(String ip, int port, String username, String password, int identity, long dbVersion) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (thread != null && !thread.isInterrupted()) {
                thread.interrupt();
            }
            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    EventLoopGroup eventExecutors = new NioEventLoopGroup();
                    try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new ClientInitializer());
                        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
                        channel = channelFuture.channel();
                        channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.LOGIN)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                                        .setLogin(DatagramProto.Login.newBuilder().setDbVersion(dbVersion).setPassword(password)
                                                .setIdentity(identity).setUsername(username).build()
                                        ).build().toByteString()
                        ).build());
                        channel.closeFuture().sync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        eventExecutors.shutdownGracefully();
                    }
                }
            });
            thread.start();
        }
        public void register(String ip, int port, String username, String password, int identity) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (thread != null && !thread.isInterrupted()) {
                thread.interrupt();
            }
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EventLoopGroup eventExecutors = new NioEventLoopGroup();
                    try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new ClientInitializer());
                        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
                        channel = channelFuture.channel();
                        channel.writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.REGISTER)
                                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                                        .setRegister(DatagramProto.Register.newBuilder().setPassword(password).setIdentityValue(identity)
                                                .setUsername(username).build()
                                        ).build().toByteString()
                        ).build());
                        channel.closeFuture().sync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        eventExecutors.shutdownGracefully();
                    }
                }
            }
            );
            thread.start();
        }

        public void pushAll() {
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            }).start();
        }

        public Channel getChannel() {
            return channel;
        }
    }

    private final MyBinder mBinder = new MyBinder();
    private Channel channel = null;
    private Thread thread = null;

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