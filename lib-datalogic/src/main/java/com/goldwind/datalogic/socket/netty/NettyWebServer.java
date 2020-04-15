package com.goldwind.datalogic.socket.netty;

import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.handler.IWebMessageHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * websocket服务端
 * 
 * @author 张超
 *
 */
public class NettyWebServer implements Runnable
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(NettyWebServer.class);
    /**
     * 服务器端口号
     */
    private int port;
    /**
     * 处理器
     */
    private IWebMessageHandler handler;

    public NettyWebServer(int port, IWebMessageHandler handler)
    {
        this.port = port;
        this.handler = handler;
    }

    /**
     * 启动websocket服务
     */
    public void run()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("http-codec", new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>()
                {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws DataAsException
                    {
                        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(null, null, false);
                        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
                        if (handshaker != null)
                        {
                            handshaker.handshake(ctx.channel(), request);
                        }
                    }
                });
                pipeline.addLast(handler);
            }
        });

        try
        {
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
