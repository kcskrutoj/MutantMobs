package com.example.examplemod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "mutantmobs";
    private static final Random RANDOM = new Random();

    public ExampleMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJoinWorld);
    }

    private void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();

        // Меняем зомби только на сервере и только один раз.
        if (event.getWorld().isRemote() || !(entity instanceof ZombieEntity)) {
            return;
        }

        if (entity.getPersistentData().getBoolean(MODID + "_changed")) {
            return;
        }

        entity.getPersistentData().putBoolean(MODID + "_changed", true);
        ZombieEntity zombie = (ZombieEntity) entity;

        switch (RANDOM.nextInt(3)) {
            case 0:
                makeVariant(zombie, "Танк-мутант", 100.0D, 12.0D, 0.18D);
                break;
            case 1:
                makeVariant(zombie, "Зомби-бегун", 35.0D, 6.0D, 0.38D);
                break;
            default:
                makeVariant(zombie, "Мутант", 60.0D, 9.0D, 0.25D);
                break;
        }
    }

    private void makeVariant(ZombieEntity zombie, String name,
                             double health, double damage, double speed) {
        setAttribute(zombie, Attributes.MAX_HEALTH, health);
        setAttribute(zombie, Attributes.ATTACK_DAMAGE, damage);
        setAttribute(zombie, Attributes.MOVEMENT_SPEED, speed);
        zombie.setHealth((float) health);
        zombie.setCustomName(new StringTextComponent(name));
        zombie.setCustomNameVisible(true);
    }

    private void setAttribute(ZombieEntity zombie, Attribute attribute, double value) {
        AttributeInstance instance = zombie.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }
}
