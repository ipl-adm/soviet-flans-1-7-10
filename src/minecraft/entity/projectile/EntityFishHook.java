package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood$FishType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook extends Entity {

   private static final List field_146039_d = Arrays.asList(new WeightedRandomFishable[]{(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10)});
   private static final List field_146041_e = Arrays.asList(new WeightedRandomFishable[]{new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).func_150707_a()});
   private static final List field_146036_f = Arrays.asList(new WeightedRandomFishable[]{new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood$FishType.COD.func_150976_a()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood$FishType.SALMON.func_150976_a()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood$FishType.CLOWNFISH.func_150976_a()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood$FishType.PUFFERFISH.func_150976_a()), 13)});
   private int field_146037_g;
   private int field_146048_h;
   private int field_146050_i;
   private Block field_146046_j;
   private boolean field_146051_au;
   public int field_146044_a;
   public EntityPlayer field_146042_b;
   private int field_146049_av;
   private int field_146047_aw;
   private int field_146045_ax;
   private int field_146040_ay;
   private int field_146038_az;
   private float field_146054_aA;
   public Entity field_146043_c;
   private int field_146055_aB;
   private double field_146056_aC;
   private double field_146057_aD;
   private double field_146058_aE;
   private double field_146059_aF;
   private double field_146060_aG;
   private double field_146061_aH;
   private double field_146052_aI;
   private double field_146053_aJ;


   public EntityFishHook(World var1) {
      super(var1);
      this.field_146037_g = -1;
      this.field_146048_h = -1;
      this.field_146050_i = -1;
      this.setSize(0.25F, 0.25F);
      super.ignoreFrustumCheck = true;
   }

   public EntityFishHook(World var1, double var2, double var4, double var6, EntityPlayer var8) {
      this(var1);
      this.setPosition(var2, var4, var6);
      super.ignoreFrustumCheck = true;
      this.field_146042_b = var8;
      var8.fishEntity = this;
   }

   public EntityFishHook(World var1, EntityPlayer var2) {
      super(var1);
      this.field_146037_g = -1;
      this.field_146048_h = -1;
      this.field_146050_i = -1;
      super.ignoreFrustumCheck = true;
      this.field_146042_b = var2;
      this.field_146042_b.fishEntity = this;
      this.setSize(0.25F, 0.25F);
      this.setLocationAndAngles(var2.posX, var2.posY + 1.62D - (double)var2.yOffset, var2.posZ, var2.rotationYaw, var2.rotationPitch);
      super.posX -= (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      super.posY -= 0.10000000149011612D;
      super.posZ -= (double)(MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(super.posX, super.posY, super.posZ);
      super.yOffset = 0.0F;
      float var3 = 0.4F;
      super.motionX = (double)(-MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F) * var3);
      super.motionZ = (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F) * var3);
      super.motionY = (double)(-MathHelper.sin(super.rotationPitch / 180.0F * 3.1415927F) * var3);
      this.func_146035_c(super.motionX, super.motionY, super.motionZ, 1.5F, 1.0F);
   }

   protected void entityInit() {}

   public boolean isInRangeToRenderDist(double var1) {
      double var3 = super.boundingBox.getAverageEdgeLength() * 4.0D;
      var3 *= 64.0D;
      return var1 < var3 * var3;
   }

   public void func_146035_c(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var3 += super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var5 += super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      super.motionX = var1;
      super.motionY = var3;
      super.motionZ = var5;
      float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / 3.1415927410125732D);
      super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / 3.1415927410125732D);
      this.field_146049_av = 0;
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
      this.field_146056_aC = var1;
      this.field_146057_aD = var3;
      this.field_146058_aE = var5;
      this.field_146059_aF = (double)var7;
      this.field_146060_aG = (double)var8;
      this.field_146055_aB = var9;
      super.motionX = this.field_146061_aH;
      super.motionY = this.field_146052_aI;
      super.motionZ = this.field_146053_aJ;
   }

   public void setVelocity(double var1, double var3, double var5) {
      this.field_146061_aH = super.motionX = var1;
      this.field_146052_aI = super.motionY = var3;
      this.field_146053_aJ = super.motionZ = var5;
   }

   public void onUpdate() {
      super.onUpdate();
      if(this.field_146055_aB > 0) {
         double var27 = super.posX + (this.field_146056_aC - super.posX) / (double)this.field_146055_aB;
         double var28 = super.posY + (this.field_146057_aD - super.posY) / (double)this.field_146055_aB;
         double var29 = super.posZ + (this.field_146058_aE - super.posZ) / (double)this.field_146055_aB;
         double var7 = MathHelper.wrapAngleTo180_double(this.field_146059_aF - (double)super.rotationYaw);
         super.rotationYaw = (float)((double)super.rotationYaw + var7 / (double)this.field_146055_aB);
         super.rotationPitch = (float)((double)super.rotationPitch + (this.field_146060_aG - (double)super.rotationPitch) / (double)this.field_146055_aB);
         --this.field_146055_aB;
         this.setPosition(var27, var28, var29);
         this.setRotation(super.rotationYaw, super.rotationPitch);
      } else {
         if(!super.worldObj.isRemote) {
            ItemStack var1 = this.field_146042_b.getCurrentEquippedItem();
            if(this.field_146042_b.isDead || !this.field_146042_b.isEntityAlive() || var1 == null || var1.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.field_146042_b) > 1024.0D) {
               this.setDead();
               this.field_146042_b.fishEntity = null;
               return;
            }

            if(this.field_146043_c != null) {
               if(!this.field_146043_c.isDead) {
                  super.posX = this.field_146043_c.posX;
                  super.posY = this.field_146043_c.boundingBox.minY + (double)this.field_146043_c.height * 0.8D;
                  super.posZ = this.field_146043_c.posZ;
                  return;
               }

               this.field_146043_c = null;
            }
         }

         if(this.field_146044_a > 0) {
            --this.field_146044_a;
         }

         if(this.field_146051_au) {
            if(super.worldObj.getBlock(this.field_146037_g, this.field_146048_h, this.field_146050_i) == this.field_146046_j) {
               ++this.field_146049_av;
               if(this.field_146049_av == 1200) {
                  this.setDead();
               }

               return;
            }

            this.field_146051_au = false;
            super.motionX *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionY *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionZ *= (double)(super.rand.nextFloat() * 0.2F);
            this.field_146049_av = 0;
            this.field_146047_aw = 0;
         } else {
            ++this.field_146047_aw;
         }

         Vec3 var26 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
         Vec3 var2 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         MovingObjectPosition var3 = super.worldObj.rayTraceBlocks(var26, var2);
         var26 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
         var2 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         if(var3 != null) {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
         }

         Entity var4 = null;
         List var5 = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.addCoord(super.motionX, super.motionY, super.motionZ).expand(1.0D, 1.0D, 1.0D));
         double var6 = 0.0D;

         double var13;
         for(int var8 = 0; var8 < var5.size(); ++var8) {
            Entity var9 = (Entity)var5.get(var8);
            if(var9.canBeCollidedWith() && (var9 != this.field_146042_b || this.field_146047_aw >= 5)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
               MovingObjectPosition var12 = var11.calculateIntercept(var26, var2);
               if(var12 != null) {
                  var13 = var26.distanceTo(var12.hitVec);
                  if(var13 < var6 || var6 == 0.0D) {
                     var4 = var9;
                     var6 = var13;
                  }
               }
            }
         }

         if(var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }

         if(var3 != null) {
            if(var3.entityHit != null) {
               if(var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.field_146042_b), 0.0F)) {
                  this.field_146043_c = var3.entityHit;
               }
            } else {
               this.field_146051_au = true;
            }
         }

         if(!this.field_146051_au) {
            this.moveEntity(super.motionX, super.motionY, super.motionZ);
            float var30 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
            super.rotationYaw = (float)(Math.atan2(super.motionX, super.motionZ) * 180.0D / 3.1415927410125732D);

            for(super.rotationPitch = (float)(Math.atan2(super.motionY, (double)var30) * 180.0D / 3.1415927410125732D); super.rotationPitch - super.prevRotationPitch < -180.0F; super.prevRotationPitch -= 360.0F) {
               ;
            }

            while(super.rotationPitch - super.prevRotationPitch >= 180.0F) {
               super.prevRotationPitch += 360.0F;
            }

            while(super.rotationYaw - super.prevRotationYaw < -180.0F) {
               super.prevRotationYaw -= 360.0F;
            }

            while(super.rotationYaw - super.prevRotationYaw >= 180.0F) {
               super.prevRotationYaw += 360.0F;
            }

            super.rotationPitch = super.prevRotationPitch + (super.rotationPitch - super.prevRotationPitch) * 0.2F;
            super.rotationYaw = super.prevRotationYaw + (super.rotationYaw - super.prevRotationYaw) * 0.2F;
            float var31 = 0.92F;
            if(super.onGround || super.isCollidedHorizontally) {
               var31 = 0.5F;
            }

            byte var32 = 5;
            double var33 = 0.0D;

            for(int var34 = 0; var34 < var32; ++var34) {
               double var14 = super.boundingBox.minY + (super.boundingBox.maxY - super.boundingBox.minY) * (double)(var34 + 0) / (double)var32 - 0.125D + 0.125D;
               double var16 = super.boundingBox.minY + (super.boundingBox.maxY - super.boundingBox.minY) * (double)(var34 + 1) / (double)var32 - 0.125D + 0.125D;
               AxisAlignedBB var18 = AxisAlignedBB.getBoundingBox(super.boundingBox.minX, var14, super.boundingBox.minZ, super.boundingBox.maxX, var16, super.boundingBox.maxZ);
               if(super.worldObj.isAABBInMaterial(var18, Material.water)) {
                  var33 += 1.0D / (double)var32;
               }
            }

            if(!super.worldObj.isRemote && var33 > 0.0D) {
               WorldServer var35 = (WorldServer)super.worldObj;
               int var36 = 1;
               if(super.rand.nextFloat() < 0.25F && super.worldObj.canLightningStrikeAt(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY) + 1, MathHelper.floor_double(super.posZ))) {
                  var36 = 2;
               }

               if(super.rand.nextFloat() < 0.5F && !super.worldObj.canBlockSeeTheSky(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY) + 1, MathHelper.floor_double(super.posZ))) {
                  --var36;
               }

               if(this.field_146045_ax > 0) {
                  --this.field_146045_ax;
                  if(this.field_146045_ax <= 0) {
                     this.field_146040_ay = 0;
                     this.field_146038_az = 0;
                  }
               } else {
                  float var15;
                  float var17;
                  double var20;
                  double var22;
                  float var37;
                  double var38;
                  if(this.field_146038_az > 0) {
                     this.field_146038_az -= var36;
                     if(this.field_146038_az <= 0) {
                        super.motionY -= 0.20000000298023224D;
                        this.playSound("random.splash", 0.25F, 1.0F + (super.rand.nextFloat() - super.rand.nextFloat()) * 0.4F);
                        var15 = (float)MathHelper.floor_double(super.boundingBox.minY);
                        var35.func_147487_a("bubble", super.posX, (double)(var15 + 1.0F), super.posZ, (int)(1.0F + super.width * 20.0F), (double)super.width, 0.0D, (double)super.width, 0.20000000298023224D);
                        var35.func_147487_a("wake", super.posX, (double)(var15 + 1.0F), super.posZ, (int)(1.0F + super.width * 20.0F), (double)super.width, 0.0D, (double)super.width, 0.20000000298023224D);
                        this.field_146045_ax = MathHelper.getRandomIntegerInRange(super.rand, 10, 30);
                     } else {
                        this.field_146054_aA = (float)((double)this.field_146054_aA + super.rand.nextGaussian() * 4.0D);
                        var15 = this.field_146054_aA * 0.017453292F;
                        var37 = MathHelper.sin(var15);
                        var17 = MathHelper.cos(var15);
                        var38 = super.posX + (double)(var37 * (float)this.field_146038_az * 0.1F);
                        var20 = (double)((float)MathHelper.floor_double(super.boundingBox.minY) + 1.0F);
                        var22 = super.posZ + (double)(var17 * (float)this.field_146038_az * 0.1F);
                        if(super.rand.nextFloat() < 0.15F) {
                           var35.func_147487_a("bubble", var38, var20 - 0.10000000149011612D, var22, 1, (double)var37, 0.1D, (double)var17, 0.0D);
                        }

                        float var24 = var37 * 0.04F;
                        float var25 = var17 * 0.04F;
                        var35.func_147487_a("wake", var38, var20, var22, 0, (double)var25, 0.01D, (double)(-var24), 1.0D);
                        var35.func_147487_a("wake", var38, var20, var22, 0, (double)(-var25), 0.01D, (double)var24, 1.0D);
                     }
                  } else if(this.field_146040_ay > 0) {
                     this.field_146040_ay -= var36;
                     var15 = 0.15F;
                     if(this.field_146040_ay < 20) {
                        var15 = (float)((double)var15 + (double)(20 - this.field_146040_ay) * 0.05D);
                     } else if(this.field_146040_ay < 40) {
                        var15 = (float)((double)var15 + (double)(40 - this.field_146040_ay) * 0.02D);
                     } else if(this.field_146040_ay < 60) {
                        var15 = (float)((double)var15 + (double)(60 - this.field_146040_ay) * 0.01D);
                     }

                     if(super.rand.nextFloat() < var15) {
                        var37 = MathHelper.randomFloatClamp(super.rand, 0.0F, 360.0F) * 0.017453292F;
                        var17 = MathHelper.randomFloatClamp(super.rand, 25.0F, 60.0F);
                        var38 = super.posX + (double)(MathHelper.sin(var37) * var17 * 0.1F);
                        var20 = (double)((float)MathHelper.floor_double(super.boundingBox.minY) + 1.0F);
                        var22 = super.posZ + (double)(MathHelper.cos(var37) * var17 * 0.1F);
                        var35.func_147487_a("splash", var38, var20, var22, 2 + super.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                     }

                     if(this.field_146040_ay <= 0) {
                        this.field_146054_aA = MathHelper.randomFloatClamp(super.rand, 0.0F, 360.0F);
                        this.field_146038_az = MathHelper.getRandomIntegerInRange(super.rand, 20, 80);
                     }
                  } else {
                     this.field_146040_ay = MathHelper.getRandomIntegerInRange(super.rand, 100, 900);
                     this.field_146040_ay -= EnchantmentHelper.func_151387_h(this.field_146042_b) * 20 * 5;
                  }
               }

               if(this.field_146045_ax > 0) {
                  super.motionY -= (double)(super.rand.nextFloat() * super.rand.nextFloat() * super.rand.nextFloat()) * 0.2D;
               }
            }

            var13 = var33 * 2.0D - 1.0D;
            super.motionY += 0.03999999910593033D * var13;
            if(var33 > 0.0D) {
               var31 = (float)((double)var31 * 0.9D);
               super.motionY *= 0.8D;
            }

            super.motionX *= (double)var31;
            super.motionY *= (double)var31;
            super.motionZ *= (double)var31;
            this.setPosition(super.posX, super.posY, super.posZ);
         }
      }
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.field_146037_g);
      var1.setShort("yTile", (short)this.field_146048_h);
      var1.setShort("zTile", (short)this.field_146050_i);
      var1.setByte("inTile", (byte)Block.getIdFromBlock(this.field_146046_j));
      var1.setByte("shake", (byte)this.field_146044_a);
      var1.setByte("inGround", (byte)(this.field_146051_au?1:0));
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      this.field_146037_g = var1.getShort("xTile");
      this.field_146048_h = var1.getShort("yTile");
      this.field_146050_i = var1.getShort("zTile");
      this.field_146046_j = Block.getBlockById(var1.getByte("inTile") & 255);
      this.field_146044_a = var1.getByte("shake") & 255;
      this.field_146051_au = var1.getByte("inGround") == 1;
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public int func_146034_e() {
      if(super.worldObj.isRemote) {
         return 0;
      } else {
         byte var1 = 0;
         if(this.field_146043_c != null) {
            double var2 = this.field_146042_b.posX - super.posX;
            double var4 = this.field_146042_b.posY - super.posY;
            double var6 = this.field_146042_b.posZ - super.posZ;
            double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1D;
            this.field_146043_c.motionX += var2 * var10;
            this.field_146043_c.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
            this.field_146043_c.motionZ += var6 * var10;
            var1 = 3;
         } else if(this.field_146045_ax > 0) {
            EntityItem var13 = new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, this.func_146033_f());
            double var3 = this.field_146042_b.posX - super.posX;
            double var5 = this.field_146042_b.posY - super.posY;
            double var7 = this.field_146042_b.posZ - super.posZ;
            double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1D;
            var13.motionX = var3 * var11;
            var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
            var13.motionZ = var7 * var11;
            super.worldObj.spawnEntityInWorld(var13);
            this.field_146042_b.worldObj.spawnEntityInWorld(new EntityXPOrb(this.field_146042_b.worldObj, this.field_146042_b.posX, this.field_146042_b.posY + 0.5D, this.field_146042_b.posZ + 0.5D, super.rand.nextInt(6) + 1));
            var1 = 1;
         }

         if(this.field_146051_au) {
            var1 = 2;
         }

         this.setDead();
         this.field_146042_b.fishEntity = null;
         return var1;
      }
   }

   private ItemStack func_146033_f() {
      float var1 = super.worldObj.rand.nextFloat();
      int var2 = EnchantmentHelper.func_151386_g(this.field_146042_b);
      int var3 = EnchantmentHelper.func_151387_h(this.field_146042_b);
      float var4 = 0.1F - (float)var2 * 0.025F - (float)var3 * 0.01F;
      float var5 = 0.05F + (float)var2 * 0.01F - (float)var3 * 0.01F;
      var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
      var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);
      if(var1 < var4) {
         this.field_146042_b.addStat(StatList.field_151183_A, 1);
         return ((WeightedRandomFishable)WeightedRandom.getRandomItem(super.rand, (Collection)field_146039_d)).func_150708_a(super.rand);
      } else {
         var1 -= var4;
         if(var1 < var5) {
            this.field_146042_b.addStat(StatList.field_151184_B, 1);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(super.rand, (Collection)field_146041_e)).func_150708_a(super.rand);
         } else {
            float var10000 = var1 - var5;
            this.field_146042_b.addStat(StatList.fishCaughtStat, 1);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(super.rand, (Collection)field_146036_f)).func_150708_a(super.rand);
         }
      }
   }

   public void setDead() {
      super.setDead();
      if(this.field_146042_b != null) {
         this.field_146042_b.fishEntity = null;
      }

   }

}
