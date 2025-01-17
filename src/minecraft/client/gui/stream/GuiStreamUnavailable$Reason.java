package net.minecraft.client.gui.stream;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum GuiStreamUnavailable$Reason {

   NO_FBO("NO_FBO", 0, new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])),
   LIBRARY_ARCH_MISMATCH("LIBRARY_ARCH_MISMATCH", 1, new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])),
   LIBRARY_FAILURE("LIBRARY_FAILURE", 2, new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
   UNSUPPORTED_OS_WINDOWS("UNSUPPORTED_OS_WINDOWS", 3, new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])),
   UNSUPPORTED_OS_MAC("UNSUPPORTED_OS_MAC", 4, new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])),
   UNSUPPORTED_OS_OTHER("UNSUPPORTED_OS_OTHER", 5, new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])),
   ACCOUNT_NOT_MIGRATED("ACCOUNT_NOT_MIGRATED", 6, new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])),
   ACCOUNT_NOT_BOUND("ACCOUNT_NOT_BOUND", 7, new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])),
   FAILED_TWITCH_AUTH("FAILED_TWITCH_AUTH", 8, new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])),
   FAILED_TWITCH_AUTH_ERROR("FAILED_TWITCH_AUTH_ERROR", 9, new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])),
   INITIALIZATION_FAILURE("INITIALIZATION_FAILURE", 10, new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
   UNKNOWN("UNKNOWN", 11, new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));
   private final IChatComponent field_152574_m;
   private final IChatComponent field_152575_n;
   // $FF: synthetic field
   private static final GuiStreamUnavailable$Reason[] $VALUES = new GuiStreamUnavailable$Reason[]{NO_FBO, LIBRARY_ARCH_MISMATCH, LIBRARY_FAILURE, UNSUPPORTED_OS_WINDOWS, UNSUPPORTED_OS_MAC, UNSUPPORTED_OS_OTHER, ACCOUNT_NOT_MIGRATED, ACCOUNT_NOT_BOUND, FAILED_TWITCH_AUTH, FAILED_TWITCH_AUTH_ERROR, INITIALIZATION_FAILURE, UNKNOWN};


   private GuiStreamUnavailable$Reason(String var1, int var2, IChatComponent var3) {
      this(var1, var2, var3, (IChatComponent)null);
   }

   private GuiStreamUnavailable$Reason(String var1, int var2, IChatComponent var3, IChatComponent var4) {
      this.field_152574_m = var3;
      this.field_152575_n = var4;
   }

   public IChatComponent func_152561_a() {
      return this.field_152574_m;
   }

   public IChatComponent func_152559_b() {
      return this.field_152575_n;
   }

}
