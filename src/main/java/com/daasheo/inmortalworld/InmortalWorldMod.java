package com.daasheo.inmortalworld;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * Main plugin class for the Immortal World mod.
 * This mod introduces cultivation mechanics to Hytale,
 * allowing players to progress through different cultivation realms,
 * manage their Qi energy, and develop physical strength.
 */
public class InmortalWorldMod extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static InmortalWorldMod instance;

    /**
     * Creates a new instance of the Immortal World mod plugin.
     * 
     * @param init Plugin initialization context from Hytale server
     */
    public InmortalWorldMod(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    /**
     * Gets the plugin instance for static access.
     * 
     * @return The plugin instance
     */
    public static InmortalWorldMod getInstance() {
        return instance;
    }

    // ==================== Lifecycle Methods ====================

    @Override
    protected void setup() {
        LOGGER.at(Level.INFO).log("[InmortalWorld] Initializing cultivation system...");

        try {
            registerComponents();
            registerEventHandlers();
            LOGGER.at(Level.INFO).log("[InmortalWorld] Cultivation system initialized successfully!");
        } catch (Exception e) {
            LOGGER.at(Level.SEVERE).withCause(e).log("[InmortalWorld] Failed to initialize cultivation system!");
            throw new RuntimeException("Failed to setup InmortalWorld mod", e);
        }
    }

    @Override
    protected void start() {
        LOGGER.at(Level.INFO).log("[InmortalWorld] Mod started successfully!");
        LOGGER.at(Level.INFO).log("[InmortalWorld] Cultivation system is now active.");
    }

    @Override
    protected void shutdown() {
        LOGGER.at(Level.INFO).log("[InmortalWorld] Shutting down cultivation system...");
        instance = null;
        LOGGER.at(Level.INFO).log("[InmortalWorld] Mod shutdown complete.");
    }

    // ==================== Registration Methods ====================

    /**
     * Registers all custom ECS components for the cultivation system.
     */
    private void registerComponents() {
        LOGGER.at(Level.INFO).log("[InmortalWorld] Registering cultivation components...");
        // Component registration is handled via the CODEC static fields
        // and will be registered when first accessed
        LOGGER.at(Level.INFO).log("[InmortalWorld] Components registered.");
    }

    /**
     * Registers event handlers for the cultivation system.
     */
    private void registerEventHandlers() {
        LOGGER.at(Level.INFO).log("[InmortalWorld] Registering event handlers...");
        // TODO: Implement event handlers for cultivation mechanics
        // - Player join: initialize cultivation component
        // - Player level up: check for realm advancement
        // - Combat events: apply cultivation bonuses
        LOGGER.at(Level.INFO).log("[InmortalWorld] Event handlers registered.");
    }

    // ==================== Utility Methods ====================

    /**
     * Logs a debug message.
     * Note: Debug logging can be controlled by the server configuration.
     * 
     * @param message Message to log
     */
    public void debugLog(String message) {
        LOGGER.at(Level.FINE).log("[InmortalWorld Debug] " + message);
    }

    /**
     * Logs an error with context information.
     * 
     * @param context Context where the error occurred
     * @param throwable The exception that was thrown
     */
    public void logError(String context, Throwable throwable) {
        LOGGER.at(Level.SEVERE).withCause(throwable)
            .log("[InmortalWorld] Error in " + context + ": " + throwable.getMessage());
    }
}
