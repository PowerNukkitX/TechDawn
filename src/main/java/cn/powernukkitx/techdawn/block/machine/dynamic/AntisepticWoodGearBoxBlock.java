package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class AntisepticWoodGearBoxBlock extends BaseGearBoxBlock {
    @SuppressWarnings("unused")
    public AntisepticWoodGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public AntisepticWoodGearBoxBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:antiseptic_wood_gear_box";
    }
}
