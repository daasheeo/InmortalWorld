package com.daasheo.immortalworld.cultivation;

import java.util.Map;

/**
 * Stores configuration data for all cultivation realms.
 * This includes EXP requirements, stat bonuses, and unlockable abilities.
 * 
 * Each realm has:
 * - Name: Display name of the realm
 * - ExpRequired: EXP needed to reach this realm from the previous one
 * - BaseMaxQi: Base maximum Qi for this realm
 * - BodyStrengthBonus: Bonus body strength when entering this realm
 * - DefenseBonus: Defense power bonus
 * - AttackBonus: Attack power bonus
 * - UnlockedAbility: Ability unlocked at this realm
 */
public class RealmData {

    /**
     * Record representing the data for a single realm.
     */
    public record RealmInfo(
        String name,
        float expRequired,
        float baseMaxQi,
        int bodyStrengthBonus,
        int defenseBonus,
        int attackBonus,
        String unlockedAbility
    ) {}

    /**
     * Map of realm tier to its configuration data.
     */
    private static final Map<RealmTier, RealmInfo> REALM_DATA = Map.ofEntries(
        Map.entry(RealmTier.MORTAL, new RealmInfo(
            "Mortal",
            0.0f,
            100.0f,
            10,
            0,
            0,
            "Ninguna"
        )),
        Map.entry(RealmTier.QI_CONDENSATION, new RealmInfo(
            "Qi Condensation",
            1000.0f,
            200.0f,
            25,
            5,
            5,
            "Meditar"
        )),
        Map.entry(RealmTier.FOUNDATION, new RealmInfo(
            "Foundation Establishment",
            5000.0f,
            400.0f,
            50,
            10,
            10,
            "Pulso de Qi"
        )),
        Map.entry(RealmTier.GOLDEN_CORE, new RealmInfo(
            "Golden Core",
            25000.0f,
            800.0f,
            100,
            20,
            20,
            "Dominio Elemental"
        )),
        Map.entry(RealmTier.NASCENT_SOUL, new RealmInfo(
            "Nascent Soul",
            100000.0f,
            1600.0f,
            200,
            40,
            40,
            "Técnicas Supremas"
        ))
    );

    /**
     * Gets the realm info for a specific tier.
     * 
     * @param tier The realm tier
     * @return RealmInfo for that tier
     */
    public static RealmInfo getRealmInfo(RealmTier tier) {
        return REALM_DATA.get(tier);
    }

    /**
     * Gets the total EXP required to reach a specific realm from Mortal.
     * 
     * @param tier The target realm tier
     * @return Total EXP from start
     */
    public static float getTotalExpRequired(RealmTier tier) {
        float total = 0.0f;
        for (RealmTier t : RealmTier.values()) {
            if (t.ordinal() < tier.ordinal()) {
                total += getRealmInfo(t).expRequired();
            }
        }
        return total;
    }

    /**
     * Gets the maximum Qi for a specific realm and subrealm.
     * Subrealms: Early (0.33x), Mid (0.66x), Late (1.0x)
     * 
     * @param tier The realm tier
     * @param subrealmIndex Subrealm index (0-2 for Early, Mid, Late)
     * @return Maximum Qi for that subrealm
     */
    public static float getMaxQiForSubrealm(RealmTier tier, int subrealmIndex) {
        RealmInfo info = getRealmInfo(tier);
        float multiplier = switch (subrealmIndex) {
            case 0 -> 0.33f;  // Early
            case 1 -> 0.66f;  // Mid
            case 2 -> 1.0f;   // Late
            default -> 1.0f;
        };
        return info.baseMaxQi() * multiplier;
    }

    /**
     * Gets the number of subrealms for a realm.
     * 
     * @param tier The realm tier
     * @return Number of subrealms
     */
    public static int getSubrealmCount(RealmTier tier) {
        return 3; // Early, Mid, Late
    }

    /**
     * Gets the maximum ring slots available at a specific realm.
     * 
     * @param tier The realm tier
     * @return Maximum number of rings that can be equipped
     */
    public static int getMaxRingSlots(RealmTier tier) {
        return switch (tier) {
            case MORTAL -> 0;
            case QI_CONDENSATION -> 1;
            case FOUNDATION -> 2;
            case GOLDEN_CORE -> 3;
            case NASCENT_SOUL -> 4;
        };
    }

    /**
     * Gets the body's strength requirement for a specific realm.
     * 
     * @param tier The realm tier
     * @return Required body strength
     */
    public static int getRequiredBodyStrength(RealmTier tier) {
        return switch (tier) {
            case MORTAL -> 0;
            case QI_CONDENSATION -> 20;
            case FOUNDATION -> 50;
            case GOLDEN_CORE -> 100;
            case NASCENT_SOUL -> 200;
        };
    }

    /**
     * Calculates the difficulty multiplier for combat at a specific realm.
     * Higher realms have higher difficulty enemies.
     * 
     * @param tier The cultivator's realm
     * @return Difficulty multiplier
     */
    public static float getDifficultyMultiplier(RealmTier tier) {
        return 1.0f + (tier.ordinal() * 0.5f);
    }

    /**
     * Gets the maximum level for rings that can be equipped at a specific realm.
     * 
     * @param tier The cultivator's realm
     * @return Maximum ring level
     */
    public static int getMaxRingLevel(RealmTier tier) {
        return switch (tier) {
            case MORTAL -> 0;
            case QI_CONDENSATION -> 10;
            case FOUNDATION -> 25;
            case GOLDEN_CORE -> 50;
            case NASCENT_SOUL -> 100;
        };
    }
}
