package com.science.gtnl.common.machine.multiMachineClasses.EdenGardenManager.buckets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.science.gtnl.common.machine.multiMachineClasses.EdenGardenManager.EIGBucket;
import com.science.gtnl.common.machine.multiMachineClasses.EdenGardenManager.EIGDropTable;
import com.science.gtnl.common.machine.multiMachineClasses.EdenGardenManager.IEIGBucketFactory;
import com.science.gtnl.common.machine.multiblock.EdenGarden;

public class EIGFlowerBucket extends EIGBucket {

    public final static IEIGBucketFactory factory = new Factory();
    private static final String NBT_IDENTIFIER = "FLOWER";
    private static final int REVISION_NUMBER = 0;

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(EdenGarden greenhouse, ItemStack input) {
            // Check if input is a flower, reed or cacti. They all drop their source item multiplied by their seed count
            Item item = input.getItem();
            Block block = Block.getBlockFromItem(item);
            if (item != Items.reeds && block != Blocks.cactus && !(block instanceof BlockFlower)) return null;
            return new EIGFlowerBucket(input);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGFlowerBucket(nbt);
        }
    }

    private EIGFlowerBucket(ItemStack input) {
        super(input, 1, null);
    }

    private EIGFlowerBucket(NBTTagCompound nbt) {
        super(nbt);
    }

    @Override
    public NBTTagCompound save() {
        NBTTagCompound nbt = super.save();
        nbt.setInteger("version", REVISION_NUMBER);
        return nbt;
    }

    @Override
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    @Override
    public void addProgress(double multiplier, EIGDropTable tracker) {
        tracker.addDrop(this.seed, this.seedCount * multiplier);
    }

    @Override
    public boolean revalidate(EdenGarden greenhouse) {
        return this.isValid();
    }
}
