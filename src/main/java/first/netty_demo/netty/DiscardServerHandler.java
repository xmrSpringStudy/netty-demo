package first.netty_demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * Created by moon on 2017/4/5.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // (2)
//        super.channelRead(ctx, msg);
    
    	System.out.println("receive socket message");
    	
    	ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
            System.out.print("\n");            
    		
    		send(ctx, "I'm ok!\n’");
            
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("receive socket exception:" + 
    			ctx.channel().remoteAddress().toString() + 
    			" is connected");
    	
        ctx.fireChannelActive();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("receive socket exception:" + 
    			ctx.channel().remoteAddress().toString() + 
    			" is inactive, maybe client shutdown");
    	
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (5)
//        super.exceptionCaught(ctx, cause);
    	System.out.println("receive socket exception:" + 
    			ctx.channel().remoteAddress().toString() + 
    			";exception:" + cause.getMessage());
    	
        cause.printStackTrace();
        ctx.close();
    }
    
    private void send(ChannelHandlerContext ctx, String content) {

		// 在当前场景下，发送的数据必须转换成ByteBuf数组
		ByteBuf encoded = ctx.alloc().buffer(4 * content.length());
		encoded.writeBytes(content.getBytes());
		
        ctx.writeAndFlush(encoded).addListener(new ChannelFutureListener(){  
            public void operationComplete(ChannelFuture future)  
                    throws Exception {  
            	System.out.println("下发成功！");
            }  
        });
    }
}