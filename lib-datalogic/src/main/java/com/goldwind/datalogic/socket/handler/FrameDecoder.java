package com.goldwind.datalogic.socket.handler;

import java.util.List;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.DataParse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自定义消息解包器
 * 
 * @author 张超
 *
 */
public class FrameDecoder extends ByteToMessageDecoder
{
    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws DataAsException // NOSONAR
    {
        Object decoded = null;
        int length = buffer.writerIndex() - buffer.readerIndex();
        if (length >= 8 && getInt(buffer, 0) <= 100000)
        {
            // 大数据
            int packSize = getInt(buffer, 4);
            if (length >= packSize + 8)
            {
                decoded = buffer.readRetainedSlice(packSize + 8);
            }
        }
        else
        {
            int startCount = 0;
            int endCount = 0;
            for (int i = buffer.readerIndex(); i < buffer.writerIndex(); i++)
            {
                if (buffer.getByte(i) == Unpooled.copiedBuffer("(".getBytes()).getByte(0))
                {
                    startCount++;
                }
                else if (startCount > 0 && buffer.getByte(i) == Unpooled.copiedBuffer(")".getBytes()).getByte(0))
                {
                    endCount++;
                }
                if (startCount > 0 && startCount == endCount)
                {
                    decoded = buffer.readRetainedSlice(i + 1 - buffer.readerIndex());
                    break;
                }
            }
            if (decoded == null)
            {
                byte lastByte = buffer.getByte(buffer.writerIndex() - 1);
                if (lastByte == Unpooled.copiedBuffer("~".getBytes()).getByte(0))
                {
                    decoded = buffer.readRetainedSlice(length);
                }
            }
        }
        if (decoded != null)
        {
            out.add(decoded);
        }
    }

    /**
     * 读取int类型数据
     * 
     * @param buffer
     *            ByteBuf
     * @param index
     *            起始位置
     * @return int类型数据
     */
    private int getInt(ByteBuf buffer, int index)
    {
        byte[] data = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            data[i] = buffer.getByte(index + i);
        }
        return DataParse.byteArrayToInt(data);
    }
}
