package me.fabric.eyephonemod.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class DummyBlock extends Block {
    public DummyBlock() {
        super(FabricBlockSettings.of(Material.STONE).breakInstantly());
    }
}
