package io.lumine.achievements.nms.v1_19_R1.network;

import io.lumine.achievements.nms.VolatileCodeEnabled_v1_19_R1;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VolatileChannelHandler extends ChannelDuplexHandler {

	private final VolatileCodeEnabled_v1_19_R1 nmsHandler;
	@Getter private final Player player;

	public VolatileChannelHandler(Player player, VolatileCodeEnabled_v1_19_R1 nmsHandler) {
		this.player = player;
		this.nmsHandler = nmsHandler;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object obj, ChannelPromise promise) {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {

	}

}
