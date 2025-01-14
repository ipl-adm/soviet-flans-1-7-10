package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

public class PacketBuffer extends ByteBuf {

   private final ByteBuf field_150794_a;


   public PacketBuffer(ByteBuf var1) {
      this.field_150794_a = var1;
   }

   public static int getVarIntSize(int var0) {
      return (var0 & -128) == 0?1:((var0 & -16384) == 0?2:((var0 & -2097152) == 0?3:((var0 & -268435456) == 0?4:5)));
   }

   public int readVarIntFromBuffer() {
      int var1 = 0;
      int var2 = 0;

      byte var3;
      do {
         var3 = this.readByte();
         var1 |= (var3 & 127) << var2++ * 7;
         if(var2 > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while((var3 & 128) == 128);

      return var1;
   }

   public void writeVarIntToBuffer(int var1) {
      while((var1 & -128) != 0) {
         this.writeByte(var1 & 127 | 128);
         var1 >>>= 7;
      }

      this.writeByte(var1);
   }

   public void writeNBTTagCompoundToBuffer(NBTTagCompound var1) {
      if(var1 == null) {
         this.writeShort(-1);
      } else {
         byte[] var2 = CompressedStreamTools.compress(var1);
         this.writeShort((short)var2.length);
         this.writeBytes(var2);
      }

   }

   public NBTTagCompound readNBTTagCompoundFromBuffer() {
      short var1 = this.readShort();
      if(var1 < 0) {
         return null;
      } else {
         byte[] var2 = new byte[var1];
         this.readBytes(var2);
         return CompressedStreamTools.func_152457_a(var2, new NBTSizeTracker(2097152L));
      }
   }

   public void writeItemStackToBuffer(ItemStack var1) {
      if(var1 == null) {
         this.writeShort(-1);
      } else {
         this.writeShort(Item.getIdFromItem(var1.getItem()));
         this.writeByte(var1.stackSize);
         this.writeShort(var1.getItemDamage());
         NBTTagCompound var2 = null;
         if(var1.getItem().isDamageable() || var1.getItem().getShareTag()) {
            var2 = var1.stackTagCompound;
         }

         this.writeNBTTagCompoundToBuffer(var2);
      }

   }

   public ItemStack readItemStackFromBuffer() {
      ItemStack var1 = null;
      short var2 = this.readShort();
      if(var2 >= 0) {
         byte var3 = this.readByte();
         short var4 = this.readShort();
         var1 = new ItemStack(Item.getItemById(var2), var3, var4);
         var1.stackTagCompound = this.readNBTTagCompoundFromBuffer();
      }

      return var1;
   }

   public String readStringFromBuffer(int var1) {
      int var2 = this.readVarIntFromBuffer();
      if(var2 > var1 * 4) {
         throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + var2 + " > " + var1 * 4 + ")");
      } else if(var2 < 0) {
         throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
      } else {
         String var3 = new String(this.readBytes(var2).array(), Charsets.UTF_8);
         if(var3.length() > var1) {
            throw new IOException("The received string length is longer than maximum allowed (" + var2 + " > " + var1 + ")");
         } else {
            return var3;
         }
      }
   }

   public void writeStringToBuffer(String var1) {
      byte[] var2 = var1.getBytes(Charsets.UTF_8);
      if(var2.length > 32767) {
         throw new IOException("String too big (was " + var1.length() + " bytes encoded, max " + 32767 + ")");
      } else {
         this.writeVarIntToBuffer(var2.length);
         this.writeBytes(var2);
      }
   }

   public int capacity() {
      return this.field_150794_a.capacity();
   }

   public ByteBuf capacity(int var1) {
      return this.field_150794_a.capacity(var1);
   }

   public int maxCapacity() {
      return this.field_150794_a.maxCapacity();
   }

   public ByteBufAllocator alloc() {
      return this.field_150794_a.alloc();
   }

   public ByteOrder order() {
      return this.field_150794_a.order();
   }

   public ByteBuf order(ByteOrder var1) {
      return this.field_150794_a.order(var1);
   }

   public ByteBuf unwrap() {
      return this.field_150794_a.unwrap();
   }

   public boolean isDirect() {
      return this.field_150794_a.isDirect();
   }

   public int readerIndex() {
      return this.field_150794_a.readerIndex();
   }

   public ByteBuf readerIndex(int var1) {
      return this.field_150794_a.readerIndex(var1);
   }

   public int writerIndex() {
      return this.field_150794_a.writerIndex();
   }

   public ByteBuf writerIndex(int var1) {
      return this.field_150794_a.writerIndex(var1);
   }

   public ByteBuf setIndex(int var1, int var2) {
      return this.field_150794_a.setIndex(var1, var2);
   }

   public int readableBytes() {
      return this.field_150794_a.readableBytes();
   }

   public int writableBytes() {
      return this.field_150794_a.writableBytes();
   }

   public int maxWritableBytes() {
      return this.field_150794_a.maxWritableBytes();
   }

   public boolean isReadable() {
      return this.field_150794_a.isReadable();
   }

   public boolean isReadable(int var1) {
      return this.field_150794_a.isReadable(var1);
   }

   public boolean isWritable() {
      return this.field_150794_a.isWritable();
   }

   public boolean isWritable(int var1) {
      return this.field_150794_a.isWritable(var1);
   }

   public ByteBuf clear() {
      return this.field_150794_a.clear();
   }

   public ByteBuf markReaderIndex() {
      return this.field_150794_a.markReaderIndex();
   }

   public ByteBuf resetReaderIndex() {
      return this.field_150794_a.resetReaderIndex();
   }

   public ByteBuf markWriterIndex() {
      return this.field_150794_a.markWriterIndex();
   }

   public ByteBuf resetWriterIndex() {
      return this.field_150794_a.resetWriterIndex();
   }

   public ByteBuf discardReadBytes() {
      return this.field_150794_a.discardReadBytes();
   }

   public ByteBuf discardSomeReadBytes() {
      return this.field_150794_a.discardSomeReadBytes();
   }

   public ByteBuf ensureWritable(int var1) {
      return this.field_150794_a.ensureWritable(var1);
   }

   public int ensureWritable(int var1, boolean var2) {
      return this.field_150794_a.ensureWritable(var1, var2);
   }

   public boolean getBoolean(int var1) {
      return this.field_150794_a.getBoolean(var1);
   }

   public byte getByte(int var1) {
      return this.field_150794_a.getByte(var1);
   }

   public short getUnsignedByte(int var1) {
      return this.field_150794_a.getUnsignedByte(var1);
   }

   public short getShort(int var1) {
      return this.field_150794_a.getShort(var1);
   }

   public int getUnsignedShort(int var1) {
      return this.field_150794_a.getUnsignedShort(var1);
   }

   public int getMedium(int var1) {
      return this.field_150794_a.getMedium(var1);
   }

   public int getUnsignedMedium(int var1) {
      return this.field_150794_a.getUnsignedMedium(var1);
   }

   public int getInt(int var1) {
      return this.field_150794_a.getInt(var1);
   }

   public long getUnsignedInt(int var1) {
      return this.field_150794_a.getUnsignedInt(var1);
   }

   public long getLong(int var1) {
      return this.field_150794_a.getLong(var1);
   }

   public char getChar(int var1) {
      return this.field_150794_a.getChar(var1);
   }

   public float getFloat(int var1) {
      return this.field_150794_a.getFloat(var1);
   }

   public double getDouble(int var1) {
      return this.field_150794_a.getDouble(var1);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2) {
      return this.field_150794_a.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3) {
      return this.field_150794_a.getBytes(var1, var2, var3);
   }

   public ByteBuf getBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.field_150794_a.getBytes(var1, var2, var3, var4);
   }

   public ByteBuf getBytes(int var1, byte[] var2) {
      return this.field_150794_a.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, byte[] var2, int var3, int var4) {
      return this.field_150794_a.getBytes(var1, var2, var3, var4);
   }

   public ByteBuf getBytes(int var1, ByteBuffer var2) {
      return this.field_150794_a.getBytes(var1, var2);
   }

   public ByteBuf getBytes(int var1, OutputStream var2, int var3) {
      return this.field_150794_a.getBytes(var1, var2, var3);
   }

   public int getBytes(int var1, GatheringByteChannel var2, int var3) {
      return this.field_150794_a.getBytes(var1, var2, var3);
   }

   public ByteBuf setBoolean(int var1, boolean var2) {
      return this.field_150794_a.setBoolean(var1, var2);
   }

   public ByteBuf setByte(int var1, int var2) {
      return this.field_150794_a.setByte(var1, var2);
   }

   public ByteBuf setShort(int var1, int var2) {
      return this.field_150794_a.setShort(var1, var2);
   }

   public ByteBuf setMedium(int var1, int var2) {
      return this.field_150794_a.setMedium(var1, var2);
   }

   public ByteBuf setInt(int var1, int var2) {
      return this.field_150794_a.setInt(var1, var2);
   }

   public ByteBuf setLong(int var1, long var2) {
      return this.field_150794_a.setLong(var1, var2);
   }

   public ByteBuf setChar(int var1, int var2) {
      return this.field_150794_a.setChar(var1, var2);
   }

   public ByteBuf setFloat(int var1, float var2) {
      return this.field_150794_a.setFloat(var1, var2);
   }

   public ByteBuf setDouble(int var1, double var2) {
      return this.field_150794_a.setDouble(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2) {
      return this.field_150794_a.setBytes(var1, var2);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3) {
      return this.field_150794_a.setBytes(var1, var2, var3);
   }

   public ByteBuf setBytes(int var1, ByteBuf var2, int var3, int var4) {
      return this.field_150794_a.setBytes(var1, var2, var3, var4);
   }

   public ByteBuf setBytes(int var1, byte[] var2) {
      return this.field_150794_a.setBytes(var1, var2);
   }

   public ByteBuf setBytes(int var1, byte[] var2, int var3, int var4) {
      return this.field_150794_a.setBytes(var1, var2, var3, var4);
   }

   public ByteBuf setBytes(int var1, ByteBuffer var2) {
      return this.field_150794_a.setBytes(var1, var2);
   }

   public int setBytes(int var1, InputStream var2, int var3) {
      return this.field_150794_a.setBytes(var1, var2, var3);
   }

   public int setBytes(int var1, ScatteringByteChannel var2, int var3) {
      return this.field_150794_a.setBytes(var1, var2, var3);
   }

   public ByteBuf setZero(int var1, int var2) {
      return this.field_150794_a.setZero(var1, var2);
   }

   public boolean readBoolean() {
      return this.field_150794_a.readBoolean();
   }

   public byte readByte() {
      return this.field_150794_a.readByte();
   }

   public short readUnsignedByte() {
      return this.field_150794_a.readUnsignedByte();
   }

   public short readShort() {
      return this.field_150794_a.readShort();
   }

   public int readUnsignedShort() {
      return this.field_150794_a.readUnsignedShort();
   }

   public int readMedium() {
      return this.field_150794_a.readMedium();
   }

   public int readUnsignedMedium() {
      return this.field_150794_a.readUnsignedMedium();
   }

   public int readInt() {
      return this.field_150794_a.readInt();
   }

   public long readUnsignedInt() {
      return this.field_150794_a.readUnsignedInt();
   }

   public long readLong() {
      return this.field_150794_a.readLong();
   }

   public char readChar() {
      return this.field_150794_a.readChar();
   }

   public float readFloat() {
      return this.field_150794_a.readFloat();
   }

   public double readDouble() {
      return this.field_150794_a.readDouble();
   }

   public ByteBuf readBytes(int var1) {
      return this.field_150794_a.readBytes(var1);
   }

   public ByteBuf readSlice(int var1) {
      return this.field_150794_a.readSlice(var1);
   }

   public ByteBuf readBytes(ByteBuf var1) {
      return this.field_150794_a.readBytes(var1);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2) {
      return this.field_150794_a.readBytes(var1, var2);
   }

   public ByteBuf readBytes(ByteBuf var1, int var2, int var3) {
      return this.field_150794_a.readBytes(var1, var2, var3);
   }

   public ByteBuf readBytes(byte[] var1) {
      return this.field_150794_a.readBytes(var1);
   }

   public ByteBuf readBytes(byte[] var1, int var2, int var3) {
      return this.field_150794_a.readBytes(var1, var2, var3);
   }

   public ByteBuf readBytes(ByteBuffer var1) {
      return this.field_150794_a.readBytes(var1);
   }

   public ByteBuf readBytes(OutputStream var1, int var2) {
      return this.field_150794_a.readBytes(var1, var2);
   }

   public int readBytes(GatheringByteChannel var1, int var2) {
      return this.field_150794_a.readBytes(var1, var2);
   }

   public ByteBuf skipBytes(int var1) {
      return this.field_150794_a.skipBytes(var1);
   }

   public ByteBuf writeBoolean(boolean var1) {
      return this.field_150794_a.writeBoolean(var1);
   }

   public ByteBuf writeByte(int var1) {
      return this.field_150794_a.writeByte(var1);
   }

   public ByteBuf writeShort(int var1) {
      return this.field_150794_a.writeShort(var1);
   }

   public ByteBuf writeMedium(int var1) {
      return this.field_150794_a.writeMedium(var1);
   }

   public ByteBuf writeInt(int var1) {
      return this.field_150794_a.writeInt(var1);
   }

   public ByteBuf writeLong(long var1) {
      return this.field_150794_a.writeLong(var1);
   }

   public ByteBuf writeChar(int var1) {
      return this.field_150794_a.writeChar(var1);
   }

   public ByteBuf writeFloat(float var1) {
      return this.field_150794_a.writeFloat(var1);
   }

   public ByteBuf writeDouble(double var1) {
      return this.field_150794_a.writeDouble(var1);
   }

   public ByteBuf writeBytes(ByteBuf var1) {
      return this.field_150794_a.writeBytes(var1);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2) {
      return this.field_150794_a.writeBytes(var1, var2);
   }

   public ByteBuf writeBytes(ByteBuf var1, int var2, int var3) {
      return this.field_150794_a.writeBytes(var1, var2, var3);
   }

   public ByteBuf writeBytes(byte[] var1) {
      return this.field_150794_a.writeBytes(var1);
   }

   public ByteBuf writeBytes(byte[] var1, int var2, int var3) {
      return this.field_150794_a.writeBytes(var1, var2, var3);
   }

   public ByteBuf writeBytes(ByteBuffer var1) {
      return this.field_150794_a.writeBytes(var1);
   }

   public int writeBytes(InputStream var1, int var2) {
      return this.field_150794_a.writeBytes(var1, var2);
   }

   public int writeBytes(ScatteringByteChannel var1, int var2) {
      return this.field_150794_a.writeBytes(var1, var2);
   }

   public ByteBuf writeZero(int var1) {
      return this.field_150794_a.writeZero(var1);
   }

   public int indexOf(int var1, int var2, byte var3) {
      return this.field_150794_a.indexOf(var1, var2, var3);
   }

   public int bytesBefore(byte var1) {
      return this.field_150794_a.bytesBefore(var1);
   }

   public int bytesBefore(int var1, byte var2) {
      return this.field_150794_a.bytesBefore(var1, var2);
   }

   public int bytesBefore(int var1, int var2, byte var3) {
      return this.field_150794_a.bytesBefore(var1, var2, var3);
   }

   public int forEachByte(ByteBufProcessor var1) {
      return this.field_150794_a.forEachByte(var1);
   }

   public int forEachByte(int var1, int var2, ByteBufProcessor var3) {
      return this.field_150794_a.forEachByte(var1, var2, var3);
   }

   public int forEachByteDesc(ByteBufProcessor var1) {
      return this.field_150794_a.forEachByteDesc(var1);
   }

   public int forEachByteDesc(int var1, int var2, ByteBufProcessor var3) {
      return this.field_150794_a.forEachByteDesc(var1, var2, var3);
   }

   public ByteBuf copy() {
      return this.field_150794_a.copy();
   }

   public ByteBuf copy(int var1, int var2) {
      return this.field_150794_a.copy(var1, var2);
   }

   public ByteBuf slice() {
      return this.field_150794_a.slice();
   }

   public ByteBuf slice(int var1, int var2) {
      return this.field_150794_a.slice(var1, var2);
   }

   public ByteBuf duplicate() {
      return this.field_150794_a.duplicate();
   }

   public int nioBufferCount() {
      return this.field_150794_a.nioBufferCount();
   }

   public ByteBuffer nioBuffer() {
      return this.field_150794_a.nioBuffer();
   }

   public ByteBuffer nioBuffer(int var1, int var2) {
      return this.field_150794_a.nioBuffer(var1, var2);
   }

   public ByteBuffer internalNioBuffer(int var1, int var2) {
      return this.field_150794_a.internalNioBuffer(var1, var2);
   }

   public ByteBuffer[] nioBuffers() {
      return this.field_150794_a.nioBuffers();
   }

   public ByteBuffer[] nioBuffers(int var1, int var2) {
      return this.field_150794_a.nioBuffers(var1, var2);
   }

   public boolean hasArray() {
      return this.field_150794_a.hasArray();
   }

   public byte[] array() {
      return this.field_150794_a.array();
   }

   public int arrayOffset() {
      return this.field_150794_a.arrayOffset();
   }

   public boolean hasMemoryAddress() {
      return this.field_150794_a.hasMemoryAddress();
   }

   public long memoryAddress() {
      return this.field_150794_a.memoryAddress();
   }

   public String toString(Charset var1) {
      return this.field_150794_a.toString(var1);
   }

   public String toString(int var1, int var2, Charset var3) {
      return this.field_150794_a.toString(var1, var2, var3);
   }

   public int hashCode() {
      return this.field_150794_a.hashCode();
   }

   public boolean equals(Object var1) {
      return this.field_150794_a.equals(var1);
   }

   public int compareTo(ByteBuf var1) {
      return this.field_150794_a.compareTo(var1);
   }

   public String toString() {
      return this.field_150794_a.toString();
   }

   public ByteBuf retain(int var1) {
      return this.field_150794_a.retain(var1);
   }

   public ByteBuf retain() {
      return this.field_150794_a.retain();
   }

   public int refCnt() {
      return this.field_150794_a.refCnt();
   }

   public boolean release() {
      return this.field_150794_a.release();
   }

   public boolean release(int var1) {
      return this.field_150794_a.release(var1);
   }
}
