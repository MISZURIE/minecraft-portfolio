package com.portfolio.minecraft;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import java.util.*;

/**
 * Manages the voxel world — stores blocks, generates terrain,
 * handles block placement/removal, and collision detection.
 */
public class VoxelWorld {

    public static final double BLOCK_SIZE = 2.0;

    private final Map<String, BlockData> blocks = new HashMap<>();
    private final Group worldGroup = new Group();

    public VoxelWorld() {}

    private String key(int x, int y, int z) { return x + "," + y + "," + z; }

    // ========== WORLD GENERATION ==========

    public void generate() {
        Random rng = new Random(42);

        // Ground layer
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                placeBlock(x, 0, z, BlockType.GRASS);
                placeBlock(x, -1, z, BlockType.DIRT);
            }
        }

        // Random hills
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (rng.nextDouble() < 0.04 && dist > 6) {
                    placeBlock(x, 1, z, BlockType.GRASS);
                }
            }
        }

        // House
        buildHouse(-6, 1, -6);

        // Trees
        buildTree(6, 1, 4, rng);
        buildTree(12, 1, -5, rng);
        buildTree(-4, 1, 9, rng);
        buildTree(9, 1, 12, rng);
        buildTree(-12, 1, -4, rng);
        buildTree(-15, 1, 10, rng);
        buildTree(15, 1, 7, rng);

        // Ores
        buildOre(10, 1, -10, BlockType.DIAMOND);
        buildOre(-14, 1, 5, BlockType.GOLD);

        // Portal
        buildPortal(14, 1, -7);

        // Water
        for (int x = -7; x <= -4; x++)
            for (int z = 5; z <= 8; z++)
                placeBlock(x, 0, z, BlockType.WATER);

        // Torches
        placeBlock(0, 3, 0, BlockType.GLOWSTONE);
        placeBlock(-14, 3, 8, BlockType.GLOWSTONE);
        placeBlock(12, 3, 6, BlockType.GLOWSTONE);

        // ===== PORTFOLIO INFO BLOCKS — placed around the world =====
        // Each info block is a special glowing block the player can interact with

        // About Me — near spawn
        placeBlock(2, 1, 2, BlockType.INFO_ABOUT);
        placeBlock(2, 2, 2, BlockType.GLOWSTONE);

        // Skills — near a tree
        placeBlock(8, 1, 5, BlockType.INFO_SKILLS);
        placeBlock(8, 2, 5, BlockType.GLOWSTONE);

        // Projects — near the house
        placeBlock(-4, 1, -4, BlockType.INFO_PROJECTS);
        placeBlock(-4, 2, -4, BlockType.GLOWSTONE);

        // Experience — near ores
        placeBlock(12, 1, -8, BlockType.INFO_EXPERIENCE);
        placeBlock(12, 2, -8, BlockType.GLOWSTONE);

        // Awards — near portal
        placeBlock(16, 1, -4, BlockType.INFO_AWARDS);
        placeBlock(16, 2, -4, BlockType.GLOWSTONE);

        // Certs — opposite side
        placeBlock(-12, 1, 7, BlockType.INFO_CERTS);
        placeBlock(-12, 2, 7, BlockType.GLOWSTONE);

        // Contact — far corner
        placeBlock(-16, 1, -10, BlockType.INFO_CONTACT);
        placeBlock(-16, 2, -10, BlockType.GLOWSTONE);
    }

    private void buildHouse(int ox, int oy, int oz) {
        for (int x = 0; x < 7; x++)
            for (int z = 0; z < 7; z++)
                placeBlock(ox + x, oy, oz + z, BlockType.WOOD);
        for (int y = 1; y <= 4; y++) {
            for (int x = 0; x < 7; x++) {
                placeBlock(ox + x, oy + y, oz, BlockType.WOOD);
                placeBlock(ox + x, oy + y, oz + 6, BlockType.WOOD);
            }
            for (int z = 1; z < 6; z++) {
                placeBlock(ox, oy + y, oz + z, BlockType.WOOD);
                placeBlock(ox + 6, oy + y, oz + z, BlockType.WOOD);
            }
        }
        removeBlock(ox + 3, oy + 1, oz);
        removeBlock(ox + 3, oy + 2, oz);
        removeBlock(ox, oy + 2, oz + 3);
        removeBlock(ox + 6, oy + 2, oz + 3);
        for (int x = -1; x <= 7; x++)
            for (int z = -1; z <= 7; z++)
                placeBlock(ox + x, oy + 5, oz + z, BlockType.STONE);
        placeBlock(ox + 3, oy + 3, oz + 3, BlockType.GLOWSTONE);
    }

    private void buildTree(int ox, int oy, int oz, Random rng) {
        for (int y = 0; y < 5; y++)
            placeBlock(ox, oy + y, oz, BlockType.WOOD);
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 3; y <= 5; y++)
                    if (Math.abs(x) + Math.abs(z) <= 3 && rng.nextDouble() > 0.1)
                        placeBlock(ox + x, oy + y, oz + z, BlockType.LEAF);
        placeBlock(ox, oy + 6, oz, BlockType.LEAF);
    }

    private void buildOre(int ox, int oy, int oz, BlockType type) {
        placeBlock(ox, oy, oz, type);
        placeBlock(ox + 1, oy, oz, type);
        placeBlock(ox, oy + 1, oz, type);
        placeBlock(ox, oy, oz + 1, type);
        placeBlock(ox - 1, oy, oz, type);
    }

    private void buildPortal(int ox, int oy, int oz) {
        for (int y = 0; y < 6; y++) {
            placeBlock(ox, oy + y, oz, BlockType.OBSIDIAN);
            placeBlock(ox + 4, oy + y, oz, BlockType.OBSIDIAN);
        }
        for (int x = 0; x <= 4; x++) {
            placeBlock(ox + x, oy, oz, BlockType.OBSIDIAN);
            placeBlock(ox + x, oy + 5, oz, BlockType.OBSIDIAN);
        }
        for (int x = 1; x <= 3; x++)
            for (int y = 1; y <= 4; y++)
                placeBlock(ox + x, oy + y, oz, BlockType.PORTAL);
    }

    // ========== BLOCK OPERATIONS ==========

    public void placeBlock(int gx, int gy, int gz, BlockType type) {
        String k = key(gx, gy, gz);
        if (blocks.containsKey(k)) {
            worldGroup.getChildren().remove(blocks.get(k).mesh);
        }

        Box box = new Box(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        box.setMaterial(type.getMaterial());
        box.setTranslateX(gx * BLOCK_SIZE);
        box.setTranslateY(-gy * BLOCK_SIZE);
        box.setTranslateZ(gz * BLOCK_SIZE);
        box.setUserData(new int[]{gx, gy, gz});

        BlockData data = new BlockData(type, box, gx, gy, gz);
        blocks.put(k, data);
        worldGroup.getChildren().add(box);
    }

    public boolean removeBlock(int gx, int gy, int gz) {
        String k = key(gx, gy, gz);
        BlockData data = blocks.remove(k);
        if (data != null) {
            worldGroup.getChildren().remove(data.mesh);
            return true;
        }
        return false;
    }

    public boolean hasBlock(int gx, int gy, int gz) {
        return blocks.containsKey(key(gx, gy, gz));
    }

    public BlockType getBlockType(int gx, int gy, int gz) {
        BlockData data = blocks.get(key(gx, gy, gz));
        return data != null ? data.type : null;
    }

    // ========== COLLISION ==========

    /** Check if a world-space position collides with any solid block */
    public boolean isSolid(double wx, double wy, double wz) {
        int gx = (int) Math.round(wx / BLOCK_SIZE);
        int gy = (int) Math.round(-wy / BLOCK_SIZE);
        int gz = (int) Math.round(wz / BLOCK_SIZE);
        BlockType t = getBlockType(gx, gy, gz);
        return t != null && t != BlockType.WATER && t != BlockType.PORTAL;
    }

    /** Get the Y world position to stand on top of a block at grid (gx, gz) */
    public double getGroundY(double wx, double wz) {
        int gx = (int) Math.round(wx / BLOCK_SIZE);
        int gz = (int) Math.round(wz / BLOCK_SIZE);
        // Find highest solid block at this column
        for (int gy = 20; gy >= -2; gy--) {
            BlockType t = getBlockType(gx, gy, gz);
            if (t != null && t != BlockType.WATER && t != BlockType.PORTAL) {
                return -(gy * BLOCK_SIZE) - BLOCK_SIZE; // top of block
            }
        }
        return 0; // default ground level
    }

    public Group getWorldGroup() { return worldGroup; }
    public int getBlockCount() { return blocks.size(); }

    // ========== INNER CLASS ==========

    public static class BlockData {
        public final BlockType type;
        public final Box mesh;
        public final int gx, gy, gz;

        public BlockData(BlockType type, Box mesh, int gx, int gy, int gz) {
            this.type = type;
            this.mesh = mesh;
            this.gx = gx;
            this.gy = gy;
            this.gz = gz;
        }
    }
}
