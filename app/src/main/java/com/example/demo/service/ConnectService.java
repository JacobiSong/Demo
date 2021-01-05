package com.example.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.demo.datagram.DatagramProto;
import com.example.demo.netty.ClientInitializer;

import java.sql.Connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectService extends Service {
    public class MyBinder extends Binder {
        public void login(String ip, int port, String username, String password, int identity, long dbVersion) {
            if (channel != null) {
                channel.close();
            }
            if (thread != null) {
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
            if (channel != null) {
                channel.close();
            }
            if (thread != null) {
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