package net.purple_network.easy_chest_dropper.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.purple_network.easy_chest_dropper.ModKeyBindings;

public class EasyChestDropperClient implements ClientModInitializer {


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
                        if (ModConfig.get().disableOnInventory && (handledScreen instanceof InventoryScreen
                            || handledScreen instanceof CreativeInventoryScreen)) return false;
                        var handle = handledScreen.getScreenHandler();
                        handle.slots.forEach(slot -> {
                            assert client.interactionManager != null;
                            client.interactionManager.clickSlot( // It simulates a drop action from the player
                                    handle.syncId,
                                    slot.getIndex(),
                                    1,
                                    SlotActionType.THROW, // Drop
                                    client.player
                            );
                        });
                        if (ModConfig.get().closeContainer){
                            screen.close();
                        }
                    }

                    return false;
                }

                return true;
            });
        });
        // CONFIG
        ModConfig.load();

        // Commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(
                    ClientCommandManager.literal("ecd")
                            .then(ClientCommandManager.literal("closeContainer")
                                    .then(ClientCommandManager.argument(
                                                    "value",
                                                    BoolArgumentType.bool()
                                            )
                                            .executes(context -> {

                                                boolean value = BoolArgumentType.getBool(
                                                        context,
                                                        "value"
                                                );

                                                ModConfig.get().closeContainer = value;
                                                ModConfig.save();

                                                context.getSource().getPlayer().sendMessage(
                                                        Text.of(
                                                                Text.translatable("msg.ecd.close_inventory")
                                                                        .getString() + value), false);

                                                return 1;
                                            }))
                            )
                            .then(ClientCommandManager.literal("disableOnInventory")
                                    .then(ClientCommandManager.argument(
                                                    "value",
                                                    BoolArgumentType.bool()
                                            )
                                            .executes(context -> {

                                                boolean value = BoolArgumentType.getBool(
                                                        context,
                                                        "value"
                                                );

                                                ModConfig.get().disableOnInventory = value;
                                                ModConfig.save();

                                                context.getSource().getPlayer().sendMessage(
                                                        Text.of(
                                                                Text.translatable("msg.ecd.disable_inventory")
                                                                        .getString() + value), false);

                                                return 1;
                                            }))
                            )
            );

        });
    }
}
