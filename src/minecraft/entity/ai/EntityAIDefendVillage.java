package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget {

   EntityIronGolem irongolem;
   EntityLivingBase villageAgressorTarget;


   public EntityAIDefendVillage(EntityIronGolem var1) {
      super(var1, false, true);
      this.irongolem = var1;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      Village var1 = this.irongolem.getVillage();
      if(var1 == null) {
         return false;
      } else {
         this.villageAgressorTarget = var1.findNearestVillageAggressor(this.irongolem);
         if(!this.isSuitableTarget(this.villageAgressorTarget, false)) {
            if(super.taskOwner.getRNG().nextInt(20) == 0) {
               this.villageAgressorTarget = var1.func_82685_c(this.irongolem);
               return this.isSuitableTarget(this.villageAgressorTarget, false);
            } else {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public void startExecuting() {
      this.irongolem.setAttackTarget(this.villageAgressorTarget);
      super.startExecuting();
   }
}
