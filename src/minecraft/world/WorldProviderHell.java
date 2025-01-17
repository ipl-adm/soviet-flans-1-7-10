package net.minecraft.world;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;

public class WorldProviderHell extends WorldProvider {

   public void registerWorldChunkManager() {
      super.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
      super.isHellWorld = true;
      super.hasNoSky = true;
      super.dimensionId = -1;
   }

   public Vec3 getFogColor(float var1, float var2) {
      return Vec3.createVectorHelper(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
   }

   protected void generateLightBrightnessTable() {
      float var1 = 0.1F;

      for(int var2 = 0; var2 <= 15; ++var2) {
         float var3 = 1.0F - (float)var2 / 15.0F;
         super.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
      }

   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderHell(super.worldObj, super.worldObj.getSeed());
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public boolean canCoordinateBeSpawn(int var1, int var2) {
      return false;
   }

   public float calculateCelestialAngle(long var1, float var3) {
      return 0.5F;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public boolean doesXZShowFog(int var1, int var2) {
      return true;
   }

   public String getDimensionName() {
      return "Nether";
   }
}
