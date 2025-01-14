package net.minecraft.world;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

public class WorldProviderEnd extends WorldProvider {

   public void registerWorldChunkManager() {
      super.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
      super.dimensionId = 1;
      super.hasNoSky = true;
   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderEnd(super.worldObj, super.worldObj.getSeed());
   }

   public float calculateCelestialAngle(long var1, float var3) {
      return 0.0F;
   }

   public float[] calcSunriseSunsetColors(float var1, float var2) {
      return null;
   }

   public Vec3 getFogColor(float var1, float var2) {
      int var3 = 10518688;
      float var4 = MathHelper.cos(var1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      if(var4 < 0.0F) {
         var4 = 0.0F;
      }

      if(var4 > 1.0F) {
         var4 = 1.0F;
      }

      float var5 = (float)(var3 >> 16 & 255) / 255.0F;
      float var6 = (float)(var3 >> 8 & 255) / 255.0F;
      float var7 = (float)(var3 & 255) / 255.0F;
      var5 *= var4 * 0.0F + 0.15F;
      var6 *= var4 * 0.0F + 0.15F;
      var7 *= var4 * 0.0F + 0.15F;
      return Vec3.createVectorHelper((double)var5, (double)var6, (double)var7);
   }

   public boolean isSkyColored() {
      return false;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public float getCloudHeight() {
      return 8.0F;
   }

   public boolean canCoordinateBeSpawn(int var1, int var2) {
      return super.worldObj.getTopBlock(var1, var2).getMaterial().blocksMovement();
   }

   public ChunkCoordinates getEntrancePortalLocation() {
      return new ChunkCoordinates(100, 50, 0);
   }

   public int getAverageGroundLevel() {
      return 50;
   }

   public boolean doesXZShowFog(int var1, int var2) {
      return true;
   }

   public String getDimensionName() {
      return "The End";
   }
}
