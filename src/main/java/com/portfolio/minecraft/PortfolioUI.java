package com.portfolio.minecraft;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

/**
 * Portfolio UI panels styled like Minecraft inventory menus.
 * Contains all CV data from Ploychomphoo Kathinthet.
 */
public class PortfolioUI {

    private final StackPane overlay;
    private VBox currentPanel;

    // Minecraft-style colors
    private static final String CYAN = "#4AEAD9";
    private static final String GOLD = "#FFD700";
    private static final String PURPLE = "#A855F7";
    private static final String GREEN = "#50C878";
    private static final String RED = "#FF3333";

    public PortfolioUI() {
        overlay = new StackPane();
        overlay.setAlignment(Pos.CENTER);
        overlay.setPickOnBounds(false);
        overlay.setMouseTransparent(false);
    }

    public StackPane getOverlay() {
        return overlay;
    }

    public void showPanel(String section) {
        overlay.getChildren().clear();
        VBox panel = switch (section) {
            case "about" -> buildAboutPanel();
            case "skills" -> buildSkillsPanel();
            case "projects" -> buildProjectsPanel();
            case "experience" -> buildExperiencePanel();
            case "awards" -> buildAwardsPanel();
            case "certs" -> buildCertsPanel();
            case "contact" -> buildContactPanel();
            default -> null;
        };
        if (panel != null) {
            currentPanel = panel;
            overlay.getChildren().add(panel);
        }
    }

    public void hidePanel() {
        overlay.getChildren().clear();
        currentPanel = null;
    }

    public boolean isShowing() {
        return currentPanel != null;
    }

    // ========== PANEL BUILDERS ==========

    private VBox buildAboutPanel() {
        VBox panel = createPanel("📖 Player Profile");
        VBox body = createBody();

        // Player name
        Label name = styledLabel("PLOYCHOMPHOO KATHINTHET", 22, GOLD, true);
        Label title = styledLabel("🎓 B.Eng. Information Technology", 18, GREEN, false);
        Label school = styledLabel("King Mongkut's University of Technology Thonburi (KMUTT)", 15, "#aaaaaa", false);
        Label location = styledLabel("📍 Bangkok, Thailand", 16, CYAN, false);

        // XP Bar
        ProgressBar xpBar = new ProgressBar(0.75);
        xpBar.setPrefWidth(400);
        xpBar.setStyle("-fx-accent: " + GREEN + ";");
        Label xpLabel = styledLabel("LEVEL: Freshman IT Student & AI Developer", 13, GREEN, false);

        VBox playerInfo = new VBox(6, name, title, school, location,
                new Region() {{ setPrefHeight(8); }}, xpLabel, xpBar);

        // Objective
        Label objTitle = styledLabel("📜 Objective", 18, GOLD, true);
        Label objText = styledLabel(
            "Freshman IT student at KMUTT and competitive software developer with 3+ years " +
            "of hands-on experience building AI-powered full-stack systems, earning top placements " +
            "across 10+ national and international competitions, and completing a research " +
            "internship at NECTEC in AI/ML/NLP.", 15, "#cccccc", false);
        objText.setWrapText(true);

        // Stats
        HBox stats = new HBox(10,
            statBox("3.70", "GPAX"), statBox("7.0", "IELTS"),
            statBox("B1", "DELE"), statBox("10+", "Comps"),
            statBox("3+", "Yrs Exp"), statBox("60K+", "IG")
        );
        stats.setAlignment(Pos.CENTER);

        body.getChildren().addAll(playerInfo,
            separator(), objTitle, objText,
            separator(), stats);

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildSkillsPanel() {
        VBox panel = createPanel("⚔️ Skill Enchantments");
        VBox body = createBody();

        body.getChildren().addAll(
            skillCategory("💎 Languages",
                new String[]{"Python|90", "TypeScript|88", "JavaScript|92",
                    "HTML5/CSS3|95", "SQL|75", "C#|65"}),
            skillCategory("⚡ Frameworks",
                new String[]{"React|90", "Next.js|85", "React Native|75",
                    "Flutter|70", "Node.js|85", "Flask|78"}),
            skillCategory("🧠 AI / ML",
                new String[]{"Machine Learning|82", "NLP|80", "GNNs|75",
                    "Gemini API|88", "OpenAI API|85"}),
            skillCategory("☁️ Cloud & Tools",
                new String[]{"Microsoft Azure|80", "Supabase|78",
                    "Git & Docker|82", "Figma|75"}),
            skillCategory("🛡️ Cybersecurity",
                new String[]{"CTF Challenges|78", "Pen Testing|72", "OWASP|75"})
        );

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildProjectsPanel() {
        VBox panel = createPanel("🏗️ Crafting Table — Projects");
        VBox body = createBody();

        body.getChildren().addAll(
            projectCard("🔍 HYDRA WATCH", "TypeScript · React · Node.js · GNNs",
                "▸ 3-layer fraud detection (Rule-Based + ML + GNNs) — 98.5% accuracy\n" +
                "▸ Explainable AI module for PDPA/GDPR compliance\n" +
                "▸ 🏅 Conrad Innovator Phase 2 — Conrad Challenge 2024–2025"),
            projectCard("🌊 TERRA-SENTINEL", "React Native · Flutter · Python · C# · Azure",
                "▸ Real-time flood warning on Azure Serverless + CV + NLP\n" +
                "▸ Emergency Dashboard with SOS triggers & map rendering\n" +
                "▸ International collaboration with India-based team"),
            projectCard("🛒 PROM TO", "Next.js · React · Flask · MySQL · Gemini API",
                "▸ AI customer engagement via QR + Gemini behavioral analysis\n" +
                "▸ Prototype in 48h at CP ALL — Top 2% (1/2,000+)\n" +
                "▸ Flask + Next.js real-time AI insights pipeline"),
            projectCard("👴 ELDERLY E-COMMERCE", "React · Next.js · LINE API · AIS 5G",
                "▸ LINE OA chat for elderly artisans — AI image + caption\n" +
                "▸ Top 10 from 300+ teams — JUMP Thailand 2025\n" +
                "▸ Accessibility-first UX validated with elderly users"),
            projectCard("🚀 ASTROCYCLE", "Python · React.js · Supabase · NASA APIs",
                "▸ Digital Twin for NASA — waste recycling simulation\n" +
                "▸ Throughput engine + resource budgeting UI\n" +
                "▸ 🏅 Galactic Problem Solver — NASA 2024 & 2025")
        );

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildExperiencePanel() {
        VBox panel = createPanel("⭐ Adventure Log — Experience");
        VBox body = createBody();

        body.getChildren().addAll(
            expCard("🧪 AI/ML NLP Research Intern",
                "NECTEC — National Electronics & Computer Technology Center",
                "Summer 2025 (2 Months)",
                "◆ NLP research using OpenAI API — Smart Conversational Model\n" +
                "◆ Thai-language NLP pipelines with NECTEC scientists\n" +
                "◆ Full research cycle in 8-week program"),
            expCard("🎓 Virtual Volunteer — CS Education",
                "Microsoft TEALS Program, California (Remote)", "2024 – 2025",
                "◆ CS education for US high school students\n" +
                "◆ Curriculum delivery & cross-timezone support"),
            expCard("💻 Freelance Web Developer",
                "Fastwork Platform", "2019 – Present",
                "◆ Client websites since age 14 — HTML5, CSS3, JS\n" +
                "◆ Client communication & iterative delivery")
        );

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildAwardsPanel() {
        VBox panel = createPanel("🏆 Achievements Unlocked");
        VBox body = createBody();

        String[][] awards = {
            {"🌍", "Conrad Challenge 2024–2025", "Conrad Innovator — Phase 2"},
            {"🇹🇭", "JUMP Thailand Hackathon 2025", "Top 10 Finalist / 300+ teams"},
            {"🤖", "CAIC Creative AI Camp 2025", "Top 2% — 1 of 40 from 2,000+"},
            {"🧠", "Super AI Engineer Season 5", "Level 2 Qualifier"},
            {"🛡️", "Cyber Top Talent 2025", "Rank 52/1,500 — Top 3.5%"},
            {"🚀", "NASA Space Apps 2024 & 2025", "Galactic Problem Solver (Both)"},
            {"💡", "Imagine World Cup 2024", "Honorable Mention — Microsoft"},
            {"🚗", "Toyota Canada Co-op 2024", "Winner"},
            {"🪐", "O'Neill Space Settlement 2023", "1st Prize — The Aegis"},
            {"🏥", "Thammasat Health Innovation", "2nd Place — Mental Health"},
        };

        for (String[] a : awards) {
            body.getChildren().add(achievementRow(a[0], a[1], a[2]));
        }

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildCertsPanel() {
        VBox panel = createPanel("📜 Enchantment Books — Certifications");
        VBox body = createBody();

        String[][] certs = {
            {"🏛️", "Certificate in Cybersecurity", "NUS — National University of Singapore (Jul 2024)"},
            {"🌐", "Introduction to Cybersecurity", "Cisco Networking Academy (2024)"},
            {"🎓", "Machine Learning Specialization (5 Courses)", "Stanford × Coursera (May 2024)"},
            {"📊", "Foundations: Data, Data, Everywhere", "Google × Coursera (2024)"},
            {"💻", "JS Algorithms & Data Structures", "freeCodeCamp (Nov 2024)"},
            {"🎨", "Responsive Web Design", "freeCodeCamp (Nov 2024)"},
        };

        for (String[] c : certs) {
            body.getChildren().add(certRow(c[0], c[1], c[2]));
        }

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    private VBox buildContactPanel() {
        VBox panel = createPanel("💌 Contact — Send Message");
        VBox body = createBody();

        body.getChildren().addAll(
            contactRow("📧", "angelinaploy999@gmail.com"),
            contactRow("📞", "+66-95-949-7469"),
            contactRow("🐙", "github.com/M"),
            contactRow("📸", "IG: @angelinaploy"),
            contactRow("🎯", "@Hatumport — 60K+ followers"),
            separator(),
            styledLabel("🌐 Languages", 18, CYAN, true),
            contactRow("🇹🇭", "Thai (Native)"),
            contactRow("🇬🇧", "English (IELTS 7.0)"),
            contactRow("🇪🇸", "Spanish (DELE B1)")
        );

        panel.getChildren().add(wrapScroll(body));
        return panel;
    }

    // ========== UI COMPONENT HELPERS ==========

    private VBox createPanel(String title) {
        VBox panel = new VBox();
        panel.setMaxWidth(720);
        panel.setMaxHeight(550);
        panel.setStyle(
            "-fx-background-color: rgba(15,15,30,0.95);" +
            "-fx-border-color: #3b3b5c;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        panel.setEffect(new DropShadow(30, Color.web("#4AEAD9", 0.3)));

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, rgba(74,234,217,0.12), rgba(168,85,247,0.08));" +
            "-fx-border-color: #3b3b5c;" +
            "-fx-border-width: 0 0 2 0;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: " + CYAN + "; -fx-font-size: 16; -fx-font-weight: bold;");
        titleLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
            "-fx-background-color: rgba(255,51,51,0.2);" +
            "-fx-text-fill: " + RED + ";" +
            "-fx-border-color: " + RED + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-font-size: 12;" +
            "-fx-cursor: hand;"
        );
        closeBtn.setOnAction(e -> hidePanel());

        header.getChildren().addAll(titleLabel, spacer, closeBtn);
        panel.getChildren().add(header);

        return panel;
    }

    private VBox createBody() {
        VBox body = new VBox(10);
        body.setPadding(new Insets(16));
        return body;
    }

    private ScrollPane wrapScroll(VBox body) {
        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(480);
        scroll.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        return scroll;
    }

    private Label styledLabel(String text, int size, String color, boolean bold) {
        Label l = new Label(text);
        String weight = bold ? "-fx-font-weight: bold;" : "";
        l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + ";" + weight);
        l.setWrapText(true);
        if (bold) {
            l.setFont(Font.font("Monospaced", FontWeight.BOLD, size));
        }
        return l;
    }

    private VBox statBox(String value, String label) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setStyle(
            "-fx-background-color: rgba(0,0,0,0.4);" +
            "-fx-border-color: rgba(74,234,217,0.2);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        box.getChildren().addAll(
            styledLabel(value, 18, GOLD, true),
            styledLabel(label, 12, "#aaaaaa", false)
        );
        return box;
    }

    private VBox skillCategory(String title, String[] skills) {
        VBox cat = new VBox(8);
        cat.setPadding(new Insets(12));
        cat.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(168,85,247,0.2);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        cat.getChildren().add(styledLabel(title, 15, PURPLE, true));

        FlowPane grid = new FlowPane(8, 8);
        for (String s : skills) {
            String[] parts = s.split("\\|");
            String name = parts[0];
            double pct = Double.parseDouble(parts[1]) / 100.0;

            VBox skill = new VBox(4);
            skill.setPrefWidth(200);
            skill.setPadding(new Insets(8));
            skill.setStyle(
                "-fx-background-color: rgba(0,0,0,0.4);" +
                "-fx-border-color: rgba(74,234,217,0.15);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;"
            );

            ProgressBar bar = new ProgressBar(pct);
            bar.setPrefWidth(180);
            bar.setStyle("-fx-accent: " + PURPLE + ";");

            skill.getChildren().addAll(
                styledLabel(name, 14, "#ffffff", false), bar
            );
            grid.getChildren().add(skill);
        }
        cat.getChildren().add(grid);
        return cat;
    }

    private VBox projectCard(String title, String tech, String details) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(14));
        card.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(80,200,120,0.2);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        card.getChildren().addAll(
            styledLabel(title, 16, GREEN, true),
            styledLabel(tech, 12, PURPLE, false),
            styledLabel(details, 14, "#bbbbbb", false)
        );
        return card;
    }

    private VBox expCard(String title, String place, String date, String details) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(14));
        card.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(255,215,0,0.2);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        card.getChildren().addAll(
            styledLabel(title, 16, GOLD, true),
            styledLabel(place, 13, "#aaaaaa", false),
            styledLabel("📅 " + date, 13, CYAN, false),
            styledLabel(details, 14, "#bbbbbb", false)
        );
        return card;
    }

    private HBox achievementRow(String icon, String title, String desc) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(255,215,0,0.15);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 24;");
        VBox info = new VBox(2,
            styledLabel(title, 14, GOLD, true),
            styledLabel(desc, 13, "#bbbbbb", false));
        row.getChildren().addAll(ic, info);
        return row;
    }

    private HBox certRow(String icon, String title, String detail) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(168,85,247,0.15);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 24;");
        VBox info = new VBox(2,
            styledLabel(title, 14, PURPLE, true),
            styledLabel(detail, 13, "#bbbbbb", false));
        row.getChildren().addAll(ic, info);
        return row;
    }

    private HBox contactRow(String icon, String text) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3);" +
            "-fx-border-color: rgba(74,234,217,0.15);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 22;");
        row.getChildren().addAll(ic, styledLabel(text, 15, "#dddddd", false));
        return row;
    }

    private Region separator() {
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);
        sep.setStyle("-fx-background-color: #3b3b5c;");
        VBox.setMargin(sep, new Insets(6, 0, 6, 0));
        return sep;
    }
}
