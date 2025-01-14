package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsTarget extends EntityAIBase {

   private EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private double speed;
   private float maxTargetDistance;


   public EntityAIMoveTowardsTarget(EntityCreature var1, double var2, float var4) {
      this.theEntity = var1;
      this.speed = var2;
      this.maxTargetDistance = var4;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      this.targetEntity = this.theEntity.getAttackTarget();
      if(this.targetEntity == null) {
         return false;
      } else if(this.targetEntity.getDistanceSqToEntity(this.theEntity) > (double)(this.maxTargetDistance * this.maxTargetDistance)) {
         return false;
      } else {
         Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, Vec3.createVectorHelper(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
         if(var1 == null) {
            return false;
         } else {
            this.movePosX = var1.xCoord;
            this.movePosY = var1.yCoord;
            this.movePosZ = var1.zCoord;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) < (double)(this.maxTargetDistance * this.maxTargetDistance);
   }

   public void resetTask() {
      this.targetEntity = null;
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }
}
