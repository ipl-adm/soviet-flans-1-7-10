package net.minecraft.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList$EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityList {

   private static final Logger logger = LogManager.getLogger();
   private static Map stringToClassMapping = new HashMap();
   private static Map classToStringMapping = new HashMap();
   private static Map IDtoClassMapping = new HashMap();
   private static Map classToIDMapping = new HashMap();
   private static Map stringToIDMapping = new HashMap();
   public static HashMap entityEggs = new LinkedHashMap();


   private static void addMapping(Class var0, String var1, int var2) {
      if(stringToClassMapping.containsKey(var1)) {
         throw new IllegalArgumentException("ID is already registered: " + var1);
      } else if(IDtoClassMapping.containsKey(Integer.valueOf(var2))) {
         throw new IllegalArgumentException("ID is already registered: " + var2);
      } else {
         stringToClassMapping.put(var1, var0);
         classToStringMapping.put(var0, var1);
         IDtoClassMapping.put(Integer.valueOf(var2), var0);
         classToIDMapping.put(var0, Integer.valueOf(var2));
         stringToIDMapping.put(var1, Integer.valueOf(var2));
      }
   }

   private static void addMapping(Class var0, String var1, int var2, int var3, int var4) {
      addMapping(var0, var1, var2);
      entityEggs.put(Integer.valueOf(var2), new EntityList$EntityEggInfo(var2, var3, var4));
   }

   public static Entity createEntityByName(String var0, World var1) {
      Entity var2 = null;

      try {
         Class var3 = (Class)stringToClassMapping.get(var0);
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var1});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
      Entity var2 = null;
      if("Minecart".equals(var0.getString("id"))) {
         switch(var0.getInteger("Type")) {
         case 0:
            var0.setString("id", "MinecartRideable");
            break;
         case 1:
            var0.setString("id", "MinecartChest");
            break;
         case 2:
            var0.setString("id", "MinecartFurnace");
         }

         var0.removeTag("Type");
      }

      try {
         Class var3 = (Class)stringToClassMapping.get(var0.getString("id"));
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var1});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if(var2 != null) {
         var2.readFromNBT(var0);
      } else {
         logger.warn("Skipping Entity with id " + var0.getString("id"));
      }

      return var2;
   }

   public static Entity createEntityByID(int var0, World var1) {
      Entity var2 = null;

      try {
         Class var3 = getClassFromID(var0);
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{var1});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if(var2 == null) {
         logger.warn("Skipping Entity with id " + var0);
      }

      return var2;
   }

   public static int getEntityID(Entity var0) {
      Class var1 = var0.getClass();
      return classToIDMapping.containsKey(var1)?((Integer)classToIDMapping.get(var1)).intValue():0;
   }

   public static Class getClassFromID(int var0) {
      return (Class)IDtoClassMapping.get(Integer.valueOf(var0));
   }

   public static String getEntityString(Entity var0) {
      return (String)classToStringMapping.get(var0.getClass());
   }

   public static String getStringFromID(int var0) {
      Class var1 = getClassFromID(var0);
      return var1 != null?(String)classToStringMapping.get(var1):null;
   }

   public static void func_151514_a() {}

   public static Set func_151515_b() {
      return Collections.unmodifiableSet(stringToIDMapping.keySet());
   }

   static {
      addMapping(EntityItem.class, "Item", 1);
      addMapping(EntityXPOrb.class, "XPOrb", 2);
      addMapping(EntityLeashKnot.class, "LeashKnot", 8);
      addMapping(EntityPainting.class, "Painting", 9);
      addMapping(EntityArrow.class, "Arrow", 10);
      addMapping(EntitySnowball.class, "Snowball", 11);
      addMapping(EntityLargeFireball.class, "Fireball", 12);
      addMapping(EntitySmallFireball.class, "SmallFireball", 13);
      addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
      addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
      addMapping(EntityPotion.class, "ThrownPotion", 16);
      addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
      addMapping(EntityItemFrame.class, "ItemFrame", 18);
      addMapping(EntityWitherSkull.class, "WitherSkull", 19);
      addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
      addMapping(EntityFallingBlock.class, "FallingSand", 21);
      addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
      addMapping(EntityBoat.class, "Boat", 41);
      addMapping(EntityMinecartEmpty.class, "MinecartRideable", 42);
      addMapping(EntityMinecartChest.class, "MinecartChest", 43);
      addMapping(EntityMinecartFurnace.class, "MinecartFurnace", 44);
      addMapping(EntityMinecartTNT.class, "MinecartTNT", 45);
      addMapping(EntityMinecartHopper.class, "MinecartHopper", 46);
      addMapping(EntityMinecartMobSpawner.class, "MinecartSpawner", 47);
      addMapping(EntityMinecartCommandBlock.class, "MinecartCommandBlock", 40);
      addMapping(EntityLiving.class, "Mob", 48);
      addMapping(EntityMob.class, "Monster", 49);
      addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
      addMapping(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
      addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
      addMapping(EntityGiantZombie.class, "Giant", 53);
      addMapping(EntityZombie.class, "Zombie", 54, '\uafaf', 7969893);
      addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
      addMapping(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
      addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
      addMapping(EntityEnderman.class, "Enderman", 58, 1447446, 0);
      addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
      addMapping(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
      addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
      addMapping(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
      addMapping(EntityDragon.class, "EnderDragon", 63);
      addMapping(EntityWither.class, "WitherBoss", 64);
      addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
      addMapping(EntityWitch.class, "Witch", 66, 3407872, 5349438);
      addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
      addMapping(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
      addMapping(EntityCow.class, "Cow", 92, 4470310, 10592673);
      addMapping(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
      addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
      addMapping(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
      addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
      addMapping(EntitySnowman.class, "SnowMan", 97);
      addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
      addMapping(EntityIronGolem.class, "VillagerGolem", 99);
      addMapping(EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
      addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
      addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
   }
}
