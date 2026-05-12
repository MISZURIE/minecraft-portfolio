# ⛏ Minecraft 3D Portfolio - Java Edition

A fully interactive **3D Minecraft-style portfolio** built entirely in **Java** using **JavaFX 3D**. Walk around a voxel world, place and break blocks, and discover glowing **info blocks** that reveal portfolio content when interacted with.

![Java](https://img.shields.io/badge/Java-26-orange?style=flat-square&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

## 🎮 Features

- **First-Person Movement** - Walk with WASD, look with mouse, jump with Space
- **Block Editing** - Left-click to break, right-click to place (10 block types)
- **Portfolio Info Blocks** - 7 glowing blocks scattered across the world; right-click to view:
  - 📖 **About Me** - Player profile, education, stats
  - ⚔️ **Skills** - Programming languages, frameworks, AI/ML, cybersecurity
  - 🏗️ **Projects** - Hydra Watch, Terra-Sentinel, Prom To, Elderly E-Commerce, AstroCycle
  - ⭐ **Experience** - NECTEC Research Intern, Microsoft TEALS, Freelance Dev
  - 🏆 **Awards** - 10+ international & national achievements
  - 📜 **Certifications** - NUS, Stanford, Cisco, Google, freeCodeCamp
  - 💌 **Contact** - Email, phone, GitHub, Instagram
- **3D Voxel World** - Procedurally generated terrain with trees, house, Nether portal, diamond/gold ores, water pond
- **Fly Mode** - Press F to toggle flying
- **Gravity & Collision** - Walk on top of blocks with realistic physics

## 🕹️ Controls

| Key | Action |
|-----|--------|
| `W A S D` | Walk forward / left / back / right |
| `Mouse` | Look around |
| `Left Click` | Break block |
| `Right Click` | Place block / Interact with info block |
| `Q / E / Scroll` | Cycle block type |
| `Space` | Jump |
| `F` | Toggle fly mode |
| `ESC` | Close portfolio panel |

## 🧱 Block Types

`GRASS` · `DIRT` · `STONE` · `WOOD` · `DIAMOND` · `GOLD` · `GLOWSTONE` · `OBSIDIAN` · `SAND` · `REDSTONE`

## 🚀 How to Run

### Prerequisites
- **Java 21+** (tested on Java 26)
- **JavaFX SDK 21.0.2** - [Download here](https://gluonhq.com/products/javafx/)

### Quick Start
1. Clone this repo
2. Download [JavaFX SDK](https://gluonhq.com/products/javafx/) and extract to `javafx-sdk-21.0.2/` in the project root
3. Run:
```bash
# Windows
run.bat

# Manual
javac --module-path javafx-sdk-21.0.2/lib --add-modules javafx.controls -d out -sourcepath src/main/java src/main/java/com/portfolio/minecraft/*.java
java --module-path javafx-sdk-21.0.2/lib --add-modules javafx.controls --enable-native-access=javafx.graphics -cp out com.portfolio.minecraft.MinecraftPortfolio
```

## 📁 Project Structure

```
src/main/java/com/portfolio/minecraft/
├── MinecraftPortfolio.java   # Main app — 3D scene, first-person camera, input, HUD
├── VoxelWorld.java           # Voxel engine — terrain gen, block place/remove, collision
├── BlockType.java            # Block types with procedural pixel textures + info blocks
└── PortfolioUI.java          # Portfolio panels (About, Skills, Projects, etc.)
```

## 🛠️ Tech Stack

- **Language:** Java 26
- **Graphics:** JavaFX 3D (PerspectiveCamera, PhongMaterial, Box)
- **Textures:** Procedurally generated pixel art via `WritableImage`
- **Physics:** Custom gravity, collision detection, ground snapping
- **Architecture:** MVC - World (model), Camera/Input (controller), PortfolioUI (view)

## 👩‍💻 About the Developer

**Ploychomphoo Kathinthet** — Freshman IT student at KMUTT with 3+ years of full-stack & AI development experience. Top placements in 10+ international competitions including NASA Space Apps, Conrad Challenge, and JUMP Thailand Hackathon.

---

*Built with ❤️ and Java — because real Minecraft runs on Java too* ☕
