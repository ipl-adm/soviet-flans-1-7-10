package de.ItsAMysterious.mods.reallifemod.core.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChair extends ModelBase {

   ModelRenderer Shape1;
   ModelRenderer Shape2;
   ModelRenderer Shape3;
   ModelRenderer Shape4;
   ModelRenderer Shape5;
   ModelRenderer Shape6;
   ModelRenderer Shape7;
   ModelRenderer Shape8;
   ModelRenderer Shape9;


   public ModelChair() {
      this.field_78090_t = 512;
      this.field_78089_u = 256;
      this.Shape1 = new ModelRenderer(this, 0, 0);
      this.Shape1.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1);
      this.Shape1.setRotationPoint(-5.0F, 14.0F, -7.0F);
      this.Shape1.setTextureSize(64, 32);
      this.Shape1.mirror = true;
      this.setRotation(this.Shape1, 0.0F, 0.0F, 0.0F);
      this.Shape2 = new ModelRenderer(this, 0, 0);
      this.Shape2.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1);
      this.Shape2.setRotationPoint(4.0F, 14.0F, -7.0F);
      this.Shape2.setTextureSize(64, 32);
      this.Shape2.mirror = true;
      this.setRotation(this.Shape2, 0.0F, 0.0F, 0.0F);
      this.Shape3 = new ModelRenderer(this, 0, 0);
      this.Shape3.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1);
      this.Shape3.setRotationPoint(-5.0F, 14.0F, 5.0F);
      this.Shape3.setTextureSize(64, 32);
      this.Shape3.mirror = true;
      this.setRotation(this.Shape3, 0.0F, 0.0F, 0.0F);
      this.Shape4 = new ModelRenderer(this, 0, 0);
      this.Shape4.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1);
      this.Shape4.setRotationPoint(4.0F, 14.0F, 5.0F);
      this.Shape4.setTextureSize(64, 32);
      this.Shape4.mirror = true;
      this.setRotation(this.Shape4, 0.0F, 0.0F, 0.0F);
      this.Shape5 = new ModelRenderer(this, 30, 0);
      this.Shape5.addBox(0.0F, 0.0F, 0.0F, 12, 1, 13);
      this.Shape5.setRotationPoint(-6.0F, 13.0F, -7.0F);
      this.Shape5.setTextureSize(64, 32);
      this.Shape5.mirror = true;
      this.setRotation(this.Shape5, 0.0F, 0.0F, 0.0F);
      this.Shape6 = new ModelRenderer(this, 0, 0);
      this.Shape6.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1);
      this.Shape6.setRotationPoint(-6.0F, 1.0F, 6.0F);
      this.Shape6.setTextureSize(512, 256);
      this.Shape6.mirror = true;
      this.setRotation(this.Shape6, -0.1047198F, 0.0F, 0.0F);
      this.Shape7 = new ModelRenderer(this, 0, 0);
      this.Shape7.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1);
      this.Shape7.setRotationPoint(5.0F, 1.0F, 6.0F);
      this.Shape7.setTextureSize(512, 256);
      this.Shape7.mirror = true;
      this.setRotation(this.Shape7, -0.1047198F, 0.0F, 0.0F);
      this.Shape8 = new ModelRenderer(this, 0, 32);
      this.Shape8.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1);
      this.Shape8.setRotationPoint(-6.0F, 0.0F, 6.0F);
      this.Shape8.setTextureSize(512, 256);
      this.Shape8.mirror = true;
      this.setRotation(this.Shape8, 0.0F, 0.0F, 0.0F);
      this.Shape9 = new ModelRenderer(this, 0, 0);
      this.Shape9.addBox(0.0F, 0.0F, 0.0F, 1, 13, 1);
      this.Shape9.setRotationPoint(-0.5F, 0.0F, 6.0F);
      this.Shape9.setTextureSize(512, 256);
      this.Shape9.mirror = true;
      this.setRotation(this.Shape9, -0.1047198F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.Shape1.render(f5);
      this.Shape2.render(f5);
      this.Shape3.render(f5);
      this.Shape4.render(f5);
      this.Shape5.render(f5);
      this.Shape6.render(f5);
      this.Shape7.render(f5);
      this.Shape8.render(f5);
      this.Shape9.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
