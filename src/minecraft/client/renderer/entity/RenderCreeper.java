package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCreeper extends RenderLiving {

   private static final ResourceLocation armoredCreeperTextures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
   private ModelBase creeperModel = new ModelCreeper(2.0F);


   public RenderCreeper() {
      super(new ModelCreeper(), 0.5F);
   }

   protected void preRenderCallback(EntityCreeper var1, float var2) {
      float var3 = var1.getCreeperFlashIntensity(var2);
      float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;
      if(var3 < 0.0F) {
         var3 = 0.0F;
      }

      if(var3 > 1.0F) {
         var3 = 1.0F;
      }

      var3 *= var3;
      var3 *= var3;
      float var5 = (1.0F + var3 * 0.4F) * var4;
      float var6 = (1.0F + var3 * 0.1F) / var4;
      GL11.glScalef(var5, var6, var5);
   }

   protected int getColorMultiplier(EntityCreeper var1, float var2, float var3) {
      float var4 = var1.getCreeperFlashIntensity(var3);
      if((int)(var4 * 10.0F) % 2 == 0) {
         return 0;
      } else {
         int var5 = (int)(var4 * 0.2F * 255.0F);
         if(var5 < 0) {
            var5 = 0;
         }

         if(var5 > 255) {
            var5 = 255;
         }

         short var6 = 255;
         short var7 = 255;
         short var8 = 255;
         return var5 << 24 | var6 << 16 | var7 << 8 | var8;
      }
   }

   protected int shouldRenderPass(EntityCreeper var1, int var2, float var3) {
      if(var1.getPowered()) {
         if(var1.isInvisible()) {
            GL11.glDepthMask(false);
         } else {
            GL11.glDepthMask(true);
         }

         if(var2 == 1) {
            float var4 = (float)var1.ticksExisted + var3;
            this.bindTexture(armoredCreeperTextures);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float var5 = var4 * 0.01F;
            float var6 = var4 * 0.01F;
            GL11.glTranslatef(var5, var6, 0.0F);
            this.setRenderPassModel(this.creeperModel);
            GL11.glMatrixMode(5888);
            GL11.glEnable(3042);
            float var7 = 0.5F;
            GL11.glColor4f(var7, var7, var7, 1.0F);
            GL11.glDisable(2896);
            GL11.glBlendFunc(1, 1);
            return 1;
         }

         if(var2 == 2) {
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
         }
      }

      return -1;
   }

   protected int inheritRenderPass(EntityCreeper var1, int var2, float var3) {
      return -1;
   }

   protected ResourceLocation getEntityTexture(EntityCreeper var1) {
      return creeperTextures;
   }

}
