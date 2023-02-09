package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class ToughCopperDust extends BaseDust {
    public ToughCopperDust() {
        super("techdawn:tough_copper_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tough_copper_dust tough_copper copper";
    }
}
