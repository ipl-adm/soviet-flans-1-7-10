package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings$GameType;

public class CommandGameMode extends CommandBase {

   public String getCommandName() {
      return "gamemode";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.gamemode.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) {
      if(var2.length > 0) {
         WorldSettings$GameType var3 = this.getGameModeFromCommand(var1, var2[0]);
         EntityPlayerMP var4 = var2.length >= 2?getPlayer(var1, var2[1]):getCommandSenderAsPlayer(var1);
         var4.setGameType(var3);
         var4.fallDistance = 0.0F;
         ChatComponentTranslation var5 = new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0]);
         if(var4 != var1) {
            func_152374_a(var1, this, 1, "commands.gamemode.success.other", new Object[]{var4.getCommandSenderName(), var5});
         } else {
            func_152374_a(var1, this, 1, "commands.gamemode.success.self", new Object[]{var5});
         }

      } else {
         throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
      }
   }

   protected WorldSettings$GameType getGameModeFromCommand(ICommandSender var1, String var2) {
      return !var2.equalsIgnoreCase(WorldSettings$GameType.SURVIVAL.getName()) && !var2.equalsIgnoreCase("s")?(!var2.equalsIgnoreCase(WorldSettings$GameType.CREATIVE.getName()) && !var2.equalsIgnoreCase("c")?(!var2.equalsIgnoreCase(WorldSettings$GameType.ADVENTURE.getName()) && !var2.equalsIgnoreCase("a")?WorldSettings.getGameTypeById(parseIntBounded(var1, var2, 0, WorldSettings$GameType.values().length - 2)):WorldSettings$GameType.ADVENTURE):WorldSettings$GameType.CREATIVE):WorldSettings$GameType.SURVIVAL;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1?getListOfStringsMatchingLastWord(var2, new String[]{"survival", "creative", "adventure"}):(var2.length == 2?getListOfStringsMatchingLastWord(var2, this.getListOfPlayerUsernames()):null);
   }

   protected String[] getListOfPlayerUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 1;
   }
}
