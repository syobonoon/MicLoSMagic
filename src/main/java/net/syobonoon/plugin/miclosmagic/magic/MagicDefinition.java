package net.syobonoon.plugin.miclosmagic.magic;

public class MagicDefinition {
    private String magicName;
    private MagicAttribute magicAttribute;
    private MagicType magicType; //攻撃、回復、防御など
    private MagicEffect magicEffect;
    private double magicMana;
    private double magicCoolTime;
    private double magicAtk;

    public MagicDefinition(String magicName, MagicAttribute magicAttribute, MagicType magicType, MagicEffect magicEffect, double magicCoolTime, double magicMana, double magicAtk){
        this.magicName = magicName;
        this.magicAttribute = magicAttribute;
        this.magicType = magicType;
        this.magicEffect = magicEffect;
        this.magicCoolTime = magicCoolTime;
        this.magicMana = magicMana;
        this.magicAtk = magicAtk;
    }

    public String getMagicName() {
        return this.magicName;
    }

    public MagicAttribute getMagicAttribute() {
        return this.magicAttribute;
    }

    public MagicType getMagicType() {
        return this.magicType;
    }

    public MagicEffect getMagicEffect() {
        return this.magicEffect;
    }

    public double getMagicCoolTime() {
        return this.magicCoolTime;
    }

    public double getMagicMana() {
        return this.magicMana;
    }

    public double getMagicAtk() {
        return this.magicAtk;
    }

}
