package de.ItsAMysterious.mods.reallifemod.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.ItsAMysterious.mods.reallifemod.core.gui.containers.ContainerSideboard;
import de.ItsAMysterious.mods.reallifemod.core.tiles.SideboardTE;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSideboard extends GuiContainer {

   private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
   private IInventory upperChestInventory;
   private IInventory lowerChestInventory;
   private int inventoryRows;
   public static int GUI_ID;


   public GuiSideboard(InventoryPlayer p_i1083_1_, SideboardTE p_i1083_2_) {
      super(new ContainerSideboard(p_i1083_1_, p_i1083_2_));
      GUI_ID = 53;
      this.upperChestInventory = p_i1083_1_;
      this.lowerChestInventory = p_i1083_2_;
      this.field_146291_p = true;
      short short1 = 222;
      int i = short1 - 108;
      this.inventoryRows = 1;
      this.field_147000_g = i + this.inventoryRows * 18;
   }

   protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
      this.field_146289_q.drawString(this.lowerChestInventory.hasCustomInventoryName()?this.lowerChestInventory.getInventoryName():I18n.format(this.lowerChestInventory.getInventoryName(), new Object[0]), 8, 6, 4210752);
      this.field_146289_q.drawString(this.upperChestInventory.hasCustomInventoryName()?this.upperChestInventory.getInventoryName():I18n.format(this.upperChestInventory.getInventoryName(), new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.getTextureManager().bindTexture(new ResourceLocation("reallifemod:textures/gui/container/Sideboard.png"));
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.inventoryRows * 18 + 18);
      this.func_73729_b(k, l + this.inventoryRows * 18 + 18, 0, 36, this.field_146999_f, 96);
   }

}
