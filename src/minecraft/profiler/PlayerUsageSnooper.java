package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper$1;

public class PlayerUsageSnooper {

   private final Map field_152773_a = Maps.newHashMap();
   private final Map field_152774_b = Maps.newHashMap();
   private final String uniqueID = UUID.randomUUID().toString();
   private final URL serverUrl;
   private final IPlayerUsage playerStatsCollector;
   private final Timer threadTrigger = new Timer("Snooper Timer", true);
   private final Object syncLock = new Object();
   private final long minecraftStartTimeMilis;
   private boolean isRunning;
   private int selfCounter;


   public PlayerUsageSnooper(String var1, IPlayerUsage var2, long var3) {
      try {
         this.serverUrl = new URL("http://snoop.minecraft.net/" + var1 + "?version=" + 2);
      } catch (MalformedURLException var6) {
         throw new IllegalArgumentException();
      }

      this.playerStatsCollector = var2;
      this.minecraftStartTimeMilis = var3;
   }

   public void startSnooper() {
      if(!this.isRunning) {
         this.isRunning = true;
         this.func_152766_h();
         this.threadTrigger.schedule(new PlayerUsageSnooper$1(this), 0L, 900000L);
      }
   }

   private void func_152766_h() {
      this.addJvmArgsToSnooper();
      this.func_152768_a("snooper_token", this.uniqueID);
      this.func_152767_b("snooper_token", this.uniqueID);
      this.func_152767_b("os_name", System.getProperty("os.name"));
      this.func_152767_b("os_version", System.getProperty("os.version"));
      this.func_152767_b("os_architecture", System.getProperty("os.arch"));
      this.func_152767_b("java_version", System.getProperty("java.version"));
      this.func_152767_b("version", "1.7.10");
      this.playerStatsCollector.addServerTypeToSnooper(this);
   }

   private void addJvmArgsToSnooper() {
      RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
      List var2 = var1.getInputArguments();
      int var3 = 0;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if(var5.startsWith("-X")) {
            this.func_152768_a("jvm_arg[" + var3++ + "]", var5);
         }
      }

      this.func_152768_a("jvm_args", Integer.valueOf(var3));
   }

   public void addMemoryStatsToSnooper() {
      this.func_152767_b("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
      this.func_152767_b("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
      this.func_152767_b("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
      this.func_152767_b("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
      this.playerStatsCollector.addServerStatsToSnooper(this);
   }

   public void func_152768_a(String var1, Object var2) {
      Object var3 = this.syncLock;
      synchronized(this.syncLock) {
         this.field_152774_b.put(var1, var2);
      }
   }

   public void func_152767_b(String var1, Object var2) {
      Object var3 = this.syncLock;
      synchronized(this.syncLock) {
         this.field_152773_a.put(var1, var2);
      }
   }

   public Map getCurrentStats() {
      LinkedHashMap var1 = new LinkedHashMap();
      Object var2 = this.syncLock;
      synchronized(this.syncLock) {
         this.addMemoryStatsToSnooper();
         Iterator var3 = this.field_152773_a.entrySet().iterator();

         Entry var4;
         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            var1.put(var4.getKey(), var4.getValue().toString());
         }

         var3 = this.field_152774_b.entrySet().iterator();

         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            var1.put(var4.getKey(), var4.getValue().toString());
         }

         return var1;
      }
   }

   public boolean isSnooperRunning() {
      return this.isRunning;
   }

   public void stopSnooper() {
      this.threadTrigger.cancel();
   }

   public String getUniqueID() {
      return this.uniqueID;
   }

   public long getMinecraftStartTimeMillis() {
      return this.minecraftStartTimeMilis;
   }

   // $FF: synthetic method
   static IPlayerUsage access$000(PlayerUsageSnooper var0) {
      return var0.playerStatsCollector;
   }

   // $FF: synthetic method
   static Object access$100(PlayerUsageSnooper var0) {
      return var0.syncLock;
   }

   // $FF: synthetic method
   static Map access$200(PlayerUsageSnooper var0) {
      return var0.field_152774_b;
   }

   // $FF: synthetic method
   static int access$300(PlayerUsageSnooper var0) {
      return var0.selfCounter;
   }

   // $FF: synthetic method
   static Map access$400(PlayerUsageSnooper var0) {
      return var0.field_152773_a;
   }

   // $FF: synthetic method
   static int access$308(PlayerUsageSnooper var0) {
      return var0.selfCounter++;
   }

   // $FF: synthetic method
   static String access$500(PlayerUsageSnooper var0) {
      return var0.uniqueID;
   }

   // $FF: synthetic method
   static URL access$600(PlayerUsageSnooper var0) {
      return var0.serverUrl;
   }
}
