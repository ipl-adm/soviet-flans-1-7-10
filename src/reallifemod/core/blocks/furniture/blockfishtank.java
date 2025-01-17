package de.ItsAMysterious.mods.reallifemod.core.blocks.furniture;

import de.ItsAMysterious.mods.reallifemod.api.util.Materials;
import de.ItsAMysterious.mods.reallifemod.core.tiles.FishtankTE;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class blockfishtank extends BlockContainer {

   public blockfishtank() {
      super(Materials.plastic);
      this.func_149663_c("fishtank");
      this.func_149658_d("reallifemod:fishtank");
   }

   public void func_149719_a(IBlockAccess world, int x, int y, int z) {
      super.func_149719_a(world, x, y, z);
      switch(world.getBlockMetadata(x, y, z)) {
      case 0:
         this.func_149676_a(-0.5F, 0.0F, 0.0F, 1.5F, 1.0F, 1.0F);
         break;
      case 1:
         this.func_149676_a(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.5F);
         break;
      case 2:
         this.func_149676_a(-0.5F, 0.0F, 0.0F, 1.5F, 1.0F, 1.0F);
         break;
      case 3:
         this.func_149676_a(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.5F);
      }

   }

   public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_) {
      return new FishtankTE();
   }

   public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer player, int par1, float par2, float par3, float par4) {
      ((FishtankTE)world.getTileEntity(x, y, z)).isFilled = !((FishtankTE)world.getTileEntity(x, y, z)).isFilled;
      world.markBlockForUpdate(x, y, z);
      return true;
   }

   public int func_149645_b() {
      return -1;
   }

   public boolean func_149662_c() {
      return false;
   }

   public boolean func_149686_d() {
      return false;
   }

   public void func_149689_a(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      if(entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(player != null && world != null) {
            int le = MathHelper.floor_double((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
            world.setBlockMetadataWithNotify(x, y, z, le, 2);
         }

         world.markBlockForUpdate(x, y, z);
      }

   }
}
