package net.minecraft.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity$S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity$S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity$S17PacketEntityLookMove;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {

   private static final Logger logger = LogManager.getLogger();
   public Entity myEntity;
   public int blocksDistanceThreshold;
   public int updateFrequency;
   public int lastScaledXPosition;
   public int lastScaledYPosition;
   public int lastScaledZPosition;
   public int lastYaw;
   public int lastPitch;
   public int lastHeadMotion;
   public double motionX;
   public double motionY;
   public double motionZ;
   public int ticks;
   private double posX;
   private double posY;
   private double posZ;
   private boolean isDataInitialized;
   private boolean sendVelocityUpdates;
   private int ticksSinceLastForcedTeleport;
   private Entity field_85178_v;
   private boolean ridingEntity;
   public boolean playerEntitiesUpdated;
   public Set trackingPlayers = new HashSet();


   public EntityTrackerEntry(Entity var1, int var2, int var3, boolean var4) {
      this.myEntity = var1;
      this.blocksDistanceThreshold = var2;
      this.updateFrequency = var3;
      this.sendVelocityUpdates = var4;
      this.lastScaledXPosition = MathHelper.floor_double(var1.posX * 32.0D);
      this.lastScaledYPosition = MathHelper.floor_double(var1.posY * 32.0D);
      this.lastScaledZPosition = MathHelper.floor_double(var1.posZ * 32.0D);
      this.lastYaw = MathHelper.floor_float(var1.rotationYaw * 256.0F / 360.0F);
      this.lastPitch = MathHelper.floor_float(var1.rotationPitch * 256.0F / 360.0F);
      this.lastHeadMotion = MathHelper.floor_float(var1.getRotationYawHead() * 256.0F / 360.0F);
   }

   public boolean equals(Object var1) {
      return var1 instanceof EntityTrackerEntry?((EntityTrackerEntry)var1).myEntity.getEntityId() == this.myEntity.getEntityId():false;
   }

   public int hashCode() {
      return this.myEntity.getEntityId();
   }

   public void sendLocationToAllClients(List var1) {
      this.playerEntitiesUpdated = false;
      if(!this.isDataInitialized || this.myEntity.getDistanceSq(this.posX, this.posY, this.posZ) > 16.0D) {
         this.posX = this.myEntity.posX;
         this.posY = this.myEntity.posY;
         this.posZ = this.myEntity.posZ;
         this.isDataInitialized = true;
         this.playerEntitiesUpdated = true;
         this.sendEventsToPlayers(var1);
      }

      if(this.field_85178_v != this.myEntity.ridingEntity || this.myEntity.ridingEntity != null && this.ticks % 60 == 0) {
         this.field_85178_v = this.myEntity.ridingEntity;
         this.func_151259_a(new S1BPacketEntityAttach(0, this.myEntity, this.myEntity.ridingEntity));
      }

      if(this.myEntity instanceof EntityItemFrame && this.ticks % 10 == 0) {
         EntityItemFrame var23 = (EntityItemFrame)this.myEntity;
         ItemStack var24 = var23.getDisplayedItem();
         if(var24 != null && var24.getItem() instanceof ItemMap) {
            MapData var26 = Items.filled_map.getMapData(var24, this.myEntity.worldObj);
            Iterator var27 = var1.iterator();

            while(var27.hasNext()) {
               EntityPlayer var28 = (EntityPlayer)var27.next();
               EntityPlayerMP var29 = (EntityPlayerMP)var28;
               var26.updateVisiblePlayers(var29, var24);
               Packet var30 = Items.filled_map.func_150911_c(var24, this.myEntity.worldObj, var29);
               if(var30 != null) {
                  var29.playerNetServerHandler.sendPacket(var30);
               }
            }
         }

         this.sendMetadataToAllAssociatedPlayers();
      } else if(this.ticks % this.updateFrequency == 0 || this.myEntity.isAirBorne || this.myEntity.getDataWatcher().hasChanges()) {
         int var2;
         int var3;
         if(this.myEntity.ridingEntity == null) {
            ++this.ticksSinceLastForcedTeleport;
            var2 = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posX);
            var3 = MathHelper.floor_double(this.myEntity.posY * 32.0D);
            int var4 = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posZ);
            int var5 = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
            int var6 = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);
            int var7 = var2 - this.lastScaledXPosition;
            int var8 = var3 - this.lastScaledYPosition;
            int var9 = var4 - this.lastScaledZPosition;
            Object var10 = null;
            boolean var11 = Math.abs(var7) >= 4 || Math.abs(var8) >= 4 || Math.abs(var9) >= 4 || this.ticks % 60 == 0;
            boolean var12 = Math.abs(var5 - this.lastYaw) >= 4 || Math.abs(var6 - this.lastPitch) >= 4;
            if(this.ticks > 0 || this.myEntity instanceof EntityArrow) {
               if(var7 >= -128 && var7 < 128 && var8 >= -128 && var8 < 128 && var9 >= -128 && var9 < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity) {
                  if(var11 && var12) {
                     var10 = new S14PacketEntity$S17PacketEntityLookMove(this.myEntity.getEntityId(), (byte)var7, (byte)var8, (byte)var9, (byte)var5, (byte)var6);
                  } else if(var11) {
                     var10 = new S14PacketEntity$S15PacketEntityRelMove(this.myEntity.getEntityId(), (byte)var7, (byte)var8, (byte)var9);
                  } else if(var12) {
                     var10 = new S14PacketEntity$S16PacketEntityLook(this.myEntity.getEntityId(), (byte)var5, (byte)var6);
                  }
               } else {
                  this.ticksSinceLastForcedTeleport = 0;
                  var10 = new S18PacketEntityTeleport(this.myEntity.getEntityId(), var2, var3, var4, (byte)var5, (byte)var6);
               }
            }

            if(this.sendVelocityUpdates) {
               double var13 = this.myEntity.motionX - this.motionX;
               double var15 = this.myEntity.motionY - this.motionY;
               double var17 = this.myEntity.motionZ - this.motionZ;
               double var19 = 0.02D;
               double var21 = var13 * var13 + var15 * var15 + var17 * var17;
               if(var21 > var19 * var19 || var21 > 0.0D && this.myEntity.motionX == 0.0D && this.myEntity.motionY == 0.0D && this.myEntity.motionZ == 0.0D) {
                  this.motionX = this.myEntity.motionX;
                  this.motionY = this.myEntity.motionY;
                  this.motionZ = this.myEntity.motionZ;
                  this.func_151259_a(new S12PacketEntityVelocity(this.myEntity.getEntityId(), this.motionX, this.motionY, this.motionZ));
               }
            }

            if(var10 != null) {
               this.func_151259_a((Packet)var10);
            }

            this.sendMetadataToAllAssociatedPlayers();
            if(var11) {
               this.lastScaledXPosition = var2;
               this.lastScaledYPosition = var3;
               this.lastScaledZPosition = var4;
            }

            if(var12) {
               this.lastYaw = var5;
               this.lastPitch = var6;
            }

            this.ridingEntity = false;
         } else {
            var2 = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
            var3 = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);
            boolean var25 = Math.abs(var2 - this.lastYaw) >= 4 || Math.abs(var3 - this.lastPitch) >= 4;
            if(var25) {
               this.func_151259_a(new S14PacketEntity$S16PacketEntityLook(this.myEntity.getEntityId(), (byte)var2, (byte)var3));
               this.lastYaw = var2;
               this.lastPitch = var3;
            }

            this.lastScaledXPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posX);
            this.lastScaledYPosition = MathHelper.floor_double(this.myEntity.posY * 32.0D);
            this.lastScaledZPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posZ);
            this.sendMetadataToAllAssociatedPlayers();
            this.ridingEntity = true;
         }

         var2 = MathHelper.floor_float(this.myEntity.getRotationYawHead() * 256.0F / 360.0F);
         if(Math.abs(var2 - this.lastHeadMotion) >= 4) {
            this.func_151259_a(new S19PacketEntityHeadLook(this.myEntity, (byte)var2));
            this.lastHeadMotion = var2;
         }

         this.myEntity.isAirBorne = false;
      }

      ++this.ticks;
      if(this.myEntity.velocityChanged) {
         this.func_151261_b(new S12PacketEntityVelocity(this.myEntity));
         this.myEntity.velocityChanged = false;
      }

   }

   private void sendMetadataToAllAssociatedPlayers() {
      DataWatcher var1 = this.myEntity.getDataWatcher();
      if(var1.hasChanges()) {
         this.func_151261_b(new S1CPacketEntityMetadata(this.myEntity.getEntityId(), var1, false));
      }

      if(this.myEntity instanceof EntityLivingBase) {
         ServersideAttributeMap var2 = (ServersideAttributeMap)((EntityLivingBase)this.myEntity).getAttributeMap();
         Set var3 = var2.getAttributeInstanceSet();
         if(!var3.isEmpty()) {
            this.func_151261_b(new S20PacketEntityProperties(this.myEntity.getEntityId(), var3));
         }

         var3.clear();
      }

   }

   public void func_151259_a(Packet var1) {
      Iterator var2 = this.trackingPlayers.iterator();

      while(var2.hasNext()) {
         EntityPlayerMP var3 = (EntityPlayerMP)var2.next();
         var3.playerNetServerHandler.sendPacket(var1);
      }

   }

   public void func_151261_b(Packet var1) {
      this.func_151259_a(var1);
      if(this.myEntity instanceof EntityPlayerMP) {
         ((EntityPlayerMP)this.myEntity).playerNetServerHandler.sendPacket(var1);
      }

   }

   public void informAllAssociatedPlayersOfItemDestruction() {
      Iterator var1 = this.trackingPlayers.iterator();

      while(var1.hasNext()) {
         EntityPlayerMP var2 = (EntityPlayerMP)var1.next();
         var2.func_152339_d(this.myEntity);
      }

   }

   public void removeFromWatchingList(EntityPlayerMP var1) {
      if(this.trackingPlayers.contains(var1)) {
         var1.func_152339_d(this.myEntity);
         this.trackingPlayers.remove(var1);
      }

   }

   public void tryStartWachingThis(EntityPlayerMP var1) {
      if(var1 != this.myEntity) {
         double var2 = var1.posX - (double)(this.lastScaledXPosition / 32);
         double var4 = var1.posZ - (double)(this.lastScaledZPosition / 32);
         if(var2 >= (double)(-this.blocksDistanceThreshold) && var2 <= (double)this.blocksDistanceThreshold && var4 >= (double)(-this.blocksDistanceThreshold) && var4 <= (double)this.blocksDistanceThreshold) {
            if(!this.trackingPlayers.contains(var1) && (this.isPlayerWatchingThisChunk(var1) || this.myEntity.forceSpawn)) {
               this.trackingPlayers.add(var1);
               Packet var6 = this.func_151260_c();
               var1.playerNetServerHandler.sendPacket(var6);
               if(!this.myEntity.getDataWatcher().getIsBlank()) {
                  var1.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.myEntity.getEntityId(), this.myEntity.getDataWatcher(), true));
               }

               if(this.myEntity instanceof EntityLivingBase) {
                  ServersideAttributeMap var7 = (ServersideAttributeMap)((EntityLivingBase)this.myEntity).getAttributeMap();
                  Collection var8 = var7.getWatchedAttributes();
                  if(!var8.isEmpty()) {
                     var1.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.myEntity.getEntityId(), var8));
                  }
               }

               this.motionX = this.myEntity.motionX;
               this.motionY = this.myEntity.motionY;
               this.motionZ = this.myEntity.motionZ;
               if(this.sendVelocityUpdates && !(var6 instanceof S0FPacketSpawnMob)) {
                  var1.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.myEntity.getEntityId(), this.myEntity.motionX, this.myEntity.motionY, this.myEntity.motionZ));
               }

               if(this.myEntity.ridingEntity != null) {
                  var1.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.myEntity, this.myEntity.ridingEntity));
               }

               if(this.myEntity instanceof EntityLiving && ((EntityLiving)this.myEntity).getLeashedToEntity() != null) {
                  var1.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.myEntity, ((EntityLiving)this.myEntity).getLeashedToEntity()));
               }

               if(this.myEntity instanceof EntityLivingBase) {
                  for(int var10 = 0; var10 < 5; ++var10) {
                     ItemStack var12 = ((EntityLivingBase)this.myEntity).getEquipmentInSlot(var10);
                     if(var12 != null) {
                        var1.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.myEntity.getEntityId(), var10, var12));
                     }
                  }
               }

               if(this.myEntity instanceof EntityPlayer) {
                  EntityPlayer var11 = (EntityPlayer)this.myEntity;
                  if(var11.isPlayerSleeping()) {
                     var1.playerNetServerHandler.sendPacket(new S0APacketUseBed(var11, MathHelper.floor_double(this.myEntity.posX), MathHelper.floor_double(this.myEntity.posY), MathHelper.floor_double(this.myEntity.posZ)));
                  }
               }

               if(this.myEntity instanceof EntityLivingBase) {
                  EntityLivingBase var13 = (EntityLivingBase)this.myEntity;
                  Iterator var14 = var13.getActivePotionEffects().iterator();

                  while(var14.hasNext()) {
                     PotionEffect var9 = (PotionEffect)var14.next();
                     var1.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.myEntity.getEntityId(), var9));
                  }
               }
            }
         } else if(this.trackingPlayers.contains(var1)) {
            this.trackingPlayers.remove(var1);
            var1.func_152339_d(this.myEntity);
         }

      }
   }

   private boolean isPlayerWatchingThisChunk(EntityPlayerMP var1) {
      return var1.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(var1, this.myEntity.chunkCoordX, this.myEntity.chunkCoordZ);
   }

   public void sendEventsToPlayers(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.tryStartWachingThis((EntityPlayerMP)var1.get(var2));
      }

   }

   private Packet func_151260_c() {
      if(this.myEntity.isDead) {
         logger.warn("Fetching addPacket for removed entity");
      }

      if(this.myEntity instanceof EntityItem) {
         return new S0EPacketSpawnObject(this.myEntity, 2, 1);
      } else if(this.myEntity instanceof EntityPlayerMP) {
         return new S0CPacketSpawnPlayer((EntityPlayer)this.myEntity);
      } else if(this.myEntity instanceof EntityMinecart) {
         EntityMinecart var9 = (EntityMinecart)this.myEntity;
         return new S0EPacketSpawnObject(this.myEntity, 10, var9.getMinecartType());
      } else if(this.myEntity instanceof EntityBoat) {
         return new S0EPacketSpawnObject(this.myEntity, 1);
      } else if(!(this.myEntity instanceof IAnimals) && !(this.myEntity instanceof EntityDragon)) {
         if(this.myEntity instanceof EntityFishHook) {
            EntityPlayer var8 = ((EntityFishHook)this.myEntity).field_146042_b;
            return new S0EPacketSpawnObject(this.myEntity, 90, var8 != null?var8.getEntityId():this.myEntity.getEntityId());
         } else if(this.myEntity instanceof EntityArrow) {
            Entity var7 = ((EntityArrow)this.myEntity).shootingEntity;
            return new S0EPacketSpawnObject(this.myEntity, 60, var7 != null?var7.getEntityId():this.myEntity.getEntityId());
         } else if(this.myEntity instanceof EntitySnowball) {
            return new S0EPacketSpawnObject(this.myEntity, 61);
         } else if(this.myEntity instanceof EntityPotion) {
            return new S0EPacketSpawnObject(this.myEntity, 73, ((EntityPotion)this.myEntity).getPotionDamage());
         } else if(this.myEntity instanceof EntityExpBottle) {
            return new S0EPacketSpawnObject(this.myEntity, 75);
         } else if(this.myEntity instanceof EntityEnderPearl) {
            return new S0EPacketSpawnObject(this.myEntity, 65);
         } else if(this.myEntity instanceof EntityEnderEye) {
            return new S0EPacketSpawnObject(this.myEntity, 72);
         } else if(this.myEntity instanceof EntityFireworkRocket) {
            return new S0EPacketSpawnObject(this.myEntity, 76);
         } else {
            S0EPacketSpawnObject var2;
            if(this.myEntity instanceof EntityFireball) {
               EntityFireball var6 = (EntityFireball)this.myEntity;
               var2 = null;
               byte var3 = 63;
               if(this.myEntity instanceof EntitySmallFireball) {
                  var3 = 64;
               } else if(this.myEntity instanceof EntityWitherSkull) {
                  var3 = 66;
               }

               if(var6.shootingEntity != null) {
                  var2 = new S0EPacketSpawnObject(this.myEntity, var3, ((EntityFireball)this.myEntity).shootingEntity.getEntityId());
               } else {
                  var2 = new S0EPacketSpawnObject(this.myEntity, var3, 0);
               }

               var2.func_149003_d((int)(var6.accelerationX * 8000.0D));
               var2.func_149000_e((int)(var6.accelerationY * 8000.0D));
               var2.func_149007_f((int)(var6.accelerationZ * 8000.0D));
               return var2;
            } else if(this.myEntity instanceof EntityEgg) {
               return new S0EPacketSpawnObject(this.myEntity, 62);
            } else if(this.myEntity instanceof EntityTNTPrimed) {
               return new S0EPacketSpawnObject(this.myEntity, 50);
            } else if(this.myEntity instanceof EntityEnderCrystal) {
               return new S0EPacketSpawnObject(this.myEntity, 51);
            } else if(this.myEntity instanceof EntityFallingBlock) {
               EntityFallingBlock var5 = (EntityFallingBlock)this.myEntity;
               return new S0EPacketSpawnObject(this.myEntity, 70, Block.getIdFromBlock(var5.func_145805_f()) | var5.field_145814_a << 16);
            } else if(this.myEntity instanceof EntityPainting) {
               return new S10PacketSpawnPainting((EntityPainting)this.myEntity);
            } else if(this.myEntity instanceof EntityItemFrame) {
               EntityItemFrame var4 = (EntityItemFrame)this.myEntity;
               var2 = new S0EPacketSpawnObject(this.myEntity, 71, var4.hangingDirection);
               var2.func_148996_a(MathHelper.floor_float((float)(var4.field_146063_b * 32)));
               var2.func_148995_b(MathHelper.floor_float((float)(var4.field_146064_c * 32)));
               var2.func_149005_c(MathHelper.floor_float((float)(var4.field_146062_d * 32)));
               return var2;
            } else if(this.myEntity instanceof EntityLeashKnot) {
               EntityLeashKnot var1 = (EntityLeashKnot)this.myEntity;
               var2 = new S0EPacketSpawnObject(this.myEntity, 77);
               var2.func_148996_a(MathHelper.floor_float((float)(var1.field_146063_b * 32)));
               var2.func_148995_b(MathHelper.floor_float((float)(var1.field_146064_c * 32)));
               var2.func_149005_c(MathHelper.floor_float((float)(var1.field_146062_d * 32)));
               return var2;
            } else if(this.myEntity instanceof EntityXPOrb) {
               return new S11PacketSpawnExperienceOrb((EntityXPOrb)this.myEntity);
            } else {
               throw new IllegalArgumentException("Don\'t know how to add " + this.myEntity.getClass() + "!");
            }
         }
      } else {
         this.lastHeadMotion = MathHelper.floor_float(this.myEntity.getRotationYawHead() * 256.0F / 360.0F);
         return new S0FPacketSpawnMob((EntityLivingBase)this.myEntity);
      }
   }

   public void removePlayerFromTracker(EntityPlayerMP var1) {
      if(this.trackingPlayers.contains(var1)) {
         this.trackingPlayers.remove(var1);
         var1.func_152339_d(this.myEntity);
      }

   }

}
