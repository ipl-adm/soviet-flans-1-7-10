package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEgg extends Item {

   public ItemEgg() {
      super.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.tabMaterials);
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      if(!var3.capabilities.isCreativeMode) {
         --var1.stackSize;
      }

      var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
      if(!var2.isRemote) {
         var2.spawnEntityInWorld(new EntityEgg(var2, var3));
      }

      return var1;
   }
}
