package net.minecraft.entity.passive;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityVillager extends EntityAgeable implements IMerchant, INpc {

   private int randomTickDivider;
   private boolean isMating;
   private boolean isPlaying;
   Village villageObj;
   private EntityPlayer buyingPlayer;
   private MerchantRecipeList buyingList;
   private int timeUntilReset;
   private boolean needsInitilization;
   private int wealth;
   private String lastBuyingPlayer;
   private boolean isLookingForHome;
   private float field_82191_bN;
   private static final Map villagersSellingList = new HashMap();
   private static final Map blacksmithSellingList = new HashMap();


   public EntityVillager(World var1) {
      this(var1, 0);
   }

   public EntityVillager(World var1, int var2) {
      super(var1);
      this.setProfession(var2);
      this.setSize(0.6F, 1.8F);
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
      super.tasks.addTask(1, new EntityAITradePlayer(this));
      super.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
      super.tasks.addTask(2, new EntityAIMoveIndoors(this));
      super.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(4, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
      super.tasks.addTask(6, new EntityAIVillagerMate(this));
      super.tasks.addTask(7, new EntityAIFollowGolem(this));
      super.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
      super.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      super.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
      super.tasks.addTask(9, new EntityAIWander(this, 0.6D));
      super.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      if(--this.randomTickDivider <= 0) {
         super.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ));
         this.randomTickDivider = 70 + super.rand.nextInt(50);
         this.villageObj = super.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), 32);
         if(this.villageObj == null) {
            this.detachHome();
         } else {
            ChunkCoordinates var1 = this.villageObj.getCenter();
            this.setHomeArea(var1.posX, var1.posY, var1.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
            if(this.isLookingForHome) {
               this.isLookingForHome = false;
               this.villageObj.setDefaultPlayerReputation(5);
            }
         }
      }

      if(!this.isTrading() && this.timeUntilReset > 0) {
         --this.timeUntilReset;
         if(this.timeUntilReset <= 0) {
            if(this.needsInitilization) {
               if(this.buyingList.size() > 1) {
                  Iterator var3 = this.buyingList.iterator();

                  while(var3.hasNext()) {
                     MerchantRecipe var2 = (MerchantRecipe)var3.next();
                     if(var2.isRecipeDisabled()) {
                        var2.func_82783_a(super.rand.nextInt(6) + super.rand.nextInt(6) + 2);
                     }
                  }
               }

               this.addDefaultEquipmentAndRecipies(1);
               this.needsInitilization = false;
               if(this.villageObj != null && this.lastBuyingPlayer != null) {
                  super.worldObj.setEntityState(this, (byte)14);
                  this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
               }
            }

            this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
         }
      }

      super.updateAITick();
   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      boolean var3 = var2 != null && var2.getItem() == Items.spawn_egg;
      if(!var3 && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
         if(!super.worldObj.isRemote) {
            this.setCustomer(var1);
            var1.displayGUIMerchant(this, this.getCustomNameTag());
         }

         return true;
      } else {
         return super.interact(var1);
      }
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Integer.valueOf(0));
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("Profession", this.getProfession());
      var1.setInteger("Riches", this.wealth);
      if(this.buyingList != null) {
         var1.setTag("Offers", this.buyingList.getRecipiesAsTags());
      }

   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setProfession(var1.getInteger("Profession"));
      this.wealth = var1.getInteger("Riches");
      if(var1.hasKey("Offers", 10)) {
         NBTTagCompound var2 = var1.getCompoundTag("Offers");
         this.buyingList = new MerchantRecipeList(var2);
      }

   }

   protected boolean canDespawn() {
      return false;
   }

   protected String getLivingSound() {
      return this.isTrading()?"mob.villager.haggle":"mob.villager.idle";
   }

   protected String getHurtSound() {
      return "mob.villager.hit";
   }

   protected String getDeathSound() {
      return "mob.villager.death";
   }

   public void setProfession(int var1) {
      super.dataWatcher.updateObject(16, Integer.valueOf(var1));
   }

   public int getProfession() {
      return super.dataWatcher.getWatchableObjectInt(16);
   }

   public boolean isMating() {
      return this.isMating;
   }

   public void setMating(boolean var1) {
      this.isMating = var1;
   }

   public void setPlaying(boolean var1) {
      this.isPlaying = var1;
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   public void setRevengeTarget(EntityLivingBase var1) {
      super.setRevengeTarget(var1);
      if(this.villageObj != null && var1 != null) {
         this.villageObj.addOrRenewAgressor(var1);
         if(var1 instanceof EntityPlayer) {
            byte var2 = -1;
            if(this.isChild()) {
               var2 = -3;
            }

            this.villageObj.setReputationForPlayer(var1.getCommandSenderName(), var2);
            if(this.isEntityAlive()) {
               super.worldObj.setEntityState(this, (byte)13);
            }
         }
      }

   }

   public void onDeath(DamageSource var1) {
      if(this.villageObj != null) {
         Entity var2 = var1.getEntity();
         if(var2 != null) {
            if(var2 instanceof EntityPlayer) {
               this.villageObj.setReputationForPlayer(var2.getCommandSenderName(), -2);
            } else if(var2 instanceof IMob) {
               this.villageObj.endMatingSeason();
            }
         } else if(var2 == null) {
            EntityPlayer var3 = super.worldObj.getClosestPlayerToEntity(this, 16.0D);
            if(var3 != null) {
               this.villageObj.endMatingSeason();
            }
         }
      }

      super.onDeath(var1);
   }

   public void setCustomer(EntityPlayer var1) {
      this.buyingPlayer = var1;
   }

   public EntityPlayer getCustomer() {
      return this.buyingPlayer;
   }

   public boolean isTrading() {
      return this.buyingPlayer != null;
   }

   public void useRecipe(MerchantRecipe var1) {
      var1.incrementToolUses();
      super.livingSoundTime = -this.getTalkInterval();
      this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
      if(var1.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1))) {
         this.timeUntilReset = 40;
         this.needsInitilization = true;
         if(this.buyingPlayer != null) {
            this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
         } else {
            this.lastBuyingPlayer = null;
         }
      }

      if(var1.getItemToBuy().getItem() == Items.emerald) {
         this.wealth += var1.getItemToBuy().stackSize;
      }

   }

   public void func_110297_a_(ItemStack var1) {
      if(!super.worldObj.isRemote && super.livingSoundTime > -this.getTalkInterval() + 20) {
         super.livingSoundTime = -this.getTalkInterval();
         if(var1 != null) {
            this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
         } else {
            this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
         }
      }

   }

   public MerchantRecipeList getRecipes(EntityPlayer var1) {
      if(this.buyingList == null) {
         this.addDefaultEquipmentAndRecipies(1);
      }

      return this.buyingList;
   }

   private float adjustProbability(float var1) {
      float var2 = var1 + this.field_82191_bN;
      return var2 > 0.9F?0.9F - (var2 - 0.9F):var2;
   }

   private void addDefaultEquipmentAndRecipies(int var1) {
      if(this.buyingList != null) {
         this.field_82191_bN = MathHelper.sqrt_float((float)this.buyingList.size()) * 0.2F;
      } else {
         this.field_82191_bN = 0.0F;
      }

      MerchantRecipeList var2;
      var2 = new MerchantRecipeList();
      int var6;
      label50:
      switch(this.getProfession()) {
      case 0:
         func_146091_a(var2, Items.wheat, super.rand, this.adjustProbability(0.9F));
         func_146091_a(var2, Item.getItemFromBlock(Blocks.wool), super.rand, this.adjustProbability(0.5F));
         func_146091_a(var2, Items.chicken, super.rand, this.adjustProbability(0.5F));
         func_146091_a(var2, Items.cooked_fished, super.rand, this.adjustProbability(0.4F));
         func_146089_b(var2, Items.bread, super.rand, this.adjustProbability(0.9F));
         func_146089_b(var2, Items.melon, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.apple, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.cookie, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.shears, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.flint_and_steel, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.cooked_chicken, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.arrow, super.rand, this.adjustProbability(0.5F));
         if(super.rand.nextFloat() < this.adjustProbability(0.5F)) {
            var2.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10), new ItemStack(Items.emerald), new ItemStack(Items.flint, 4 + super.rand.nextInt(2), 0)));
         }
         break;
      case 1:
         func_146091_a(var2, Items.paper, super.rand, this.adjustProbability(0.8F));
         func_146091_a(var2, Items.book, super.rand, this.adjustProbability(0.8F));
         func_146091_a(var2, Items.written_book, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Item.getItemFromBlock(Blocks.bookshelf), super.rand, this.adjustProbability(0.8F));
         func_146089_b(var2, Item.getItemFromBlock(Blocks.glass), super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.compass, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.clock, super.rand, this.adjustProbability(0.2F));
         if(super.rand.nextFloat() < this.adjustProbability(0.07F)) {
            Enchantment var8 = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
            int var10 = MathHelper.getRandomIntegerInRange(super.rand, var8.getMinLevel(), var8.getMaxLevel());
            ItemStack var11 = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var8, var10));
            var6 = 2 + super.rand.nextInt(5 + var10 * 10) + 3 * var10;
            var2.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, var6), var11));
         }
         break;
      case 2:
         func_146089_b(var2, Items.ender_eye, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.experience_bottle, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.redstone, super.rand, this.adjustProbability(0.4F));
         func_146089_b(var2, Item.getItemFromBlock(Blocks.glowstone), super.rand, this.adjustProbability(0.3F));
         Item[] var3 = new Item[]{Items.iron_sword, Items.diamond_sword, Items.iron_chestplate, Items.diamond_chestplate, Items.iron_axe, Items.diamond_axe, Items.iron_pickaxe, Items.diamond_pickaxe};
         Item[] var4 = var3;
         int var5 = var3.length;
         var6 = 0;

         while(true) {
            if(var6 >= var5) {
               break label50;
            }

            Item var7 = var4[var6];
            if(super.rand.nextFloat() < this.adjustProbability(0.05F)) {
               var2.add(new MerchantRecipe(new ItemStack(var7, 1, 0), new ItemStack(Items.emerald, 2 + super.rand.nextInt(3), 0), EnchantmentHelper.addRandomEnchantment(super.rand, new ItemStack(var7, 1, 0), 5 + super.rand.nextInt(15))));
            }

            ++var6;
         }
      case 3:
         func_146091_a(var2, Items.coal, super.rand, this.adjustProbability(0.7F));
         func_146091_a(var2, Items.iron_ingot, super.rand, this.adjustProbability(0.5F));
         func_146091_a(var2, Items.gold_ingot, super.rand, this.adjustProbability(0.5F));
         func_146091_a(var2, Items.diamond, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.iron_sword, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.diamond_sword, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.iron_axe, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.diamond_axe, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.iron_pickaxe, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.diamond_pickaxe, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.iron_shovel, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_shovel, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.iron_hoe, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_hoe, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.iron_boots, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_boots, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.iron_helmet, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_helmet, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.iron_chestplate, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_chestplate, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.iron_leggings, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.diamond_leggings, super.rand, this.adjustProbability(0.2F));
         func_146089_b(var2, Items.chainmail_boots, super.rand, this.adjustProbability(0.1F));
         func_146089_b(var2, Items.chainmail_helmet, super.rand, this.adjustProbability(0.1F));
         func_146089_b(var2, Items.chainmail_chestplate, super.rand, this.adjustProbability(0.1F));
         func_146089_b(var2, Items.chainmail_leggings, super.rand, this.adjustProbability(0.1F));
         break;
      case 4:
         func_146091_a(var2, Items.coal, super.rand, this.adjustProbability(0.7F));
         func_146091_a(var2, Items.porkchop, super.rand, this.adjustProbability(0.5F));
         func_146091_a(var2, Items.beef, super.rand, this.adjustProbability(0.5F));
         func_146089_b(var2, Items.saddle, super.rand, this.adjustProbability(0.1F));
         func_146089_b(var2, Items.leather_chestplate, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.leather_boots, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.leather_helmet, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.leather_leggings, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.cooked_porkchop, super.rand, this.adjustProbability(0.3F));
         func_146089_b(var2, Items.cooked_beef, super.rand, this.adjustProbability(0.3F));
      }

      if(var2.isEmpty()) {
         func_146091_a(var2, Items.gold_ingot, super.rand, 1.0F);
      }

      Collections.shuffle(var2);
      if(this.buyingList == null) {
         this.buyingList = new MerchantRecipeList();
      }

      for(int var9 = 0; var9 < var1 && var9 < var2.size(); ++var9) {
         this.buyingList.addToListWithCheck((MerchantRecipe)var2.get(var9));
      }

   }

   public void setRecipes(MerchantRecipeList var1) {}

   private static void func_146091_a(MerchantRecipeList var0, Item var1, Random var2, float var3) {
      if(var2.nextFloat() < var3) {
         var0.add(new MerchantRecipe(func_146088_a(var1, var2), Items.emerald));
      }

   }

   private static ItemStack func_146088_a(Item var0, Random var1) {
      return new ItemStack(var0, func_146092_b(var0, var1), 0);
   }

   private static int func_146092_b(Item var0, Random var1) {
      Tuple var2 = (Tuple)villagersSellingList.get(var0);
      return var2 == null?1:(((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue()?((Integer)var2.getFirst()).intValue():((Integer)var2.getFirst()).intValue() + var1.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
   }

   private static void func_146089_b(MerchantRecipeList var0, Item var1, Random var2, float var3) {
      if(var2.nextFloat() < var3) {
         int var4 = func_146090_c(var1, var2);
         ItemStack var5;
         ItemStack var6;
         if(var4 < 0) {
            var5 = new ItemStack(Items.emerald, 1, 0);
            var6 = new ItemStack(var1, -var4, 0);
         } else {
            var5 = new ItemStack(Items.emerald, var4, 0);
            var6 = new ItemStack(var1, 1, 0);
         }

         var0.add(new MerchantRecipe(var5, var6));
      }

   }

   private static int func_146090_c(Item var0, Random var1) {
      Tuple var2 = (Tuple)blacksmithSellingList.get(var0);
      return var2 == null?1:(((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue()?((Integer)var2.getFirst()).intValue():((Integer)var2.getFirst()).intValue() + var1.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
   }

   public void handleHealthUpdate(byte var1) {
      if(var1 == 12) {
         this.generateRandomParticles("heart");
      } else if(var1 == 13) {
         this.generateRandomParticles("angryVillager");
      } else if(var1 == 14) {
         this.generateRandomParticles("happyVillager");
      } else {
         super.handleHealthUpdate(var1);
      }

   }

   private void generateRandomParticles(String var1) {
      for(int var2 = 0; var2 < 5; ++var2) {
         double var3 = super.rand.nextGaussian() * 0.02D;
         double var5 = super.rand.nextGaussian() * 0.02D;
         double var7 = super.rand.nextGaussian() * 0.02D;
         super.worldObj.spawnParticle(var1, super.posX + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, super.posY + 1.0D + (double)(super.rand.nextFloat() * super.height), super.posZ + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, var3, var5, var7);
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData var1) {
      var1 = super.onSpawnWithEgg(var1);
      this.setProfession(super.worldObj.rand.nextInt(5));
      return var1;
   }

   public void setLookingForHome() {
      this.isLookingForHome = true;
   }

   public EntityVillager createChild(EntityAgeable var1) {
      EntityVillager var2 = new EntityVillager(super.worldObj);
      var2.onSpawnWithEgg((IEntityLivingData)null);
      return var2;
   }

   public boolean allowLeashing() {
      return false;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   static {
      villagersSellingList.put(Items.coal, new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
      villagersSellingList.put(Items.iron_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      villagersSellingList.put(Items.gold_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      villagersSellingList.put(Items.diamond, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      villagersSellingList.put(Items.paper, new Tuple(Integer.valueOf(24), Integer.valueOf(36)));
      villagersSellingList.put(Items.book, new Tuple(Integer.valueOf(11), Integer.valueOf(13)));
      villagersSellingList.put(Items.written_book, new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
      villagersSellingList.put(Items.ender_pearl, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      villagersSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
      villagersSellingList.put(Items.porkchop, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.beef, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.chicken, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.cooked_fished, new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
      villagersSellingList.put(Items.wheat_seeds, new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
      villagersSellingList.put(Items.melon_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
      villagersSellingList.put(Items.pumpkin_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
      villagersSellingList.put(Items.wheat, new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
      villagersSellingList.put(Item.getItemFromBlock(Blocks.wool), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
      villagersSellingList.put(Items.rotten_flesh, new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
      blacksmithSellingList.put(Items.flint_and_steel, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.shears, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.iron_sword, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.diamond_sword, new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.iron_axe, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.diamond_axe, new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.iron_pickaxe, new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
      blacksmithSellingList.put(Items.diamond_pickaxe, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.iron_shovel, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_shovel, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_hoe, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_hoe, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_boots, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_boots, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_helmet, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_helmet, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_chestplate, new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.diamond_chestplate, new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
      blacksmithSellingList.put(Items.iron_leggings, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      blacksmithSellingList.put(Items.diamond_leggings, new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.chainmail_boots, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
      blacksmithSellingList.put(Items.chainmail_helmet, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
      blacksmithSellingList.put(Items.chainmail_chestplate, new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
      blacksmithSellingList.put(Items.chainmail_leggings, new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.bread, new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
      blacksmithSellingList.put(Items.melon, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
      blacksmithSellingList.put(Items.apple, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
      blacksmithSellingList.put(Items.cookie, new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glass), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.bookshelf), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_chestplate, new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
      blacksmithSellingList.put(Items.leather_boots, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_helmet, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_leggings, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.saddle, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.experience_bottle, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.redstone, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.compass, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.clock, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glowstone), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.cooked_porkchop, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
      blacksmithSellingList.put(Items.cooked_beef, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
      blacksmithSellingList.put(Items.cooked_chicken, new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
      blacksmithSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.arrow, new Tuple(Integer.valueOf(-12), Integer.valueOf(-8)));
   }
}
