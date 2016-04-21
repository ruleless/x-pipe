package com.ctrip.xpipe.redis.keeper.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.xpipe.redis.keeper.RedisKeeperServer;
import com.ctrip.xpipe.redis.protocal.Command;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wenchao.meng
 *
 * 2016年4月21日 下午3:09:44
 */
public class NettySlaveHandler extends ChannelDuplexHandler{
	
	private RedisKeeperServer redisKeeperServer;
	
	private Map<Channel, Command> commands = new ConcurrentHashMap<Channel, Command>();
	
	public NettySlaveHandler(RedisKeeperServer redisKeeperServer) {
		this.redisKeeperServer = redisKeeperServer;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		Channel channel = ctx.channel();
		Command command  = redisKeeperServer.slaveConnected(channel);
		commands.put(channel, command);
		command.request();
		super.channelActive(ctx);
	}

	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		redisKeeperServer.slaveDisconntected(ctx.channel());
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		Command command = commands.get(ctx.channel());
		command.handleResponse((ByteBuf)msg);
		super.channelRead(ctx, msg);
	}

}
