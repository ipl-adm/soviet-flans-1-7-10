package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces$Start;
import net.minecraft.world.gen.structure.StructureVillagePieces$Village;

public class StructureVillagePieces$Field1 extends StructureVillagePieces$Village {

   private Block cropTypeA;
   private Block cropTypeB;
   private Block cropTypeC;
   private Block cropTypeD;


   public StructureVillagePieces$Field1() {}

   public StructureVillagePieces$Field1(StructureVillagePieces$Start var1, int var2, Random var3, StructureBoundingBox var4, int var5) {
      super(var1, var2);
      super.coordBaseMode = var5;
      super.boundingBox = var4;
      this.cropTypeA = this.func_151559_a(var3);
      this.cropTypeB = this.func_151559_a(var3);
      this.cropTypeC = this.func_151559_a(var3);
      this.cropTypeD = this.func_151559_a(var3);
   }

   protected void func_143012_a(NBTTagCompound var1) {
      super.func_143012_a(var1);
      var1.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
      var1.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
      var1.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
      var1.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
   }

   protected void func_143011_b(NBTTagCompound var1) {
      super.func_143011_b(var1);
      this.cropTypeA = Block.getBlockById(var1.getInteger("CA"));
      this.cropTypeB = Block.getBlockById(var1.getInteger("CB"));
      this.cropTypeC = Block.getBlockById(var1.getInteger("CC"));
      this.cropTypeD = Block.getBlockById(var1.getInteger("CD"));
   }

   private Block func_151559_a(Random var1) {
      switch(var1.nextInt(5)) {
      case 0:
         return Blocks.carrots;
      case 1:
         return Blocks.potatoes;
      default:
         return Blocks.wheat;
      }
   }

   public static StructureVillagePieces$Field1 func_74900_a(StructureVillagePieces$Start var0, List var1, Random var2, int var3, int var4, int var5, int var6, int var7) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 13, 4, 9, var6);
      return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null?new StructureVillagePieces$Field1(var0, var7, var2, var8, var6):null;
   }

   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if(super.field_143015_k < 0) {
         super.field_143015_k = this.getAverageGroundLevel(var1, var3);
         if(super.field_143015_k < 0) {
            return true;
         }

         super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 4 - 1, 0);
      }

      this.fillWithBlocks(var1, var3, 0, 1, 0, 12, 4, 8, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(var1, var3, 1, 0, 1, 2, 0, 7, Blocks.farmland, Blocks.farmland, false);
      this.fillWithBlocks(var1, var3, 4, 0, 1, 5, 0, 7, Blocks.farmland, Blocks.farmland, false);
      this.fillWithBlocks(var1, var3, 7, 0, 1, 8, 0, 7, Blocks.farmland, Blocks.farmland, false);
      this.fillWithBlocks(var1, var3, 10, 0, 1, 11, 0, 7, Blocks.farmland, Blocks.farmland, false);
      this.fillWithBlocks(var1, var3, 0, 0, 0, 0, 0, 8, Blocks.log, Blocks.log, false);
      this.fillWithBlocks(var1, var3, 6, 0, 0, 6, 0, 8, Blocks.log, Blocks.log, false);
      this.fillWithBlocks(var1, var3, 12, 0, 0, 12, 0, 8, Blocks.log, Blocks.log, false);
      this.fillWithBlocks(var1, var3, 1, 0, 0, 11, 0, 0, Blocks.log, Blocks.log, false);
      this.fillWithBlocks(var1, var3, 1, 0, 8, 11, 0, 8, Blocks.log, Blocks.log, false);
      this.fillWithBlocks(var1, var3, 3, 0, 1, 3, 0, 7, Blocks.water, Blocks.water, false);
      this.fillWithBlocks(var1, var3, 9, 0, 1, 9, 0, 7, Blocks.water, Blocks.water, false);

      int var4;
      for(var4 = 1; var4 <= 7; ++var4) {
         this.placeBlockAtCurrentPosition(var1, this.cropTypeA, MathHelper.getRandomIntegerInRange(var2, 2, 7), 1, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeA, MathHelper.getRandomIntegerInRange(var2, 2, 7), 2, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeB, MathHelper.getRandomIntegerInRange(var2, 2, 7), 4, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeB, MathHelper.getRandomIntegerInRange(var2, 2, 7), 5, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeC, MathHelper.getRandomIntegerInRange(var2, 2, 7), 7, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeC, MathHelper.getRandomIntegerInRange(var2, 2, 7), 8, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeD, MathHelper.getRandomIntegerInRange(var2, 2, 7), 10, 1, var4, var3);
         this.placeBlockAtCurrentPosition(var1, this.cropTypeD, MathHelper.getRandomIntegerInRange(var2, 2, 7), 11, 1, var4, var3);
      }

      for(var4 = 0; var4 < 9; ++var4) {
         for(int var5 = 0; var5 < 13; ++var5) {
            this.clearCurrentPositionBlocksUpwards(var1, var5, 4, var4, var3);
            this.func_151554_b(var1, Blocks.dirt, 0, var5, -1, var4, var3);
         }
      }

      return true;
   }
}
