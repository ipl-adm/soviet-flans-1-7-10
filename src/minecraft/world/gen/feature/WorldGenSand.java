package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSand extends WorldGenerator {

   private Block field_150517_a;
   private int radius;


   public WorldGenSand(Block var1, int var2) {
      this.field_150517_a = var1;
      this.radius = var2;
   }

   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      if(var1.getBlock(var3, var4, var5).getMaterial() != Material.water) {
         return false;
      } else {
         int var6 = var2.nextInt(this.radius - 2) + 2;
         byte var7 = 2;

         for(int var8 = var3 - var6; var8 <= var3 + var6; ++var8) {
            for(int var9 = var5 - var6; var9 <= var5 + var6; ++var9) {
               int var10 = var8 - var3;
               int var11 = var9 - var5;
               if(var10 * var10 + var11 * var11 <= var6 * var6) {
                  for(int var12 = var4 - var7; var12 <= var4 + var7; ++var12) {
                     Block var13 = var1.getBlock(var8, var12, var9);
                     if(var13 == Blocks.dirt || var13 == Blocks.grass) {
                        var1.setBlock(var8, var12, var9, this.field_150517_a, 0, 2);
                     }
                  }
               }
            }
         }

         return true;
      }
   }
}
