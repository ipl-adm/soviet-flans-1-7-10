package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;

public class FoliageColorReloadListener implements IResourceManagerReloadListener {

   private static final ResourceLocation field_130079_a = new ResourceLocation("textures/colormap/foliage.png");


   public void onResourceManagerReload(IResourceManager var1) {
      try {
         ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(var1, field_130079_a));
      } catch (IOException var3) {
         ;
      }

   }

}
