package net.minecraft.world.gen.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStructure$1;
import net.minecraft.world.gen.structure.MapGenStructure$2;
import net.minecraft.world.gen.structure.MapGenStructure$3;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public abstract class MapGenStructure extends MapGenBase {

   private MapGenStructureData field_143029_e;
   protected Map structureMap = new HashMap();


   public abstract String func_143025_a();

   protected final void func_151538_a(World var1, int var2, int var3, int var4, int var5, Block[] var6) {
      this.func_143027_a(var1);
      if(!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2, var3)))) {
         super.rand.nextInt();

         try {
            if(this.canSpawnStructureAtCoords(var2, var3)) {
               StructureStart var7 = this.getStructureStart(var2, var3);
               this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2, var3)), var7);
               this.func_143026_a(var2, var3, var7);
            }

         } catch (Throwable var10) {
            CrashReport var8 = CrashReport.makeCrashReport(var10, "Exception preparing structure feature");
            CrashReportCategory var9 = var8.makeCategory("Feature being prepared");
            var9.addCrashSectionCallable("Is feature chunk", new MapGenStructure$1(this, var2, var3));
            var9.addCrashSection("Chunk location", String.format("%d,%d", new Object[]{Integer.valueOf(var2), Integer.valueOf(var3)}));
            var9.addCrashSectionCallable("Chunk pos hash", new MapGenStructure$2(this, var2, var3));
            var9.addCrashSectionCallable("Structure type", new MapGenStructure$3(this));
            throw new ReportedException(var8);
         }
      }
   }

   public boolean generateStructuresInChunk(World var1, Random var2, int var3, int var4) {
      this.func_143027_a(var1);
      int var5 = (var3 << 4) + 8;
      int var6 = (var4 << 4) + 8;
      boolean var7 = false;
      Iterator var8 = this.structureMap.values().iterator();

      while(var8.hasNext()) {
         StructureStart var9 = (StructureStart)var8.next();
         if(var9.isSizeableStructure() && var9.getBoundingBox().intersectsWith(var5, var6, var5 + 15, var6 + 15)) {
            var9.generateStructure(var1, var2, new StructureBoundingBox(var5, var6, var5 + 15, var6 + 15));
            var7 = true;
            this.func_143026_a(var9.func_143019_e(), var9.func_143018_f(), var9);
         }
      }

      return var7;
   }

   public boolean hasStructureAt(int var1, int var2, int var3) {
      this.func_143027_a(super.worldObj);
      return this.func_143028_c(var1, var2, var3) != null;
   }

   protected StructureStart func_143028_c(int var1, int var2, int var3) {
      Iterator var4 = this.structureMap.values().iterator();

      while(var4.hasNext()) {
         StructureStart var5 = (StructureStart)var4.next();
         if(var5.isSizeableStructure() && var5.getBoundingBox().intersectsWith(var1, var3, var1, var3)) {
            Iterator var6 = var5.getComponents().iterator();

            while(var6.hasNext()) {
               StructureComponent var7 = (StructureComponent)var6.next();
               if(var7.getBoundingBox().isVecInside(var1, var2, var3)) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public boolean func_142038_b(int var1, int var2, int var3) {
      this.func_143027_a(super.worldObj);
      Iterator var4 = this.structureMap.values().iterator();

      StructureStart var5;
      do {
         if(!var4.hasNext()) {
            return false;
         }

         var5 = (StructureStart)var4.next();
      } while(!var5.isSizeableStructure());

      return var5.getBoundingBox().intersectsWith(var1, var3, var1, var3);
   }

   public ChunkPosition func_151545_a(World var1, int var2, int var3, int var4) {
      super.worldObj = var1;
      this.func_143027_a(var1);
      super.rand.setSeed(var1.getSeed());
      long var5 = super.rand.nextLong();
      long var7 = super.rand.nextLong();
      long var9 = (long)(var2 >> 4) * var5;
      long var11 = (long)(var4 >> 4) * var7;
      super.rand.setSeed(var9 ^ var11 ^ var1.getSeed());
      this.func_151538_a(var1, var2 >> 4, var4 >> 4, 0, 0, (Block[])null);
      double var13 = Double.MAX_VALUE;
      ChunkPosition var15 = null;
      Iterator var16 = this.structureMap.values().iterator();

      ChunkPosition var19;
      int var20;
      int var21;
      int var22;
      double var23;
      while(var16.hasNext()) {
         StructureStart var17 = (StructureStart)var16.next();
         if(var17.isSizeableStructure()) {
            StructureComponent var18 = (StructureComponent)var17.getComponents().get(0);
            var19 = var18.func_151553_a();
            var20 = var19.chunkPosX - var2;
            var21 = var19.chunkPosY - var3;
            var22 = var19.chunkPosZ - var4;
            var23 = (double)(var20 * var20 + var21 * var21 + var22 * var22);
            if(var23 < var13) {
               var13 = var23;
               var15 = var19;
            }
         }
      }

      if(var15 != null) {
         return var15;
      } else {
         List var25 = this.getCoordList();
         if(var25 != null) {
            ChunkPosition var26 = null;
            Iterator var27 = var25.iterator();

            while(var27.hasNext()) {
               var19 = (ChunkPosition)var27.next();
               var20 = var19.chunkPosX - var2;
               var21 = var19.chunkPosY - var3;
               var22 = var19.chunkPosZ - var4;
               var23 = (double)(var20 * var20 + var21 * var21 + var22 * var22);
               if(var23 < var13) {
                  var13 = var23;
                  var26 = var19;
               }
            }

            return var26;
         } else {
            return null;
         }
      }
   }

   protected List getCoordList() {
      return null;
   }

   private void func_143027_a(World var1) {
      if(this.field_143029_e == null) {
         this.field_143029_e = (MapGenStructureData)var1.loadItemData(MapGenStructureData.class, this.func_143025_a());
         if(this.field_143029_e == null) {
            this.field_143029_e = new MapGenStructureData(this.func_143025_a());
            var1.setItemData(this.func_143025_a(), this.field_143029_e);
         } else {
            NBTTagCompound var2 = this.field_143029_e.func_143041_a();
            Iterator var3 = var2.func_150296_c().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               NBTBase var5 = var2.getTag(var4);
               if(var5.getId() == 10) {
                  NBTTagCompound var6 = (NBTTagCompound)var5;
                  if(var6.hasKey("ChunkX") && var6.hasKey("ChunkZ")) {
                     int var7 = var6.getInteger("ChunkX");
                     int var8 = var6.getInteger("ChunkZ");
                     StructureStart var9 = MapGenStructureIO.func_143035_a(var6, var1);
                     if(var9 != null) {
                        this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var7, var8)), var9);
                     }
                  }
               }
            }
         }
      }

   }

   private void func_143026_a(int var1, int var2, StructureStart var3) {
      this.field_143029_e.func_143043_a(var3.func_143021_a(var1, var2), var1, var2);
      this.field_143029_e.markDirty();
   }

   protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

   protected abstract StructureStart getStructureStart(int var1, int var2);
}
