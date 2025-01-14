package com.hea3ven.colladamodel;

import com.hea3ven.colladamodel.client.model.collada.ColladaModelLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;

@Mod(
   modid = "colladamodel",
   version = "1.0a1",
   dependencies = "required-after:Forge@[10.12.0.1024,)"
)
public class ModColladaModel {

   @Instance("colladamodel")
   public static ModColladaModel instance;


   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      AdvancedModelLoader.registerModelHandler(new ColladaModelLoader());
   }
}
