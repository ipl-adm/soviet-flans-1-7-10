package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiFlatPresets$LayerItem;
import net.minecraft.client.gui.GuiFlatPresets$ListSlot;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen {

   private static RenderItem field_146437_a = new RenderItem();
   private static final List field_146431_f = new ArrayList();
   private final GuiCreateFlatWorld field_146432_g;
   private String field_146438_h;
   private String field_146439_i;
   private String field_146436_r;
   private GuiFlatPresets$ListSlot field_146435_s;
   private GuiButton field_146434_t;
   private GuiTextField field_146433_u;


   public GuiFlatPresets(GuiCreateFlatWorld var1) {
      this.field_146432_g = var1;
   }

   public void initGui() {
      super.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.field_146438_h = I18n.format("createWorld.customize.presets.title", new Object[0]);
      this.field_146439_i = I18n.format("createWorld.customize.presets.share", new Object[0]);
      this.field_146436_r = I18n.format("createWorld.customize.presets.list", new Object[0]);
      this.field_146433_u = new GuiTextField(super.fontRendererObj, 50, 40, super.width - 100, 20);
      this.field_146435_s = new GuiFlatPresets$ListSlot(this);
      this.field_146433_u.setMaxStringLength(1230);
      this.field_146433_u.setText(this.field_146432_g.func_146384_e());
      super.buttonList.add(this.field_146434_t = new GuiButton(0, super.width / 2 - 155, super.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
      super.buttonList.add(new GuiButton(1, super.width / 2 + 5, super.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
      this.func_146426_g();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void mouseClicked(int var1, int var2, int var3) {
      this.field_146433_u.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   protected void keyTyped(char var1, int var2) {
      if(!this.field_146433_u.textboxKeyTyped(var1, var2)) {
         super.keyTyped(var1, var2);
      }

   }

   protected void actionPerformed(GuiButton var1) {
      if(var1.id == 0 && this.func_146430_p()) {
         this.field_146432_g.func_146383_a(this.field_146433_u.getText());
         super.mc.displayGuiScreen(this.field_146432_g);
      } else if(var1.id == 1) {
         super.mc.displayGuiScreen(this.field_146432_g);
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.field_146435_s.drawScreen(var1, var2, var3);
      this.drawCenteredString(super.fontRendererObj, this.field_146438_h, super.width / 2, 8, 16777215);
      this.drawString(super.fontRendererObj, this.field_146439_i, 50, 30, 10526880);
      this.drawString(super.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
      this.field_146433_u.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   public void updateScreen() {
      this.field_146433_u.updateCursorCounter();
      super.updateScreen();
   }

   public void func_146426_g() {
      boolean var1 = this.func_146430_p();
      this.field_146434_t.enabled = var1;
   }

   private boolean func_146430_p() {
      return this.field_146435_s.field_148175_k > -1 && this.field_146435_s.field_148175_k < field_146431_f.size() || this.field_146433_u.getText().length() > 1;
   }

   private static void func_146425_a(String var0, Item var1, BiomeGenBase var2, FlatLayerInfo ... var3) {
      func_146421_a(var0, var1, var2, (List)null, var3);
   }

   private static void func_146421_a(String var0, Item var1, BiomeGenBase var2, List var3, FlatLayerInfo ... var4) {
      FlatGeneratorInfo var5 = new FlatGeneratorInfo();

      for(int var6 = var4.length - 1; var6 >= 0; --var6) {
         var5.getFlatLayers().add(var4[var6]);
      }

      var5.setBiome(var2.biomeID);
      var5.func_82645_d();
      if(var3 != null) {
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            String var7 = (String)var8.next();
            var5.getWorldFeatures().put(var7, new HashMap());
         }
      }

      field_146431_f.add(new GuiFlatPresets$LayerItem(var1, var0, var5.toString()));
   }

   // $FF: synthetic method
   static RenderItem access$000() {
      return field_146437_a;
   }

   // $FF: synthetic method
   static float access$100(GuiFlatPresets var0) {
      return var0.zLevel;
   }

   // $FF: synthetic method
   static float access$200(GuiFlatPresets var0) {
      return var0.zLevel;
   }

   // $FF: synthetic method
   static float access$300(GuiFlatPresets var0) {
      return var0.zLevel;
   }

   // $FF: synthetic method
   static float access$400(GuiFlatPresets var0) {
      return var0.zLevel;
   }

   // $FF: synthetic method
   static List access$500() {
      return field_146431_f;
   }

   // $FF: synthetic method
   static GuiFlatPresets$ListSlot access$600(GuiFlatPresets var0) {
      return var0.field_146435_s;
   }

   // $FF: synthetic method
   static GuiTextField access$700(GuiFlatPresets var0) {
      return var0.field_146433_u;
   }

   static {
      func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains, Arrays.asList(new String[]{"village"}), new FlatLayerInfo[]{new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146421_a("Tunnelers\' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills, Arrays.asList(new String[]{"biome_1", "dungeon", "decoration", "stronghold", "mineshaft"}), new FlatLayerInfo[]{new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146421_a("Water World", Item.getItemFromBlock(Blocks.flowing_water), BiomeGenBase.plains, Arrays.asList(new String[]{"village", "biome_1"}), new FlatLayerInfo[]{new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146421_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BiomeGenBase.plains, Arrays.asList(new String[]{"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"}), new FlatLayerInfo[]{new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains, Arrays.asList(new String[]{"village", "biome_1"}), new FlatLayerInfo[]{new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, Arrays.asList(new String[]{"village", "biome_1"}), new FlatLayerInfo[]{new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone)});
      func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert, Arrays.asList(new String[]{"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"}), new FlatLayerInfo[]{new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
      func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo[]{new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock)});
   }
}
