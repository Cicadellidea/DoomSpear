package com.Cicadellidea.doom_spear.capability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerExtraDataProvider implements ICapabilitySerializable<CompoundTag> {
    private final PlayerExtraDataImpl data = new PlayerExtraDataImpl();
    private final LazyOptional<PlayerExtraDataImpl> opt = LazyOptional.of(() -> data);

    public static final Capability<PlayerExtraDataImpl> PLAYER_EXTRA_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return PLAYER_EXTRA_DATA.orEmpty(cap, opt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.saveNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.loadNBT(nbt);
    }
}