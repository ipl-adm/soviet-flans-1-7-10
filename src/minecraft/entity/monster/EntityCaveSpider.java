package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider {

   public EntityCaveSpider(World var1) {
      super(var1);
      this.setSize(0.7F, 0.5F);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
   }

   public boolean attackEntityAsMob(Entity var1) {
      if(super.attackEntityAsMob(var1)) {
         if(var1 instanceof EntityLivingBase) {
            byte var2 = 0;
            if(super.worldObj.difficultySetting == EnumDifficulty.NORMAL) {
               var2 = 7;
            } else if(super.worldObj.difficultySetting == EnumDifficulty.HARD) {
               var2 = 15;
            }

            if(var2 > 0) {
               ((EntityLivingBase)var1).addPotionEffect(new PotionEffect(Potion.poison.id, var2 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData var1) {
      return var1;
   }
}
