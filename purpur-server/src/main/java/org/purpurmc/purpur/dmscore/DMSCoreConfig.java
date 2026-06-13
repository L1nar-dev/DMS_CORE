package org.purpurmc.purpur.dmscore;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

public class DMSCoreConfig {
    private static final String HEADER = "DMS Core - Story & Cinematic Server Engine\n"
            + "Configuration for all DMS Core modules.\n"
            + "Docs: https://github.com/L1nar-dev/DMS_CORE\n";

    private static File CONFIG_FILE;
    public static YamlConfiguration config;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load story-core.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);

        readConfig(DMSCoreConfig.class, null);
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }
        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.addDefault(path, val);
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    // ==========================================
    // MODULE: Camera & Cinematic
    // ==========================================
    public static boolean cameraModuleEnabled = true;
    public static int entityUpdateIntervalDefault = 3;
    public static int entityUpdateIntervalCinematic = 1;

    private static void cameraModule() {
        cameraModuleEnabled = getBoolean("modules.camera.enabled", cameraModuleEnabled);
        entityUpdateIntervalDefault = getInt("modules.camera.entity-update-interval.default", entityUpdateIntervalDefault);
        entityUpdateIntervalCinematic = getInt("modules.camera.entity-update-interval.cinematic", entityUpdateIntervalCinematic);
    }

    // ==========================================
    // MODULE: Per-Player Time & Weather
    // ==========================================
    public static boolean perPlayerTimeEnabled = true;
    public static boolean perPlayerWeatherEnabled = true;

    private static void perPlayerModule() {
        perPlayerTimeEnabled = getBoolean("modules.per-player.time.enabled", perPlayerTimeEnabled);
        perPlayerWeatherEnabled = getBoolean("modules.per-player.weather.enabled", perPlayerWeatherEnabled);
    }

    // ==========================================
    // MODULE: Scene Freeze
    // ==========================================
    public static boolean sceneFreezeEnabled = true;
    public static boolean sceneFreezeAllowDirectorMovement = true;

    private static void sceneFreezeModule() {
        sceneFreezeEnabled = getBoolean("modules.scene-freeze.enabled", sceneFreezeEnabled);
        sceneFreezeAllowDirectorMovement = getBoolean("modules.scene-freeze.allow-director-movement", sceneFreezeAllowDirectorMovement);
    }

    // ==========================================
    // MODULE: World Protection
    // ==========================================
    public static boolean worldProtectionEnabled = true;
    public static boolean worldProtectionBlockExplosions = true;
    public static boolean worldProtectionBlockAdminBreak = true;

    private static void worldProtectionModule() {
        worldProtectionEnabled = getBoolean("modules.world-protection.enabled", worldProtectionEnabled);
        worldProtectionBlockExplosions = getBoolean("modules.world-protection.block-explosions", worldProtectionBlockExplosions);
        worldProtectionBlockAdminBreak = getBoolean("modules.world-protection.block-admin-break", worldProtectionBlockAdminBreak);
    }

    // ==========================================
    // MODULE: Resource Pack Enforcement
    // ==========================================
    public static boolean resourcePackEnforcementEnabled = false;
    public static boolean resourcePackHoldInVoid = true;

    private static void resourcePackModule() {
        resourcePackEnforcementEnabled = getBoolean("modules.resource-pack.enforcement.enabled", resourcePackEnforcementEnabled);
        resourcePackHoldInVoid = getBoolean("modules.resource-pack.enforcement.hold-in-void", resourcePackHoldInVoid);
    }

    // ==========================================
    // MODULE: True Spectator
    // ==========================================
    public static boolean trueSpectatorEnabled = true;

    private static void trueSpectatorModule() {
        trueSpectatorEnabled = getBoolean("modules.true-spectator.enabled", trueSpectatorEnabled);
    }
          }
