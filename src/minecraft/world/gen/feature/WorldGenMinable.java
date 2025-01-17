package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMinable extends WorldGenerator {

   private Block field_150519_a;
   private int numberOfBlocks;
   private Block field_150518_c;


   public WorldGenMinable(Block var1, int var2) {
      this(var1, var2, Blocks.stone);
   }

   public WorldGenMinable(Block var1, int var2, Block var3) {
      this.field_150519_a = var1;
      this.numberOfBlocks = var2;
      this.field_150518_c = var3;
   }

   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      float var6 = var2.nextFloat() * 3.1415927F;
      double var7 = (double)((float)(var3 + 8) + MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
      double var9 = (double)((float)(var3 + 8) - MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
      double var11 = (double)((float)(var5 + 8) + MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
      double var13 = (double)((float)(var5 + 8) - MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
      double var15 = (double)(var4 + var2.nextInt(3) - 2);
      double var17 = (double)(var4 + var2.nextInt(3) - 2);

      for(int var19 = 0; var19 <= this.numberOfBlocks; ++var19) {
         double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.numberOfBlocks;
         double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.numberOfBlocks;
         double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.numberOfBlocks;
         double var26 = var2.nextDouble() * (double)this.numberOfBlocks / 16.0D;
         double var28 = (double)(MathHelper.sin((float)var19 * 3.1415927F / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
         double var30 = (double)(MathHelper.sin((float)var19 * 3.1415927F / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
         int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
         int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
         int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
         int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
         int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
         int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);

         for(int var38 = var32; var38 <= var35; ++var38) {
            double var39 = ((double)var38 + 0.5D - var20) / (var28 / 2.0D);
            if(var39 * var39 < 1.0D) {
               for(int var41 = var33; var41 <= var36; ++var41) {
                  double var42 = ((double)var41 + 0.5D - var22) / (var30 / 2.0D);
                  if(var39 * var39 + var42 * var42 < 1.0D) {
                     for(int var44 = var34; var44 <= var37; ++var44) {
                        double var45 = ((double)var44 + 0.5D - var24) / (var28 / 2.0D);
                        if(var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && var1.getBlock(var38, var41, var44) == this.field_150518_c) {
                           var1.setBlock(var38, var41, var44, this.field_150519_a, 0, 2);
                        }
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}
