package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslationFormatException;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ChatComponentTranslation extends ChatComponentStyle {

   private final String key;
   private final Object[] formatArgs;
   private final Object syncLock = new Object();
   private long lastTranslationUpdateTimeInMilliseconds = -1L;
   List children = Lists.newArrayList();
   public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");


   public ChatComponentTranslation(String var1, Object ... var2) {
      this.key = var1;
      this.formatArgs = var2;
      Object[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if(var6 instanceof IChatComponent) {
            ((IChatComponent)var6).getChatStyle().setParentStyle(this.getChatStyle());
         }
      }

   }

   synchronized void ensureInitialized() {
      Object var1 = this.syncLock;
      synchronized(this.syncLock) {
         long var2 = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
         if(var2 == this.lastTranslationUpdateTimeInMilliseconds) {
            return;
         }

         this.lastTranslationUpdateTimeInMilliseconds = var2;
         this.children.clear();
      }

      try {
         this.initializeFromFormat(StatCollector.translateToLocal(this.key));
      } catch (ChatComponentTranslationFormatException var6) {
         this.children.clear();

         try {
            this.initializeFromFormat(StatCollector.translateToFallback(this.key));
         } catch (ChatComponentTranslationFormatException var5) {
            throw var6;
         }
      }

   }

   protected void initializeFromFormat(String var1) {
      boolean var2 = false;
      Matcher var3 = stringVariablePattern.matcher(var1);
      int var4 = 0;
      int var5 = 0;

      try {
         int var7;
         for(; var3.find(var5); var5 = var7) {
            int var6 = var3.start();
            var7 = var3.end();
            if(var6 > var5) {
               ChatComponentText var8 = new ChatComponentText(String.format(var1.substring(var5, var6), new Object[0]));
               var8.getChatStyle().setParentStyle(this.getChatStyle());
               this.children.add(var8);
            }

            String var14 = var3.group(2);
            String var9 = var1.substring(var6, var7);
            if("%".equals(var14) && "%%".equals(var9)) {
               ChatComponentText var15 = new ChatComponentText("%");
               var15.getChatStyle().setParentStyle(this.getChatStyle());
               this.children.add(var15);
            } else {
               if(!"s".equals(var14)) {
                  throw new ChatComponentTranslationFormatException(this, "Unsupported format: \'" + var9 + "\'");
               }

               String var10 = var3.group(1);
               int var11 = var10 != null?Integer.parseInt(var10) - 1:var4++;
               this.children.add(this.getFormatArgumentAsComponent(var11));
            }
         }

         if(var5 < var1.length()) {
            ChatComponentText var13 = new ChatComponentText(String.format(var1.substring(var5), new Object[0]));
            var13.getChatStyle().setParentStyle(this.getChatStyle());
            this.children.add(var13);
         }

      } catch (IllegalFormatException var12) {
         throw new ChatComponentTranslationFormatException(this, var12);
      }
   }

   private IChatComponent getFormatArgumentAsComponent(int var1) {
      if(var1 >= this.formatArgs.length) {
         throw new ChatComponentTranslationFormatException(this, var1);
      } else {
         Object var2 = this.formatArgs[var1];
         Object var3;
         if(var2 instanceof IChatComponent) {
            var3 = (IChatComponent)var2;
         } else {
            var3 = new ChatComponentText(var2 == null?"null":var2.toString());
            ((IChatComponent)var3).getChatStyle().setParentStyle(this.getChatStyle());
         }

         return (IChatComponent)var3;
      }
   }

   public IChatComponent setChatStyle(ChatStyle var1) {
      super.setChatStyle(var1);
      Object[] var2 = this.formatArgs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if(var5 instanceof IChatComponent) {
            ((IChatComponent)var5).getChatStyle().setParentStyle(this.getChatStyle());
         }
      }

      if(this.lastTranslationUpdateTimeInMilliseconds > -1L) {
         Iterator var6 = this.children.iterator();

         while(var6.hasNext()) {
            IChatComponent var7 = (IChatComponent)var6.next();
            var7.getChatStyle().setParentStyle(var1);
         }
      }

      return this;
   }

   public Iterator iterator() {
      this.ensureInitialized();
      return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(super.siblings));
   }

   public String getUnformattedTextForChat() {
      this.ensureInitialized();
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         IChatComponent var3 = (IChatComponent)var2.next();
         var1.append(var3.getUnformattedTextForChat());
      }

      return var1.toString();
   }

   public ChatComponentTranslation createCopy() {
      Object[] var1 = new Object[this.formatArgs.length];

      for(int var2 = 0; var2 < this.formatArgs.length; ++var2) {
         if(this.formatArgs[var2] instanceof IChatComponent) {
            var1[var2] = ((IChatComponent)this.formatArgs[var2]).createCopy();
         } else {
            var1[var2] = this.formatArgs[var2];
         }
      }

      ChatComponentTranslation var5 = new ChatComponentTranslation(this.key, var1);
      var5.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var3 = this.getSiblings().iterator();

      while(var3.hasNext()) {
         IChatComponent var4 = (IChatComponent)var3.next();
         var5.appendSibling(var4.createCopy());
      }

      return var5;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(!(var1 instanceof ChatComponentTranslation)) {
         return false;
      } else {
         ChatComponentTranslation var2 = (ChatComponentTranslation)var1;
         return Arrays.equals(this.formatArgs, var2.formatArgs) && this.key.equals(var2.key) && super.equals(var1);
      }
   }

   public int hashCode() {
      int var1 = super.hashCode();
      var1 = 31 * var1 + this.key.hashCode();
      var1 = 31 * var1 + Arrays.hashCode(this.formatArgs);
      return var1;
   }

   public String toString() {
      return "TranslatableComponent{key=\'" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + super.siblings + ", style=" + this.getChatStyle() + '}';
   }

   public String getKey() {
      return this.key;
   }

   public Object[] getFormatArgs() {
      return this.formatArgs;
   }

   // $FF: synthetic method
   public IChatComponent createCopy() {
      return this.createCopy();
   }

}
