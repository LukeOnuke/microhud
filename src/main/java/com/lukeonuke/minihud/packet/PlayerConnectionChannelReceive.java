package com.lukeonuke.minihud.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerConnectionChannelReceive extends ChannelInboundHandlerAdapter {

    private final ClientPlayerEntity owner;

    public PlayerConnectionChannelReceive(ClientPlayerEntity player) {
        this.owner = player;
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object packet) throws Exception {
        // here check what is packet, and if it's the PlayerInfoPacket -> that's it !

        super.channelRead(ctx, packet); // don't forget this line !!
    }
}
