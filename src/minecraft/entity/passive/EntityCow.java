package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCow extends EntityAnimal {

   public EntityCow(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
      super.tasks.addTask(2, new EntityAIMate(this, 1.0D));
      super.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.wheat, false));
      super.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
      super.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(7, new EntityAILookIdle(this));
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
   }

   protected String getLivingSound() {
      return "mob.cow.say";
   }

   protected String getHurtSound() {
      return "mob.cow.hurt";
   }

   protected String getDeathSound() {
      return "mob.cow.hurt";
   }

   protected void func_145780_a(int var1, int var2, int var3, Block var4) {
      this.playSound("mob.cow.step", 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   protected Item getDropItem() {
      return Items.leather;
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = super.rand.nextInt(3) + super.rand.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.leather, 1);
      }

      var3 = super.rand.nextInt(3) + 1 + super.rand.nextInt(1 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         if(this.isBurning()) {
            this.dropItem(Items.cooked_beef, 1);
         } else {
            this.dropItem(Items.beef, 1);
         }
      }

   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if(var2 != null && var2.getItem() == Items.bucket && !var1.capabilities.isCreativeMode) {
         if(var2.stackSize-- == 1) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(Items.milk_bucket));
         } else if(!var1.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
            var1.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
         }

         return true;
      } else {
         return super.interact(var1);
      }
   }

   public EntityCow createChild(EntityAgeable var1) {
      return new EntityCow(super.worldObj);
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }
}
