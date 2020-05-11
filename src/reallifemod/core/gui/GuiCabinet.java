package de.ItsAMysterious.mods.reallifemod.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.ItsAMysterious.mods.reallifemod.core.gui.containers.ContainerCabinet;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCabinet extends GuiContainer {

   private IInventory upperChestInventory;
   private IInventory lowerChestInventory;
   private int inventoryRows;
   public static int GUI_ID;


   public GuiCabinet() {
      super((Container)null);
      GUI_ID = 51;
   }

   public GuiCabinet(IInventory p_i1083_1_, IInventory p_i1083_2_) {
      super(new ContainerCabinet(p_i1083_1_, p_i1083_2_));
      this.upperChestInventory = p_i1083_1_;
      this.lowerChestInventory = p_i1083_2_;
      this.field_146291_p = true;
      short short1 = 222;
      int i = short1 - 108;
      this.inventoryRows = 1;
      this.field_147000_g = i + this.inventoryRows * 18;
   }

   protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
      this.field_146289_q.drawString(this.lowerChestInventory.hasCustomInventoryName()?this.lowerChestInventory.getInventoryName():I18n.format(this.lowerChestInventory.getInventoryName(), new Object[0]), 67, -10, 4210752);
      this.field_146289_q.drawString(this.upperChestInventory.hasCustomInventoryName()?this.upperChestInventory.getInventoryName():I18n.format(this.upperChestInventory.getInventoryName(), new Object[0]), 12, this.field_147000_g - 76 + 2, 4210752);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.getTextureManager().bindTexture(new ResourceLocation("reallifemod:textures/gui/container/Cabinet.png"));
      int k = (this.field_146294_l - this.field_146999_f) / 2 + 5;
      int l = (this.field_146295_m - this.field_147000_g) / 2 - 16;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.inventoryRows * 18 + 18);
      this.func_73729_b(k, l + this.inventoryRows * 18 + 18, 0, 36, this.field_146999_f, 136);
   }
}