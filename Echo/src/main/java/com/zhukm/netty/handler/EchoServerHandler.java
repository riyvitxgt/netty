package com.zhukm.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Created by king on 2016/11/21.
 */
@ChannelHandler.Sharable    //标识这类实例可以在channel里共享
public class EchoServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();    //打印异常堆栈
        ctx.close();    //关闭通道
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));   //日志输出到控制台
        ctx.write(in);  //将接收的数据返回给发送者（还没有冲刷数据，只在缓存中）
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)    //冲刷所有待审信息到远程节点
                .addListener(ChannelFutureListener.CLOSE);  //数据刷出后关闭通道
    }
}
