package com.daasheo.immortalworld.cultivation;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * ECS Component that stores cultivation data for entities.
 * This component tracks the cultivator's progress through different realms,
 * their Qi reserves, and physical strength.
 * 
 * This component is serializable and will be saved with the entity.
 */
public class CultivationComponent implements Component<EntityStore> {

    /**
     * Codec for serialization and deserialization of CultivationComponent.
     * Uses KeyedCodec to allow for flexible field ordering and backward compatibility.
     */
    public static final BuilderCodec<CultivationComponent> CODEC =
        BuilderCodec.builder(CultivationComponent.class, CultivationComponent::new)
            .append(new KeyedCodec<>("CurrentQi", Codec.FLOAT),
                (c, v) -> c.currentQi = v, c -> c.currentQi)
            .add()
            .append(new KeyedCodec<>("MaxQi", Codec.FLOAT),
                (c, v) -> c.maxQi = v, c -> c.maxQi)
            .add()
            .append(new KeyedCodec<>("RealmTier", Codec.STRING),
                (c, v) -> c.realmTier = RealmTier.valueOf(v), c -> c.realmTier.name())
            .add()
            .append(new KeyedCodec<>("BodyStrength", Codec.INTEGER),
                (c, v) -> c.bodyStrength = v, c -> c.bodyStrength)
            .add()
            .build();

    /**
     * Default maximum Qi for newly created components.
     */
    public static final float DEFAULT_MAX_QI = 100.0f;

    /**
     * Current amount of Qi the cultivator has available.
     */
    private float currentQi;

    /**
     * Maximum Qi capacity for the cultivator.
     * This value increases as the cultivator advances realms.
     */
    private float maxQi;

    /**
     * Current cultivation realm tier.
     * Determines available abilities and Qi efficiency.
     */
    private RealmTier realmTier;

    /**
     * Physical strength level of the cultivator.
     * Affects combat damage and physical abilities.
     */
    private int bodyStrength;

    /**
     * Default constructor creates a mortal cultivator with base stats.
     */
    public CultivationComponent() {
        this.currentQi = 0.0f;
        this.maxQi = DEFAULT_MAX_QI;
        this.realmTier = RealmTier.MORTAL;
        this.bodyStrength = 0;
    }

    /**
     * Constructor with all fields specified.
     * 
     * @param currentQi Current Qi amount
     * @param maxQi Maximum Qi capacity
     * @param realmTier Current cultivation realm
     * @param bodyStrength Physical strength level
     */
    public CultivationComponent(float currentQi, float maxQi, RealmTier realmTier, int bodyStrength) {
        this.currentQi = currentQi;
        this.maxQi = maxQi;
        this.realmTier = realmTier;
        this.bodyStrength = bodyStrength;
    }

    // ==================== Qi Management ====================

    /**
     * Gets the current Qi amount.
     * 
     * @return Current Qi value
     */
    public float getCurrentQi() {
        return currentQi;
    }

    /**
     * Sets the current Qi amount.
     * Qi cannot exceed maxQi or go below 0.
     * 
     * @param currentQi New Qi amount
     */
    public void setCurrentQi(float currentQi) {
        this.currentQi = Math.max(0.0f, Math.min(currentQi, this.maxQi));
    }

    /**
     * Gets the maximum Qi capacity.
     * 
     * @return Maximum Qi value
     */
    public float getMaxQi() {
        return maxQi;
    }

    /**
     * Sets the maximum Qi capacity.
     * Must be greater than or equal to currentQi.
     * 
     * @param maxQi New maximum Qi value
     */
    public void setMaxQi(float maxQi) {
        this.maxQi = Math.max(DEFAULT_MAX_QI, maxQi);
        // Ensure current Qi doesn't exceed new maximum
        if (this.currentQi > this.maxQi) {
            this.currentQi = this.maxQi;
        }
    }

    /**
     * Consumes Qi for using abilities.
     * 
     * @param amount Amount of Qi to consume
     * @return true if enough Qi was available, false otherwise
     */
    public boolean consumeQi(float amount) {
        if (this.currentQi >= amount) {
            this.currentQi -= amount;
            return true;
        }
        return false;
    }

    /**
     * Regenerates Qi over time.
     * 
     * @param amount Amount of Qi to restore
     */
    public void regenerateQi(float amount) {
        this.currentQi = Math.min(this.maxQi, this.currentQi + amount);
    }

    /**
     * Gets the percentage of Qi remaining.
     * 
     * @return Qi percentage (0.0 to 1.0)
     */
    public float getQiPercentage() {
        return maxQi > 0 ? currentQi / maxQi : 0.0f;
    }

    // ==================== Realm Tier Management ====================

    /**
     * Gets the current cultivation realm tier.
     * 
     * @return Current RealmTier
     */
    public RealmTier getRealmTier() {
        return realmTier;
    }

    /**
     * Sets the cultivation realm tier.
     * 
     * @param realmTier New cultivation realm
     */
    public void setRealmTier(RealmTier realmTier) {
        this.realmTier = realmTier;
    }

    /**
     * Checks if the cultivator can advance to the next realm.
     * 
     * @return true if advancement is possible, false otherwise
     */
    public boolean canAdvanceRealm() {
        return realmTier != RealmTier.NASCENT_SOUL;
    }

    /**
     * Gets the next realm tier.
     * 
     * @return Next RealmTier, or null if at maximum
     */
    public RealmTier getNextRealmTier() {
        return switch (realmTier) {
            case MORTAL -> RealmTier.QI_CONDENSATION;
            case QI_CONDENSATION -> RealmTier.FOUNDATION;
            case FOUNDATION -> RealmTier.GOLDEN_CORE;
            case GOLDEN_CORE -> RealmTier.NASCENT_SOUL;
            case NASCENT_SOUL -> null;
        };
    }

    // ==================== Body Strength Management ====================

    /**
     * Gets the physical strength level.
     * 
     * @return Body strength value
     */
    public int getBodyStrength() {
        return bodyStrength;
    }

    /**
     * Sets the physical strength level.
     * 
     * @param bodyStrength New strength value
     */
    public void setBodyStrength(int bodyStrength) {
        this.bodyStrength = Math.max(0, bodyStrength);
    }

    /**
     * Increases body strength by a given amount.
     * 
     * @param amount Amount to add
     */
    public void increaseBodyStrength(int amount) {
        this.bodyStrength += Math.max(0, amount);
    }

    /**
     * Calculates damage multiplier based on body strength.
     * 
     * @return Damage multiplier
     */
    public float getStrengthDamageMultiplier() {
        return 1.0f + (bodyStrength / 100.0f);
    }

    // ==================== Utility Methods ====================

    /**
     * Checks if the cultivator is at full Qi.
     * 
     * @return true if current Qi equals max Qi
     */
    public boolean isAtFullQi() {
        return currentQi >= maxQi;
    }

    /**
     * Checks if the cultivator is in the mortal realm.
     * 
     * @return true if still mortal
     */
    public boolean isMortal() {
        return realmTier == RealmTier.MORTAL;
    }

    /**
     * Gets a description of the current cultivation status.
     * 
     * @return Status description
     */
    @Nonnull
    public String getStatusDescription() {
        return String.format("%s [Qi: %.1f/%.1f (%.0f%%), Strength: %d]",
            realmTier.name(),
            currentQi, maxQi,
            getQiPercentage() * 100,
            bodyStrength);
    }

    // ==================== Required Component Methods ====================

    /**
     * Creates a copy of this component.
     * Required for ECS operations like entity copying.
     * 
     * @return Cloned CultivationComponent
     */
    @Override
    public CultivationComponent clone() {
        return new CultivationComponent(this.currentQi, this.maxQi, this.realmTier, this.bodyStrength);
    }

    @Override
    public String toString() {
        return "CultivationComponent{" +
            "currentQi=" + currentQi +
            ", maxQi=" + maxQi +
            ", realmTier=" + realmTier +
            ", bodyStrength=" + bodyStrength +
            '}';
    }
}
