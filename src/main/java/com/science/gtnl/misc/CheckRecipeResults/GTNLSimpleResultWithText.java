package com.science.gtnl.misc.CheckRecipeResults;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.science.gtnl.Utils.item.TextEnums;

import gregtech.api.recipe.check.CheckRecipeResult;

public class GTNLSimpleResultWithText implements CheckRecipeResult {

    public boolean success;
    public String key;
    public boolean persistsOnShutdown;

    GTNLSimpleResultWithText(boolean success, String key, boolean persistsOnShutdown) {
        this.success = success;
        this.key = key;
        this.persistsOnShutdown = persistsOnShutdown;
    }

    @Override
    public @NotNull String getID() {
        return "GTNLSimpleResultWithText";
    }

    @Override
    public boolean wasSuccessful() {
        return success;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return Objects.requireNonNull(TextEnums.tr(key));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean("success", success);
        tag.setBoolean("persistsOnShutdown", persistsOnShutdown);
        tag.setString("key", key);

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        success = tag.getBoolean("success");
        persistsOnShutdown = tag.getBoolean("persistsOnShutdown");
        key = tag.getString("key");
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new GTNLSimpleResultWithText(false, "", false);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeBoolean(success);
        NetworkUtils.writeStringSafe(buffer, key);
        buffer.writeBoolean(persistsOnShutdown);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        success = buffer.readBoolean();
        key = NetworkUtils.readStringSafe(buffer);
        persistsOnShutdown = buffer.readBoolean();
    }

    @Override
    public boolean persistsOnShutdown() {
        return persistsOnShutdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof GTNLSimpleResultWithText that) {
            return success == that.success && Objects.equals(key, that.key)
                && persistsOnShutdown == that.persistsOnShutdown;
        }
        return false;
    }

    /**
     * Creates new result with successful state. Add your localized description with `{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static CheckRecipeResult ofSuccess(String key) {
        return new GTNLSimpleResultWithText(true, key, false);
    }

    /**
     * Creates new result with failed state. Add your localized description with `{key}`.
     * This is already registered to registry.
     */
    @Nonnull
    public static CheckRecipeResult ofFailure(String key) {
        return new GTNLSimpleResultWithText(false, key, false);
    }

    /**
     * Creates new result object with failed state that does not get reset on shutdown. Add your localized description
     * with `{key}`. This is already registered to registry.
     */
    public static CheckRecipeResult ofFailurePersistOnShutdown(String key) {
        return new GTNLSimpleResultWithText(false, key, true);
    }
}
