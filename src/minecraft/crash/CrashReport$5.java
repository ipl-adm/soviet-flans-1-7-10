package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;

class CrashReport$5 implements Callable {

   // $FF: synthetic field
   final CrashReport theCrashReport;


   CrashReport$5(CrashReport var1) {
      this.theCrashReport = var1;
   }

   public String call() {
      Runtime var1 = Runtime.getRuntime();
      long var2 = var1.maxMemory();
      long var4 = var1.totalMemory();
      long var6 = var1.freeMemory();
      long var8 = var2 / 1024L / 1024L;
      long var10 = var4 / 1024L / 1024L;
      long var12 = var6 / 1024L / 1024L;
      return var6 + " bytes (" + var12 + " MB) / " + var4 + " bytes (" + var10 + " MB) up to " + var2 + " bytes (" + var8 + " MB)";
   }

   // $FF: synthetic method
   public Object call() {
      return this.call();
   }
}
