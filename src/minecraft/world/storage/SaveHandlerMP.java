package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class SaveHandlerMP implements ISaveHandler {

   public WorldInfo loadWorldInfo() {
      return null;
   }

   public void checkSessionLock() {}

   public IChunkLoader getChunkLoader(WorldProvider var1) {
      return null;
   }

   public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {}

   public void saveWorldInfo(WorldInfo var1) {}

   public IPlayerFileData getSaveHandler() {
      return null;
   }

   public void flush() {}

   public File getMapFileFromName(String var1) {
      return null;
   }

   public String getWorldDirectoryName() {
      return "none";
   }

   public File getWorldDirectory() {
      return null;
   }
}
