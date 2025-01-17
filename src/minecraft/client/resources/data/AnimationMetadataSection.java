package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.IMetadataSection;

public class AnimationMetadataSection implements IMetadataSection {

   private final List animationFrames;
   private final int frameWidth;
   private final int frameHeight;
   private final int frameTime;


   public AnimationMetadataSection(List var1, int var2, int var3, int var4) {
      this.animationFrames = var1;
      this.frameWidth = var2;
      this.frameHeight = var3;
      this.frameTime = var4;
   }

   public int getFrameHeight() {
      return this.frameHeight;
   }

   public int getFrameWidth() {
      return this.frameWidth;
   }

   public int getFrameCount() {
      return this.animationFrames.size();
   }

   public int getFrameTime() {
      return this.frameTime;
   }

   private AnimationFrame getAnimationFrame(int var1) {
      return (AnimationFrame)this.animationFrames.get(var1);
   }

   public int getFrameTimeSingle(int var1) {
      AnimationFrame var2 = this.getAnimationFrame(var1);
      return var2.hasNoTime()?this.frameTime:var2.getFrameTime();
   }

   public boolean frameHasTime(int var1) {
      return !((AnimationFrame)this.animationFrames.get(var1)).hasNoTime();
   }

   public int getFrameIndex(int var1) {
      return ((AnimationFrame)this.animationFrames.get(var1)).getFrameIndex();
   }

   public Set getFrameIndexSet() {
      HashSet var1 = Sets.newHashSet();
      Iterator var2 = this.animationFrames.iterator();

      while(var2.hasNext()) {
         AnimationFrame var3 = (AnimationFrame)var2.next();
         var1.add(Integer.valueOf(var3.getFrameIndex()));
      }

      return var1;
   }
}
