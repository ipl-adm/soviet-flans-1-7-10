package net.minecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate {

   private String field_73727_a = "";
   private Minecraft mc;
   private String currentlyDisplayedText = "";
   private long field_73723_d = Minecraft.getSystemTime();
   private boolean field_73724_e;
   private ScaledResolution field_146587_f;
   private Framebuffer field_146588_g;


   public LoadingScreenRenderer(Minecraft var1) {
      this.mc = var1;
      this.field_146587_f = new ScaledResolution(var1, var1.displayWidth, var1.displayHeight);
      this.field_146588_g = new Framebuffer(var1.displayWidth, var1.displayHeight, false);
      this.field_146588_g.setFramebufferFilter(9728);
   }

   public void resetProgressAndMessage(String var1) {
      this.field_73724_e = false;
      this.func_73722_d(var1);
   }

   public void displayProgressMessage(String var1) {
      this.field_73724_e = true;
      this.func_73722_d(var1);
   }

   public void func_73722_d(String var1) {
      this.currentlyDisplayedText = var1;
      if(!this.mc.running) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         if(OpenGlHelper.isFramebufferEnabled()) {
            int var2 = this.field_146587_f.getScaleFactor();
            GL11.glOrtho(0.0D, (double)(this.field_146587_f.getScaledWidth() * var2), (double)(this.field_146587_f.getScaledHeight() * var2), 0.0D, 100.0D, 300.0D);
         } else {
            ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glOrtho(0.0D, var3.getScaledWidth_double(), var3.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
         }

         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      }
   }

   public void resetProgresAndWorkingMessage(String var1) {
      if(!this.mc.running) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         this.field_73723_d = 0L;
         this.field_73727_a = var1;
         this.setLoadingProgress(-1);
         this.field_73723_d = 0L;
      }
   }

   public void setLoadingProgress(int var1) {
      if(!this.mc.running) {
         if(!this.field_73724_e) {
            throw new MinecraftError();
         }
      } else {
         long var2 = Minecraft.getSystemTime();
         if(var2 - this.field_73723_d >= 100L) {
            this.field_73723_d = var2;
            ScaledResolution var4 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int var5 = var4.getScaleFactor();
            int var6 = var4.getScaledWidth();
            int var7 = var4.getScaledHeight();
            if(OpenGlHelper.isFramebufferEnabled()) {
               this.field_146588_g.framebufferClear();
            } else {
               GL11.glClear(256);
            }

            this.field_146588_g.bindFramebuffer(false);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, var4.getScaledWidth_double(), var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
            if(!OpenGlHelper.isFramebufferEnabled()) {
               GL11.glClear(16640);
            }

            Tessellator var8 = Tessellator.instance;
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            float var9 = 32.0F;
            var8.startDrawingQuads();
            var8.setColorOpaque_I(4210752);
            var8.addVertexWithUV(0.0D, (double)var7, 0.0D, 0.0D, (double)((float)var7 / var9));
            var8.addVertexWithUV((double)var6, (double)var7, 0.0D, (double)((float)var6 / var9), (double)((float)var7 / var9));
            var8.addVertexWithUV((double)var6, 0.0D, 0.0D, (double)((float)var6 / var9), 0.0D);
            var8.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
            var8.draw();
            if(var1 >= 0) {
               byte var10 = 100;
               byte var11 = 2;
               int var12 = var6 / 2 - var10 / 2;
               int var13 = var7 / 2 + 16;
               GL11.glDisable(3553);
               var8.startDrawingQuads();
               var8.setColorOpaque_I(8421504);
               var8.addVertex((double)var12, (double)var13, 0.0D);
               var8.addVertex((double)var12, (double)(var13 + var11), 0.0D);
               var8.addVertex((double)(var12 + var10), (double)(var13 + var11), 0.0D);
               var8.addVertex((double)(var12 + var10), (double)var13, 0.0D);
               var8.setColorOpaque_I(8454016);
               var8.addVertex((double)var12, (double)var13, 0.0D);
               var8.addVertex((double)var12, (double)(var13 + var11), 0.0D);
               var8.addVertex((double)(var12 + var1), (double)(var13 + var11), 0.0D);
               var8.addVertex((double)(var12 + var1), (double)var13, 0.0D);
               var8.draw();
               GL11.glEnable(3553);
            }

            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (var6 - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, var7 / 2 - 4 - 16, 16777215);
            this.mc.fontRenderer.drawStringWithShadow(this.field_73727_a, (var6 - this.mc.fontRenderer.getStringWidth(this.field_73727_a)) / 2, var7 / 2 - 4 + 8, 16777215);
            this.field_146588_g.unbindFramebuffer();
            if(OpenGlHelper.isFramebufferEnabled()) {
               this.field_146588_g.framebufferRender(var6 * var5, var7 * var5);
            }

            this.mc.func_147120_f();

            try {
               Thread.yield();
            } catch (Exception var14) {
               ;
            }

         }
      }
   }

   public void func_146586_a() {}
}
