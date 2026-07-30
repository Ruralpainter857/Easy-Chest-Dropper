package net.purple_network.simple_chest_dropper.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.slot.SlotActionType;
import net.purple_network.simple_chest_dropper.ModKeyBindings;

public class SimpleChestDropperClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModKeyBindings.initialize();
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (!(screen instanceof HandledScreen<?>)) {
                return;
            }

            ScreenKeyboardEvents.allowKeyPress(screen).register((screen1, key) -> {

                KeyBinding boundKey = ModKeyBindings.DROP_INVENTORY;
                if (boundKey.matchesKey(key)) {
                    if (screen1 instanceof HandledScreen<?> handledScreen){ // We check if the screen can contain items
                        var handle = handledScreen.getScreenHandler();
                        handle.slots.forEach(slot -> {
                            assert client.interactionManager != null;
                            client.interactionManager.clickSlot( // It simulate a drop action from the player
                                    handle.syncId,
                                    slot.getIndex(),
                                    1,
                                    SlotActionType.THROW,
                                    client.player
                            );
                        });
                    }

                    return false;
                }

                return true;
            });
        });
    }
}
