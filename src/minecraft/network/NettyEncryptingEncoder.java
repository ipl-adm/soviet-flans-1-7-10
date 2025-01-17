package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;
import net.minecraft.network.NettyEncryptionTranslator;

public class NettyEncryptingEncoder extends MessageToByteEncoder {

   private final NettyEncryptionTranslator field_150750_a;


   public NettyEncryptingEncoder(Cipher var1) {
      this.field_150750_a = new NettyEncryptionTranslator(var1);
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) {
      this.field_150750_a.func_150504_a(var2, var3);
   }

   // $FF: synthetic method
   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) {
      this.encode(var1, (ByteBuf)var2, var3);
   }
}
