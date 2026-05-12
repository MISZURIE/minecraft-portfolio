package com.portfolio.minecraft;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import java.util.Random;

/**
 * Block types with Minecraft-style pixel textures.
 * INFO_ blocks are special portfolio blocks that display content when interacted.
 */
public enum BlockType {
    GRASS(new Color[]{ Color.web("#4a8c2a"), Color.web("#5d9b3a"), Color.web("#3d7a22"), Color.web("#68a84f"), Color.web("#52913a") }),
    DIRT(new Color[]{ Color.web("#8B6914"), Color.web("#7a5c10"), Color.web("#9a7518"), Color.web("#6b520e"), Color.web("#5a4510") }),
    STONE(new Color[]{ Color.web("#7F7F7F"), Color.web("#6a6a6a"), Color.web("#8a8a8a"), Color.web("#757575"), Color.web("#696969") }),
    WOOD(new Color[]{ Color.web("#6b4226"), Color.web("#5a3720"), Color.web("#7c4f2e"), Color.web("#4d2e18"), Color.web("#8b6339") }),
    LEAF(new Color[]{ Color.web("#2d6b1a"), Color.web("#3a7f28"), Color.web("#1e5c10"), Color.web("#47923a"), Color.web("#2b6518") }),
    DIAMOND(new Color[]{ Color.web("#4AEAD9"), Color.web("#3dc5b7"), Color.web("#5df5e8"), Color.web("#2db8ab"), Color.web("#6ef8ee") }),
    GOLD(new Color[]{ Color.web("#FFD700"), Color.web("#e6c200"), Color.web("#ffde33"), Color.web("#ccaa00"), Color.web("#fff066") }),
    OBSIDIAN(new Color[]{ Color.web("#1B0B2E"), Color.web("#2a1545"), Color.web("#120825"), Color.web("#331a55"), Color.web("#0d0518") }),
    GLOWSTONE(new Color[]{ Color.web("#f5d98e"), Color.web("#e8c86a"), Color.web("#fce6a6"), Color.web("#dbb95c"), Color.web("#fff3c4") }),
    SAND(new Color[]{ Color.web("#e8d5a3"), Color.web("#d4c090"), Color.web("#f0e0b0"), Color.web("#c8b080"), Color.web("#dcc898") }),
    REDSTONE(new Color[]{ Color.web("#cc2222"), Color.web("#ee3333"), Color.web("#aa1111"), Color.web("#ff4444"), Color.web("#bb2020") }),
    WATER(new Color[]{ Color.web("#2b5ea7"), Color.web("#3468b5"), Color.web("#1e508f"), Color.web("#4078c8"), Color.web("#2660aa") }),
    PORTAL(new Color[]{ Color.web("#8B5CF6"), Color.web("#7C3AED"), Color.web("#A78BFA"), Color.web("#6D28D9"), Color.web("#9F67FF") }),

    // Special portfolio info blocks — glow and pulse to attract attention
    INFO_ABOUT(new Color[]{ Color.web("#00FFAA"), Color.web("#00DD88"), Color.web("#33FFBB"), Color.web("#00CC77"), Color.web("#66FFCC") }, "about"),
    INFO_SKILLS(new Color[]{ Color.web("#FF6BFF"), Color.web("#DD44DD"), Color.web("#FF88FF"), Color.web("#CC33CC"), Color.web("#FFAAFF") }, "skills"),
    INFO_PROJECTS(new Color[]{ Color.web("#FFB020"), Color.web("#FF9900"), Color.web("#FFC844"), Color.web("#E88800"), Color.web("#FFD866") }, "projects"),
    INFO_EXPERIENCE(new Color[]{ Color.web("#20B0FF"), Color.web("#0099FF"), Color.web("#44C8FF"), Color.web("#0088E8"), Color.web("#66D8FF") }, "experience"),
    INFO_AWARDS(new Color[]{ Color.web("#FFD700"), Color.web("#FFC200"), Color.web("#FFE033"), Color.web("#FFAA00"), Color.web("#FFF066") }, "awards"),
    INFO_CERTS(new Color[]{ Color.web("#A855F7"), Color.web("#9333EA"), Color.web("#BB77FF"), Color.web("#7C22D9"), Color.web("#CC99FF") }, "certs"),
    INFO_CONTACT(new Color[]{ Color.web("#FF4466"), Color.web("#FF2244"), Color.web("#FF6688"), Color.web("#EE1133"), Color.web("#FF88AA") }, "contact");

    private final Color[] palette;
    private final String infoSection; // null for normal blocks
    private PhongMaterial cachedMaterial;

    BlockType(Color[] palette) {
        this.palette = palette;
        this.infoSection = null;
    }

    BlockType(Color[] palette, String infoSection) {
        this.palette = palette;
        this.infoSection = infoSection;
    }

    /** Is this a special portfolio info block? */
    public boolean isInfoBlock() {
        return infoSection != null;
    }

    /** Get the portfolio section this info block opens */
    public String getInfoSection() {
        return infoSection;
    }

    /** Generate a pixelated texture material */
    public PhongMaterial getMaterial() {
        if (cachedMaterial != null) return cachedMaterial;

        int texSize = 16;
        WritableImage img = new WritableImage(texSize, texSize);
        PixelWriter pw = img.getPixelWriter();
        Random rng = new Random(this.ordinal() * 1000L);

        for (int y = 0; y < texSize; y++) {
            for (int x = 0; x < texSize; x++) {
                Color c = palette[rng.nextInt(palette.length)];
                pw.setColor(x, y, c);
            }
        }

        cachedMaterial = new PhongMaterial();
        cachedMaterial.setDiffuseMap(img);

        // Info blocks and glowstone self-illuminate
        if (isInfoBlock() || this == GLOWSTONE) {
            cachedMaterial.setSelfIlluminationMap(img);
        }

        return cachedMaterial;
    }

    /** Get the average color for UI display */
    public Color getAverageColor() {
        double r = 0, g = 0, b = 0;
        for (Color c : palette) {
            r += c.getRed(); g += c.getGreen(); b += c.getBlue();
        }
        int n = palette.length;
        return Color.color(r / n, g / n, b / n);
    }

    /** Get emoji label for info blocks */
    public String getInfoLabel() {
        return switch (this) {
            case INFO_ABOUT -> "📖 ABOUT ME";
            case INFO_SKILLS -> "⚔️ SKILLS";
            case INFO_PROJECTS -> "🏗️ PROJECTS";
            case INFO_EXPERIENCE -> "⭐ EXPERIENCE";
            case INFO_AWARDS -> "🏆 AWARDS";
            case INFO_CERTS -> "📜 CERTS";
            case INFO_CONTACT -> "💌 CONTACT";
            default -> name();
        };
    }
}
