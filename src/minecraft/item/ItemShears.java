package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemShears extends Item {

   public ItemShears() {
      this.setMaxStackSize(1);
      this.setMaxDamage(238);
      this.setCreativeTab(CreativeTabs.tabTools);
   }

   public boolean onBlockDestroyed(ItemStack var1, World var2, Block var3, int var4, int var5, int var6, EntityLivingBase var7) {
      if(var3.getMaterial() != Material.leaves && var3 != Blocks.web && var3 != Blocks.tallgrass && var3 != Blocks.vine && var3 != Blocks.tripwire) {
         return super.onBlockDestroyed(var1, var2, var3, var4, var5, var6, var7);
      } else {
         var1.damageItem(1, var7);
         return true;
      }
   }

   public boolean func_150897_b(Block var1) {
      return var1 == Blocks.web || var1 == Blocks.redstone_wire || var1 == Blocks.tripwire;
   }

   public float func_150893_a(ItemStack var1, Block var2) {
      return var2 != Blocks.web && var2.getMaterial() != Material.leaves?(var2 == Blocks.wool?5.0F:super.func_150893_a(var1, var2)):15.0F;
   }
}
