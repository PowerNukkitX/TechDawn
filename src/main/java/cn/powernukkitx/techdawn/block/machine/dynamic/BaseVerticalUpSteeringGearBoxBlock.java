package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.dynamic.BaseVerticalSteeringGearBoxBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class BaseVerticalUpSteeringGearBoxBlock extends BlockSolidMeta implements CustomBlock, Faceable, BlockEntityHolder<BaseVerticalSteeringGearBoxBlockEntity> {
    public static final BooleanBlockProperty TRANSPOSED = new BooleanBlockProperty("transposed", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(TRANSPOSED, CommonBlockProperties.DIRECTION);

    @SuppressWarnings("unused")
    public BaseVerticalUpSteeringGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public BaseVerticalUpSteeringGearBoxBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_vertical_up_steering_gear_box";
    }

    protected String getMainTextureName() {
        return "techdawn-blocks-gear_box-antiseptic_wood_gear_box";
    }

    protected Materials createMaterials() {
        return Materials.builder().north(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front")
                .up(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front")
                .any(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_back");
    }

    protected Materials createTransposedMaterials() {
        return Materials.builder().north(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front_transposed")
                .up(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front_transposed")
                .any(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_back");
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var transposedUpMaterial = createTransposedMaterials();
        return CustomBlockDefinition
                .builder(this, createMaterials())
                .permutations(
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 180, 0))).build(),
                                "q.block_property('direction') == 0 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                                "q.block_property('direction') == 2 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 270, 0))).build(),
                                "q.block_property('direction') == 3 && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 90, 0))).build(),
                                "q.block_property('direction') == 1 && q.block_property('transposed') == false"),

                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 180, 0))).materialInstances(transposedUpMaterial).build(),
                                "q.block_property('direction') == 0 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 0, 0))).materialInstances(transposedUpMaterial).build(),
                                "q.block_property('direction') == 2 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 270, 0))).materialInstances(transposedUpMaterial).build(),
                                "q.block_property('direction') == 3 && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().transformation(fromRotation(new Vector3f(0, 90, 0))).materialInstances(transposedUpMaterial).build(),
                                "q.block_property('direction') == 1 && q.block_property('transposed') == true"))
                                .build();
    }

    protected CompoundTag createBlockEntityNBT() {
        return new CompoundTag().putString("hinge_type", "techdawn:antiseptic_wood_hinge")
                .putDouble("transfer_rate", 4.5)
                .putBoolean("is_up", true);
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            if (player.isSneaking() && face != BlockFace.UP && face != BlockFace.DOWN) {
                setBlockFace(face.getOpposite());
            } else {
                setBlockFace(player.getDirection().getOpposite());
            }
        }
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, createBlockEntityNBT()) != null;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    protected Sound getTransposeSound() {
        return Sound.HIT_WOOD;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (item.isNull()) return false;
        if (player != null && !InventoryUtil.ensurePlayerSafeForCustomInv(player)) return false;
        var tags = ItemTag.getTagSet(item.getNamespaceId());
        if (tags.contains("hammer") || tags.contains("wrench")) {
            setTransposed(!isTransposed());
            level.setBlock(this, this, true, true);
            level.addSound(this, getTransposeSound());
            return true;
        }
        return false;
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.DIRECTION, face);
    }

    public boolean isTransposed() {
        return getPropertyValue(TRANSPOSED);
    }

    public void setTransposed(boolean transposed) {
        setPropertyValue(TRANSPOSED, transposed);
    }

    @NotNull
    @Override
    public Class<? extends BaseVerticalSteeringGearBoxBlockEntity> getBlockEntityClass() {
        return BaseVerticalSteeringGearBoxBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseVerticalSteeringGearBoxBlockEntity";
    }
}
