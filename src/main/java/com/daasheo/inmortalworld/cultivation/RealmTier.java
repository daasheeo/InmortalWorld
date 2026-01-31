package com.daasheo.inmortalworld.cultivation;

import com.hypixel.hytale.codec.Codec;

/**
 * Represents the cultivation realms in the Immortal World mod.
 * Each tier represents a stage of spiritual advancement.
 */
public enum RealmTier {
    /**
     * The mortal realm - the starting point for all cultivators.
     * No special abilities, basic physical stats.
     */
    MORTAL,

    /**
     * Qi Condensation - The first step of cultivation.
     * Cultivators begin to condense and control their internal Qi.
     * Unlocks basic spiritual abilities.
     */
    QI_CONDENSATION,

    /**
     * Foundation Establishment - Building a stable foundation for cultivation.
     * Cultivators establish their Dao foundation.
     * Unlocks enhanced physical and spiritual abilities.
     */
    FOUNDATION,

    /**
     * Golden Core - Forming a golden core of condensed energy.
     * Represents a major milestone in cultivation.
     * Unlocks powerful spiritual techniques.
     */
    GOLDEN_CORE,

    /**
     * Nascent Soul - The soul becomes mature and can exist independently.
     * Advanced stage where the cultivator's soul is strengthened.
     * Unlocks supreme cultivation techniques and extended lifespan.
     */
    NASCENT_SOUL
}
