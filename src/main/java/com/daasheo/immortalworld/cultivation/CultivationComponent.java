package com.daasheo.immortalworld.cultivation;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Cultivation data component for players.
 * Stores all cultivation progression data including Qi, realm, stats, and rings.
 * 
 * Required: Component<EntityStore> interface must exist in Hytale API.
 * If compilation fails, verify the interface and methods exist.
 */
public class CultivationComponent implements Component<EntityStore> {
    
    // ===== ESTADO DEL COMPONENTE =====
    
    /** Qi actual del jugador */
    private float currentQi;
    
    /** Qi máximo del jugador */
    private float maxQi;
    
    /** Realm actual del jugador (ordinal del RealmTier) */
    private int realmTier;
    
    /** Subrealm actual (0=Early, 1=Mid, 2=Late, 3=Peak) */
    private int subrealm;
    
    /** Progreso hacia el siguiente subrealm (0-100) */
    private float subrealmProgress;
    
    /** Experiencia espiritual total acumulada */
    private float totalSpiritualExp;
    
    /** Fuerza física (afecta daño cuerpo a cuerpo) */
    private int bodyStrength;
    
    /** Sentido espiritual (afecta regeneración de Qi) */
    private int spiritualSense;
    
    /** Constitución (afecta Qi máximo) */
    private int constitution;
    
    /** Talento (afecta velocidad de aprendizaje) */
    private int talent;
    
    /** Slots de anillos ocupados (0-6) */
    private int ringSlotsUsed;
    
    /** Karma del jugador (-1000 a +1000) */
    private int karma;
    
    /** Última vez que se modificó el karma (timestamp) */
    private long lastKarmaModification;
    
    /** Tiempo de meditación acumulado hoy (para bonuses diarios) */
    private float meditationTimeToday;
    
    /** Timestamp del último tick de meditación */
    private long lastMeditationTick;
    
    /** Número de tribulaciones completadas */
    private int tribulationsCompleted;
    
    /** Constructor por defecto requerido por el codec builder */
    public CultivationComponent() {
        this.currentQi = 100.0f;
        this.maxQi = 100.0f;
        this.realmTier = 0; // MORTAL
        this.subrealm = 0; // EARLY
        this.subrealmProgress = 0.0f;
        this.totalSpiritualExp = 0.0f;
        this.bodyStrength = 10;
        this.spiritualSense = 10;
        this.constitution = 10;
        this.talent = 50;
        this.ringSlotsUsed = 0;
        this.karma = 0;
        this.lastKarmaModification = System.currentTimeMillis();
        this.meditationTimeToday = 0.0f;
        this.lastMeditationTick = 0;
        this.tribulationsCompleted = 0;
    }
    
    // ===== CONSTRUCTOR COMPLETO =====
    
    /**
     * Constructor con todos los parámetros.
     * Usado para clonación y deserialización.
     */
    public CultivationComponent(float currentQi, float maxQi, int realmTier, int subrealm,
                                 float subrealmProgress, float totalSpiritualExp, int bodyStrength,
                                 int spiritualSense, int constitution, int talent, int ringSlotsUsed,
                                 int karma, long lastKarmaModification, float meditationTimeToday,
                                 long lastMeditationTick, int tribulationsCompleted) {
        this.currentQi = currentQi;
        this.maxQi = maxQi;
        this.realmTier = realmTier;
        this.subrealm = subrealm;
        this.subrealmProgress = subrealmProgress;
        this.totalSpiritualExp = totalSpiritualExp;
        this.bodyStrength = bodyStrength;
        this.spiritualSense = spiritualSense;
        this.constitution = constitution;
        this.talent = talent;
        this.ringSlotsUsed = ringSlotsUsed;
        this.karma = karma;
        this.lastKarmaModification = lastKarmaModification;
        this.meditationTimeToday = meditationTimeToday;
        this.lastMeditationTick = lastMeditationTick;
        this.tribulationsCompleted = tribulationsCompleted;
    }
    
    // ===== CODEC DE SERIALIZACIÓN =====
    
    /**
     * Codec para serialización/deserialización del componente.
     * Formato: BSON con claves named para flexibilidad.
     * 
     * Verificación: BuilderCodec, KeyedCodec, Codec existen en com.hypixel.hytale.codec
     * Si hay errores de compilación, verificar la API de Hytale.
     */
    public static final BuilderCodec<CultivationComponent> CODEC = 
        BuilderCodec.builder(CultivationComponent.class, CultivationComponent::new)
            .append(new KeyedCodec<>("CurrentQi", Codec.FLOAT), 
                (c, v) -> c.currentQi = v, c -> c.currentQi)
            .add()
            .append(new KeyedCodec<>("MaxQi", Codec.FLOAT),
                (c, v) -> c.maxQi = v, c -> c.maxQi)
            .add()
            .append(new KeyedCodec<>("RealmTier", Codec.INTEGER),
                (c, v) -> c.realmTier = v, c -> c.realmTier)
            .add()
            .append(new KeyedCodec<>("Subrealm", Codec.INTEGER),
                (c, v) -> c.subrealm = v, c -> c.subrealm)
            .add()
            .append(new KeyedCodec<>("SubrealmProgress", Codec.FLOAT),
                (c, v) -> c.subrealmProgress = v, c -> c.subrealmProgress)
            .add()
            .append(new KeyedCodec<>("TotalSpiritualExp", Codec.FLOAT),
                (c, v) -> c.totalSpiritualExp = v, c -> c.totalSpiritualExp)
            .add()
            .append(new KeyedCodec<>("BodyStrength", Codec.INTEGER),
                (c, v) -> c.bodyStrength = v, c -> c.bodyStrength)
            .add()
            .append(new KeyedCodec<>("SpiritualSense", Codec.INTEGER),
                (c, v) -> c.spiritualSense = v, c -> c.spiritualSense)
            .add()
            .append(new KeyedCodec<>("Constitution", Codec.INTEGER),
                (c, v) -> c.constitution = v, c -> c.constitution)
            .add()
            .append(new KeyedCodec<>("Talent", Codec.INTEGER),
                (c, v) -> c.talent = v, c -> c.talent)
            .add()
            .append(new KeyedCodec<>("RingSlotsUsed", Codec.INTEGER),
                (c, v) -> c.ringSlotsUsed = v, c -> c.ringSlotsUsed)
            .add()
            .append(new KeyedCodec<>("Karma", Codec.INTEGER),
                (c, v) -> c.karma = v, c -> c.karma)
            .add()
            .append(new KeyedCodec<>("LastKarmaModification", Codec.LONG),
                (c, v) -> c.lastKarmaModification = v, c -> c.lastKarmaModification)
            .add()
            .append(new KeyedCodec<>("MeditationTimeToday", Codec.FLOAT),
                (c, v) -> c.meditationTimeToday = v, c -> c.meditationTimeToday)
            .add()
            .append(new KeyedCodec<>("LastMeditationTick", Codec.LONG),
                (c, v) -> c.lastMeditationTick = v, c -> c.lastMeditationTick)
            .add()
            .append(new KeyedCodec<>("TribulationsCompleted", Codec.INTEGER),
                (c, v) -> c.tribulationsCompleted = v, c -> c.tribulationsCompleted)
            .add()
            .build();
    
    // ===== MÉTODOS REQUERIDOS POR Component<EntityStore> =====
    
    /**
     * Clona el componente.
     * Requerido por Component<EntityStore>.
     */
    @Override
    public CultivationComponent clone() {
        return new CultivationComponent(
            this.currentQi, this.maxQi, this.realmTier, this.subrealm,
            this.subrealmProgress, this.totalSpiritualExp, this.bodyStrength,
            this.spiritualSense, this.constitution, this.talent, this.ringSlotsUsed,
            this.karma, this.lastKarmaModification, this.meditationTimeToday,
            this.lastMeditationTick, this.tribulationsCompleted
        );
    }
    
    /**
     * Representación en string del componente.
     */
    @Override
    public String toString() {
        RealmTier realm = RealmTier.values().length > realmTier ? RealmTier.values()[realmTier] : RealmTier.MORTAL;
        return String.format("CultivationComponent{realm=%s-%s, qi=%.1f/%.1f, exp=%.1f, karma=%d}",
            realm.name(), getSubrealmName(), currentQi, maxQi, totalSpiritualExp, karma);
    }
    
    // ===== GETTERS =====
    
    public float getCurrentQi() { return currentQi; }
    public float getMaxQi() { return maxQi; }
    public int getRealmTier() { return realmTier; }
    public int getSubrealm() { return subrealm; }
    public float getSubrealmProgress() { return subrealmProgress; }
    public float getTotalSpiritualExp() { return totalSpiritualExp; }
    public int getBodyStrength() { return bodyStrength; }
    public int getSpiritualSense() { return spiritualSense; }
    public int getConstitution() { return constitution; }
    public int getTalent() { return talent; }
    public int getRingSlotsUsed() { return ringSlotsUsed; }
    public int getKarma() { return karma; }
    public long getLastKarmaModification() { return lastKarmaModification; }
    public float getMeditationTimeToday() { return meditationTimeToday; }
    public long getLastMeditationTick() { return lastMeditationTick; }
    public int getTribulationsCompleted() { return tribulationsCompleted; }
    
    // ===== GETTERS DERIVADOS =====
    
    /**
     * Obtiene el nombre legible del subrealm actual.
     */
    public String getSubrealmName() {
        return switch (subrealm) {
            case 0 -> "EARLY";
            case 1 -> "MID";
            case 2 -> "LATE";
            case 3 -> "PEAK";
            default -> "UNKNOWN";
        };
    }
    
    /**
     * Obtiene el RealmTier actual como enum.
     */
    public RealmTier getRealmTierEnum() {
        RealmTier[] realms = RealmTier.values();
        return realmTier < realms.length ? realms[realmTier] : RealmTier.MORTAL;
    }
    
    /**
     * Calcula el porcentaje de Qi actual (0.0 a 1.0).
     */
    public float getQiPercentage() {
        return maxQi > 0 ? currentQi / maxQi : 0.0f;
    }
    
    /**
     * Obtiene el RealmTier siguiente (o null si está en el máximo).
     */
    public RealmTier getNextRealmTier() {
        RealmTier[] realms = RealmTier.values();
        if (realmTier + 1 < realms.length) {
            return realms[realmTier + 1];
        }
        return null; // Ya está en el máximo realm
    }
    
    /**
     * Verifica si puede avanzar al siguiente subrealm.
     */
    public boolean canAdvanceSubrealm() {
        return subrealmProgress >= 100.0f && subrealm < 3;
    }
    
    /**
     * Verifica si puede avanzar al siguiente realm.
     */
    public boolean canAdvanceRealm() {
        return subrealm >= 3 && subrealmProgress >= 100.0f;
    }
    
    /**
     * Verifica si el jugador está en meditación.
     */
    public boolean isMeditating() {
        // Un jugador está meditando si su último tick de meditación fue reciente
        // (esto se actualiza desde el sistema de cultivo)
        return System.currentTimeMillis() - lastMeditationTick < 5000; // 5 segundos de gracia
    }
    
    // ===== SETTERS CON VALIDACIÓN =====
    
    public void setCurrentQi(float currentQi) {
        // Math.max/min de java.lang.Math
        this.currentQi = Math.max(0.0f, Math.min(currentQi, this.maxQi));
    }
    
    public void setMaxQi(float maxQi) {
        // Mínimo de 100 Qi
        this.maxQi = Math.max(100.0f, maxQi);
        // Asegurar que currentQi no exceda el nuevo máximo
        if (this.currentQi > this.maxQi) {
            this.currentQi = this.maxQi;
        }
    }
    
    public void setRealmTier(int realmTier) {
        RealmTier[] realms = RealmTier.values();
        this.realmTier = Math.max(0, Math.min(realmTier, realms.length - 1));
    }
    
    public void setSubrealm(int subrealm) {
        this.subrealm = Math.max(0, Math.min(subrealm, 3));
    }
    
    public void setSubrealmProgress(float subrealmProgress) {
        this.subrealmProgress = Math.max(0.0f, Math.min(subrealmProgress, 100.0f));
    }
    
    public void setTotalSpiritualExp(float totalSpiritualExp) {
        this.totalSpiritualExp = Math.max(0.0f, totalSpiritualExp);
    }
    
    public void setBodyStrength(int bodyStrength) {
        this.bodyStrength = Math.max(0, bodyStrength);
    }
    
    public void setSpiritualSense(int spiritualSense) {
        this.spiritualSense = Math.max(0, spiritualSense);
    }
    
    public void setConstitution(int constitution) {
        this.constitution = Math.max(0, constitution);
    }
    
    public void setTalent(int talent) {
        this.talent = Math.max(0, Math.min(talent, 100)); // Máximo 100
    }
    
    public void setRingSlotsUsed(int ringSlotsUsed) {
        this.ringSlotsUsed = Math.max(0, Math.min(ringSlotsUsed, 6)); // Máximo 6 slots
    }
    
    public void setKarma(int karma) {
        this.karma = Math.max(-1000, Math.min(karma, 1000)); // Rango -1000 a +1000
        this.lastKarmaModification = System.currentTimeMillis();
    }
    
    public void setMeditationTimeToday(float meditationTimeToday) {
        this.meditationTimeToday = Math.max(0.0f, meditationTimeToday);
    }
    
    public void setLastMeditationTick(long lastMeditationTick) {
        this.lastMeditationTick = lastMeditationTick;
    }
    
    public void setTribulationsCompleted(int tribulationsCompleted) {
        this.tribulationsCompleted = Math.max(0, tribulationsCompleted);
    }
    
    // ===== MÉTODOS DE LÓGICA DE NEGOCIO =====
    
    /**
     * Consume Qi. Retorna true si fue exitoso, false si no había suficiente.
     * 
     * @param amount Cantidad de Qi a consumir
     * @return true si se pudo consumir, false si no
     */
    public boolean consumeQi(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Qi amount cannot be negative");
        }
        if (currentQi >= amount) {
            currentQi -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Añade Qi al jugador, sin exceder el máximo.
     * 
     * @param amount Cantidad de Qi a añadir
     */
    public void addQi(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Qi amount cannot be negative");
        }
        currentQi = Math.min(currentQi + amount, maxQi);
    }
    
    /**
     * Añade experiencia espiritual y maneja el progreso de subrealm.
     * 
     * @param amount Cantidad de experiencia a añadir
     * @return true si avanzado de subrealm o realm
     */
    public boolean addSpiritualExp(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Experience amount cannot be negative");
        }
        
        totalSpiritualExp += amount;
        subrealmProgress += amount;
        
        // Manejar avance de subrealm
        if (subrealmProgress >= 100.0f) {
            if (subrealm < 3) {
                // Avanzar subrealm
                subrealm++;
                subrealmProgress -= 100.0f;
                // Aumentar stats al avanzar
                bodyStrength += 5;
                maxQi += 50;
                currentQi = maxQi; // Curación completa al avanzar
                return true;
            } else if (realmTier < RealmTier.values().length - 1) {
                // Avanzar realm completo
                realmTier++;
                subrealm = 0;
                subrealmProgress = 0.0f;
                // Aumentos mayores al avanzar de realm
                bodyStrength += 15;
                maxQi += 200;
                currentQi = maxQi;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Regenera Qi basado en la fórmula del sistema.
     * 
     * @param baseRegen Regeneración base por tick
     * @param multiplier Multiplicador de bioma/talent
     */
    public void regenerateQi(float baseRegen, float multiplier) {
        float regenAmount = baseRegen * multiplier * (1.0f + spiritualSense / 100.0f);
        addQi(regenAmount);
    }
    
    /**
     * Añade tiempo de meditación.
     * 
     * @param seconds Segundos de meditación
     */
    public void addMeditationTime(float seconds) {
        meditationTimeToday += seconds;
        lastMeditationTick = System.currentTimeMillis();
    }
    
    /**
     * Modifica el karma con validación de rango.
     * 
     * @param amount Cantidad a añadir (puede ser negativa)
     */
    public void modifyKarma(int amount) {
        setKarma(karma + amount);
    }
    
    /**
     * Calcula el modificador de Karma para el avance de realm.
     * Si el karma es negativo, el avance requiere más EXP.
     * 
     * @return Multiplicador (0.1 a 1.0)
     */
    public float getKarmaAdvancementMultiplier() {
        if (karma >= 0) {
            return 1.0f;
        }
        // Karma negativo: requiere más EXP
        return Math.max(0.1f, 1.0f - (Math.abs(karma) / 1000.0f));
    }
    
    /**
     * Obtiene el bonus de daño basado en la fuerza corporal.
     * 
     * @return Multiplicador de daño
     */
    public float getBodyStrengthDamageBonus() {
        return 1.0f + (bodyStrength / 100.0f);
    }
    
    /**
     * Obtiene la reducción de daño basada en la constitución.
     * 
     * @return Porcentaje de reducción (0.0 a 0.9)
     */
    public float getConstitutionDefenseBonus() {
        return Math.min(0.9f, constitution / 200.0f); // Máximo 90% reducción
    }
    
    /**
     * Verifica si el jugador puede equipar otro anillo.
     * 
     * @return true si hay slots disponibles
     */
    public boolean canEquipRing() {
        return ringSlotsUsed < 6;
    }
    
    /**
     * Equipa un anillo.
     * 
     * @throws IllegalStateException si no hay slots disponibles
     */
    public void equipRing() {
        if (!canEquipRing()) {
            throw new IllegalStateException("No ring slots available");
        }
        ringSlotsUsed++;
    }
    
    /**
     * Desequipa un anillo.
     * 
     * @throws IllegalStateException si no hay anillos equipados
     */
    public void unequipRing() {
        if (ringSlotsUsed <= 0) {
            throw new IllegalStateException("No rings equipped");
        }
        ringSlotsUsed--;
    }
}
