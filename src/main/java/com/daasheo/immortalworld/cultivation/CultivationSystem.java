package com.daasheo.immortalworld.cultivation;

import com.daasheo.immortalworld.ImmortalWorldMod;

/**
 * Core cultivation system that handles:
 * - Qi regeneration over time
 * - Meditation mechanics and experience gain
 * - Realm advancement logic
 * - Stat calculations
 * 
 * This system is designed to work with Hytale's ECS (Entity Component System).
 * 
 * FASE 1: Implementación simplificada - Core Cultivation
 * - Qi regeneration
 * - Meditation and EXP gain
 * - Realm advancement
 * - Basic stat calculations
 */
public class CultivationSystem {

    private final ImmortalWorldMod mod;

    // Constants for cultivation mechanics
    public static final float BASE_QI_REGEN_PER_SECOND = 2.0f;
    public static final float MEDITATION_BONUS_MULTIPLIER = 3.0f;
    public static final float MEDITATION_EXP_PER_5_MIN = 50.0f;
    public static final float MAX_QI_REGEN_PER_SECOND = 50.0f;
    public static final float DEFAULT_MAX_QI = 100.0f;

    public CultivationSystem(ImmortalWorldMod mod) {
        this.mod = mod;
    }

    /**
     * Called every server tick to update cultivation state.
     * Handles Qi regeneration for all entities with CultivationComponent.
     * 
     * FASE 1: Versión simplificada sin query de EntityStore
     * En fases posteriores se integrará con el sistema ECS de Hytale.
     * 
     * @param deltaTime Time elapsed since last tick (in seconds)
     */
    public void onTick(float deltaTime) {
        // FASE 1: Placeholder - En Fase 4 se integrará con Hytale ECS
        // La regeneración de Qi se manejará por eventos del servidor
    }

    /**
     * Regenerates Qi for a specific cultivator.
     * Called from event handlers or tick systems.
     * 
     * @param component The cultivator's cultivation component
     * @param deltaTime Time elapsed since last tick (in seconds)
     */
    public void regenerateQi(CultivationComponent component, float deltaTime) {
        float regenAmount = calculateQiRegen(component) * deltaTime;
        component.addQi(regenAmount);
    }

    /**
     * Calculates Qi regeneration rate for a cultivator.
     * Base rate is Constitution / 10 per second.
     * Meditation bonus multiplies this by 3x.
     * 
     * @param component The cultivator's cultivation component
     * @return Qi regenerated per second
     */
    public float calculateQiRegen(CultivationComponent component) {
        float baseRegen = component.getConstitution() / 10.0f;
        float meditationBonus = isMeditating(component) ? MEDITATION_BONUS_MULTIPLIER : 1.0f;
        return Math.min(baseRegen * meditationBonus, MAX_QI_REGEN_PER_SECOND);
    }

    /**
     * Calculates experience gained from meditation.
     * Formula: Base EXP × (1 + Spiritual Sense / 100) × Biome Multiplier
     * 
     * @param component The cultivator's cultivation component
     * @param meditationDuration Duration of meditation in minutes
     * @param biomeMultiplier Biome-specific bonus (1.0 = normal)
     * @return Total experience gained
     */
    public float calculateMeditationExp(CultivationComponent component, 
                                         float meditationDuration, 
                                         float biomeMultiplier) {
        float spiritualSenseBonus = 1.0f + (component.getSpiritualSense() / 100.0f);
        float baseExp = MEDITATION_EXP_PER_5_MIN * (meditationDuration / 5.0f);
        return baseExp * spiritualSenseBonus * biomeMultiplier;
    }

    /**
     * Awards experience to a cultivator and handles realm advancement.
     * 
     * @param component The cultivator's cultivation component
     * @param expAmount Amount of experience to add
     * @return true if the cultivator advanced to a new realm or subrealm
     */
    public boolean addExperience(CultivationComponent component, float expAmount) {
        boolean advanced = component.addSpiritualExp(expAmount);
        
        if (advanced) {
            RealmTier currentRealm = component.getRealmTierEnum();
            mod.debugLog("Cultivator advanced! Realm: " +
                    currentRealm.name() + " - " + component.getSubrealmName());
        }
        
        return advanced;
    }

    /**
     * Attempts to start meditation for a cultivator.
     * 
     * @param component The cultivator's cultivation component
     * @return true if meditation can begin
     */
    public boolean startMeditation(CultivationComponent component) {
        // Check if cultivator is not at max realm (NASCENT_SOUL cannot meditate)
        // getRealmTier() returns int, so compare with ordinal of NASCENT_SOUL
        if (component.getRealmTier() >= RealmTier.NASCENT_SOUL.ordinal()) {
            return false;
        }
        
        // Check if cultivator has enough Qi to meditate (minimum 10%)
        if (component.getCurrentQi() < component.getMaxQi() * 0.1f) {
            return false;
        }
        
        return true;
    }

    /**
     * Completes a meditation session and awards experience.
     * 
     * @param component The cultivator's cultivation component
     * @param durationMinutes Duration of meditation in minutes
     * @param biomeMultiplier Biome-specific bonus
     * @return Experience gained
     */
    public float completeMeditation(CultivationComponent component, 
                                     float durationMinutes, 
                                     float biomeMultiplier) {
        float exp = calculateMeditationExp(component, durationMinutes, biomeMultiplier);
        addExperience(component, exp);
        component.addMeditationTime(durationMinutes * 60); // Convert to seconds
        return exp;
    }

    /**
     * Consumes Qi for using an ability.
     * 
     * @param component The cultivator's cultivation component
     * @param qiCost Cost of the ability in Qi
     * @return true if the ability can be used
     */
    public boolean consumeQi(CultivationComponent component, float qiCost) {
        return component.consumeQi(qiCost);
    }

    /**
     * Calculates damage bonus from cultivation stats.
     * FASE 1: Simplified - uses body strength only
     * 
     * @param component The cultivator's cultivation component
     * @return Total damage multiplier
     */
    public float calculateDamageBonus(CultivationComponent component) {
        // FASE 1: Simplified calculation using body strength
        return component.getBodyStrengthDamageBonus();
    }

    /**
     * Calculates damage reduction from cultivation stats.
     * FASE 1: Simplified - uses constitution only
     * 
     * @param component The cultivator's cultivation component
     * @return Total damage reduction multiplier (0.0 = no reduction, 0.9 = 90% reduction)
     */
    public float calculateDamageReduction(CultivationComponent component) {
        // FASE 1: Simplified calculation using constitution
        return component.getConstitutionDefenseBonus();
    }

    /**
     * Calculates critical hit chance including cultivation bonuses.
     * FASE 1: Placeholder - returns base crit chance based on realm
     * 
     * @param component The cultivator's cultivation component
     * @return Final critical hit chance (0-100)
     */
    public float calculateCritChance(CultivationComponent component) {
        // FASE 1: Base crit chance increases with realm
        // 2% per realm tier (Mortal = 0%, Nascent Soul = 8%)
        return component.getRealmTier() * 2.0f;
    }

    /**
     * Calculates dodge chance including cultivation bonuses.
     * FASE 1: Placeholder - returns base dodge chance based on realm
     * 
     * @param component The cultivator's cultivation component
     * @return Final dodge chance (0-100)
     */
    public float calculateDodgeChance(CultivationComponent component) {
        // FASE 1: Base dodge chance increases with realm
        // 1% per realm tier (Mortal = 0%, Nascent Soul = 4%)
        return component.getRealmTier() * 1.0f;
    }

    /**
     * Checks if a cultivator is currently meditating.
     * 
     * @param component The cultivator's cultivation component
     * @return true if meditating
     */
    private boolean isMeditating(CultivationComponent component) {
        return component.isMeditating();
    }

    /**
     * Gets the maximum Qi for a cultivator based on their realm and constitution.
     * 
     * @param component The cultivator's cultivation component
     * @return Maximum Qi capacity
     */
    public float calculateMaxQi(CultivationComponent component) {
        // getRealmTier() returns int, so we can use it directly for calculations
        float realmBonus = component.getRealmTier() * 50.0f;
        float constitutionBonus = component.getConstitution() * 2.0f;
        return DEFAULT_MAX_QI + realmBonus + constitutionBonus;
    }

    /**
     * Advances a cultivator to the next realm tier.
     * 
     * @param component The cultivator's cultivation component
     * @return true if advancement was successful
     */
    public boolean advanceRealm(CultivationComponent component) {
        if (!component.canAdvanceRealm()) {
            return false;
        }
        
        // getNextRealmTier() returns RealmTier enum, but setRealmTier expects int
        RealmTier nextRealm = component.getNextRealmTier();
        if (nextRealm == null) {
            return false;
        }
        
        component.setRealmTier(nextRealm.ordinal());
        component.setSubrealm(0); // Reset to Early subrealm
        component.setSubrealmProgress(0.0f);
        
        // Apply realm advancement bonuses (handled in addSpiritualExp, but we can add more here)
        // FASE 1: Bonuses are applied automatically when advancing
        mod.debugLog("Realm advanced to: " + nextRealm.name());
        
        return true;
    }

    /**
     * Advances a cultivator to the next subrealm.
     * 
     * @param component The cultivator's cultivation component
     * @return true if advancement was successful
     */
    public boolean advanceSubrealm(CultivationComponent component) {
        if (!component.canAdvanceSubrealm()) {
            return false;
        }
        
        int newSubrealm = component.getSubrealm() + 1;
        component.setSubrealm(newSubrealm);
        component.setSubrealmProgress(0.0f);
        
        return true;
    }

    /**
     * Gets the realm tier as enum for display purposes.
     * 
     * @param component The cultivator's cultivation component
     * @return The current RealmTier enum value
     */
    public RealmTier getRealmTierEnum(CultivationComponent component) {
        return component.getRealmTierEnum();
    }

    /**
     * Gets the current subrealm name for display.
     * 
     * @param component The cultivator's cultivation component
     * @return The subrealm name (EARLY, MID, LATE, PEAK)
     */
    public String getSubrealmName(CultivationComponent component) {
        return component.getSubrealmName();
    }

    /**
     * Gets the current Qi percentage (0.0 to 1.0).
     * 
     * @param component The cultivator's cultivation component
     * @return Qi percentage
     */
    public float getQiPercentage(CultivationComponent component) {
        return component.getQiPercentage();
    }

    /**
     * Checks if the cultivator is at the maximum realm.
     * 
     * @param component The cultivator's cultivation component
     * @return true if at max realm
     */
    public boolean isAtMaxRealm(CultivationComponent component) {
        return component.getRealmTier() >= RealmTier.NASCENT_SOUL.ordinal();
    }

    /**
     * Gets the progress percentage towards the next subrealm.
     * 
     * @param component The cultivator's cultivation component
     * @return Progress percentage (0.0 to 100.0)
     */
    public float getSubrealmProgress(CultivationComponent component) {
        return component.getSubrealmProgress();
    }

    /**
     * Gets the total spiritual experience accumulated.
     * 
     * @param component The cultivator's cultivation component
     * @return Total experience
     */
    public float getTotalSpiritualExp(CultivationComponent component) {
        return component.getTotalSpiritualExp();
    }

    /**
     * Gets the karma value.
     * 
     * @param component The cultivator's cultivation component
     * @return Karma value (-1000 to +1000)
     */
    public int getKarma(CultivationComponent component) {
        return component.getKarma();
    }

    /**
     * Modifies karma by the given amount.
     * 
     * @param component The cultivator's cultivation component
     * @param amount Amount to modify (can be negative)
     */
    public void modifyKarma(CultivationComponent component, int amount) {
        component.modifyKarma(amount);
    }

    /**
     * Gets the karma advancement multiplier.
     * Negative karma requires more EXP to advance.
     * 
     * @param component The cultivator's cultivation component
     * @return Multiplier (0.1 to 1.0)
     */
    public float getKarmaAdvancementMultiplier(CultivationComponent component) {
        return component.getKarmaAdvancementMultiplier();
    }

    /**
     * Gets the meditation time accumulated today.
     * 
     * @param component The cultivator's cultivation component
     * @return Meditation time in seconds
     */
    public float getMeditationTimeToday(CultivationComponent component) {
        return component.getMeditationTimeToday();
    }

    /**
     * Resets the daily meditation time.
     * Should be called once per day.
     * 
     * @param component The cultivator's cultivation component
     */
    public void resetDailyMeditationTime(CultivationComponent component) {
        component.setMeditationTimeToday(0.0f);
    }

    /**
     * Gets the number of tribulations completed.
     * 
     * @param component The cultivator's cultivation component
     * @return Number of tribulations
     */
    public int getTribulationsCompleted(CultivationComponent component) {
        return component.getTribulationsCompleted();
    }

    /**
     * Increments the tribulation count.
     * 
     * @param component The cultivator's cultivation component
     */
    public void incrementTribulations(CultivationComponent component) {
        component.setTribulationsCompleted(component.getTribulationsCompleted() + 1);
    }

    /**
     * Gets the body strength stat.
     * 
     * @param component The cultivator's cultivation component
     * @return Body strength value
     */
    public int getBodyStrength(CultivationComponent component) {
        return component.getBodyStrength();
    }

    /**
     * Gets the spiritual sense stat.
     * 
     * @param component The cultivator's cultivation component
     * @return Spiritual sense value
     */
    public int getSpiritualSense(CultivationComponent component) {
        return component.getSpiritualSense();
    }

    /**
     * Gets the constitution stat.
     * 
     * @param component The cultivator's cultivation component
     * @return Constitution value
     */
    public int getConstitution(CultivationComponent component) {
        return component.getConstitution();
    }

    /**
     * Gets the talent stat.
     * 
     * @param component The cultivator's cultivation component
     * @return Talent value (0-100)
     */
    public int getTalent(CultivationComponent component) {
        return component.getTalent();
    }

    /**
     * Gets the number of ring slots used.
     * 
     * @param component The cultivator's cultivation component
     * @return Ring slots used (0-6)
     */
    public int getRingSlotsUsed(CultivationComponent component) {
        return component.getRingSlotsUsed();
    }

    /**
     * Checks if the cultivator can equip another ring.
     * 
     * @param component The cultivator's cultivation component
     * @return true if can equip
     */
    public boolean canEquipRing(CultivationComponent component) {
        return component.canEquipRing();
    }

    /**
     * Equips a ring.
     * 
     * @param component The cultivator's cultivation component
     * @throws IllegalStateException if no slots available
     */
    public void equipRing(CultivationComponent component) {
        component.equipRing();
    }

    /**
     * Unequips a ring.
     * 
     * @param component The cultivator's cultivation component
     * @throws IllegalStateException if no rings equipped
     */
    public void unequipRing(CultivationComponent component) {
        component.unequipRing();
    }

    /**
     * Gets a summary of the cultivator's status.
     * 
     * @param component The cultivator's cultivation component
     * @return Status summary string
     */
    public String getStatusSummary(CultivationComponent component) {
        return String.format(
            "Realm: %s %s (%.1f%%)\n" +
            "Qi: %.1f/%.1f (%.1f%%)\n" +
            "Total EXP: %.1f\n" +
            "Stats: STR=%d, SNS=%d, CON=%d, TAL=%d\n" +
            "Karma: %d | Rings: %d/6 | Tribulations: %d",
            component.getRealmTierEnum().name(),
            component.getSubrealmName(),
            component.getSubrealmProgress(),
            component.getCurrentQi(),
            component.getMaxQi(),
            component.getQiPercentage() * 100,
            component.getTotalSpiritualExp(),
            component.getBodyStrength(),
            component.getSpiritualSense(),
            component.getConstitution(),
            component.getTalent(),
            component.getKarma(),
            component.getRingSlotsUsed(),
            component.getTribulationsCompleted()
        );
    }
}
