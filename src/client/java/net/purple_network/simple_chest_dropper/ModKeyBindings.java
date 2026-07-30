package net.purple_network.simple_chest_dropper;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

        public static final KeyBinding DROP_INVENTORY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.scd.drop_inventory",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        KeyBinding.Category.INVENTORY
                    )
                );

        public static void initialize() {}
}

