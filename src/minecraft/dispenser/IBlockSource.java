package net.minecraft.dispenser;

import net.minecraft.dispenser.ILocatableSource;
import net.minecraft.tileentity.TileEntity;

public interface IBlockSource extends ILocatableSource {

   double getX();

   double getY();

   double getZ();

   int getXInt();

   int getYInt();

   int getZInt();

   int getBlockMetadata();

   TileEntity getBlockTileEntity();
}
