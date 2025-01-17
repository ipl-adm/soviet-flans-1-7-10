package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S21PacketChunkData$Extracted;
import net.minecraft.world.chunk.Chunk;

public class S26PacketMapChunkBulk extends Packet {

   private int[] field_149266_a;
   private int[] field_149264_b;
   private int[] field_149265_c;
   private int[] field_149262_d;
   private byte[] field_149263_e;
   private byte[][] field_149260_f;
   private int field_149261_g;
   private boolean field_149267_h;
   private static byte[] field_149268_i = new byte[0];


   public S26PacketMapChunkBulk() {}

   public S26PacketMapChunkBulk(List var1) {
      int var2 = var1.size();
      this.field_149266_a = new int[var2];
      this.field_149264_b = new int[var2];
      this.field_149265_c = new int[var2];
      this.field_149262_d = new int[var2];
      this.field_149260_f = new byte[var2][];
      this.field_149267_h = !var1.isEmpty() && !((Chunk)var1.get(0)).worldObj.provider.hasNoSky;
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         Chunk var5 = (Chunk)var1.get(var4);
         S21PacketChunkData$Extracted var6 = S21PacketChunkData.func_149269_a(var5, true, '\uffff');
         if(field_149268_i.length < var3 + var6.field_150282_a.length) {
            byte[] var7 = new byte[var3 + var6.field_150282_a.length];
            System.arraycopy(field_149268_i, 0, var7, 0, field_149268_i.length);
            field_149268_i = var7;
         }

         System.arraycopy(var6.field_150282_a, 0, field_149268_i, var3, var6.field_150282_a.length);
         var3 += var6.field_150282_a.length;
         this.field_149266_a[var4] = var5.xPosition;
         this.field_149264_b[var4] = var5.zPosition;
         this.field_149265_c[var4] = var6.field_150280_b;
         this.field_149262_d[var4] = var6.field_150281_c;
         this.field_149260_f[var4] = var6.field_150282_a;
      }

      Deflater var11 = new Deflater(-1);

      try {
         var11.setInput(field_149268_i, 0, var3);
         var11.finish();
         this.field_149263_e = new byte[var3];
         this.field_149261_g = var11.deflate(this.field_149263_e);
      } finally {
         var11.end();
      }

   }

   public static int func_149258_c() {
      return 5;
   }

   public void readPacketData(PacketBuffer var1) {
      short var2 = var1.readShort();
      this.field_149261_g = var1.readInt();
      this.field_149267_h = var1.readBoolean();
      this.field_149266_a = new int[var2];
      this.field_149264_b = new int[var2];
      this.field_149265_c = new int[var2];
      this.field_149262_d = new int[var2];
      this.field_149260_f = new byte[var2][];
      if(field_149268_i.length < this.field_149261_g) {
         field_149268_i = new byte[this.field_149261_g];
      }

      var1.readBytes(field_149268_i, 0, this.field_149261_g);
      byte[] var3 = new byte[S21PacketChunkData.func_149275_c() * var2];
      Inflater var4 = new Inflater();
      var4.setInput(field_149268_i, 0, this.field_149261_g);

      try {
         var4.inflate(var3);
      } catch (DataFormatException var12) {
         throw new IOException("Bad compressed data format");
      } finally {
         var4.end();
      }

      int var5 = 0;

      for(int var6 = 0; var6 < var2; ++var6) {
         this.field_149266_a[var6] = var1.readInt();
         this.field_149264_b[var6] = var1.readInt();
         this.field_149265_c[var6] = var1.readShort();
         this.field_149262_d[var6] = var1.readShort();
         int var7 = 0;
         int var8 = 0;

         int var9;
         for(var9 = 0; var9 < 16; ++var9) {
            var7 += this.field_149265_c[var6] >> var9 & 1;
            var8 += this.field_149262_d[var6] >> var9 & 1;
         }

         var9 = 2048 * 4 * var7 + 256;
         var9 += 2048 * var8;
         if(this.field_149267_h) {
            var9 += 2048 * var7;
         }

         this.field_149260_f[var6] = new byte[var9];
         System.arraycopy(var3, var5, this.field_149260_f[var6], 0, var9);
         var5 += var9;
      }

   }

   public void writePacketData(PacketBuffer var1) {
      var1.writeShort(this.field_149266_a.length);
      var1.writeInt(this.field_149261_g);
      var1.writeBoolean(this.field_149267_h);
      var1.writeBytes(this.field_149263_e, 0, this.field_149261_g);

      for(int var2 = 0; var2 < this.field_149266_a.length; ++var2) {
         var1.writeInt(this.field_149266_a[var2]);
         var1.writeInt(this.field_149264_b[var2]);
         var1.writeShort((short)(this.field_149265_c[var2] & '\uffff'));
         var1.writeShort((short)(this.field_149262_d[var2] & '\uffff'));
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleMapChunkBulk(this);
   }

   public int func_149255_a(int var1) {
      return this.field_149266_a[var1];
   }

   public int func_149253_b(int var1) {
      return this.field_149264_b[var1];
   }

   public int func_149254_d() {
      return this.field_149266_a.length;
   }

   public byte[] func_149256_c(int var1) {
      return this.field_149260_f[var1];
   }

   public String serialize() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.field_149266_a.length; ++var2) {
         if(var2 > 0) {
            var1.append(", ");
         }

         var1.append(String.format("{x=%d, z=%d, sections=%d, adds=%d, data=%d}", new Object[]{Integer.valueOf(this.field_149266_a[var2]), Integer.valueOf(this.field_149264_b[var2]), Integer.valueOf(this.field_149265_c[var2]), Integer.valueOf(this.field_149262_d[var2]), Integer.valueOf(this.field_149260_f[var2].length)}));
      }

      return String.format("size=%d, chunks=%d[%s]", new Object[]{Integer.valueOf(this.field_149261_g), Integer.valueOf(this.field_149266_a.length), var1});
   }

   public int[] func_149252_e() {
      return this.field_149265_c;
   }

   public int[] func_149257_f() {
      return this.field_149262_d;
   }

}
