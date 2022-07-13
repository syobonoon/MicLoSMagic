package net.syobonoon.plugin.miclosmagic.magic;

public class MagicDefinition {
    private String magicName;
    private MagicAttribute magicAttribute;
    private MagicType magicType; //攻撃、回復、防御など
    private double magicMana;
    private double magicAtk;

    public MagicDefinition(String magicName, MagicAttribute magicAttribute, MagicType magicType, double magicMana, double magicAtk){
        this.magicName = magicName;
        this.magicAttribute = magicAttribute;
        this.magicType = magicType;
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

    public double getMagicMana() {
        return this.magicMana;
    }

    public double getMagicAtk() {
        return this.magicAtk;
    }

}
