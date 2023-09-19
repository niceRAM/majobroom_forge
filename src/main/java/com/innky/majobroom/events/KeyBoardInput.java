package com.innky.majobroom.events;

import com.innky.majobroom.item.BroomItem;
import com.innky.majobroom.network.Networking;
import com.innky.majobroom.network.RidePack;
import com.innky.majobroom.network.SummonBroomPack;
import com.innky.majobroom.registry.ItemRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBoardInput {
    public static final KeyMapping UP_KEY = new KeyMapping("key.up",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SPACE,
            "key.category.majobroom");
    public static final KeyMapping DOWN_KEY = new KeyMapping("key.down",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "key.category.majobroom");
    public static final KeyMapping SUMMON_KEY = new KeyMapping("key.summon",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.category.majobroom");

    public static boolean up = false;
    public static boolean down = false;
    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (UP_KEY.getKey().getValue() == event.getKey()) {
            if (event.getAction() == GLFW.GLFW_PRESS){
                up = true;
            }else if (event.getAction() == GLFW.GLFW_RELEASE){
                up = false;
            }
        }
        if (DOWN_KEY.getKey().getValue() == event.getKey()) {
            if (event.getAction() == GLFW.GLFW_PRESS){
                down = true;
            }else if (event.getAction() == GLFW.GLFW_RELEASE){
                down = false;
            }
        }
        if (SUMMON_KEY.isDown() && event.getAction() == GLFW.GLFW_PRESS){

            Player playerEntity = Minecraft.getInstance().player;
            if (playerEntity!=null){
                if (playerEntity.isPassenger()){
                    Networking.INSTANCE.sendToServer(new RidePack(playerEntity.getVehicle().getId(),false));
                }else {
                    Networking.INSTANCE.sendToServer(new SummonBroomPack());
                }
                for (ItemStack item:playerEntity.getInventory().items) {
                    if (item.is(ItemRegistry.broomItem.get()) || playerEntity.isCreative()){
                        playerEntity.level.playSound(playerEntity,playerEntity.blockPosition(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 10F,1f);
                        BroomItem.addParticle(playerEntity.level,playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 30,2,1);
                        break;
                    }
                }
            }
        }
    }
}
