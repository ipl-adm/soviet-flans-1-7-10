package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

   private static final Logger logger = LogManager.getLogger();
   private static final Random rand = new Random();
   private float updateCounter;
   private String splashText;
   private GuiButton buttonResetDemo;
   private int panoramaTimer;
   private DynamicTexture viewportTexture;
   private final Object field_104025_t = new Object();
   private String field_92025_p;
   private String field_146972_A;
   private String field_104024_v;
   private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
   public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   private int field_92024_r;
   private int field_92023_s;
   private int field_92022_t;
   private int field_92021_u;
   private int field_92020_v;
   private int field_92019_w;
   private ResourceLocation field_110351_G;


   public GuiMainMenu() {
      this.field_146972_A = field_96138_a;
      this.splashText = "missingno";
      BufferedReader var1 = null;

      try {
         ArrayList var2 = new ArrayList();
         var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

         String var3;
         while((var3 = var1.readLine()) != null) {
            var3 = var3.trim();
            if(!var3.isEmpty()) {
               var2.add(var3);
            }
         }

         if(!var2.isEmpty()) {
            do {
               this.splashText = (String)var2.get(rand.nextInt(var2.size()));
            } while(this.splashText.hashCode() == 125780783);
         }
      } catch (IOException var12) {
         ;
      } finally {
         if(var1 != null) {
            try {
               var1.close();
            } catch (IOException var11) {
               ;
            }
         }

      }

      this.updateCounter = rand.nextFloat();
      this.field_92025_p = "";
      if(!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
         this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
         this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
         this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

   }

   public void updateScreen() {
      ++this.panoramaTimer;
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char var1, int var2) {}

   public void initGui() {
      this.viewportTexture = new DynamicTexture(256, 256);
      this.field_110351_G = super.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
      Calendar var1 = Calendar.getInstance();
      var1.setTime(new Date());
      if(var1.get(2) + 1 == 11 && var1.get(5) == 9) {
         this.splashText = "Happy birthday, ez!";
      } else if(var1.get(2) + 1 == 6 && var1.get(5) == 1) {
         this.splashText = "Happy birthday, Notch!";
      } else if(var1.get(2) + 1 == 12 && var1.get(5) == 24) {
         this.splashText = "Merry X-mas!";
      } else if(var1.get(2) + 1 == 1 && var1.get(5) == 1) {
         this.splashText = "Happy new year!";
      } else if(var1.get(2) + 1 == 10 && var1.get(5) == 31) {
         this.splashText = "OOoooOOOoooo! Spooky!";
      }

      boolean var2 = true;
      int var3 = super.height / 4 + 48;
      if(super.mc.isDemo()) {
         this.addDemoButtons(var3, 24);
      } else {
         this.addSingleplayerMultiplayerButtons(var3, 24);
      }

      super.buttonList.add(new GuiButton(0, super.width / 2 - 100, var3 + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
      super.buttonList.add(new GuiButton(4, super.width / 2 + 2, var3 + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
      super.buttonList.add(new GuiButtonLanguage(5, super.width / 2 - 124, var3 + 72 + 12));
      Object var4 = this.field_104025_t;
      synchronized(this.field_104025_t) {
         this.field_92023_s = super.fontRendererObj.getStringWidth(this.field_92025_p);
         this.field_92024_r = super.fontRendererObj.getStringWidth(this.field_146972_A);
         int var5 = Math.max(this.field_92023_s, this.field_92024_r);
         this.field_92022_t = (super.width - var5) / 2;
         this.field_92021_u = ((GuiButton)super.buttonList.get(0)).yPosition - 24;
         this.field_92020_v = this.field_92022_t + var5;
         this.field_92019_w = this.field_92021_u + 24;
      }
   }

   private void addSingleplayerMultiplayerButtons(int var1, int var2) {
      super.buttonList.add(new GuiButton(1, super.width / 2 - 100, var1, I18n.format("menu.singleplayer", new Object[0])));
      super.buttonList.add(new GuiButton(2, super.width / 2 - 100, var1 + var2 * 1, I18n.format("menu.multiplayer", new Object[0])));
      super.buttonList.add(new GuiButton(14, super.width / 2 - 100, var1 + var2 * 2, I18n.format("menu.online", new Object[0])));
   }

   private void addDemoButtons(int var1, int var2) {
      super.buttonList.add(new GuiButton(11, super.width / 2 - 100, var1, I18n.format("menu.playdemo", new Object[0])));
      super.buttonList.add(this.buttonResetDemo = new GuiButton(12, super.width / 2 - 100, var1 + var2 * 1, I18n.format("menu.resetdemo", new Object[0])));
      ISaveFormat var3 = super.mc.getSaveLoader();
      WorldInfo var4 = var3.getWorldInfo("Demo_World");
      if(var4 == null) {
         this.buttonResetDemo.enabled = false;
      }

   }

   protected void actionPerformed(GuiButton var1) {
      if(var1.id == 0) {
         super.mc.displayGuiScreen(new GuiOptions(this, super.mc.gameSettings));
      }

      if(var1.id == 5) {
         super.mc.displayGuiScreen(new GuiLanguage(this, super.mc.gameSettings, super.mc.getLanguageManager()));
      }

      if(var1.id == 1) {
         super.mc.displayGuiScreen(new GuiSelectWorld(this));
      }

      if(var1.id == 2) {
         super.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if(var1.id == 14) {
         this.func_140005_i();
      }

      if(var1.id == 4) {
         super.mc.shutdown();
      }

      if(var1.id == 11) {
         super.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
      }

      if(var1.id == 12) {
         ISaveFormat var2 = super.mc.getSaveLoader();
         WorldInfo var3 = var2.getWorldInfo("Demo_World");
         if(var3 != null) {
            GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
            super.mc.displayGuiScreen(var4);
         }
      }

   }

   private void func_140005_i() {
      RealmsBridge var1 = new RealmsBridge();
      var1.switchToRealms(this);
   }

   public void confirmClicked(boolean var1, int var2) {
      if(var1 && var2 == 12) {
         ISaveFormat var6 = super.mc.getSaveLoader();
         var6.flushCache();
         var6.deleteWorldDirectory("Demo_World");
         super.mc.displayGuiScreen(this);
      } else if(var2 == 13) {
         if(var1) {
            try {
               Class var3 = Class.forName("java.awt.Desktop");
               Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
               var3.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{new URI(this.field_104024_v)});
            } catch (Throwable var5) {
               logger.error("Couldn\'t open link", var5);
            }
         }

         super.mc.displayGuiScreen(this);
      }

   }

   private void drawPanorama(int var1, int var2, float var3) {
      Tessellator var4 = Tessellator.instance;
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glDisable(2884);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      byte var5 = 8;

      for(int var6 = 0; var6 < var5 * var5; ++var6) {
         GL11.glPushMatrix();
         float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
         float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
         float var9 = 0.0F;
         GL11.glTranslatef(var7, var8, var9);
         GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + var3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-((float)this.panoramaTimer + var3) * 0.1F, 0.0F, 1.0F, 0.0F);

         for(int var10 = 0; var10 < 6; ++var10) {
            GL11.glPushMatrix();
            if(var10 == 1) {
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 2) {
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 3) {
               GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 4) {
               GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var10 == 5) {
               GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            super.mc.getTextureManager().bindTexture(titlePanoramaPaths[var10]);
            var4.startDrawingQuads();
            var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
            float var11 = 0.0F;
            var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + var11), (double)(0.0F + var11));
            var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - var11), (double)(0.0F + var11));
            var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - var11), (double)(1.0F - var11));
            var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + var11), (double)(1.0F - var11));
            var4.draw();
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
         GL11.glColorMask(true, true, true, false);
      }

      var4.setTranslation(0.0D, 0.0D, 0.0D);
      GL11.glColorMask(true, true, true, true);
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glEnable(2884);
      GL11.glEnable(2929);
   }

   private void rotateAndBlurSkybox(float var1) {
      super.mc.getTextureManager().bindTexture(this.field_110351_G);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColorMask(true, true, true, false);
      Tessellator var2 = Tessellator.instance;
      var2.startDrawingQuads();
      GL11.glDisable(3008);
      byte var3 = 3;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(var4 + 1));
         int var5 = super.width;
         int var6 = super.height;
         float var7 = (float)(var4 - var3 / 2) / 256.0F;
         var2.addVertexWithUV((double)var5, (double)var6, (double)super.zLevel, (double)(0.0F + var7), 1.0D);
         var2.addVertexWithUV((double)var5, 0.0D, (double)super.zLevel, (double)(1.0F + var7), 1.0D);
         var2.addVertexWithUV(0.0D, 0.0D, (double)super.zLevel, (double)(1.0F + var7), 0.0D);
         var2.addVertexWithUV(0.0D, (double)var6, (double)super.zLevel, (double)(0.0F + var7), 0.0D);
      }

      var2.draw();
      GL11.glEnable(3008);
      GL11.glColorMask(true, true, true, true);
   }

   private void renderSkybox(int var1, int var2, float var3) {
      super.mc.getFramebuffer().unbindFramebuffer();
      GL11.glViewport(0, 0, 256, 256);
      this.drawPanorama(var1, var2, var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      this.rotateAndBlurSkybox(var3);
      super.mc.getFramebuffer().bindFramebuffer(true);
      GL11.glViewport(0, 0, super.mc.displayWidth, super.mc.displayHeight);
      Tessellator var4 = Tessellator.instance;
      var4.startDrawingQuads();
      float var5 = super.width > super.height?120.0F / (float)super.width:120.0F / (float)super.height;
      float var6 = (float)super.height * var5 / 256.0F;
      float var7 = (float)super.width * var5 / 256.0F;
      var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      int var8 = super.width;
      int var9 = super.height;
      var4.addVertexWithUV(0.0D, (double)var9, (double)super.zLevel, (double)(0.5F - var6), (double)(0.5F + var7));
      var4.addVertexWithUV((double)var8, (double)var9, (double)super.zLevel, (double)(0.5F - var6), (double)(0.5F - var7));
      var4.addVertexWithUV((double)var8, 0.0D, (double)super.zLevel, (double)(0.5F + var6), (double)(0.5F - var7));
      var4.addVertexWithUV(0.0D, 0.0D, (double)super.zLevel, (double)(0.5F + var6), (double)(0.5F + var7));
      var4.draw();
   }

   public void drawScreen(int var1, int var2, float var3) {
      GL11.glDisable(3008);
      this.renderSkybox(var1, var2, var3);
      GL11.glEnable(3008);
      Tessellator var4 = Tessellator.instance;
      short var5 = 274;
      int var6 = super.width / 2 - var5 / 2;
      byte var7 = 30;
      this.drawGradientRect(0, 0, super.width, super.height, -2130706433, 16777215);
      this.drawGradientRect(0, 0, super.width, super.height, 0, Integer.MIN_VALUE);
      super.mc.getTextureManager().bindTexture(minecraftTitleTextures);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if((double)this.updateCounter < 1.0E-4D) {
         this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 99, 44);
         this.drawTexturedModalRect(var6 + 99, var7 + 0, 129, 0, 27, 44);
         this.drawTexturedModalRect(var6 + 99 + 26, var7 + 0, 126, 0, 3, 44);
         this.drawTexturedModalRect(var6 + 99 + 26 + 3, var7 + 0, 99, 0, 26, 44);
         this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
      } else {
         this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
         this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
      }

      var4.setColorOpaque_I(-1);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(super.width / 2 + 90), 70.0F, 0.0F);
      GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
      float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
      var8 = var8 * 100.0F / (float)(super.fontRendererObj.getStringWidth(this.splashText) + 32);
      GL11.glScalef(var8, var8, var8);
      this.drawCenteredString(super.fontRendererObj, this.splashText, 0, -8, -256);
      GL11.glPopMatrix();
      String var9 = "Minecraft 1.7.10";
      if(super.mc.isDemo()) {
         var9 = var9 + " Demo";
      }

      this.drawString(super.fontRendererObj, var9, 2, super.height - 10, -1);
      String var10 = "Copyright Mojang AB. Do not distribute!";
      this.drawString(super.fontRendererObj, var10, super.width - super.fontRendererObj.getStringWidth(var10) - 2, super.height - 10, -1);
      if(this.field_92025_p != null && this.field_92025_p.length() > 0) {
         drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
         this.drawString(super.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
         this.drawString(super.fontRendererObj, this.field_146972_A, (super.width - this.field_92024_r) / 2, ((GuiButton)super.buttonList.get(0)).yPosition - 12, -1);
      }

      super.drawScreen(var1, var2, var3);
   }

   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      Object var4 = this.field_104025_t;
      synchronized(this.field_104025_t) {
         if(this.field_92025_p.length() > 0 && var1 >= this.field_92022_t && var1 <= this.field_92020_v && var2 >= this.field_92021_u && var2 <= this.field_92019_w) {
            GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
            var5.func_146358_g();
            super.mc.displayGuiScreen(var5);
         }

      }
   }

}
