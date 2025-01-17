package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;

public enum SoundCategory {

   MASTER("MASTER", 0, "master", 0),
   MUSIC("MUSIC", 1, "music", 1),
   RECORDS("RECORDS", 2, "record", 2),
   WEATHER("WEATHER", 3, "weather", 3),
   BLOCKS("BLOCKS", 4, "block", 4),
   MOBS("MOBS", 5, "hostile", 5),
   ANIMALS("ANIMALS", 6, "neutral", 6),
   PLAYERS("PLAYERS", 7, "player", 7),
   AMBIENT("AMBIENT", 8, "ambient", 8);
   private static final Map field_147168_j = Maps.newHashMap();
   private static final Map field_147169_k = Maps.newHashMap();
   private final String categoryName;
   private final int categoryId;
   // $FF: synthetic field
   private static final SoundCategory[] $VALUES = new SoundCategory[]{MASTER, MUSIC, RECORDS, WEATHER, BLOCKS, MOBS, ANIMALS, PLAYERS, AMBIENT};


   private SoundCategory(String var1, int var2, String var3, int var4) {
      this.categoryName = var3;
      this.categoryId = var4;
   }

   public String getCategoryName() {
      return this.categoryName;
   }

   public int getCategoryId() {
      return this.categoryId;
   }

   public static SoundCategory func_147154_a(String var0) {
      return (SoundCategory)field_147168_j.get(var0);
   }

   static {
      SoundCategory[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         SoundCategory var3 = var0[var2];
         if(field_147168_j.containsKey(var3.getCategoryName()) || field_147169_k.containsKey(Integer.valueOf(var3.getCategoryId()))) {
            throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + var3);
         }

         field_147168_j.put(var3.getCategoryName(), var3);
         field_147169_k.put(Integer.valueOf(var3.getCategoryId()), var3);
      }

   }
}
