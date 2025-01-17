package de.ItsAMysterious.mods.reallifemod;

import cpw.mods.fml.common.registry.GameRegistry;
import de.ItsAMysterious.mods.reallifemod.RealLifeMod;
import de.ItsAMysterious.mods.reallifemod.api.util.Materials;
import de.ItsAMysterious.mods.reallifemod.core.blocks.BlockAirExtended;
import de.ItsAMysterious.mods.reallifemod.core.blocks.BlockFakeDiamond;
import de.ItsAMysterious.mods.reallifemod.core.blocks.BlockFlooring;
import de.ItsAMysterious.mods.reallifemod.core.blocks.ResourceBlock;
import de.ItsAMysterious.mods.reallifemod.core.blocks.Industries.blockFurnace;
import de.ItsAMysterious.mods.reallifemod.core.blocks.Industries.blockMeltedIron;
import de.ItsAMysterious.mods.reallifemod.core.blocks.crops.blockMaryGold;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockBabyBed;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockComputer;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockDesk;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockDrawer;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockFreezer;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockFridge;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.BlockSideboard;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockChair;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockCupboard;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockDishwasher;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockEspresso;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockGrowpot;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockHeating;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockNeonLamp;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockRadio;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockRailing;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockSafe;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockShelf;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockSink;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockTV;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockTable;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockToaster;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockcabinet;
import de.ItsAMysterious.mods.reallifemod.core.blocks.furniture.blockfishtank;
import de.ItsAMysterious.mods.reallifemod.core.blocks.indoor.sanitary.BlockPissoir;
import de.ItsAMysterious.mods.reallifemod.core.blocks.indoor.sanitary.BlockWashingbasin;
import de.ItsAMysterious.mods.reallifemod.core.blocks.indoor.sanitary.FloorTile;
import de.ItsAMysterious.mods.reallifemod.core.blocks.indoor.sanitary.blockToilet;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockBuchs;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockCookingRod;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockFirTree;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockFountain;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockLawnRobotRim;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockTrap_Fall;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockTrap_Hold;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockTrap_Kill;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockTree;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.BlockWindow;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.OakTreeBlock;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.bankBlock;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.blockRooftile;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.fireplaceBlock;
import de.ItsAMysterious.mods.reallifemod.core.blocks.outdoor.pillarBlock;
import de.ItsAMysterious.mods.reallifemod.core.blocks.publicenvironment.BlockDrinksMachine;
import de.ItsAMysterious.mods.reallifemod.core.blocks.publicenvironment.blockATM;
import de.ItsAMysterious.mods.reallifemod.core.blocks.seasonalstuff.blockChristmasPyramid;
import de.ItsAMysterious.mods.reallifemod.core.blocks.seasonalstuff.blockChristmasTree;
import de.ItsAMysterious.mods.reallifemod.core.blocks.seasonalstuff.doorwreath;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.BlockLantern;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.BlockTar;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.BlockTrafficLight;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.Block_SpeedBoost;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.SidewalkRim;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.blockBilboard;
import de.ItsAMysterious.mods.reallifemod.core.blocks.street.crashbarrierBl;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class RealLifeMod_Blocks {

   public static Block meltedIron;
   public static Block Steel;
   public static Block blastFurnace;
   public static Block freezer;
   public static Block boxbush;
   public static Block PC;
   public static Block advertisingShield;
   public static Block tile;
   public static Block cupboard;
   public static Block chair;
   public static Block basin;
   public static Block television;
   public static Block blockTar;
   public static Block SidewalkRim;
   public static Block BlockTarLine;
   public static Block BlockTarLine2;
   public static Block lantern;
   public static Block TrafficLight;
   public static Block DrinksMachine;
   public static Block NeonLamp;
   public static Block urinal;
   public static Block mirror;
   public static Block deskLamp;
   public static Block christmaspyramid;
   public static Block christmastree;
   public static Block Doorwreath;
   public static Block Marmor;
   public static Block cb;
   public static Block pillarM;
   public static Block pillarI;
   public static Block pillarB;
   public static Block safe;
   public static Block bank;
   public static Block table;
   public static Block radio;
   public static Block fireplace;
   public static Block air;
   public static Block toaster;
   public static Block atm;
   public static Block heating;
   public static Block rooftile;
   public static Block rooftile1;
   public static Block rooftile2;
   public static Block toilet;
   public static Block fishtank;
   public static Block kitchencabinet;
   public static Block shelf;
   public static Block dishwasher;
   public static Block sink;
   public static Block marygold;
   public static Block growpot;
   public static Block parquet;
   public static Block espresso;
   public static Block desk;
   public static Block railing;
   public static Block lenoleum;
   public static Block railingleft;
   public static Block falltrap;
   public static Block holdtrap;
   public static Block killtrap;
   public static Block drawer;
   public static Block Sideboard;
   public static Block firtree;
   public static Block birch_tree;
   public static Block fridge;
   public static Block diamond;
   public static Block oaktree;
   public static Block fountain;
   public static Block window;
   public static Block babybed;
   public static Block lawnmowerrim;
   public static Block cookingrod;
   public static Fluid meltedI = new Fluid("meltedIron");
   public static List rlmBlockList = new ArrayList();
   private static Block cork;


   public static void defineBlocks() {
      FluidRegistry.registerFluid(meltedI);
      meltedIron = (new blockMeltedIron(meltedI)).func_149663_c("ironmeldt");
      meltedI.setBlock(meltedIron);
      meltedI.setUnlocalizedName(meltedIron.getUnlocalizedName());
      rlmBlockList.add(boxbush = (new BlockBuchs(Material.grass)).func_149647_a(RealLifeMod.Outdoor).setStepSound(Block.soundTypeGrass));
      rlmBlockList.add(advertisingShield = (new blockBilboard(Material.wood)).func_149647_a(RealLifeMod.RLMTechnologie));
      rlmBlockList.add(tile = (new FloorTile(Material.rock)).func_149647_a(RealLifeMod.RLMTechnologie));
      rlmBlockList.add(blastFurnace = (new blockFurnace()).func_149647_a(RealLifeMod.RLMTechnologie));
      rlmBlockList.add(freezer = (new BlockFreezer()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(cupboard = (new blockCupboard()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(PC = (new BlockComputer(Material.iron)).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(chair = (new blockChair()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(television = (new blockTV()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(SidewalkRim = (new SidewalkRim()).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(lantern = (new BlockLantern()).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(TrafficLight = (new BlockTrafficLight()).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(blockTar = (new BlockTar()).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(BlockTarLine = (new BlockTar()).func_149658_d("reallifemod:tarWhiteLine").setBlockName("whiteline1").setCreativeTab(RealLifeMod.Streets));
      rlmBlockList.add(BlockTarLine2 = (new BlockTar()).func_149658_d("reallifemod:tarWhiteLine2").setBlockName("whiteline2").setCreativeTab(RealLifeMod.Streets));
      rlmBlockList.add(DrinksMachine = (new BlockDrinksMachine()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(NeonLamp = (new blockNeonLamp()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(urinal = (new BlockPissoir()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(basin = (new BlockWashingbasin()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(christmaspyramid = (new blockChristmasPyramid()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(christmastree = (new blockChristmasTree()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(Doorwreath = (new doorwreath()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(Marmor = new ResourceBlock("marmor", Material.rock));
      rlmBlockList.add(cb = (new crashbarrierBl()).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(pillarB = (new pillarBlock(Material.rock, pillarBlock.pillarType.ROCK)).func_149658_d("minecraft:brick").setBlockName("pillar-bricks"));
      rlmBlockList.add(pillarI = (new pillarBlock(Material.iron, pillarBlock.pillarType.IRON)).func_149658_d("minecraft:iron_block").setBlockName("pillar-iron"));
      rlmBlockList.add(pillarM = (new pillarBlock(Materials.marmor, pillarBlock.pillarType.MARMOR)).func_149658_d("reallifemod:marmor").setBlockName("pillar-marmor"));
      rlmBlockList.add(safe = (new blockSafe()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(bank = (new bankBlock()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(table = (new blockTable()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(radio = (new blockRadio()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(fireplace = (new fireplaceBlock()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(toaster = (new blockToaster()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(atm = (new blockATM()).func_149647_a(RealLifeMod.TLMEverydaylife));
      rlmBlockList.add(heating = (new blockHeating()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(rooftile = (new blockRooftile(0)).func_149647_a(RealLifeMod.Outdoor).setBlockName("Rooftile"));
      rlmBlockList.add(rooftile1 = (new blockRooftile(1)).func_149647_a(RealLifeMod.Outdoor).setBlockName("Rooftile-Edge1"));
      rlmBlockList.add(rooftile2 = (new blockRooftile(2)).func_149647_a(RealLifeMod.Outdoor).setBlockName("Rooftile-Edge2"));
      rlmBlockList.add(toilet = (new blockToilet()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(fishtank = (new blockfishtank()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(kitchencabinet = (new blockcabinet()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(shelf = (new blockShelf()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(dishwasher = (new blockDishwasher()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(sink = (new blockSink()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(marygold = (new blockMaryGold()).func_149647_a(RealLifeMod.medicine));
      rlmBlockList.add(growpot = new blockGrowpot(Material.clay));
      rlmBlockList.add(parquet = (new ResourceBlock(Material.wood)).func_149663_c("parquetBlock").setHardness(2.0F).setResistance(5.0F).setCreativeTab(RealLifeMod.Furniture).setBlockTextureName("reallifemod:parquet"));
      rlmBlockList.add(parquet = (new BlockFlooring(Material.wood)).func_149663_c("parquet").setHardness(2.0F).setResistance(5.0F).setCreativeTab(RealLifeMod.Furniture).setBlockTextureName("reallifemod:parquet"));
      rlmBlockList.add(lenoleum = (new BlockFlooring(Materials.plastic)).func_149663_c("lenoleum").setCreativeTab(RealLifeMod.Furniture).setBlockTextureName("reallifemod:lenoleum"));
      rlmBlockList.add(espresso = (new blockEspresso()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(desk = (new BlockDesk()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(railing = (new blockRailing()).func_149647_a(RealLifeMod.Furniture).setHardness(2.0F).setResistance(5.0F));
      rlmBlockList.add(falltrap = (new BlockTrap_Fall(Material.grass)).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(holdtrap = (new BlockTrap_Hold(Material.grass)).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(killtrap = (new BlockTrap_Kill(Material.grass)).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add((new BlockDrawer()).func_149647_a(RealLifeMod.Furniture).setHardness(2.0F).setResistance(5.0F));
      rlmBlockList.add((new BlockTree()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(Sideboard = (new BlockSideboard()).func_149711_c(2.0F).setResistance(5.0F));
      rlmBlockList.add(firtree = (new BlockFirTree()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(fridge = new BlockFridge());
      rlmBlockList.add(new BlockFakeDiamond());
      rlmBlockList.add(oaktree = (new OakTreeBlock()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add((new Block_SpeedBoost(Material.rock)).func_149647_a(RealLifeMod.Streets));
      rlmBlockList.add(fountain = (new BlockFountain()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(window = (new BlockWindow()).func_149647_a(RealLifeMod.Outdoor).setHardness(2.0F).setResistance(5.0F));
      rlmBlockList.add(babybed = (new BlockBabyBed()).func_149647_a(RealLifeMod.Furniture));
      rlmBlockList.add(lawnmowerrim = new BlockLawnRobotRim());
      rlmBlockList.add(cookingrod = (new BlockCookingRod()).func_149647_a(RealLifeMod.Outdoor));
      rlmBlockList.add(cork = (new ResourceBlock(Material.wood)).func_149658_d("reallifemod:corc").setCreativeTab(RealLifeMod.Furniture));
      GameRegistry.registerBlock(air = new BlockAirExtended(), "airExtended");
      registerBlocks();
   }

   public static void registerBlocks() {
      for(int i = 0; i < rlmBlockList.size(); ++i) {
         GameRegistry.registerBlock((Block)rlmBlockList.get(i), ((Block)rlmBlockList.get(i)).getUnlocalizedName());
      }

   }

}
