package de.ItsAMysterious.mods.reallifemod.core.rendering.entitys;

import de.ItsAMysterious.mods.reallifemod.core.entities.cars.EntityJeep;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderJeep extends Render {

   public IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/colored.obj"));
   public IModelCustom interior = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/model.obj"));
   public IModelCustom wheels_back = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/backwheels.obj"));
   public IModelCustom wheel_right = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/rightwheel.obj"));
   public IModelCustom wheel_left = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/leftwheel.obj"));
   public IModelCustom lights = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/lights.obj"));
   public IModelCustom steeringwheel = AdvancedModelLoader.loadModel(new ResourceLocation("reallifemod:models/vehicles/jeep/steeringwheel.obj"));
   ResourceLocation MainTexture = new ResourceLocation("reallifemod:textures/entity/VehicleTextures/jeeptexture.png");


   public static FloatBuffer allocFloats(int howmany) {
      return ByteBuffer.allocateDirect(howmany * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
   }

   public static FloatBuffer allocFloats(float[] floatarray) {
      FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      fb.put(floatarray).flip();
      return fb;
   }

   public void render(EntityJeep entity, double x, double y, double z, float rotatedYaw, float rotatedPitch) {
      GL11.glPushMatrix();
      GL11.glEnable(3008);
      GL11.glDisable(2884);
      GL11.glEnable(2896);
      double var10000 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * x;
      var10000 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * y;
      var10000 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * z;
      GL11.glTranslated(x, y + 0.574D, z);
      GL11.glScalef(1.2F, 1.2F, 1.2F);
      GL11.glPushMatrix();
      GL11.glRotatef(rotatedYaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(entity.field_70125_A - entity.field_70127_C, 1.0F, 0.0F, 0.0F);
      GL11.glPushMatrix();
      this.field_76990_c.renderEngine.bindTexture(new ResourceLocation("reallifemod:textures/entity/VehicleTextures/jeepLights.png"));
      this.lights.renderAll();
      GL11.glPopMatrix();
      this.field_76990_c.renderEngine.bindTexture(this.MainTexture);
      this.interior.renderAll();
      GL11.glPushMatrix();
      GL11.glRotated(entity.wheelrotation, 1.0D, 0.0D, 0.0D);
      this.wheels_back.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.8F, 0.0F, 3.625F);
      GL11.glRotated(entity.deltasteer, 0.0D, 1.0D, 0.0D);
      GL11.glRotated(entity.wheelrotation, 1.0D, 0.0D, 0.0D);
      this.wheel_left.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(-0.8F, 0.0F, 3.625F);
      GL11.glRotated(entity.deltasteer, 0.0D, 1.0D, 0.0D);
      GL11.glRotated(entity.wheelrotation, 1.0D, 0.0D, 0.0D);
      this.wheel_right.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef(0.5F, 0.875F, 2.38F);
      GL11.glRotatef(15.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotated(-entity.deltasteer, 0.0D, 0.0D, 1.0D);
      this.steeringwheel.renderAll();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      this.field_76990_c.renderEngine.bindTexture(new ResourceLocation("reallifemod:textures/entity/VehicleTextures/jeep_colored.png"));
      GL11.glColor4f(entity.getColor().x, entity.getColor().y, entity.getColor().z, 1.0F);
      this.model.renderAll();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   protected ResourceLocation func_110775_a(Entity p_110775_1_) {
      return this.MainTexture;
   }

   public boolean func_147905_a() {
      return false;
   }

   private static FloatBuffer asFloatBuffer(float[] values) {
      FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
      buffer.put(values);
      buffer.flip();
      return buffer;
   }

   public FloatBuffer floatBuffer(float[] data) {
      FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
      fb.put(data);
      fb.flip();
      return fb;
   }

   public void func_76986_a(Entity entity, double x, double y, double z, float rotationYaw, float rotationPitch) {
      this.render((EntityJeep)entity, x, y, z, rotationYaw, rotationPitch);
   }
}
