package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S2CPacketSpawnGlobalEntity extends Packet {

   private int field_149059_a;
   private int field_149057_b;
   private int field_149058_c;
   private int field_149055_d;
   private int field_149056_e;


   public S2CPacketSpawnGlobalEntity() {}

   public S2CPacketSpawnGlobalEntity(Entity var1) {
      this.field_149059_a = var1.getEntityId();
      this.field_149057_b = MathHelper.floor_double(var1.posX * 32.0D);
      this.field_149058_c = MathHelper.floor_double(var1.posY * 32.0D);
      this.field_149055_d = MathHelper.floor_double(var1.posZ * 32.0D);
      if(var1 instanceof EntityLightningBolt) {
         this.field_149056_e = 1;
      }

   }

   public void readPacketData(PacketBuffer var1) {
      this.field_149059_a = var1.readVarIntFromBuffer();
      this.field_149056_e = var1.readByte();
      this.field_149057_b = var1.readInt();
      this.field_149058_c = var1.readInt();
      this.field_149055_d = var1.readInt();
   }

   public void writePacketData(PacketBuffer var1) {
      var1.writeVarIntToBuffer(this.field_149059_a);
      var1.writeByte(this.field_149056_e);
      var1.writeInt(this.field_149057_b);
      var1.writeInt(this.field_149058_c);
      var1.writeInt(this.field_149055_d);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSpawnGlobalEntity(this);
   }

   public String serialize() {
      return String.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f", new Object[]{Integer.valueOf(this.field_149059_a), Integer.valueOf(this.field_149056_e), Float.valueOf((float)this.field_149057_b / 32.0F), Float.valueOf((float)this.field_149058_c / 32.0F), Float.valueOf((float)this.field_149055_d / 32.0F)});
   }

   public int func_149052_c() {
      return this.field_149059_a;
   }

   public int func_149051_d() {
      return this.field_149057_b;
   }

   public int func_149050_e() {
      return this.field_149058_c;
   }

   public int func_149049_f() {
      return this.field_149055_d;
   }

   public int func_149053_g() {
      return this.field_149056_e;
   }
}
