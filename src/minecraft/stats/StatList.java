package net.minecraft.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList$EntityEggInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatCrafting;
import net.minecraft.util.ChatComponentTranslation;

public class StatList {

   protected static Map oneShotStats = new HashMap();
   public static List allStats = new ArrayList();
   public static List generalStats = new ArrayList();
   public static List itemStats = new ArrayList();
   public static List objectMineStats = new ArrayList();
   public static StatBase leaveGameStat = (new StatBasic("stat.leaveGame", new ChatComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
   public static StatBase minutesPlayedStat = (new StatBasic("stat.playOneMinute", new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
   public static StatBase distanceWalkedStat = (new StatBasic("stat.walkOneCm", new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceSwumStat = (new StatBasic("stat.swimOneCm", new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceFallenStat = (new StatBasic("stat.fallOneCm", new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceClimbedStat = (new StatBasic("stat.climbOneCm", new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceFlownStat = (new StatBasic("stat.flyOneCm", new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceDoveStat = (new StatBasic("stat.diveOneCm", new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceByMinecartStat = (new StatBasic("stat.minecartOneCm", new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceByBoatStat = (new StatBasic("stat.boatOneCm", new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase distanceByPigStat = (new StatBasic("stat.pigOneCm", new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase field_151185_q = (new StatBasic("stat.horseOneCm", new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
   public static StatBase jumpStat = (new StatBasic("stat.jump", new ChatComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
   public static StatBase dropStat = (new StatBasic("stat.drop", new ChatComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
   public static StatBase damageDealtStat = (new StatBasic("stat.damageDealt", new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k)).registerStat();
   public static StatBase damageTakenStat = (new StatBasic("stat.damageTaken", new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k)).registerStat();
   public static StatBase deathsStat = (new StatBasic("stat.deaths", new ChatComponentTranslation("stat.deaths", new Object[0]))).registerStat();
   public static StatBase mobKillsStat = (new StatBasic("stat.mobKills", new ChatComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
   public static StatBase field_151186_x = (new StatBasic("stat.animalsBred", new ChatComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
   public static StatBase playerKillsStat = (new StatBasic("stat.playerKills", new ChatComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
   public static StatBase fishCaughtStat = (new StatBasic("stat.fishCaught", new ChatComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
   public static StatBase field_151183_A = (new StatBasic("stat.junkFished", new ChatComponentTranslation("stat.junkFished", new Object[0]))).registerStat();
   public static StatBase field_151184_B = (new StatBasic("stat.treasureFished", new ChatComponentTranslation("stat.treasureFished", new Object[0]))).registerStat();
   public static final StatBase[] mineBlockStatArray = new StatBase[4096];
   public static final StatBase[] objectCraftStats = new StatBase[32000];
   public static final StatBase[] objectUseStats = new StatBase[32000];
   public static final StatBase[] objectBreakStats = new StatBase[32000];


   public static void func_151178_a() {
      func_151181_c();
      initStats();
      func_151179_e();
      initCraftableStats();
      AchievementList.init();
      EntityList.func_151514_a();
   }

   private static void initCraftableStats() {
      HashSet var0 = new HashSet();
      Iterator var1 = CraftingManager.getInstance().getRecipeList().iterator();

      while(var1.hasNext()) {
         IRecipe var2 = (IRecipe)var1.next();
         if(var2.getRecipeOutput() != null) {
            var0.add(var2.getRecipeOutput().getItem());
         }
      }

      var1 = FurnaceRecipes.smelting().getSmeltingList().values().iterator();

      while(var1.hasNext()) {
         ItemStack var4 = (ItemStack)var1.next();
         var0.add(var4.getItem());
      }

      var1 = var0.iterator();

      while(var1.hasNext()) {
         Item var5 = (Item)var1.next();
         if(var5 != null) {
            int var3 = Item.getIdFromItem(var5);
            objectCraftStats[var3] = (new StatCrafting("stat.craftItem." + var3, new ChatComponentTranslation("stat.craftItem", new Object[]{(new ItemStack(var5)).func_151000_E()}), var5)).registerStat();
         }
      }

      replaceAllSimilarBlocks(objectCraftStats);
   }

   private static void func_151181_c() {
      Iterator var0 = Block.blockRegistry.iterator();

      while(var0.hasNext()) {
         Block var1 = (Block)var0.next();
         if(Item.getItemFromBlock(var1) != null) {
            int var2 = Block.getIdFromBlock(var1);
            if(var1.getEnableStats()) {
               mineBlockStatArray[var2] = (new StatCrafting("stat.mineBlock." + var2, new ChatComponentTranslation("stat.mineBlock", new Object[]{(new ItemStack(var1)).func_151000_E()}), Item.getItemFromBlock(var1))).registerStat();
               objectMineStats.add((StatCrafting)mineBlockStatArray[var2]);
            }
         }
      }

      replaceAllSimilarBlocks(mineBlockStatArray);
   }

   private static void initStats() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if(var1 != null) {
            int var2 = Item.getIdFromItem(var1);
            objectUseStats[var2] = (new StatCrafting("stat.useItem." + var2, new ChatComponentTranslation("stat.useItem", new Object[]{(new ItemStack(var1)).func_151000_E()}), var1)).registerStat();
            if(!(var1 instanceof ItemBlock)) {
               itemStats.add((StatCrafting)objectUseStats[var2]);
            }
         }
      }

      replaceAllSimilarBlocks(objectUseStats);
   }

   private static void func_151179_e() {
      Iterator var0 = Item.itemRegistry.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if(var1 != null) {
            int var2 = Item.getIdFromItem(var1);
            if(var1.isDamageable()) {
               objectBreakStats[var2] = (new StatCrafting("stat.breakItem." + var2, new ChatComponentTranslation("stat.breakItem", new Object[]{(new ItemStack(var1)).func_151000_E()}), var1)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(objectBreakStats);
   }

   private static void replaceAllSimilarBlocks(StatBase[] var0) {
      func_151180_a(var0, Blocks.water, Blocks.flowing_water);
      func_151180_a(var0, Blocks.lava, Blocks.flowing_lava);
      func_151180_a(var0, Blocks.lit_pumpkin, Blocks.pumpkin);
      func_151180_a(var0, Blocks.lit_furnace, Blocks.furnace);
      func_151180_a(var0, Blocks.lit_redstone_ore, Blocks.redstone_ore);
      func_151180_a(var0, Blocks.powered_repeater, Blocks.unpowered_repeater);
      func_151180_a(var0, Blocks.powered_comparator, Blocks.unpowered_comparator);
      func_151180_a(var0, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
      func_151180_a(var0, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
      func_151180_a(var0, Blocks.red_mushroom, Blocks.brown_mushroom);
      func_151180_a(var0, Blocks.double_stone_slab, Blocks.stone_slab);
      func_151180_a(var0, Blocks.double_wooden_slab, Blocks.wooden_slab);
      func_151180_a(var0, Blocks.grass, Blocks.dirt);
      func_151180_a(var0, Blocks.farmland, Blocks.dirt);
   }

   private static void func_151180_a(StatBase[] var0, Block var1, Block var2) {
      int var3 = Block.getIdFromBlock(var1);
      int var4 = Block.getIdFromBlock(var2);
      if(var0[var3] != null && var0[var4] == null) {
         var0[var4] = var0[var3];
      } else {
         allStats.remove(var0[var3]);
         objectMineStats.remove(var0[var3]);
         generalStats.remove(var0[var3]);
         var0[var3] = var0[var4];
      }
   }

   public static StatBase func_151182_a(EntityList$EntityEggInfo var0) {
      String var1 = EntityList.getStringFromID(var0.spawnedID);
      return var1 == null?null:(new StatBase("stat.killEntity." + var1, new ChatComponentTranslation("stat.entityKill", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase func_151176_b(EntityList$EntityEggInfo var0) {
      String var1 = EntityList.getStringFromID(var0.spawnedID);
      return var1 == null?null:(new StatBase("stat.entityKilledBy." + var1, new ChatComponentTranslation("stat.entityKilledBy", new Object[]{new ChatComponentTranslation("entity." + var1 + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase func_151177_a(String var0) {
      return (StatBase)oneShotStats.get(var0);
   }

}
