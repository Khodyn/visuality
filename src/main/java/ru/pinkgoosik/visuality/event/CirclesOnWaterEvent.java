package ru.pinkgoosik.visuality.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import ru.pinkgoosik.visuality.VisualityMod;
import ru.pinkgoosik.visuality.registry.VisualityParticles;
import ru.pinkgoosik.visuality.util.ParticleUtils;

import java.util.Random;

public class CirclesOnWaterEvent implements ClientTickEvents.StartWorldTick {
    static Random random = new Random();

    @Override
    public void onStartTick(ClientLevel world) {
        if (!VisualityMod.CONFIG.getBoolean("water_circle")) return;
        if(Minecraft.getInstance().isPaused()) return;
        if (Minecraft.getInstance().options.particles == ParticleStatus.MINIMAL) return;
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        if(player.isUnderWater() || !Level.OVERWORLD.equals(world.dimension())) return;
        if(!world.isRaining()) return;
        Biome biome = world.getBiome(player.getOnPos());
        if (!(biome.getPrecipitation().equals(Biome.Precipitation.RAIN)) || !(biome.getTemperature(player.getOnPos()) >= 0.15F)) return;

        for (int i = 0; i<= random.nextInt(10) + 5; i++){
            int x = random.nextInt(15) - 7;
            int z = random.nextInt(15) - 7;
            BlockPos playerPos = new BlockPos((int)player.getX() + x, (int)player.getY(), (int)player.getZ() + z);
            BlockPos pos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, playerPos);

            if (world.getBlockState(pos.below()).is(Blocks.WATER) && world.getBlockState(pos).isAir()){
                if(world.getFluidState(pos.below()).getAmount() == 8){
                    ParticleUtils.add(world, VisualityParticles.WATER_CIRCLE, pos.getX() + random.nextDouble(), pos.getY() + 0.05D, pos.getZ()  + random.nextDouble(), biome.getWaterColor());
                }
            }
        }
    }
}