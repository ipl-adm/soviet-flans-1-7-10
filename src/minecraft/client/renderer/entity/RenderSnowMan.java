package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSnowMan extends RenderLiving {

   private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
   private ModelSnowMan snowmanModel;


   public RenderSnowMan() {
      super(new ModelSnowMan(), 0.5F);
      this.snowmanModel = (ModelSnowMan)super.mainModel;
      this.setRenderPassModel(this.snowmanModel);
   }

   protected void renderEquippedItems(EntitySnowman var1, float var2) {
      super.renderEquippedItems(var1, var2);
      ItemStack var3 = new ItemStack(Blocks.pumpkin, 1);
      if(var3.getItem() instanceof ItemBlock) {
         GL11.glPushMatrix();
         this.snowmanModel.head.postRender(0.0625F);
         if(RenderBlocks.renderItemIn3d(Block.getBlockFromItem(var3.getItem()).getRenderType())) {
            float var4 = 0.625F;
            GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(var4, -var4, var4);
         }

         super.renderManager.itemRenderer.renderItem(var1, var3, 0);
         GL11.glPopMatrix();
      }

   }

   protected ResourceLocation getEntityTexture(EntitySnowman var1) {
      return snowManTextures;
   }

   // $FF: synthetic method
   protected void renderEquippedItems(EntityLivingBase var1, float var2) {
      this.renderEquippedItems((EntitySnowman)var1, var2);
   }

}
