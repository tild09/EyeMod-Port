package me.fabric.eyephonemod.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {

    @Accessor("screenHandlerSyncId")
    int eyephone_screenHandlerSyncId();

    @Invoker("incrementScreenHandlerSyncId")
    void eyephone_incrementScreenHandlerSyncId();
}
