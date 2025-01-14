package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.client.particle.EntityFishWakeFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntityFootStepFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntityHugeExplodeFX;
import net.minecraft.client.particle.EntityLargeExplodeFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntitySnowShovelFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntitySuspendFX;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.EntitySorter;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlCapsChecker;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal$1;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.RenderSorter;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.util.RenderDistanceSorter;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition$MovingObjectType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

public class RenderGlobal implements IWorldAccess {

   private static final Logger logger = LogManager.getLogger();
   private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
   private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
   private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
   private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
   public List tileEntities = new ArrayList();
   private WorldClient theWorld;
   private final TextureManager renderEngine;
   private List worldRenderersToUpdate = new ArrayList();
   private WorldRenderer[] sortedWorldRenderers;
   private WorldRenderer[] worldRenderers;
   private int renderChunksWide;
   private int renderChunksTall;
   private int renderChunksDeep;
   private int glRenderListBase;
   private Minecraft mc;
   private RenderBlocks renderBlocksRg;
   private IntBuffer glOcclusionQueryBase;
   private boolean occlusionEnabled;
   private int cloudTickCounter;
   private int starGLCallList;
   private int glSkyList;
   private int glSkyList2;
   private int minBlockX;
   private int minBlockY;
   private int minBlockZ;
   private int maxBlockX;
   private int maxBlockY;
   private int maxBlockZ;
   private final Map damagedBlocks = new HashMap();
   private final Map mapSoundPositions = Maps.newHashMap();
   private IIcon[] destroyBlockIcons;
   private boolean displayListEntitiesDirty;
   private int displayListEntities;
   private int renderDistanceChunks = -1;
   private int renderEntitiesStartupCounter = 2;
   private int countEntitiesTotal;
   private int countEntitiesRendered;
   private int countEntitiesHidden;
   IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
   private int renderersLoaded;
   private int renderersBeingClipped;
   private int renderersBeingOccluded;
   private int renderersBeingRendered;
   private int renderersSkippingRenderPass;
   private int dummyRenderInt;
   private int worldRenderersCheckIndex;
   private List glRenderLists = new ArrayList();
   private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};
   double prevSortX = -9999.0D;
   double prevSortY = -9999.0D;
   double prevSortZ = -9999.0D;
   double prevRenderSortX = -9999.0D;
   double prevRenderSortY = -9999.0D;
   double prevRenderSortZ = -9999.0D;
   int prevChunkSortX = -999;
   int prevChunkSortY = -999;
   int prevChunkSortZ = -999;
   int frustumCheckOffset;


   public RenderGlobal(Minecraft var1) {
      this.mc = var1;
      this.renderEngine = var1.getTextureManager();
      byte var2 = 34;
      byte var3 = 16;
      this.glRenderListBase = GLAllocation.generateDisplayLists(var2 * var2 * var3 * 3);
      this.displayListEntitiesDirty = false;
      this.displayListEntities = GLAllocation.generateDisplayLists(1);
      this.occlusionEnabled = OpenGlCapsChecker.checkARBOcclusion();
      if(this.occlusionEnabled) {
         this.occlusionResult.clear();
         this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(var2 * var2 * var3);
         this.glOcclusionQueryBase.clear();
         this.glOcclusionQueryBase.position(0);
         this.glOcclusionQueryBase.limit(var2 * var2 * var3);
         ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
      }

      this.starGLCallList = GLAllocation.generateDisplayLists(3);
      GL11.glPushMatrix();
      GL11.glNewList(this.starGLCallList, 4864);
      this.renderStars();
      GL11.glEndList();
      GL11.glPopMatrix();
      Tessellator var4 = Tessellator.instance;
      this.glSkyList = this.starGLCallList + 1;
      GL11.glNewList(this.glSkyList, 4864);
      byte var6 = 64;
      int var7 = 256 / var6 + 2;
      float var5 = 16.0F;

      int var8;
      int var9;
      for(var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
         for(var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
            var4.startDrawingQuads();
            var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + 0));
            var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + 0));
            var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + var6));
            var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + var6));
            var4.draw();
         }
      }

      GL11.glEndList();
      this.glSkyList2 = this.starGLCallList + 2;
      GL11.glNewList(this.glSkyList2, 4864);
      var5 = -16.0F;
      var4.startDrawingQuads();

      for(var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
         for(var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
            var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + 0));
            var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + 0));
            var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + var6));
            var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + var6));
         }
      }

      var4.draw();
      GL11.glEndList();
   }

   private void renderStars() {
      Random var1 = new Random(10842L);
      Tessellator var2 = Tessellator.instance;
      var2.startDrawingQuads();

      for(int var3 = 0; var3 < 1500; ++var3) {
         double var4 = (double)(var1.nextFloat() * 2.0F - 1.0F);
         double var6 = (double)(var1.nextFloat() * 2.0F - 1.0F);
         double var8 = (double)(var1.nextFloat() * 2.0F - 1.0F);
         double var10 = (double)(0.15F + var1.nextFloat() * 0.1F);
         double var12 = var4 * var4 + var6 * var6 + var8 * var8;
         if(var12 < 1.0D && var12 > 0.01D) {
            var12 = 1.0D / Math.sqrt(var12);
            var4 *= var12;
            var6 *= var12;
            var8 *= var12;
            double var14 = var4 * 100.0D;
            double var16 = var6 * 100.0D;
            double var18 = var8 * 100.0D;
            double var20 = Math.atan2(var4, var8);
            double var22 = Math.sin(var20);
            double var24 = Math.cos(var20);
            double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
            double var28 = Math.sin(var26);
            double var30 = Math.cos(var26);
            double var32 = var1.nextDouble() * 3.141592653589793D * 2.0D;
            double var34 = Math.sin(var32);
            double var36 = Math.cos(var32);

            for(int var38 = 0; var38 < 4; ++var38) {
               double var39 = 0.0D;
               double var41 = (double)((var38 & 2) - 1) * var10;
               double var43 = (double)((var38 + 1 & 2) - 1) * var10;
               double var47 = var41 * var36 - var43 * var34;
               double var49 = var43 * var36 + var41 * var34;
               double var53 = var47 * var28 + var39 * var30;
               double var55 = var39 * var28 - var47 * var30;
               double var57 = var55 * var22 - var49 * var24;
               double var61 = var49 * var22 + var55 * var24;
               var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
            }
         }
      }

      var2.draw();
   }

   public void setWorldAndLoadRenderers(WorldClient var1) {
      if(this.theWorld != null) {
         this.theWorld.removeWorldAccess(this);
      }

      this.prevSortX = -9999.0D;
      this.prevSortY = -9999.0D;
      this.prevSortZ = -9999.0D;
      this.prevRenderSortX = -9999.0D;
      this.prevRenderSortY = -9999.0D;
      this.prevRenderSortZ = -9999.0D;
      this.prevChunkSortX = -9999;
      this.prevChunkSortY = -9999;
      this.prevChunkSortZ = -9999;
      RenderManager.instance.set(var1);
      this.theWorld = var1;
      this.renderBlocksRg = new RenderBlocks(var1);
      if(var1 != null) {
         var1.addWorldAccess(this);
         this.loadRenderers();
      }

   }

   public void loadRenderers() {
      if(this.theWorld != null) {
         Blocks.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
         Blocks.leaves2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
         this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
         int var1;
         if(this.worldRenderers != null) {
            for(var1 = 0; var1 < this.worldRenderers.length; ++var1) {
               this.worldRenderers[var1].stopRendering();
            }
         }

         var1 = this.renderDistanceChunks * 2 + 1;
         this.renderChunksWide = var1;
         this.renderChunksTall = 16;
         this.renderChunksDeep = var1;
         this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
         this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
         int var2 = 0;
         int var3 = 0;
         this.minBlockX = 0;
         this.minBlockY = 0;
         this.minBlockZ = 0;
         this.maxBlockX = this.renderChunksWide;
         this.maxBlockY = this.renderChunksTall;
         this.maxBlockZ = this.renderChunksDeep;

         int var4;
         for(var4 = 0; var4 < this.worldRenderersToUpdate.size(); ++var4) {
            ((WorldRenderer)this.worldRenderersToUpdate.get(var4)).needsUpdate = false;
         }

         this.worldRenderersToUpdate.clear();
         this.tileEntities.clear();
         this.onStaticEntitiesChanged();

         for(var4 = 0; var4 < this.renderChunksWide; ++var4) {
            for(int var5 = 0; var5 < this.renderChunksTall; ++var5) {
               for(int var6 = 0; var6 < this.renderChunksDeep; ++var6) {
                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4] = new WorldRenderer(this.theWorld, this.tileEntities, var4 * 16, var5 * 16, var6 * 16, this.glRenderListBase + var2);
                  if(this.occlusionEnabled) {
                     this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].glOcclusionQuery = this.glOcclusionQueryBase.get(var3);
                  }

                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isWaitingOnOcclusionQuery = false;
                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isVisible = true;
                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isInFrustum = true;
                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].chunkIndex = var3++;
                  this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].markDirty();
                  this.sortedWorldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4] = this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4];
                  this.worldRenderersToUpdate.add(this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4]);
                  var2 += 3;
               }
            }
         }

         if(this.theWorld != null) {
            EntityLivingBase var7 = this.mc.renderViewEntity;
            if(var7 != null) {
               this.markRenderersForNewPosition(MathHelper.floor_double(var7.posX), MathHelper.floor_double(var7.posY), MathHelper.floor_double(var7.posZ));
               Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var7));
            }
         }

         this.renderEntitiesStartupCounter = 2;
      }
   }

   public void renderEntities(EntityLivingBase var1, ICamera var2, float var3) {
      if(this.renderEntitiesStartupCounter > 0) {
         --this.renderEntitiesStartupCounter;
      } else {
         double var4 = var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var3;
         double var6 = var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var3;
         double var8 = var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var3;
         this.theWorld.theProfiler.startSection("prepare");
         TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.renderViewEntity, var3);
         RenderManager.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.pointedEntity, this.mc.gameSettings, var3);
         this.countEntitiesTotal = 0;
         this.countEntitiesRendered = 0;
         this.countEntitiesHidden = 0;
         EntityLivingBase var10 = this.mc.renderViewEntity;
         double var11 = var10.lastTickPosX + (var10.posX - var10.lastTickPosX) * (double)var3;
         double var13 = var10.lastTickPosY + (var10.posY - var10.lastTickPosY) * (double)var3;
         double var15 = var10.lastTickPosZ + (var10.posZ - var10.lastTickPosZ) * (double)var3;
         TileEntityRendererDispatcher.staticPlayerX = var11;
         TileEntityRendererDispatcher.staticPlayerY = var13;
         TileEntityRendererDispatcher.staticPlayerZ = var15;
         this.theWorld.theProfiler.endStartSection("staticentities");
         if(this.displayListEntitiesDirty) {
            RenderManager.renderPosX = 0.0D;
            RenderManager.renderPosY = 0.0D;
            RenderManager.renderPosZ = 0.0D;
            this.rebuildDisplayListEntities();
         }

         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         GL11.glTranslated(-var11, -var13, -var15);
         GL11.glCallList(this.displayListEntities);
         GL11.glPopMatrix();
         RenderManager.renderPosX = var11;
         RenderManager.renderPosY = var13;
         RenderManager.renderPosZ = var15;
         this.mc.entityRenderer.enableLightmap((double)var3);
         this.theWorld.theProfiler.endStartSection("global");
         List var17 = this.theWorld.getLoadedEntityList();
         this.countEntitiesTotal = var17.size();

         int var18;
         Entity var19;
         for(var18 = 0; var18 < this.theWorld.weatherEffects.size(); ++var18) {
            var19 = (Entity)this.theWorld.weatherEffects.get(var18);
            ++this.countEntitiesRendered;
            if(var19.isInRangeToRender3d(var4, var6, var8)) {
               RenderManager.instance.renderEntitySimple(var19, var3);
            }
         }

         this.theWorld.theProfiler.endStartSection("entities");

         for(var18 = 0; var18 < var17.size(); ++var18) {
            var19 = (Entity)var17.get(var18);
            boolean var20 = var19.isInRangeToRender3d(var4, var6, var8) && (var19.ignoreFrustumCheck || var2.isBoundingBoxInFrustum(var19.boundingBox) || var19.riddenByEntity == this.mc.thePlayer);
            if(!var20 && var19 instanceof EntityLiving) {
               EntityLiving var21 = (EntityLiving)var19;
               if(var21.getLeashed() && var21.getLeashedToEntity() != null) {
                  Entity var22 = var21.getLeashedToEntity();
                  var20 = var2.isBoundingBoxInFrustum(var22.boundingBox);
               }
            }

            if(var20 && (var19 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView != 0 || this.mc.renderViewEntity.isPlayerSleeping()) && this.theWorld.blockExists(MathHelper.floor_double(var19.posX), 0, MathHelper.floor_double(var19.posZ))) {
               ++this.countEntitiesRendered;
               RenderManager.instance.renderEntitySimple(var19, var3);
            }
         }

         this.theWorld.theProfiler.endStartSection("blockentities");
         RenderHelper.enableStandardItemLighting();

         for(var18 = 0; var18 < this.tileEntities.size(); ++var18) {
            TileEntityRendererDispatcher.instance.renderTileEntity((TileEntity)this.tileEntities.get(var18), var3);
         }

         this.mc.entityRenderer.disableLightmap((double)var3);
         this.theWorld.theProfiler.endSection();
      }
   }

   public String getDebugInfoRenders() {
      return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
   }

   public String getDebugInfoEntities() {
      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
   }

   public void onStaticEntitiesChanged() {
      this.displayListEntitiesDirty = true;
   }

   public void rebuildDisplayListEntities() {
      this.theWorld.theProfiler.startSection("staticentityrebuild");
      GL11.glPushMatrix();
      GL11.glNewList(this.displayListEntities, 4864);
      List var1 = this.theWorld.getLoadedEntityList();
      this.displayListEntitiesDirty = false;

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Entity var3 = (Entity)var1.get(var2);
         if(RenderManager.instance.getEntityRenderObject(var3).isStaticEntity()) {
            this.displayListEntitiesDirty = this.displayListEntitiesDirty || !RenderManager.instance.renderEntityStatic(var3, 0.0F, true);
         }
      }

      GL11.glEndList();
      GL11.glPopMatrix();
      this.theWorld.theProfiler.endSection();
   }

   private void markRenderersForNewPosition(int var1, int var2, int var3) {
      var1 -= 8;
      var2 -= 8;
      var3 -= 8;
      this.minBlockX = Integer.MAX_VALUE;
      this.minBlockY = Integer.MAX_VALUE;
      this.minBlockZ = Integer.MAX_VALUE;
      this.maxBlockX = Integer.MIN_VALUE;
      this.maxBlockY = Integer.MIN_VALUE;
      this.maxBlockZ = Integer.MIN_VALUE;
      int var4 = this.renderChunksWide * 16;
      int var5 = var4 / 2;

      for(int var6 = 0; var6 < this.renderChunksWide; ++var6) {
         int var7 = var6 * 16;
         int var8 = var7 + var5 - var1;
         if(var8 < 0) {
            var8 -= var4 - 1;
         }

         var8 /= var4;
         var7 -= var8 * var4;
         if(var7 < this.minBlockX) {
            this.minBlockX = var7;
         }

         if(var7 > this.maxBlockX) {
            this.maxBlockX = var7;
         }

         for(int var9 = 0; var9 < this.renderChunksDeep; ++var9) {
            int var10 = var9 * 16;
            int var11 = var10 + var5 - var3;
            if(var11 < 0) {
               var11 -= var4 - 1;
            }

            var11 /= var4;
            var10 -= var11 * var4;
            if(var10 < this.minBlockZ) {
               this.minBlockZ = var10;
            }

            if(var10 > this.maxBlockZ) {
               this.maxBlockZ = var10;
            }

            for(int var12 = 0; var12 < this.renderChunksTall; ++var12) {
               int var13 = var12 * 16;
               if(var13 < this.minBlockY) {
                  this.minBlockY = var13;
               }

               if(var13 > this.maxBlockY) {
                  this.maxBlockY = var13;
               }

               WorldRenderer var14 = this.worldRenderers[(var9 * this.renderChunksTall + var12) * this.renderChunksWide + var6];
               boolean var15 = var14.needsUpdate;
               var14.setPosition(var7, var13, var10);
               if(!var15 && var14.needsUpdate) {
                  this.worldRenderersToUpdate.add(var14);
               }
            }
         }
      }

   }

   public int sortAndRender(EntityLivingBase var1, int var2, double var3) {
      this.theWorld.theProfiler.startSection("sortchunks");

      for(int var5 = 0; var5 < 10; ++var5) {
         this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
         WorldRenderer var6 = this.worldRenderers[this.worldRenderersCheckIndex];
         if(var6.needsUpdate && !this.worldRenderersToUpdate.contains(var6)) {
            this.worldRenderersToUpdate.add(var6);
         }
      }

      if(this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
         this.loadRenderers();
      }

      if(var2 == 0) {
         this.renderersLoaded = 0;
         this.dummyRenderInt = 0;
         this.renderersBeingClipped = 0;
         this.renderersBeingOccluded = 0;
         this.renderersBeingRendered = 0;
         this.renderersSkippingRenderPass = 0;
      }

      double var39 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
      double var7 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
      double var9 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
      double var11 = var1.posX - this.prevSortX;
      double var13 = var1.posY - this.prevSortY;
      double var15 = var1.posZ - this.prevSortZ;
      if(this.prevChunkSortX != var1.chunkCoordX || this.prevChunkSortY != var1.chunkCoordY || this.prevChunkSortZ != var1.chunkCoordZ || var11 * var11 + var13 * var13 + var15 * var15 > 16.0D) {
         this.prevSortX = var1.posX;
         this.prevSortY = var1.posY;
         this.prevSortZ = var1.posZ;
         this.prevChunkSortX = var1.chunkCoordX;
         this.prevChunkSortY = var1.chunkCoordY;
         this.prevChunkSortZ = var1.chunkCoordZ;
         this.markRenderersForNewPosition(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
         Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
      }

      double var17 = var1.posX - this.prevRenderSortX;
      double var19 = var1.posY - this.prevRenderSortY;
      double var21 = var1.posZ - this.prevRenderSortZ;
      int var23;
      if(var17 * var17 + var19 * var19 + var21 * var21 > 1.0D) {
         this.prevRenderSortX = var1.posX;
         this.prevRenderSortY = var1.posY;
         this.prevRenderSortZ = var1.posZ;

         for(var23 = 0; var23 < 27; ++var23) {
            this.sortedWorldRenderers[var23].updateRendererSort(var1);
         }
      }

      RenderHelper.disableStandardItemLighting();
      byte var40 = 0;
      if(this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && var2 == 0) {
         byte var24 = 0;
         int var25 = 16;
         this.checkOcclusionQueryResult(var24, var25);

         for(int var26 = var24; var26 < var25; ++var26) {
            this.sortedWorldRenderers[var26].isVisible = true;
         }

         this.theWorld.theProfiler.endStartSection("render");
         var23 = var40 + this.renderSortedRenderers(var24, var25, var2, var3);

         do {
            this.theWorld.theProfiler.endStartSection("occ");
            int var41 = var25;
            var25 *= 2;
            if(var25 > this.sortedWorldRenderers.length) {
               var25 = this.sortedWorldRenderers.length;
            }

            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(3008);
            GL11.glDisable(2912);
            GL11.glColorMask(false, false, false, false);
            GL11.glDepthMask(false);
            this.theWorld.theProfiler.startSection("check");
            this.checkOcclusionQueryResult(var41, var25);
            this.theWorld.theProfiler.endSection();
            GL11.glPushMatrix();
            float var42 = 0.0F;
            float var27 = 0.0F;
            float var28 = 0.0F;

            for(int var29 = var41; var29 < var25; ++var29) {
               if(this.sortedWorldRenderers[var29].skipAllRenderPasses()) {
                  this.sortedWorldRenderers[var29].isInFrustum = false;
               } else {
                  if(!this.sortedWorldRenderers[var29].isInFrustum) {
                     this.sortedWorldRenderers[var29].isVisible = true;
                  }

                  if(this.sortedWorldRenderers[var29].isInFrustum && !this.sortedWorldRenderers[var29].isWaitingOnOcclusionQuery) {
                     float var30 = MathHelper.sqrt_float(this.sortedWorldRenderers[var29].distanceToEntitySquared(var1));
                     int var31 = (int)(1.0F + var30 / 128.0F);
                     if(this.cloudTickCounter % var31 == var29 % var31) {
                        WorldRenderer var32 = this.sortedWorldRenderers[var29];
                        float var33 = (float)((double)var32.posXMinus - var39);
                        float var34 = (float)((double)var32.posYMinus - var7);
                        float var35 = (float)((double)var32.posZMinus - var9);
                        float var36 = var33 - var42;
                        float var37 = var34 - var27;
                        float var38 = var35 - var28;
                        if(var36 != 0.0F || var37 != 0.0F || var38 != 0.0F) {
                           GL11.glTranslatef(var36, var37, var38);
                           var42 += var36;
                           var27 += var37;
                           var28 += var38;
                        }

                        this.theWorld.theProfiler.startSection("bb");
                        ARBOcclusionQuery.glBeginQueryARB('\u8914', this.sortedWorldRenderers[var29].glOcclusionQuery);
                        this.sortedWorldRenderers[var29].callOcclusionQueryList();
                        ARBOcclusionQuery.glEndQueryARB('\u8914');
                        this.theWorld.theProfiler.endSection();
                        this.sortedWorldRenderers[var29].isWaitingOnOcclusionQuery = true;
                     }
                  }
               }
            }

            GL11.glPopMatrix();
            if(this.mc.gameSettings.anaglyph) {
               if(EntityRenderer.anaglyphField == 0) {
                  GL11.glColorMask(false, true, true, true);
               } else {
                  GL11.glColorMask(true, false, false, true);
               }
            } else {
               GL11.glColorMask(true, true, true, true);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glEnable(2912);
            this.theWorld.theProfiler.endStartSection("render");
            var23 += this.renderSortedRenderers(var41, var25, var2, var3);
         } while(var25 < this.sortedWorldRenderers.length);
      } else {
         this.theWorld.theProfiler.endStartSection("render");
         var23 = var40 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var2, var3);
      }

      this.theWorld.theProfiler.endSection();
      return var23;
   }

   private void checkOcclusionQueryResult(int var1, int var2) {
      for(int var3 = var1; var3 < var2; ++var3) {
         if(this.sortedWorldRenderers[var3].isWaitingOnOcclusionQuery) {
            this.occlusionResult.clear();
            ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[var3].glOcclusionQuery, '\u8867', this.occlusionResult);
            if(this.occlusionResult.get(0) != 0) {
               this.sortedWorldRenderers[var3].isWaitingOnOcclusionQuery = false;
               this.occlusionResult.clear();
               ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[var3].glOcclusionQuery, '\u8866', this.occlusionResult);
               this.sortedWorldRenderers[var3].isVisible = this.occlusionResult.get(0) != 0;
            }
         }
      }

   }

   private int renderSortedRenderers(int var1, int var2, int var3, double var4) {
      this.glRenderLists.clear();
      int var6 = 0;
      int var7 = var1;
      int var8 = var2;
      byte var9 = 1;
      if(var3 == 1) {
         var7 = this.sortedWorldRenderers.length - 1 - var1;
         var8 = this.sortedWorldRenderers.length - 1 - var2;
         var9 = -1;
      }

      for(int var10 = var7; var10 != var8; var10 += var9) {
         if(var3 == 0) {
            ++this.renderersLoaded;
            if(this.sortedWorldRenderers[var10].skipRenderPass[var3]) {
               ++this.renderersSkippingRenderPass;
            } else if(!this.sortedWorldRenderers[var10].isInFrustum) {
               ++this.renderersBeingClipped;
            } else if(this.occlusionEnabled && !this.sortedWorldRenderers[var10].isVisible) {
               ++this.renderersBeingOccluded;
            } else {
               ++this.renderersBeingRendered;
            }
         }

         if(!this.sortedWorldRenderers[var10].skipRenderPass[var3] && this.sortedWorldRenderers[var10].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[var10].isVisible)) {
            int var11 = this.sortedWorldRenderers[var10].getGLCallListForPass(var3);
            if(var11 >= 0) {
               this.glRenderLists.add(this.sortedWorldRenderers[var10]);
               ++var6;
            }
         }
      }

      EntityLivingBase var22 = this.mc.renderViewEntity;
      double var23 = var22.lastTickPosX + (var22.posX - var22.lastTickPosX) * var4;
      double var13 = var22.lastTickPosY + (var22.posY - var22.lastTickPosY) * var4;
      double var15 = var22.lastTickPosZ + (var22.posZ - var22.lastTickPosZ) * var4;
      int var17 = 0;

      int var18;
      for(var18 = 0; var18 < this.allRenderLists.length; ++var18) {
         this.allRenderLists[var18].resetList();
      }

      int var20;
      int var21;
      for(var18 = 0; var18 < this.glRenderLists.size(); ++var18) {
         WorldRenderer var19 = (WorldRenderer)this.glRenderLists.get(var18);
         var20 = -1;

         for(var21 = 0; var21 < var17; ++var21) {
            if(this.allRenderLists[var21].rendersChunk(var19.posXMinus, var19.posYMinus, var19.posZMinus)) {
               var20 = var21;
            }
         }

         if(var20 < 0) {
            var20 = var17++;
            this.allRenderLists[var20].setupRenderList(var19.posXMinus, var19.posYMinus, var19.posZMinus, var23, var13, var15);
         }

         this.allRenderLists[var20].addGLRenderList(var19.getGLCallListForPass(var3));
      }

      var18 = MathHelper.floor_double(var23);
      int var24 = MathHelper.floor_double(var15);
      var20 = var18 - (var18 & 1023);
      var21 = var24 - (var24 & 1023);
      Arrays.sort(this.allRenderLists, new RenderDistanceSorter(var20, var21));
      this.renderAllRenderLists(var3, var4);
      return var6;
   }

   public void renderAllRenderLists(int var1, double var2) {
      this.mc.entityRenderer.enableLightmap(var2);

      for(int var4 = 0; var4 < this.allRenderLists.length; ++var4) {
         this.allRenderLists[var4].callLists();
      }

      this.mc.entityRenderer.disableLightmap(var2);
   }

   public void updateClouds() {
      ++this.cloudTickCounter;
      if(this.cloudTickCounter % 20 == 0) {
         Iterator var1 = this.damagedBlocks.values().iterator();

         while(var1.hasNext()) {
            DestroyBlockProgress var2 = (DestroyBlockProgress)var1.next();
            int var3 = var2.getCreationCloudUpdateTick();
            if(this.cloudTickCounter - var3 > 400) {
               var1.remove();
            }
         }
      }

   }

   public void renderSky(float var1) {
      if(this.mc.theWorld.provider.dimensionId == 1) {
         GL11.glDisable(2912);
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         GL11.glDepthMask(false);
         this.renderEngine.bindTexture(locationEndSkyPng);
         Tessellator var21 = Tessellator.instance;

         for(int var22 = 0; var22 < 6; ++var22) {
            GL11.glPushMatrix();
            if(var22 == 1) {
               GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var22 == 2) {
               GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var22 == 3) {
               GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var22 == 4) {
               GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if(var22 == 5) {
               GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            var21.startDrawingQuads();
            var21.setColorOpaque_I(2631720);
            var21.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
            var21.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
            var21.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
            var21.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
            var21.draw();
            GL11.glPopMatrix();
         }

         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glEnable(3008);
      } else if(this.mc.theWorld.provider.isSurfaceWorld()) {
         GL11.glDisable(3553);
         Vec3 var2 = this.theWorld.getSkyColor(this.mc.renderViewEntity, var1);
         float var3 = (float)var2.xCoord;
         float var4 = (float)var2.yCoord;
         float var5 = (float)var2.zCoord;
         float var8;
         if(this.mc.gameSettings.anaglyph) {
            float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
            float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var5 = var8;
         }

         GL11.glColor3f(var3, var4, var5);
         Tessellator var23 = Tessellator.instance;
         GL11.glDepthMask(false);
         GL11.glEnable(2912);
         GL11.glColor3f(var3, var4, var5);
         GL11.glCallList(this.glSkyList);
         GL11.glDisable(2912);
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         float[] var24 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(var1), var1);
         float var9;
         float var10;
         float var11;
         float var12;
         if(var24 != null) {
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(this.theWorld.getCelestialAngleRadians(var1)) < 0.0F?180.0F:0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            var8 = var24[0];
            var9 = var24[1];
            var10 = var24[2];
            float var13;
            if(this.mc.gameSettings.anaglyph) {
               var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
               var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
               var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
               var8 = var11;
               var9 = var12;
               var10 = var13;
            }

            var23.startDrawing(6);
            var23.setColorRGBA_F(var8, var9, var10, var24[3]);
            var23.addVertex(0.0D, 100.0D, 0.0D);
            byte var26 = 16;
            var23.setColorRGBA_F(var24[0], var24[1], var24[2], 0.0F);

            for(int var27 = 0; var27 <= var26; ++var27) {
               var13 = (float)var27 * 3.1415927F * 2.0F / (float)var26;
               float var14 = MathHelper.sin(var13);
               float var15 = MathHelper.cos(var13);
               var23.addVertex((double)(var14 * 120.0F), (double)(var15 * 120.0F), (double)(-var15 * 40.0F * var24[3]));
            }

            var23.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(7424);
         }

         GL11.glEnable(3553);
         OpenGlHelper.glBlendFunc(770, 1, 1, 0);
         GL11.glPushMatrix();
         var8 = 1.0F - this.theWorld.getRainStrength(var1);
         var9 = 0.0F;
         var10 = 0.0F;
         var11 = 0.0F;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
         GL11.glTranslatef(var9, var10, var11);
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.theWorld.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
         var12 = 30.0F;
         this.renderEngine.bindTexture(locationSunPng);
         var23.startDrawingQuads();
         var23.addVertexWithUV((double)(-var12), 100.0D, (double)(-var12), 0.0D, 0.0D);
         var23.addVertexWithUV((double)var12, 100.0D, (double)(-var12), 1.0D, 0.0D);
         var23.addVertexWithUV((double)var12, 100.0D, (double)var12, 1.0D, 1.0D);
         var23.addVertexWithUV((double)(-var12), 100.0D, (double)var12, 0.0D, 1.0D);
         var23.draw();
         var12 = 20.0F;
         this.renderEngine.bindTexture(locationMoonPhasesPng);
         int var28 = this.theWorld.getMoonPhase();
         int var29 = var28 % 4;
         int var30 = var28 / 4 % 2;
         float var16 = (float)(var29 + 0) / 4.0F;
         float var17 = (float)(var30 + 0) / 2.0F;
         float var18 = (float)(var29 + 1) / 4.0F;
         float var19 = (float)(var30 + 1) / 2.0F;
         var23.startDrawingQuads();
         var23.addVertexWithUV((double)(-var12), -100.0D, (double)var12, (double)var18, (double)var19);
         var23.addVertexWithUV((double)var12, -100.0D, (double)var12, (double)var16, (double)var19);
         var23.addVertexWithUV((double)var12, -100.0D, (double)(-var12), (double)var16, (double)var17);
         var23.addVertexWithUV((double)(-var12), -100.0D, (double)(-var12), (double)var18, (double)var17);
         var23.draw();
         GL11.glDisable(3553);
         float var20 = this.theWorld.getStarBrightness(var1) * var8;
         if(var20 > 0.0F) {
            GL11.glColor4f(var20, var20, var20, var20);
            GL11.glCallList(this.starGLCallList);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(3042);
         GL11.glEnable(3008);
         GL11.glEnable(2912);
         GL11.glPopMatrix();
         GL11.glDisable(3553);
         GL11.glColor3f(0.0F, 0.0F, 0.0F);
         double var25 = this.mc.thePlayer.getPosition(var1).yCoord - this.theWorld.getHorizon();
         if(var25 < 0.0D) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 12.0F, 0.0F);
            GL11.glCallList(this.glSkyList2);
            GL11.glPopMatrix();
            var10 = 1.0F;
            var11 = -((float)(var25 + 65.0D));
            var12 = -var10;
            var23.startDrawingQuads();
            var23.setColorRGBA_I(0, 255);
            var23.addVertex((double)(-var10), (double)var11, (double)var10);
            var23.addVertex((double)var10, (double)var11, (double)var10);
            var23.addVertex((double)var10, (double)var12, (double)var10);
            var23.addVertex((double)(-var10), (double)var12, (double)var10);
            var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var23.addVertex((double)var10, (double)var12, (double)(-var10));
            var23.addVertex((double)var10, (double)var11, (double)(-var10));
            var23.addVertex((double)(-var10), (double)var11, (double)(-var10));
            var23.addVertex((double)var10, (double)var12, (double)(-var10));
            var23.addVertex((double)var10, (double)var12, (double)var10);
            var23.addVertex((double)var10, (double)var11, (double)var10);
            var23.addVertex((double)var10, (double)var11, (double)(-var10));
            var23.addVertex((double)(-var10), (double)var11, (double)(-var10));
            var23.addVertex((double)(-var10), (double)var11, (double)var10);
            var23.addVertex((double)(-var10), (double)var12, (double)var10);
            var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var23.addVertex((double)(-var10), (double)var12, (double)var10);
            var23.addVertex((double)var10, (double)var12, (double)var10);
            var23.addVertex((double)var10, (double)var12, (double)(-var10));
            var23.draw();
         }

         if(this.theWorld.provider.isSkyColored()) {
            GL11.glColor3f(var3 * 0.2F + 0.04F, var4 * 0.2F + 0.04F, var5 * 0.6F + 0.1F);
         } else {
            GL11.glColor3f(var3, var4, var5);
         }

         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, -((float)(var25 - 16.0D)), 0.0F);
         GL11.glCallList(this.glSkyList2);
         GL11.glPopMatrix();
         GL11.glEnable(3553);
         GL11.glDepthMask(true);
      }
   }

   public void renderClouds(float var1) {
      if(this.mc.theWorld.provider.isSurfaceWorld()) {
         if(this.mc.gameSettings.fancyGraphics) {
            this.renderCloudsFancy(var1);
         } else {
            GL11.glDisable(2884);
            float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)var1);
            byte var3 = 32;
            int var4 = 256 / var3;
            Tessellator var5 = Tessellator.instance;
            this.renderEngine.bindTexture(locationCloudsPng);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Vec3 var6 = this.theWorld.getCloudColour(var1);
            float var7 = (float)var6.xCoord;
            float var8 = (float)var6.yCoord;
            float var9 = (float)var6.zCoord;
            float var10;
            if(this.mc.gameSettings.anaglyph) {
               var10 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
               float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
               float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
               var7 = var10;
               var8 = var11;
               var9 = var12;
            }

            var10 = 4.8828125E-4F;
            double var24 = (double)((float)this.cloudTickCounter + var1);
            double var13 = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)var1 + var24 * 0.029999999329447746D;
            double var15 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)var1;
            int var17 = MathHelper.floor_double(var13 / 2048.0D);
            int var18 = MathHelper.floor_double(var15 / 2048.0D);
            var13 -= (double)(var17 * 2048);
            var15 -= (double)(var18 * 2048);
            float var19 = this.theWorld.provider.getCloudHeight() - var2 + 0.33F;
            float var20 = (float)(var13 * (double)var10);
            float var21 = (float)(var15 * (double)var10);
            var5.startDrawingQuads();
            var5.setColorRGBA_F(var7, var8, var9, 0.8F);

            for(int var22 = -var3 * var4; var22 < var3 * var4; var22 += var3) {
               for(int var23 = -var3 * var4; var23 < var3 * var4; var23 += var3) {
                  var5.addVertexWithUV((double)(var22 + 0), (double)var19, (double)(var23 + var3), (double)((float)(var22 + 0) * var10 + var20), (double)((float)(var23 + var3) * var10 + var21));
                  var5.addVertexWithUV((double)(var22 + var3), (double)var19, (double)(var23 + var3), (double)((float)(var22 + var3) * var10 + var20), (double)((float)(var23 + var3) * var10 + var21));
                  var5.addVertexWithUV((double)(var22 + var3), (double)var19, (double)(var23 + 0), (double)((float)(var22 + var3) * var10 + var20), (double)((float)(var23 + 0) * var10 + var21));
                  var5.addVertexWithUV((double)(var22 + 0), (double)var19, (double)(var23 + 0), (double)((float)(var22 + 0) * var10 + var20), (double)((float)(var23 + 0) * var10 + var21));
               }
            }

            var5.draw();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
            GL11.glEnable(2884);
         }
      }
   }

   public boolean hasCloudFog(double var1, double var3, double var5, float var7) {
      return false;
   }

   public void renderCloudsFancy(float var1) {
      GL11.glDisable(2884);
      float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)var1);
      Tessellator var3 = Tessellator.instance;
      float var4 = 12.0F;
      float var5 = 4.0F;
      double var6 = (double)((float)this.cloudTickCounter + var1);
      double var8 = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)var1 + var6 * 0.029999999329447746D) / (double)var4;
      double var10 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)var1) / (double)var4 + 0.33000001311302185D;
      float var12 = this.theWorld.provider.getCloudHeight() - var2 + 0.33F;
      int var13 = MathHelper.floor_double(var8 / 2048.0D);
      int var14 = MathHelper.floor_double(var10 / 2048.0D);
      var8 -= (double)(var13 * 2048);
      var10 -= (double)(var14 * 2048);
      this.renderEngine.bindTexture(locationCloudsPng);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      Vec3 var15 = this.theWorld.getCloudColour(var1);
      float var16 = (float)var15.xCoord;
      float var17 = (float)var15.yCoord;
      float var18 = (float)var15.zCoord;
      float var19;
      float var20;
      float var21;
      if(this.mc.gameSettings.anaglyph) {
         var19 = (var16 * 30.0F + var17 * 59.0F + var18 * 11.0F) / 100.0F;
         var20 = (var16 * 30.0F + var17 * 70.0F) / 100.0F;
         var21 = (var16 * 30.0F + var18 * 70.0F) / 100.0F;
         var16 = var19;
         var17 = var20;
         var18 = var21;
      }

      var19 = (float)(var8 * 0.0D);
      var20 = (float)(var10 * 0.0D);
      var21 = 0.00390625F;
      var19 = (float)MathHelper.floor_double(var8) * var21;
      var20 = (float)MathHelper.floor_double(var10) * var21;
      float var22 = (float)(var8 - (double)MathHelper.floor_double(var8));
      float var23 = (float)(var10 - (double)MathHelper.floor_double(var10));
      byte var24 = 8;
      byte var25 = 4;
      float var26 = 9.765625E-4F;
      GL11.glScalef(var4, 1.0F, var4);

      for(int var27 = 0; var27 < 2; ++var27) {
         if(var27 == 0) {
            GL11.glColorMask(false, false, false, false);
         } else if(this.mc.gameSettings.anaglyph) {
            if(EntityRenderer.anaglyphField == 0) {
               GL11.glColorMask(false, true, true, true);
            } else {
               GL11.glColorMask(true, false, false, true);
            }
         } else {
            GL11.glColorMask(true, true, true, true);
         }

         for(int var28 = -var25 + 1; var28 <= var25; ++var28) {
            for(int var29 = -var25 + 1; var29 <= var25; ++var29) {
               var3.startDrawingQuads();
               float var30 = (float)(var28 * var24);
               float var31 = (float)(var29 * var24);
               float var32 = var30 - var22;
               float var33 = var31 - var23;
               if(var12 > -var5 - 1.0F) {
                  var3.setColorRGBA_F(var16 * 0.7F, var17 * 0.7F, var18 * 0.7F, 0.8F);
                  var3.setNormal(0.0F, -1.0F, 0.0F);
                  var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
               }

               if(var12 <= var5 + 1.0F) {
                  var3.setColorRGBA_F(var16, var17, var18, 0.8F);
                  var3.setNormal(0.0F, 1.0F, 0.0F);
                  var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5 - var26), (double)(var33 + (float)var24), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5 - var26), (double)(var33 + (float)var24), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5 - var26), (double)(var33 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                  var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5 - var26), (double)(var33 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
               }

               var3.setColorRGBA_F(var16 * 0.9F, var17 * 0.9F, var18 * 0.9F, 0.8F);
               int var34;
               if(var28 > -1) {
                  var3.setNormal(-1.0F, 0.0F, 0.0F);

                  for(var34 = 0; var34 < var24; ++var34) {
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + var5), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                  }
               }

               if(var28 <= 1) {
                  var3.setNormal(1.0F, 0.0F, 0.0F);

                  for(var34 = 0; var34 < var24; ++var34) {
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + var5), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + var5), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
                  }
               }

               var3.setColorRGBA_F(var16 * 0.8F, var17 * 0.8F, var18 * 0.8F, 0.8F);
               if(var29 > -1) {
                  var3.setNormal(0.0F, 0.0F, -1.0F);

                  for(var34 = 0; var34 < var24; ++var34) {
                     var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                  }
               }

               if(var29 <= 1) {
                  var3.setNormal(0.0F, 0.0F, 1.0F);

                  for(var34 = 0; var34 < var24; ++var34) {
                     var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                     var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
                  }
               }

               var3.draw();
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glEnable(2884);
   }

   public boolean updateRenderers(EntityLivingBase var1, boolean var2) {
      byte var3 = 2;
      RenderSorter var4 = new RenderSorter(var1);
      WorldRenderer[] var5 = new WorldRenderer[var3];
      ArrayList var6 = null;
      int var7 = this.worldRenderersToUpdate.size();
      int var8 = 0;
      this.theWorld.theProfiler.startSection("nearChunksSearch");

      int var9;
      WorldRenderer var10;
      int var11;
      int var12;
      label136:
      for(var9 = 0; var9 < var7; ++var9) {
         var10 = (WorldRenderer)this.worldRenderersToUpdate.get(var9);
         if(var10 != null) {
            if(!var2) {
               if(var10.distanceToEntitySquared(var1) > 272.0F) {
                  for(var11 = 0; var11 < var3 && (var5[var11] == null || var4.compare(var5[var11], var10) <= 0); ++var11) {
                     ;
                  }

                  --var11;
                  if(var11 > 0) {
                     var12 = var11;

                     while(true) {
                        --var12;
                        if(var12 == 0) {
                           var5[var11] = var10;
                           continue label136;
                        }

                        var5[var12 - 1] = var5[var12];
                     }
                  }
                  continue;
               }
            } else if(!var10.isInFrustum) {
               continue;
            }

            if(var6 == null) {
               var6 = new ArrayList();
            }

            ++var8;
            var6.add(var10);
            this.worldRenderersToUpdate.set(var9, (Object)null);
         }
      }

      this.theWorld.theProfiler.endSection();
      this.theWorld.theProfiler.startSection("sort");
      if(var6 != null) {
         if(var6.size() > 1) {
            Collections.sort(var6, var4);
         }

         for(var9 = var6.size() - 1; var9 >= 0; --var9) {
            var10 = (WorldRenderer)var6.get(var9);
            var10.updateRenderer(var1);
            var10.needsUpdate = false;
         }
      }

      this.theWorld.theProfiler.endSection();
      var9 = 0;
      this.theWorld.theProfiler.startSection("rebuild");

      int var16;
      for(var16 = var3 - 1; var16 >= 0; --var16) {
         WorldRenderer var17 = var5[var16];
         if(var17 != null) {
            if(!var17.isInFrustum && var16 != var3 - 1) {
               var5[var16] = null;
               var5[0] = null;
               break;
            }

            var5[var16].updateRenderer(var1);
            var5[var16].needsUpdate = false;
            ++var9;
         }
      }

      this.theWorld.theProfiler.endSection();
      this.theWorld.theProfiler.startSection("cleanup");
      var16 = 0;
      var11 = 0;

      for(var12 = this.worldRenderersToUpdate.size(); var16 != var12; ++var16) {
         WorldRenderer var13 = (WorldRenderer)this.worldRenderersToUpdate.get(var16);
         if(var13 != null) {
            boolean var14 = false;

            for(int var15 = 0; var15 < var3 && !var14; ++var15) {
               if(var13 == var5[var15]) {
                  var14 = true;
               }
            }

            if(!var14) {
               if(var11 != var16) {
                  this.worldRenderersToUpdate.set(var11, var13);
               }

               ++var11;
            }
         }
      }

      this.theWorld.theProfiler.endSection();
      this.theWorld.theProfiler.startSection("trim");

      while(true) {
         --var16;
         if(var16 < var11) {
            this.theWorld.theProfiler.endSection();
            return var7 == var8 + var9;
         }

         this.worldRenderersToUpdate.remove(var16);
      }
   }

   public void drawBlockDamageTexture(Tessellator var1, EntityPlayer var2, float var3) {
      double var4 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var3;
      double var6 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var3;
      double var8 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var3;
      if(!this.damagedBlocks.isEmpty()) {
         OpenGlHelper.glBlendFunc(774, 768, 1, 0);
         this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
         GL11.glPushMatrix();
         GL11.glPolygonOffset(-3.0F, -3.0F);
         GL11.glEnable('\u8037');
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glEnable(3008);
         var1.startDrawingQuads();
         var1.setTranslation(-var4, -var6, -var8);
         var1.disableColor();
         Iterator var10 = this.damagedBlocks.values().iterator();

         while(var10.hasNext()) {
            DestroyBlockProgress var11 = (DestroyBlockProgress)var10.next();
            double var12 = (double)var11.getPartialBlockX() - var4;
            double var14 = (double)var11.getPartialBlockY() - var6;
            double var16 = (double)var11.getPartialBlockZ() - var8;
            if(var12 * var12 + var14 * var14 + var16 * var16 > 1024.0D) {
               var10.remove();
            } else {
               Block var18 = this.theWorld.getBlock(var11.getPartialBlockX(), var11.getPartialBlockY(), var11.getPartialBlockZ());
               if(var18.getMaterial() != Material.air) {
                  this.renderBlocksRg.renderBlockUsingTexture(var18, var11.getPartialBlockX(), var11.getPartialBlockY(), var11.getPartialBlockZ(), this.destroyBlockIcons[var11.getPartialBlockDamage()]);
               }
            }
         }

         var1.draw();
         var1.setTranslation(0.0D, 0.0D, 0.0D);
         GL11.glDisable(3008);
         GL11.glPolygonOffset(0.0F, 0.0F);
         GL11.glDisable('\u8037');
         GL11.glEnable(3008);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
      }

   }

   public void drawSelectionBox(EntityPlayer var1, MovingObjectPosition var2, int var3, float var4) {
      if(var3 == 0 && var2.typeOfHit == MovingObjectPosition$MovingObjectType.BLOCK) {
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
         GL11.glLineWidth(2.0F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         float var5 = 0.002F;
         Block var6 = this.theWorld.getBlock(var2.blockX, var2.blockY, var2.blockZ);
         if(var6.getMaterial() != Material.air) {
            var6.setBlockBoundsBasedOnState(this.theWorld, var2.blockX, var2.blockY, var2.blockZ);
            double var7 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var4;
            double var9 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var4;
            double var11 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var4;
            drawOutlinedBoundingBox(var6.getSelectedBoundingBoxFromPool(this.theWorld, var2.blockX, var2.blockY, var2.blockZ).expand((double)var5, (double)var5, (double)var5).getOffsetBoundingBox(-var7, -var9, -var11), -1);
         }

         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glDisable(3042);
      }

   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB var0, int var1) {
      Tessellator var2 = Tessellator.instance;
      var2.startDrawing(3);
      if(var1 != -1) {
         var2.setColorOpaque_I(var1);
      }

      var2.addVertex(var0.minX, var0.minY, var0.minZ);
      var2.addVertex(var0.maxX, var0.minY, var0.minZ);
      var2.addVertex(var0.maxX, var0.minY, var0.maxZ);
      var2.addVertex(var0.minX, var0.minY, var0.maxZ);
      var2.addVertex(var0.minX, var0.minY, var0.minZ);
      var2.draw();
      var2.startDrawing(3);
      if(var1 != -1) {
         var2.setColorOpaque_I(var1);
      }

      var2.addVertex(var0.minX, var0.maxY, var0.minZ);
      var2.addVertex(var0.maxX, var0.maxY, var0.minZ);
      var2.addVertex(var0.maxX, var0.maxY, var0.maxZ);
      var2.addVertex(var0.minX, var0.maxY, var0.maxZ);
      var2.addVertex(var0.minX, var0.maxY, var0.minZ);
      var2.draw();
      var2.startDrawing(1);
      if(var1 != -1) {
         var2.setColorOpaque_I(var1);
      }

      var2.addVertex(var0.minX, var0.minY, var0.minZ);
      var2.addVertex(var0.minX, var0.maxY, var0.minZ);
      var2.addVertex(var0.maxX, var0.minY, var0.minZ);
      var2.addVertex(var0.maxX, var0.maxY, var0.minZ);
      var2.addVertex(var0.maxX, var0.minY, var0.maxZ);
      var2.addVertex(var0.maxX, var0.maxY, var0.maxZ);
      var2.addVertex(var0.minX, var0.minY, var0.maxZ);
      var2.addVertex(var0.minX, var0.maxY, var0.maxZ);
      var2.draw();
   }

   public void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = MathHelper.bucketInt(var1, 16);
      int var8 = MathHelper.bucketInt(var2, 16);
      int var9 = MathHelper.bucketInt(var3, 16);
      int var10 = MathHelper.bucketInt(var4, 16);
      int var11 = MathHelper.bucketInt(var5, 16);
      int var12 = MathHelper.bucketInt(var6, 16);

      for(int var13 = var7; var13 <= var10; ++var13) {
         int var14 = var13 % this.renderChunksWide;
         if(var14 < 0) {
            var14 += this.renderChunksWide;
         }

         for(int var15 = var8; var15 <= var11; ++var15) {
            int var16 = var15 % this.renderChunksTall;
            if(var16 < 0) {
               var16 += this.renderChunksTall;
            }

            for(int var17 = var9; var17 <= var12; ++var17) {
               int var18 = var17 % this.renderChunksDeep;
               if(var18 < 0) {
                  var18 += this.renderChunksDeep;
               }

               int var19 = (var18 * this.renderChunksTall + var16) * this.renderChunksWide + var14;
               WorldRenderer var20 = this.worldRenderers[var19];
               if(var20 != null && !var20.needsUpdate) {
                  this.worldRenderersToUpdate.add(var20);
                  var20.markDirty();
               }
            }
         }
      }

   }

   public void markBlockForUpdate(int var1, int var2, int var3) {
      this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
   }

   public void markBlockForRenderUpdate(int var1, int var2, int var3) {
      this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
   }

   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.markBlocksForUpdate(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
   }

   public void clipRenderersByFrustum(ICamera var1, float var2) {
      for(int var3 = 0; var3 < this.worldRenderers.length; ++var3) {
         if(!this.worldRenderers[var3].skipAllRenderPasses() && (!this.worldRenderers[var3].isInFrustum || (var3 + this.frustumCheckOffset & 15) == 0)) {
            this.worldRenderers[var3].updateInFrustum(var1);
         }
      }

      ++this.frustumCheckOffset;
   }

   public void playRecord(String var1, int var2, int var3, int var4) {
      ChunkCoordinates var5 = new ChunkCoordinates(var2, var3, var4);
      ISound var6 = (ISound)this.mapSoundPositions.get(var5);
      if(var6 != null) {
         this.mc.getSoundHandler().stopSound(var6);
         this.mapSoundPositions.remove(var5);
      }

      if(var1 != null) {
         ItemRecord var7 = ItemRecord.getRecord(var1);
         if(var7 != null) {
            this.mc.ingameGUI.setRecordPlayingMessage(var7.getRecordNameLocal());
         }

         PositionedSoundRecord var8 = PositionedSoundRecord.func_147675_a(new ResourceLocation(var1), (float)var2, (float)var3, (float)var4);
         this.mapSoundPositions.put(var5, var8);
         this.mc.getSoundHandler().playSound(var8);
      }

   }

   public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {}

   public void playSoundToNearExcept(EntityPlayer var1, String var2, double var3, double var5, double var7, float var9, float var10) {}

   public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      try {
         this.doSpawnParticle(var1, var2, var4, var6, var8, var10, var12);
      } catch (Throwable var17) {
         CrashReport var15 = CrashReport.makeCrashReport(var17, "Exception while adding particle");
         CrashReportCategory var16 = var15.makeCategory("Particle being added");
         var16.addCrashSection("Name", var1);
         var16.addCrashSectionCallable("Position", new RenderGlobal$1(this, var2, var4, var6));
         throw new ReportedException(var15);
      }
   }

   public EntityFX doSpawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      if(this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
         int var14 = this.mc.gameSettings.particleSetting;
         if(var14 == 1 && this.theWorld.rand.nextInt(3) == 0) {
            var14 = 2;
         }

         double var15 = this.mc.renderViewEntity.posX - var2;
         double var17 = this.mc.renderViewEntity.posY - var4;
         double var19 = this.mc.renderViewEntity.posZ - var6;
         Object var21 = null;
         if(var1.equals("hugeexplosion")) {
            this.mc.effectRenderer.addEffect(var21 = new EntityHugeExplodeFX(this.theWorld, var2, var4, var6, var8, var10, var12));
         } else if(var1.equals("largeexplode")) {
            this.mc.effectRenderer.addEffect(var21 = new EntityLargeExplodeFX(this.renderEngine, this.theWorld, var2, var4, var6, var8, var10, var12));
         } else if(var1.equals("fireworksSpark")) {
            this.mc.effectRenderer.addEffect(var21 = new EntityFireworkSparkFX(this.theWorld, var2, var4, var6, var8, var10, var12, this.mc.effectRenderer));
         }

         if(var21 != null) {
            return (EntityFX)var21;
         } else {
            double var22 = 16.0D;
            if(var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22) {
               return null;
            } else if(var14 > 1) {
               return null;
            } else {
               if(var1.equals("bubble")) {
                  var21 = new EntityBubbleFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("suspended")) {
                  var21 = new EntitySuspendFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("depthsuspend")) {
                  var21 = new EntityAuraFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("townaura")) {
                  var21 = new EntityAuraFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("crit")) {
                  var21 = new EntityCritFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("magicCrit")) {
                  var21 = new EntityCritFX(this.theWorld, var2, var4, var6, var8, var10, var12);
                  ((EntityFX)var21).setRBGColorF(((EntityFX)var21).getRedColorF() * 0.3F, ((EntityFX)var21).getGreenColorF() * 0.8F, ((EntityFX)var21).getBlueColorF());
                  ((EntityFX)var21).nextTextureIndexX();
               } else if(var1.equals("smoke")) {
                  var21 = new EntitySmokeFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("mobSpell")) {
                  var21 = new EntitySpellParticleFX(this.theWorld, var2, var4, var6, 0.0D, 0.0D, 0.0D);
                  ((EntityFX)var21).setRBGColorF((float)var8, (float)var10, (float)var12);
               } else if(var1.equals("mobSpellAmbient")) {
                  var21 = new EntitySpellParticleFX(this.theWorld, var2, var4, var6, 0.0D, 0.0D, 0.0D);
                  ((EntityFX)var21).setAlphaF(0.15F);
                  ((EntityFX)var21).setRBGColorF((float)var8, (float)var10, (float)var12);
               } else if(var1.equals("spell")) {
                  var21 = new EntitySpellParticleFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("instantSpell")) {
                  var21 = new EntitySpellParticleFX(this.theWorld, var2, var4, var6, var8, var10, var12);
                  ((EntitySpellParticleFX)var21).setBaseSpellTextureIndex(144);
               } else if(var1.equals("witchMagic")) {
                  var21 = new EntitySpellParticleFX(this.theWorld, var2, var4, var6, var8, var10, var12);
                  ((EntitySpellParticleFX)var21).setBaseSpellTextureIndex(144);
                  float var24 = this.theWorld.rand.nextFloat() * 0.5F + 0.35F;
                  ((EntityFX)var21).setRBGColorF(1.0F * var24, 0.0F * var24, 1.0F * var24);
               } else if(var1.equals("note")) {
                  var21 = new EntityNoteFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("portal")) {
                  var21 = new EntityPortalFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("enchantmenttable")) {
                  var21 = new EntityEnchantmentTableParticleFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("explode")) {
                  var21 = new EntityExplodeFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("flame")) {
                  var21 = new EntityFlameFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("lava")) {
                  var21 = new EntityLavaFX(this.theWorld, var2, var4, var6);
               } else if(var1.equals("footstep")) {
                  var21 = new EntityFootStepFX(this.renderEngine, this.theWorld, var2, var4, var6);
               } else if(var1.equals("splash")) {
                  var21 = new EntitySplashFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("wake")) {
                  var21 = new EntityFishWakeFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("largesmoke")) {
                  var21 = new EntitySmokeFX(this.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
               } else if(var1.equals("cloud")) {
                  var21 = new EntityCloudFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("reddust")) {
                  var21 = new EntityReddustFX(this.theWorld, var2, var4, var6, (float)var8, (float)var10, (float)var12);
               } else if(var1.equals("snowballpoof")) {
                  var21 = new EntityBreakingFX(this.theWorld, var2, var4, var6, Items.snowball);
               } else if(var1.equals("dripWater")) {
                  var21 = new EntityDropParticleFX(this.theWorld, var2, var4, var6, Material.water);
               } else if(var1.equals("dripLava")) {
                  var21 = new EntityDropParticleFX(this.theWorld, var2, var4, var6, Material.lava);
               } else if(var1.equals("snowshovel")) {
                  var21 = new EntitySnowShovelFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("slime")) {
                  var21 = new EntityBreakingFX(this.theWorld, var2, var4, var6, Items.slime_ball);
               } else if(var1.equals("heart")) {
                  var21 = new EntityHeartFX(this.theWorld, var2, var4, var6, var8, var10, var12);
               } else if(var1.equals("angryVillager")) {
                  var21 = new EntityHeartFX(this.theWorld, var2, var4 + 0.5D, var6, var8, var10, var12);
                  ((EntityFX)var21).setParticleTextureIndex(81);
                  ((EntityFX)var21).setRBGColorF(1.0F, 1.0F, 1.0F);
               } else if(var1.equals("happyVillager")) {
                  var21 = new EntityAuraFX(this.theWorld, var2, var4, var6, var8, var10, var12);
                  ((EntityFX)var21).setParticleTextureIndex(82);
                  ((EntityFX)var21).setRBGColorF(1.0F, 1.0F, 1.0F);
               } else {
                  int var26;
                  String[] var27;
                  if(var1.startsWith("iconcrack_")) {
                     var27 = var1.split("_", 3);
                     int var25 = Integer.parseInt(var27[1]);
                     if(var27.length > 2) {
                        var26 = Integer.parseInt(var27[2]);
                        var21 = new EntityBreakingFX(this.theWorld, var2, var4, var6, var8, var10, var12, Item.getItemById(var25), var26);
                     } else {
                        var21 = new EntityBreakingFX(this.theWorld, var2, var4, var6, var8, var10, var12, Item.getItemById(var25), 0);
                     }
                  } else {
                     Block var28;
                     if(var1.startsWith("blockcrack_")) {
                        var27 = var1.split("_", 3);
                        var28 = Block.getBlockById(Integer.parseInt(var27[1]));
                        var26 = Integer.parseInt(var27[2]);
                        var21 = (new EntityDiggingFX(this.theWorld, var2, var4, var6, var8, var10, var12, var28, var26)).applyRenderColor(var26);
                     } else if(var1.startsWith("blockdust_")) {
                        var27 = var1.split("_", 3);
                        var28 = Block.getBlockById(Integer.parseInt(var27[1]));
                        var26 = Integer.parseInt(var27[2]);
                        var21 = (new EntityBlockDustFX(this.theWorld, var2, var4, var6, var8, var10, var12, var28, var26)).applyRenderColor(var26);
                     }
                  }
               }

               if(var21 != null) {
                  this.mc.effectRenderer.addEffect((EntityFX)var21);
               }

               return (EntityFX)var21;
            }
         }
      } else {
         return null;
      }
   }

   public void onEntityCreate(Entity var1) {}

   public void onEntityDestroy(Entity var1) {}

   public void deleteAllDisplayLists() {
      GLAllocation.deleteDisplayLists(this.glRenderListBase);
   }

   public void broadcastSound(int var1, int var2, int var3, int var4, int var5) {
      Random var6 = this.theWorld.rand;
      switch(var1) {
      case 1013:
      case 1018:
         if(this.mc.renderViewEntity != null) {
            double var7 = (double)var2 - this.mc.renderViewEntity.posX;
            double var9 = (double)var3 - this.mc.renderViewEntity.posY;
            double var11 = (double)var4 - this.mc.renderViewEntity.posZ;
            double var13 = Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
            double var15 = this.mc.renderViewEntity.posX;
            double var17 = this.mc.renderViewEntity.posY;
            double var19 = this.mc.renderViewEntity.posZ;
            if(var13 > 0.0D) {
               var15 += var7 / var13 * 2.0D;
               var17 += var9 / var13 * 2.0D;
               var19 += var11 / var13 * 2.0D;
            }

            if(var1 == 1013) {
               this.theWorld.playSound(var15, var17, var19, "mob.wither.spawn", 1.0F, 1.0F, false);
            } else if(var1 == 1018) {
               this.theWorld.playSound(var15, var17, var19, "mob.enderdragon.end", 5.0F, 1.0F, false);
            }
         }
      default:
      }
   }

   public void playAuxSFX(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
      Random var7 = this.theWorld.rand;
      Block var8 = null;
      double var9;
      double var11;
      double var13;
      String var15;
      int var16;
      double var22;
      double var26;
      double var28;
      double var30;
      int var40;
      double var41;
      switch(var2) {
      case 1000:
         this.theWorld.playSound((double)var3, (double)var4, (double)var5, "random.click", 1.0F, 1.0F, false);
         break;
      case 1001:
         this.theWorld.playSound((double)var3, (double)var4, (double)var5, "random.click", 1.0F, 1.2F, false);
         break;
      case 1002:
         this.theWorld.playSound((double)var3, (double)var4, (double)var5, "random.bow", 1.0F, 1.2F, false);
         break;
      case 1003:
         if(Math.random() < 0.5D) {
            this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         } else {
            this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         }
         break;
      case 1004:
         this.theWorld.playSound((double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), "random.fizz", 0.5F, 2.6F + (var7.nextFloat() - var7.nextFloat()) * 0.8F, false);
         break;
      case 1005:
         if(Item.getItemById(var6) instanceof ItemRecord) {
            this.theWorld.playRecord("records." + ((ItemRecord)Item.getItemById(var6)).recordName, var3, var4, var5);
         } else {
            this.theWorld.playRecord((String)null, var3, var4, var5);
         }
         break;
      case 1007:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.ghast.charge", 10.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1008:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.ghast.fireball", 10.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1009:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.ghast.fireball", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1010:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.zombie.wood", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1011:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.zombie.metal", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1012:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.zombie.woodbreak", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1014:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.wither.shoot", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1015:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.bat.takeoff", 0.05F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1016:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.zombie.infect", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1017:
         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "mob.zombie.unfect", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1020:
         this.theWorld.playSound((double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1021:
         this.theWorld.playSound((double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1022:
         this.theWorld.playSound((double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2000:
         int var34 = var6 % 3 - 1;
         int var10 = var6 / 3 % 3 - 1;
         var11 = (double)var3 + (double)var34 * 0.6D + 0.5D;
         var13 = (double)var4 + 0.5D;
         double var35 = (double)var5 + (double)var10 * 0.6D + 0.5D;

         for(int var37 = 0; var37 < 10; ++var37) {
            double var38 = var7.nextDouble() * 0.2D + 0.01D;
            double var39 = var11 + (double)var34 * 0.01D + (var7.nextDouble() - 0.5D) * (double)var10 * 0.5D;
            var22 = var13 + (var7.nextDouble() - 0.5D) * 0.5D;
            var41 = var35 + (double)var10 * 0.01D + (var7.nextDouble() - 0.5D) * (double)var34 * 0.5D;
            var26 = (double)var34 * var38 + var7.nextGaussian() * 0.01D;
            var28 = -0.03D + var7.nextGaussian() * 0.01D;
            var30 = (double)var10 * var38 + var7.nextGaussian() * 0.01D;
            this.spawnParticle("smoke", var39, var22, var41, var26, var28, var30);
         }

         return;
      case 2001:
         var8 = Block.getBlockById(var6 & 4095);
         if(var8.getMaterial() != Material.air) {
            this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var8.stepSound.getBreakSound()), (var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F, (float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F));
         }

         this.mc.effectRenderer.addBlockDestroyEffects(var3, var4, var5, var8, var6 >> 12 & 255);
         break;
      case 2002:
         var9 = (double)var3;
         var11 = (double)var4;
         var13 = (double)var5;
         var15 = "iconcrack_" + Item.getIdFromItem(Items.potionitem) + "_" + var6;

         for(var16 = 0; var16 < 8; ++var16) {
            this.spawnParticle(var15, var9, var11, var13, var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D, var7.nextGaussian() * 0.15D);
         }

         var16 = Items.potionitem.getColorFromDamage(var6);
         float var17 = (float)(var16 >> 16 & 255) / 255.0F;
         float var18 = (float)(var16 >> 8 & 255) / 255.0F;
         float var19 = (float)(var16 >> 0 & 255) / 255.0F;
         String var20 = "spell";
         if(Items.potionitem.isEffectInstant(var6)) {
            var20 = "instantSpell";
         }

         for(var40 = 0; var40 < 100; ++var40) {
            var22 = var7.nextDouble() * 4.0D;
            var41 = var7.nextDouble() * 3.141592653589793D * 2.0D;
            var26 = Math.cos(var41) * var22;
            var28 = 0.01D + var7.nextDouble() * 0.5D;
            var30 = Math.sin(var41) * var22;
            EntityFX var42 = this.doSpawnParticle(var20, var9 + var26 * 0.1D, var11 + 0.3D, var13 + var30 * 0.1D, var26, var28, var30);
            if(var42 != null) {
               float var33 = 0.75F + var7.nextFloat() * 0.25F;
               var42.setRBGColorF(var17 * var33, var18 * var33, var19 * var33);
               var42.multiplyVelocity((float)var22);
            }
         }

         this.theWorld.playSound((double)var3 + 0.5D, (double)var4 + 0.5D, (double)var5 + 0.5D, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2003:
         var9 = (double)var3 + 0.5D;
         var11 = (double)var4;
         var13 = (double)var5 + 0.5D;
         var15 = "iconcrack_" + Item.getIdFromItem(Items.ender_eye);

         for(var16 = 0; var16 < 8; ++var16) {
            this.spawnParticle(var15, var9, var11, var13, var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D, var7.nextGaussian() * 0.15D);
         }

         for(double var36 = 0.0D; var36 < 6.283185307179586D; var36 += 0.15707963267948966D) {
            this.spawnParticle("portal", var9 + Math.cos(var36) * 5.0D, var11 - 0.4D, var13 + Math.sin(var36) * 5.0D, Math.cos(var36) * -5.0D, 0.0D, Math.sin(var36) * -5.0D);
            this.spawnParticle("portal", var9 + Math.cos(var36) * 5.0D, var11 - 0.4D, var13 + Math.sin(var36) * 5.0D, Math.cos(var36) * -7.0D, 0.0D, Math.sin(var36) * -7.0D);
         }

         return;
      case 2004:
         for(var40 = 0; var40 < 20; ++var40) {
            var22 = (double)var3 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            var41 = (double)var4 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            var26 = (double)var5 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            this.theWorld.spawnParticle("smoke", var22, var41, var26, 0.0D, 0.0D, 0.0D);
            this.theWorld.spawnParticle("flame", var22, var41, var26, 0.0D, 0.0D, 0.0D);
         }

         return;
      case 2005:
         ItemDye.func_150918_a(this.theWorld, var3, var4, var5, var6);
         break;
      case 2006:
         var8 = this.theWorld.getBlock(var3, var4, var5);
         if(var8.getMaterial() != Material.air) {
            double var21 = (double)Math.min(0.2F + (float)var6 / 15.0F, 10.0F);
            if(var21 > 2.5D) {
               var21 = 2.5D;
            }

            int var23 = (int)(150.0D * var21);

            for(int var24 = 0; var24 < var23; ++var24) {
               float var25 = MathHelper.randomFloatClamp(var7, 0.0F, 6.2831855F);
               var26 = (double)MathHelper.randomFloatClamp(var7, 0.75F, 1.0F);
               var28 = 0.20000000298023224D + var21 / 100.0D;
               var30 = (double)(MathHelper.cos(var25) * 0.2F) * var26 * var26 * (var21 + 0.2D);
               double var32 = (double)(MathHelper.sin(var25) * 0.2F) * var26 * var26 * (var21 + 0.2D);
               this.theWorld.spawnParticle("blockdust_" + Block.getIdFromBlock(var8) + "_" + this.theWorld.getBlockMetadata(var3, var4, var5), (double)((float)var3 + 0.5F), (double)((float)var4 + 1.0F), (double)((float)var5 + 0.5F), var30, var28, var32);
            }
         }
      }

   }

   public void destroyBlockPartially(int var1, int var2, int var3, int var4, int var5) {
      if(var5 >= 0 && var5 < 10) {
         DestroyBlockProgress var6 = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(var1));
         if(var6 == null || var6.getPartialBlockX() != var2 || var6.getPartialBlockY() != var3 || var6.getPartialBlockZ() != var4) {
            var6 = new DestroyBlockProgress(var1, var2, var3, var4);
            this.damagedBlocks.put(Integer.valueOf(var1), var6);
         }

         var6.setPartialBlockDamage(var5);
         var6.setCloudUpdateTick(this.cloudTickCounter);
      } else {
         this.damagedBlocks.remove(Integer.valueOf(var1));
      }

   }

   public void registerDestroyBlockIcons(IIconRegister var1) {
      this.destroyBlockIcons = new IIcon[10];

      for(int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2) {
         this.destroyBlockIcons[var2] = var1.registerIcon("destroy_stage_" + var2);
      }

   }

}
