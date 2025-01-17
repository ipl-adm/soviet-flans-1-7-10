package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {

   public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
   public static final AttributeModifier field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
   private PathEntity pathToEntity;
   protected Entity entityToAttack;
   protected boolean hasAttacked;
   protected int fleeingTick;
   private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
   private float maximumHomeDistance = -1.0F;
   private EntityAIBase field_110178_bs = new EntityAIMoveTowardsRestriction(this, 1.0D);
   private boolean field_110180_bt;


   public EntityCreature(World var1) {
      super(var1);
   }

   protected boolean isMovementCeased() {
      return false;
   }

   protected void updateEntityActionState() {
      super.worldObj.theProfiler.startSection("ai");
      if(this.fleeingTick > 0 && --this.fleeingTick == 0) {
         IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
         var1.removeModifier(field_110181_i);
      }

      this.hasAttacked = this.isMovementCeased();
      float var21 = 16.0F;
      if(this.entityToAttack == null) {
         this.entityToAttack = this.findPlayerToAttack();
         if(this.entityToAttack != null) {
            this.pathToEntity = super.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
         }
      } else if(this.entityToAttack.isEntityAlive()) {
         float var2 = this.entityToAttack.getDistanceToEntity(this);
         if(this.canEntityBeSeen(this.entityToAttack)) {
            this.attackEntity(this.entityToAttack, var2);
         }
      } else {
         this.entityToAttack = null;
      }

      if(this.entityToAttack instanceof EntityPlayerMP && ((EntityPlayerMP)this.entityToAttack).theItemInWorldManager.isCreative()) {
         this.entityToAttack = null;
      }

      super.worldObj.theProfiler.endSection();
      if(!this.hasAttacked && this.entityToAttack != null && (this.pathToEntity == null || super.rand.nextInt(20) == 0)) {
         this.pathToEntity = super.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
      } else if(!this.hasAttacked && (this.pathToEntity == null && super.rand.nextInt(180) == 0 || super.rand.nextInt(120) == 0 || this.fleeingTick > 0) && super.entityAge < 100) {
         this.updateWanderPath();
      }

      int var22 = MathHelper.floor_double(super.boundingBox.minY + 0.5D);
      boolean var3 = this.isInWater();
      boolean var4 = this.handleLavaMovement();
      super.rotationPitch = 0.0F;
      if(this.pathToEntity != null && super.rand.nextInt(100) != 0) {
         super.worldObj.theProfiler.startSection("followpath");
         Vec3 var5 = this.pathToEntity.getPosition(this);
         double var6 = (double)(super.width * 2.0F);

         while(var5 != null && var5.squareDistanceTo(super.posX, var5.yCoord, super.posZ) < var6 * var6) {
            this.pathToEntity.incrementPathIndex();
            if(this.pathToEntity.isFinished()) {
               var5 = null;
               this.pathToEntity = null;
            } else {
               var5 = this.pathToEntity.getPosition(this);
            }
         }

         super.isJumping = false;
         if(var5 != null) {
            double var8 = var5.xCoord - super.posX;
            double var10 = var5.zCoord - super.posZ;
            double var12 = var5.yCoord - (double)var22;
            float var14 = (float)(Math.atan2(var10, var8) * 180.0D / 3.1415927410125732D) - 90.0F;
            float var15 = MathHelper.wrapAngleTo180_float(var14 - super.rotationYaw);
            super.moveForward = (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
            if(var15 > 30.0F) {
               var15 = 30.0F;
            }

            if(var15 < -30.0F) {
               var15 = -30.0F;
            }

            super.rotationYaw += var15;
            if(this.hasAttacked && this.entityToAttack != null) {
               double var16 = this.entityToAttack.posX - super.posX;
               double var18 = this.entityToAttack.posZ - super.posZ;
               float var20 = super.rotationYaw;
               super.rotationYaw = (float)(Math.atan2(var18, var16) * 180.0D / 3.1415927410125732D) - 90.0F;
               var15 = (var20 - super.rotationYaw + 90.0F) * 3.1415927F / 180.0F;
               super.moveStrafing = -MathHelper.sin(var15) * super.moveForward * 1.0F;
               super.moveForward = MathHelper.cos(var15) * super.moveForward * 1.0F;
            }

            if(var12 > 0.0D) {
               super.isJumping = true;
            }
         }

         if(this.entityToAttack != null) {
            this.faceEntity(this.entityToAttack, 30.0F, 30.0F);
         }

         if(super.isCollidedHorizontally && !this.hasPath()) {
            super.isJumping = true;
         }

         if(super.rand.nextFloat() < 0.8F && (var3 || var4)) {
            super.isJumping = true;
         }

         super.worldObj.theProfiler.endSection();
      } else {
         super.updateEntityActionState();
         this.pathToEntity = null;
      }
   }

   protected void updateWanderPath() {
      super.worldObj.theProfiler.startSection("stroll");
      boolean var1 = false;
      int var2 = -1;
      int var3 = -1;
      int var4 = -1;
      float var5 = -99999.0F;

      for(int var6 = 0; var6 < 10; ++var6) {
         int var7 = MathHelper.floor_double(super.posX + (double)super.rand.nextInt(13) - 6.0D);
         int var8 = MathHelper.floor_double(super.posY + (double)super.rand.nextInt(7) - 3.0D);
         int var9 = MathHelper.floor_double(super.posZ + (double)super.rand.nextInt(13) - 6.0D);
         float var10 = this.getBlockPathWeight(var7, var8, var9);
         if(var10 > var5) {
            var5 = var10;
            var2 = var7;
            var3 = var8;
            var4 = var9;
            var1 = true;
         }
      }

      if(var1) {
         this.pathToEntity = super.worldObj.getEntityPathToXYZ(this, var2, var3, var4, 10.0F, true, false, false, true);
      }

      super.worldObj.theProfiler.endSection();
   }

   protected void attackEntity(Entity var1, float var2) {}

   public float getBlockPathWeight(int var1, int var2, int var3) {
      return 0.0F;
   }

   protected Entity findPlayerToAttack() {
      return null;
   }

   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(super.posX);
      int var2 = MathHelper.floor_double(super.boundingBox.minY);
      int var3 = MathHelper.floor_double(super.posZ);
      return super.getCanSpawnHere() && this.getBlockPathWeight(var1, var2, var3) >= 0.0F;
   }

   public boolean hasPath() {
      return this.pathToEntity != null;
   }

   public void setPathToEntity(PathEntity var1) {
      this.pathToEntity = var1;
   }

   public Entity getEntityToAttack() {
      return this.entityToAttack;
   }

   public void setTarget(Entity var1) {
      this.entityToAttack = var1;
   }

   public boolean isWithinHomeDistanceCurrentPosition() {
      return this.isWithinHomeDistance(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ));
   }

   public boolean isWithinHomeDistance(int var1, int var2, int var3) {
      return this.maximumHomeDistance == -1.0F?true:this.homePosition.getDistanceSquared(var1, var2, var3) < this.maximumHomeDistance * this.maximumHomeDistance;
   }

   public void setHomeArea(int var1, int var2, int var3, int var4) {
      this.homePosition.set(var1, var2, var3);
      this.maximumHomeDistance = (float)var4;
   }

   public ChunkCoordinates getHomePosition() {
      return this.homePosition;
   }

   public float func_110174_bM() {
      return this.maximumHomeDistance;
   }

   public void detachHome() {
      this.maximumHomeDistance = -1.0F;
   }

   public boolean hasHome() {
      return this.maximumHomeDistance != -1.0F;
   }

   protected void updateLeashedState() {
      super.updateLeashedState();
      if(this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == super.worldObj) {
         Entity var1 = this.getLeashedToEntity();
         this.setHomeArea((int)var1.posX, (int)var1.posY, (int)var1.posZ, 5);
         float var2 = this.getDistanceToEntity(var1);
         if(this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
            if(var2 > 10.0F) {
               this.clearLeashed(true, true);
            }

            return;
         }

         if(!this.field_110180_bt) {
            super.tasks.addTask(2, this.field_110178_bs);
            this.getNavigator().setAvoidsWater(false);
            this.field_110180_bt = true;
         }

         this.func_142017_o(var2);
         if(var2 > 4.0F) {
            this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
         }

         if(var2 > 6.0F) {
            double var3 = (var1.posX - super.posX) / (double)var2;
            double var5 = (var1.posY - super.posY) / (double)var2;
            double var7 = (var1.posZ - super.posZ) / (double)var2;
            super.motionX += var3 * Math.abs(var3) * 0.4D;
            super.motionY += var5 * Math.abs(var5) * 0.4D;
            super.motionZ += var7 * Math.abs(var7) * 0.4D;
         }

         if(var2 > 10.0F) {
            this.clearLeashed(true, true);
         }
      } else if(!this.getLeashed() && this.field_110180_bt) {
         this.field_110180_bt = false;
         super.tasks.removeTask(this.field_110178_bs);
         this.getNavigator().setAvoidsWater(true);
         this.detachHome();
      }

   }

   protected void func_142017_o(float var1) {}

}
