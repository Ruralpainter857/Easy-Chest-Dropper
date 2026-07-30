package net.purple_network.easy_chest_dropper.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("easy_chest_dropper.json");


    public boolean closeContainer = true;
    public boolean disableOnInventory = false;


    private static ModConfig INSTANCE;


    public static ModConfig get() {
        if (INSTANCE == null) {
            load();
        }

        return INSTANCE;
    }


    public static void load() {
        try {
            if (Files.exists(PATH)) {
                String json = Files.readString(PATH);
                INSTANCE = GSON.fromJson(json, ModConfig.class);
            } else {
                INSTANCE = new ModConfig();
                save();
            }

        } catch (IOException e) {
            e.printStackTrace();
            INSTANCE = new ModConfig();
        }
    }


    public static void save() {
        try {
            Files.writeString(
                    PATH,
                    GSON.toJson(INSTANCE)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

