package de.ItsAMysterious.mods.reallifemod.core.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCabinet extends Container {

   private int numRows;
   private IInventory CabinetInventory;


   public ContainerCabinet(IInventory p_i1083_1_, IInventory CabinetInventory) {
      this.CabinetInventory = CabinetInventory;
      this.func_75146_a(new Slot(CabinetInventory, 0, 67, 4));
      this.func_75146_a(new Slot(CabinetInventory, 1, 85, 4));
      this.func_75146_a(new Slot(CabinetInventory, 2, 103, 4));
      this.func_75146_a(new Slot(CabinetInventory, 3, 67, 22));
      this.func_75146_a(new Slot(CabinetInventory, 4, 85, 22));
      this.func_75146_a(new Slot(CabinetInventory, 5, 103, 22));
      this.func_75146_a(new Slot(CabinetInventory, 6, 67, 40));
      this.func_75146_a(new Slot(CabinetInventory, 7, 85, 40));
      this.func_75146_a(new Slot(CabinetInventory, 8, 103, 40));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(p_i1083_1_, j + i * 9 + 9, 13 + j * 18, 70 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(p_i1083_1_, i, 13 + i * 18, 128));
      }

   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.CabinetInventory.isUseableByPlayer(player);
   }

   public ItemStack func_82846_b(EntityPlayer p_82846_1_, int p_82846_2_) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(p_82846_2_);
      if(slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if(p_82846_2_ < this.numRows * 3) {
            if(!this.func_75135_a(itemstack1, this.numRows * 3, this.field_75151_b.size(), true)) {
               return null;
            }
         } else if(!this.func_75135_a(itemstack1, 0, this.numRows * 3, false)) {
            return null;
         }

         if(itemstack1.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }
}
