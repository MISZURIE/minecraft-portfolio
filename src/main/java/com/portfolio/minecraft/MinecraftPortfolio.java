package com.portfolio.minecraft;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

/**
 * Minecraft 3D Portfolio — First Person Edition
 *
 * WASD    = Walk
 * Mouse   = Look around (hold right button or move freely)
 * Left Click  = Break block
 * Right Click = Place block / Interact with info block
 * Q/E    = Cycle block type
 * SPACE  = Jump
 * ESC    = Close panel
 * F      = Toggle fly mode
 */
public class MinecraftPortfolio extends Application {

    private SubScene scene3D;
    private Group root3D;
    private PerspectiveCamera camera;
    private VoxelWorld world;
    private PortfolioUI portfolioUI;

    // Player state
    private double playerX = 0, playerY = -6, playerZ = 8;
    private double yaw = 180, pitch = -15; // degrees
    private double velocityY = 0;
    private boolean onGround = true;
    private boolean flyMode = false;

    // Input
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private double lastMouseX, lastMouseY;

    // Block editing
    private BlockType selectedBlock = BlockType.GRASS;
    private final BlockType[] buildBlocks = {
        BlockType.GRASS, BlockType.DIRT, BlockType.STONE, BlockType.WOOD,
        BlockType.DIAMOND, BlockType.GOLD, BlockType.GLOWSTONE,
        BlockType.OBSIDIAN, BlockType.SAND, BlockType.REDSTONE
    };
    private int blockIndex = 0;

    // HUD
    private Label coordsLabel, blockLabel, interactHint;
    private static final double MOVE_SPEED = 0.12;
    private static final double MOUSE_SENS = 0.15;
    private boolean mouseInited = false;
    private static final double GRAVITY = 0.012;
    private static final double JUMP_VEL = 0.22;
    private static final double PLAYER_HEIGHT = 3.2;

    @Override
    public void start(Stage stage) {
        // ===== 3D World =====
        root3D = new Group();
        world = new VoxelWorld();
        world.generate();
        root3D.getChildren().add(world.getWorldGroup());

        // Lighting
        root3D.getChildren().add(new AmbientLight(Color.web("#9999bb")));
        PointLight sun = new PointLight(Color.web("#fff5e0"));
        sun.setTranslateX(40); sun.setTranslateY(-80); sun.setTranslateZ(-20);
        root3D.getChildren().add(sun);
        PointLight fill = new PointLight(Color.web("#6688cc", 0.4));
        fill.setTranslateX(-30); fill.setTranslateY(-50); fill.setTranslateZ(30);
        root3D.getChildren().add(fill);

        // Camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(500);
        camera.setFieldOfView(70);
        root3D.getChildren().add(camera);
        updateCamera();

        scene3D = new SubScene(root3D, 1280, 720, true, SceneAntialiasing.BALANCED);
        scene3D.setFill(Color.web("#87CEEB"));
        scene3D.setCamera(camera);

        // ===== UI =====
        portfolioUI = new PortfolioUI();

        // Crosshair
        Label crosshair = new Label("+");
        crosshair.setStyle("-fx-text-fill: white; -fx-font-size: 28; -fx-font-family: monospace;" +
            "-fx-effect: dropshadow(gaussian, black, 2, 0, 0, 0);");
        crosshair.setMouseTransparent(true);

        // HUD labels
        coordsLabel = hudLabel("");
        blockLabel = hudLabel("Block: GRASS");
        interactHint = hudLabel("");
        interactHint.setStyle(interactHint.getStyle() +
            "-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 8 16;" +
            "-fx-background-radius: 6; -fx-font-size: 18;");

        HBox topHud = new HBox(20, coordsLabel, blockLabel);
        topHud.setPadding(new Insets(10));
        topHud.setMouseTransparent(true);

        // Controls help
        Label controls = hudLabel("WASD=Walk | Mouse=Look | LClick=Break | RClick=Place/Interact | Q/E=Block | Space=Jump | F=Fly");
        controls.setStyle(controls.getStyle() +
            "-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 6 12; -fx-background-radius: 4;");
        controls.setMouseTransparent(true);

        // Hotbar
        HBox hotbar = buildBlockHotbar();

        VBox bottomHud = new VBox(6, interactHint, controls, hotbar);
        bottomHud.setAlignment(Pos.BOTTOM_CENTER);
        bottomHud.setPadding(new Insets(0, 0, 12, 0));
        bottomHud.setMouseTransparent(true);

        // Title
        Label title = new Label("⛏ MINECRAFT PORTFOLIO");
        title.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 18; -fx-font-weight: bold;" +
            "-fx-font-family: monospace; -fx-effect: dropshadow(gaussian, black, 4, 0, 2, 2);");
        title.setMouseTransparent(true);
        crosshair.setMouseTransparent(true);

        // Layout
        StackPane root = new StackPane();
        scene3D.widthProperty().bind(root.widthProperty());
        scene3D.heightProperty().bind(root.heightProperty());

        root.getChildren().addAll(scene3D, crosshair, topHud, title, bottomHud, portfolioUI.getOverlay());
        StackPane.setAlignment(topHud, Pos.TOP_LEFT);
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(10, 0, 0, 0));
        StackPane.setAlignment(crosshair, Pos.CENTER);
        StackPane.setAlignment(bottomHud, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(interactHint, Pos.CENTER);

        Scene scene = new Scene(root, 1280, 720, true);
        scene.setFill(Color.BLACK);

        // ===== Input =====
        scene.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        scene.setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode());
            if (e.getCode() == KeyCode.ESCAPE) portfolioUI.hidePanel();
            if (e.getCode() == KeyCode.F) flyMode = !flyMode;
            if (e.getCode() == KeyCode.Q) cycleBlock(-1);
            if (e.getCode() == KeyCode.E) cycleBlock(1);
        });

        // Mouse events on main scene so UI overlay doesn't block them
        scene.setOnMousePressed(e -> {
            // If portfolio panel is showing, let it handle clicks
            if (portfolioUI.isShowing()) return;

            // Pick the block under cursor
            PickResult pick = e.getPickResult();
            Node picked = pick.getIntersectedNode();

            if (picked instanceof Box box && box.getUserData() instanceof int[] pos) {
                BlockType bt = world.getBlockType(pos[0], pos[1], pos[2]);

                if (e.getButton() == MouseButton.PRIMARY) {
                    if (bt != null && (!bt.isInfoBlock() || e.isShiftDown())) {
                        world.removeBlock(pos[0], pos[1], pos[2]);
                    }
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    if (bt != null && bt.isInfoBlock()) {
                        portfolioUI.showPanel(bt.getInfoSection());
                    } else {
                        Point3D hit = pick.getIntersectedPoint();
                        Point3D normal = estimateNormal(hit);
                        int nx = pos[0] + (int) normal.getX();
                        int ny = pos[1] - (int) normal.getY();
                        int nz = pos[2] + (int) normal.getZ();
                        if (!world.hasBlock(nx, ny, nz)) {
                            world.placeBlock(nx, ny, nz, selectedBlock);
                        }
                    }
                }
            }
        });

        // Mouse look — bound to main scene, works everywhere
        scene.setOnMouseMoved(e -> {
            if (portfolioUI.isShowing()) return;
            if (!mouseInited) { lastMouseX = e.getSceneX(); lastMouseY = e.getSceneY(); mouseInited = true; return; }
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            yaw += dx * MOUSE_SENS;
            pitch = Math.max(-89, Math.min(89, pitch + dy * MOUSE_SENS));
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        scene.setOnMouseDragged(e -> {
            if (portfolioUI.isShowing()) return;
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            yaw += dx * MOUSE_SENS;
            pitch = Math.max(-89, Math.min(89, pitch + dy * MOUSE_SENS));
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        scene.setOnScroll(e -> {
            if (e.getDeltaY() > 0) cycleBlock(1);
            else if (e.getDeltaY() < 0) cycleBlock(-1);
        });

        // ===== Game Loop =====
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!portfolioUI.isShowing()) {
                    updateMovement();
                }
                updateCamera();
                updateHUD();
            }
        }.start();

        // ===== Stage =====
        stage.setTitle("Minecraft Portfolio — Ploychomphoo Kathinthet");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    // ========== MOVEMENT ==========

    private void updateMovement() {
        double yawRad = Math.toRadians(yaw);
        double forwardX = -Math.sin(yawRad);
        double forwardZ = Math.cos(yawRad);
        double rightX = Math.cos(yawRad);
        double rightZ = Math.sin(yawRad);

        double mx = 0, mz = 0, my = 0;

        if (keysPressed.contains(KeyCode.W)) { mx += forwardX; mz += forwardZ; }
        if (keysPressed.contains(KeyCode.S)) { mx -= forwardX; mz -= forwardZ; }
        if (keysPressed.contains(KeyCode.A)) { mx -= rightX; mz -= rightZ; }
        if (keysPressed.contains(KeyCode.D)) { mx += rightX; mz += rightZ; }

        // Normalize horizontal movement
        double len = Math.sqrt(mx * mx + mz * mz);
        if (len > 0) { mx = mx / len * MOVE_SPEED; mz = mz / len * MOVE_SPEED; }

        if (flyMode) {
            if (keysPressed.contains(KeyCode.SPACE)) my -= MOVE_SPEED;
            if (keysPressed.contains(KeyCode.SHIFT)) my += MOVE_SPEED;
            playerX += mx;
            playerY += my;
            playerZ += mz;
        } else {
            // Gravity
            velocityY += GRAVITY;
            if (keysPressed.contains(KeyCode.SPACE) && onGround) {
                velocityY = -JUMP_VEL;
                onGround = false;
            }

            // Try move X
            double newX = playerX + mx;
            if (!world.isSolid(newX, playerY, playerZ) &&
                !world.isSolid(newX, playerY + 1, playerZ)) {
                playerX = newX;
            }

            // Try move Z
            double newZ = playerZ + mz;
            if (!world.isSolid(playerX, playerY, newZ) &&
                !world.isSolid(playerX, playerY + 1, newZ)) {
                playerZ = newZ;
            }

            // Apply gravity
            double newY = playerY + velocityY;
            double groundY = world.getGroundY(playerX, playerZ) - PLAYER_HEIGHT;

            if (newY >= groundY) {
                playerY = groundY;
                velocityY = 0;
                onGround = true;
            } else {
                playerY = newY;
                onGround = false;
            }
        }
    }

    // ========== CAMERA ==========

    private void updateCamera() {
        camera.getTransforms().clear();
        camera.getTransforms().addAll(
            new Translate(playerX, playerY, playerZ),
            new Rotate(yaw, Rotate.Y_AXIS),
            new Rotate(pitch, Rotate.X_AXIS)
        );
    }

    // ========== HUD ==========

    private void updateHUD() {
        int gx = (int) Math.round(playerX / VoxelWorld.BLOCK_SIZE);
        int gy = (int) Math.round(-playerY / VoxelWorld.BLOCK_SIZE);
        int gz = (int) Math.round(playerZ / VoxelWorld.BLOCK_SIZE);
        coordsLabel.setText(String.format("XYZ: %d / %d / %d  [%s]",
            gx, gy, gz, flyMode ? "FLY" : "WALK"));

        // Check if looking at an info block — show hint
        interactHint.setVisible(false);
        // Simple proximity check for info blocks
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                for (int dy = 0; dy <= 3; dy++) {
                    BlockType bt = world.getBlockType(gx + dx, gy + dy, gz + dz);
                    if (bt != null && bt.isInfoBlock()) {
                        interactHint.setText("▶ Right-click the glowing " + bt.getInfoLabel() + " block to interact!");
                        interactHint.setVisible(true);
                        return;
                    }
                }
            }
        }
    }

    private Point3D estimateNormal(Point3D hitPt) {
        double ax = Math.abs(hitPt.getX());
        double ay = Math.abs(hitPt.getY());
        double az = Math.abs(hitPt.getZ());
        if (ax >= ay && ax >= az) return new Point3D(hitPt.getX() > 0 ? 1 : -1, 0, 0);
        else if (ay >= ax && ay >= az) return new Point3D(0, hitPt.getY() > 0 ? 1 : -1, 0);
        else return new Point3D(0, 0, hitPt.getZ() > 0 ? 1 : -1);
    }

    private void cycleBlock(int dir) {
        blockIndex = (blockIndex + dir + buildBlocks.length) % buildBlocks.length;
        selectedBlock = buildBlocks[blockIndex];
        blockLabel.setText("Block: " + selectedBlock.name());
        updateHotbarHighlight();
    }

    // ========== HOTBAR ==========

    private HBox hotbarUI;

    private HBox buildBlockHotbar() {
        hotbarUI = new HBox(3);
        hotbarUI.setAlignment(Pos.CENTER);
        hotbarUI.setPadding(new Insets(4));
        hotbarUI.setStyle("-fx-background-color: rgba(0,0,0,0.75);" +
            "-fx-border-color: #555; -fx-border-width: 3; -fx-border-radius: 4; -fx-background-radius: 4;");

        for (int i = 0; i < buildBlocks.length; i++) {
            VBox slot = new VBox();
            slot.setAlignment(Pos.CENTER);
            slot.setPrefSize(42, 42);
            slot.setUserData(i);

            Color c = buildBlocks[i].getAverageColor();
            String hex = String.format("#%02X%02X%02X",
                (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
            String border = i == blockIndex ? "#4AEAD9" : "#444";

            slot.setStyle("-fx-background-color: " + hex + ";" +
                "-fx-border-color: " + border + "; -fx-border-width: 2;" +
                "-fx-border-radius: 3; -fx-background-radius: 3; -fx-cursor: hand;");

            Label lbl = new Label(buildBlocks[i].name().substring(0, 2));
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 10; -fx-font-weight: bold; -fx-font-family: monospace;");
            slot.getChildren().add(lbl);

            final int idx = i;
            slot.setOnMouseClicked(ev -> {
                blockIndex = idx;
                selectedBlock = buildBlocks[idx];
                blockLabel.setText("Block: " + selectedBlock.name());
                updateHotbarHighlight();
                ev.consume();
            });

            Tooltip.install(slot, new Tooltip(buildBlocks[i].name()));
            hotbarUI.getChildren().add(slot);
        }
        return hotbarUI;
    }

    private void updateHotbarHighlight() {
        for (Node n : hotbarUI.getChildren()) {
            if (n instanceof VBox vbox && vbox.getUserData() instanceof Integer idx) {
                String style = vbox.getStyle();
                String newBorder = idx == blockIndex ? "#4AEAD9" : "#444";
                vbox.setStyle(style.replaceAll("-fx-border-color: #[0-9a-fA-F]+;",
                    "-fx-border-color: " + newBorder + ";"));
            }
        }
    }

    private Label hudLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: white; -fx-font-size: 14; -fx-font-family: monospace;" +
            "-fx-effect: dropshadow(gaussian, black, 3, 0, 1, 1);");
        return l;
    }

    public static void main(String[] args) { launch(args); }
}
