package com.goldwind.datalogic.socket.handler;

import java.util.List;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.SyncUUIDMemery;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class SyncUUIDHandler extends ChannelInboundHandlerAdapter
{
    /**
     * 发送消息
     */
    private byte[] message;
    /**
     * 返回字节消息
     */
    private List<Object> result;

    private String uuid;

    public SyncUUIDHandler(byte[] message, List<Object> result, String uuid)
    {
        this.message = message;
        this.result = result;
        this.uuid = uuid;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws DataAsException
    {
        SyncUUIDMemery.UuidCtxs.put(uuid, ctx);
        SyncUUIDMemery.results.put(uuid, new SyncUUIDMemery().new result());
        ctx.channel().writeAndFlush(message);
        System.out.println("channelactive uuid:" + uuid + ";memery.uuid:" + SyncUUIDMemery.UuidCtxs.get(uuid).toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws DataAsException
    {
        result.add(msg);
        ReferenceCountUtil.release(msg);
        SyncUUIDMemery.UuidCtxs.remove(uuid);
        SyncUUIDMemery.results.remove(uuid);
        System.out.println("channelRead uuid:" + uuid + ";memery.uuid:" + SyncUUIDMemery.UuidCtxs.get(uuid));
        ctx.close();
    }

}
