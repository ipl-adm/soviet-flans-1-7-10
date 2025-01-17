package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.ResourcePackRepository$1;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class ResourcePackRepository$Entry {

   private final File resourcePackFile;
   private IResourcePack reResourcePack;
   private PackMetadataSection rePackMetadataSection;
   private BufferedImage texturePackIcon;
   private ResourceLocation locationTexturePackIcon;
   // $FF: synthetic field
   final ResourcePackRepository reResourcePackRepository;


   private ResourcePackRepository$Entry(ResourcePackRepository var1, File var2) {
      this.reResourcePackRepository = var1;
      this.resourcePackFile = var2;
   }

   public void updateResourcePack() {
      this.reResourcePack = (IResourcePack)(this.resourcePackFile.isDirectory()?new FolderResourcePack(this.resourcePackFile):new FileResourcePack(this.resourcePackFile));
      this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(this.reResourcePackRepository.rprMetadataSerializer, "pack");

      try {
         this.texturePackIcon = this.reResourcePack.getPackImage();
      } catch (IOException var2) {
         ;
      }

      if(this.texturePackIcon == null) {
         this.texturePackIcon = this.reResourcePackRepository.rprDefaultResourcePack.getPackImage();
      }

      this.closeResourcePack();
   }

   public void bindTexturePackIcon(TextureManager var1) {
      if(this.locationTexturePackIcon == null) {
         this.locationTexturePackIcon = var1.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
      }

      var1.bindTexture(this.locationTexturePackIcon);
   }

   public void closeResourcePack() {
      if(this.reResourcePack instanceof Closeable) {
         IOUtils.closeQuietly((Closeable)this.reResourcePack);
      }

   }

   public IResourcePack getResourcePack() {
      return this.reResourcePack;
   }

   public String getResourcePackName() {
      return this.reResourcePack.getPackName();
   }

   public String getTexturePackDescription() {
      return this.rePackMetadataSection == null?EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)":this.rePackMetadataSection.func_152805_a().getFormattedText();
   }

   public boolean equals(Object var1) {
      return this == var1?true:(var1 instanceof ResourcePackRepository$Entry?this.toString().equals(var1.toString()):false);
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public String toString() {
      return String.format("%s:%s:%d", new Object[]{this.resourcePackFile.getName(), this.resourcePackFile.isDirectory()?"folder":"zip", Long.valueOf(this.resourcePackFile.lastModified())});
   }

   // $FF: synthetic method
   ResourcePackRepository$Entry(ResourcePackRepository var1, File var2, ResourcePackRepository$1 var3) {
      this(var1, var2);
   }
}
