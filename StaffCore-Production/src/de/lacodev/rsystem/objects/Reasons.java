package de.lacodev.rsystem.objects;

import org.bukkit.Material;

public class Reasons {

    public Material item;
    public String name;

    public Reasons(Material item, String name) {
        this.item = item;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Material getItem() {
        return this.item;
    }
}
