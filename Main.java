package projectcode;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * FitnessTracker – main entry point and GUI controller.
 *
 * Layout: left sidebar (nav) + right card panel (Food | Workout | Rank | Dashboard).
 */
public class Main extends JFrame
{
    //Model
    private final Food        food        = new Food();
    private final Workout     workout     = new Workout();
    private final FitnessRank fitnessRank = new FitnessRank();
    private final UserProfile userProfile = new UserProfile();

    //Palette
    static final Color BG        = new Color(13,  17,  23);
    static final Color CARD      = new Color(22,  27,  34);
    static final Color BORDER    = new Color(48,  54,  61);
    static final Color ACCENT    = new Color(88, 166, 255);
    static final Color GREEN     = new Color(63, 185, 80);
    static final Color ORANGE    = new Color(255,166, 77);
    static final Color RED       = new Color(255, 99, 99);
    static final Color TEXT      = new Color(230,237,243);
    static final Color MUTED     = new Color(139,148,158);

    //Cards
    private CardLayout cardLayout;
    private JPanel     cardPanel;

    // Food tab state
    private JComboBox<FoodList> foodCombo;
    private JTextField          foodAmountField;
    private JTable              foodTable;
    private DefaultTableModel   foodTableModel;
    private JLabel              totalCalLabel;

    // Workout tab state
    private JComboBox<Effort>       effortCombo;
    private JSpinner                timeSpinner;
    private JList<WorkoutList>      suggestionList;
    private JTextField              logMinField;
    private JLabel                  burnedLabel;
    private JLabel                  totalBurnedLabel;

    // Rank tab state
    private JLabel      rankLabel;
    private JLabel      rankEmojiLabel;
    private JLabel      motivLabel;
    private JProgressBar rankBar;
    private JLabel      pointsLabel;

    // Dashboard labels
    private JLabel dashCalIn, dashCalBurned, dashNet, dashRank, dashPoints;
    private JProgressBar dashBar;

    // Profile tab fields
    private JTextField profAgeField, profWeightField, profHeightField;
    private JComboBox<UserProfile.ActivityLevel> profActivityCombo;
    private JComboBox<UserProfile.Goal>          profGoalCombo;
    private JComboBox<String>                    profSexCombo;
    private JLabel profBmiLabel, profBmiCatLabel, profTdeeLabel, profGoalCalLabel, profStatusLabel;

    // Dashboard BMI / calorie goal labels
    private JLabel dashCalGoal, dashBmiStatus, dashWorkoutAdvice;

    // ── Nav buttons (to highlight active) ───────────────────────────────────
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    // ════════════════════════════════════════════════════════════════════════
    public Main()
    {
        super("💪 FitnessTracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(860, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildCardPanel(), BorderLayout.CENTER);

        navigate("Dashboard");
        setVisible(true);
    }

    //Sidebar
    private JPanel buildSidebar()
    {
        JPanel side = new JPanel();
        side.setBackground(CARD);
        side.setPreferredSize(new Dimension(190, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        // Logo
        JLabel logo = new JLabel("FitTracker");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(28, 0, 8, 0));
        side.add(logo);

        JLabel sub = new JLabel("your fitness companion");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(sub);

        side.add(Box.createVerticalStrut(28));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(160, 1));
        sep.setForeground(BORDER);
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(sep);
        side.add(Box.createVerticalStrut(14));

        // Nav items
        String[][] navItems = {
            {"📊", "Dashboard"},
            {"👤", "Profile"},
            {"🍽", "Food"},
            {"🏋", "Workout"},
            {"🏆", "Rank"},
        };

        for (String[] item : navItems)
        {
            JButton btn = navButton(item[0] + "  " + item[1], item[1]);
            navButtons.put(item[1], btn);
            side.add(btn);
            side.add(Box.createVerticalStrut(4));
        }

        side.add(Box.createVerticalGlue());

        // Version footer
        JLabel ver = new JLabel("v1.0  ·  CSE 1325");
        ver.setFont(new Font("SansSerif", Font.PLAIN, 10));
        ver.setForeground(MUTED);
        ver.setAlignmentX(Component.CENTER_ALIGNMENT);
        ver.setBorder(new EmptyBorder(0, 0, 14, 0));
        side.add(ver);

        return side;
    }

    private JButton navButton(String label, String card)
    {
        JButton btn = new JButton(label)
        {
            @Override protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed())
                    g2.setColor(new Color(48, 54, 61));
                else if (Boolean.TRUE.equals(getClientProperty("active")))
                    g2.setColor(new Color(31, 78, 120, 180));
                else if (getModel().isRollover())
                    g2.setColor(new Color(35, 40, 48));
                else
                    g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(170, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> navigate(card));
        return btn;
    }

    private void navigate(String card)
    {
        navButtons.forEach((k, b) -> b.putClientProperty("active", k.equals(card)));
        navButtons.forEach((k, b) -> b.repaint());
        if (card.equals("Rank"))      refreshRank();
        if (card.equals("Dashboard"))  refreshDashboard();
        if (card.equals("Workout"))    refreshWorkoutAdvice();
        cardLayout.show(cardPanel, card);
    }

    // ── Card Panel ───────────────────────────────────────────────────────────
    private JPanel buildCardPanel()
    {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG);

        cardPanel.add(buildDashboard(),   "Dashboard");
        cardPanel.add(buildProfilePanel(),"Profile");
        cardPanel.add(buildFoodPanel(),   "Food");
        cardPanel.add(buildWorkoutPanel(),"Workout");
        cardPanel.add(buildRankPanel(),   "Rank");

        return cardPanel;
    }



    //  PROFILE PANEL
    private JPanel buildProfilePanel()
    {
        JPanel p = darkPanel();
        p.setLayout(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(32, 36, 32, 36));
        p.add(sectionTitle("👤  My Profile & BMI"), BorderLayout.NORTH);

        // ── Input form card ──────────────────────────────────────────────────
        JPanel formCard = card();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets  = new Insets(8, 10, 8, 10);
        gc.anchor  = GridBagConstraints.WEST;

        // Row 0: age / sex
        gc.gridx = 0; gc.gridy = 0; formCard.add(muted("Age:"), gc);
        gc.gridx = 1; profAgeField = styledField(70); formCard.add(profAgeField, gc);

        gc.gridx = 2; formCard.add(muted("Sex:"), gc);
        gc.gridx = 3;
        profSexCombo = new JComboBox<>(new String[]{"Male", "Female"});
        style(profSexCombo);
        profSexCombo.setPreferredSize(new Dimension(110, 30));
        formCard.add(profSexCombo, gc);

        // Row 1: weight / height
        gc.gridx = 0; gc.gridy = 1; formCard.add(muted("Weight (kg):"), gc);
        gc.gridx = 1; profWeightField = styledField(70); formCard.add(profWeightField, gc);

        gc.gridx = 2; formCard.add(muted("Height (cm):"), gc);
        gc.gridx = 3; profHeightField = styledField(70); formCard.add(profHeightField, gc);

        // Row 2: activity / goal
        gc.gridx = 0; gc.gridy = 2; formCard.add(muted("Activity level:"), gc);
        gc.gridx = 1; gc.gridwidth = 1;
        profActivityCombo = new JComboBox<>(UserProfile.ActivityLevel.values());
        style(profActivityCombo);
        profActivityCombo.setPreferredSize(new Dimension(260, 30));
        formCard.add(profActivityCombo, gc);

        gc.gridx = 2; formCard.add(muted("Goal:"), gc);
        gc.gridx = 3;
        profGoalCombo = new JComboBox<>(UserProfile.Goal.values());
        style(profGoalCombo);
        profGoalCombo.setPreferredSize(new Dimension(160, 30));
        formCard.add(profGoalCombo, gc);

        // Row 3: Save button
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 4;
        gc.anchor = GridBagConstraints.CENTER;
        JButton saveBtn = accentButton("💾  Save Profile", ACCENT);
        formCard.add(saveBtn, gc);

        // ── BMI Result card ──────────────────────────────────────────────────
        JPanel bmiCard = card();
        bmiCard.setLayout(new BoxLayout(bmiCard, BoxLayout.Y_AXIS));
        bmiCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(18, 24, 18, 24)));

        JLabel bmiTitle = new JLabel("BMI Report");
        bmiTitle.setFont(new Font("Georgia", Font.BOLD, 16));
        bmiTitle.setForeground(TEXT);
        bmiTitle.setAlignmentX(CENTER_ALIGNMENT);
        bmiCard.add(bmiTitle);
        bmiCard.add(Box.createVerticalStrut(12));

        profBmiLabel = new JLabel("—");
        profBmiLabel.setFont(new Font("Georgia", Font.BOLD, 42));
        profBmiLabel.setForeground(ACCENT);
        profBmiLabel.setAlignmentX(CENTER_ALIGNMENT);
        bmiCard.add(profBmiLabel);

        profBmiCatLabel = new JLabel("Enter your details above");
        profBmiCatLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        profBmiCatLabel.setForeground(MUTED);
        profBmiCatLabel.setAlignmentX(CENTER_ALIGNMENT);
        bmiCard.add(profBmiCatLabel);
        bmiCard.add(Box.createVerticalStrut(16));

        // Scale reference
        JPanel scaleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        scaleRow.setOpaque(false);
        addBmiBand(scaleRow, "< 18.5",    "Underweight", ACCENT);
        addBmiBand(scaleRow, "18.5 – 25", "Normal",      GREEN);
        addBmiBand(scaleRow, "25 – 30",   "Overweight",  ORANGE);
        addBmiBand(scaleRow, "> 30",      "Obese",       RED);
        bmiCard.add(scaleRow);
        bmiCard.add(Box.createVerticalStrut(16));

        //Calorie report card
        JPanel calCard = card();
        calCard.setLayout(new BoxLayout(calCard, BoxLayout.Y_AXIS));
        calCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(14, 24, 14, 24)));

        JLabel calTitle = new JLabel("Daily Calorie Targets");
        calTitle.setFont(new Font("Georgia", Font.BOLD, 15));
        calTitle.setForeground(TEXT);
        calTitle.setAlignmentX(LEFT_ALIGNMENT);
        calCard.add(calTitle);
        calCard.add(Box.createVerticalStrut(10));

        profTdeeLabel    = infoRow(calCard, "TDEE (maintenance):", "—");
        profGoalCalLabel = infoRow(calCard, "Your daily goal:",    "—");
        calCard.add(Box.createVerticalStrut(10));
        profStatusLabel = new JLabel("Save your profile to see personalised advice.");
        profStatusLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        profStatusLabel.setForeground(MUTED);
        profStatusLabel.setAlignmentX(LEFT_ALIGNMENT);
        calCard.add(profStatusLabel);

        //Layout
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(formCard, BorderLayout.NORTH);

        JPanel infoRow2 = new JPanel(new GridLayout(1, 2, 12, 0));
        infoRow2.setOpaque(false);
        infoRow2.add(bmiCard);
        infoRow2.add(calCard);
        center.add(infoRow2, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);

        //Save action
        saveBtn.addActionListener(e -> {
            try {
                double w  = Double.parseDouble(profWeightField.getText().trim());
                double h  = Double.parseDouble(profHeightField.getText().trim());
                int    a  = Integer.parseInt(profAgeField.getText().trim());
                if (w <= 0 || h <= 0 || a <= 0)
                    throw new NumberFormatException("non-positive");

                boolean male = profSexCombo.getSelectedItem().equals("Male");
                UserProfile.ActivityLevel act =
                    (UserProfile.ActivityLevel) profActivityCombo.getSelectedItem();
                UserProfile.Goal goal2 =
                    (UserProfile.Goal) profGoalCombo.getSelectedItem();

                userProfile.setProfile(w, h, a, male, act, goal2);
                refreshProfileDisplay();
                JOptionPane.showMessageDialog(this,
                    "Profile saved! Your Dashboard has been updated.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter valid positive numbers for age, weight, and height.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return p;
    }

    private JLabel infoRow(JPanel parent, String label, String value)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 13));
        val.setForeground(TEXT);
        row.add(lbl);
        row.add(val);
        parent.add(row);
        return val;
    }

    private void addBmiBand(JPanel parent, String range, String label, Color col)
    {
        JPanel b = new JPanel();
        b.setBackground(new Color(col.getRed(), col.getGreen(), col.getBlue(), 40));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(col, 1, true),
            new EmptyBorder(4, 8, 4, 8)));
        b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));
        JLabel r = new JLabel(range);
        r.setFont(new Font("SansSerif", Font.BOLD, 10));
        r.setForeground(col);
        r.setAlignmentX(CENTER_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 10));
        l.setForeground(MUTED);
        l.setAlignmentX(CENTER_ALIGNMENT);
        b.add(r); b.add(l);
        parent.add(b);
    }

    private void refreshProfileDisplay()
    {
        if (!userProfile.isProfileSet()) return;
        double bmi = userProfile.getBMI();
        String cat = userProfile.getBMICategory();

        profBmiLabel.setText(String.valueOf(bmi));
        profBmiCatLabel.setText(cat + "  " + userProfile.getBMIEmoji());

        Color bmiColor = cat.equals("Normal weight") ? GREEN
                       : cat.equals("Underweight")   ? ACCENT
                       : cat.equals("Overweight")    ? ORANGE : RED;
        profBmiLabel.setForeground(bmiColor);
        profBmiCatLabel.setForeground(bmiColor);

        profTdeeLabel.setText(String.format("%.0f kcal / day", userProfile.getTDEE()));
        profGoalCalLabel.setText(String.format("%.0f kcal / day  (%s)",
            userProfile.getDailyCalorieGoal(), userProfile.getGoal()));

        double consumed = food.getTotalCalories();
        profStatusLabel.setText(userProfile.getCalorieStatusMessage(consumed));
        profStatusLabel.setForeground(userProfile.getRemainingCalories(consumed) >= 0 ? GREEN : RED);
    }

    
    // DASHBOARD
    private JPanel buildDashboard()
    {
        JPanel p = darkPanel();
        p.setLayout(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(32, 36, 32, 36));

        // Title
        JLabel title = sectionTitle("Dashboard");
        p.add(title, BorderLayout.NORTH);

        // Summary cards row
        // Top row: 3 calorie stats
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(24, 0, 8, 0));

        dashCalIn      = statCard(cards, "Calories In",    "0 kcal",  ORANGE);
        dashCalBurned  = statCard(cards, "Calories Burned","0 kcal",  GREEN);
        dashNet        = statCard(cards, "Net Calories",   "0 kcal",  ACCENT);

        // Second row: goal + BMI
        JPanel cards2 = new JPanel(new GridLayout(1, 2, 16, 0));
        cards2.setOpaque(false);
        cards2.setBorder(new EmptyBorder(0, 0, 8, 0));

        dashCalGoal    = statCard(cards2, "Daily Calorie Goal", "Set profile ▶", MUTED);
        dashBmiStatus  = statCard(cards2, "BMI Status",         "Set profile ▶", MUTED);

        // Workout advice banner
        JPanel adviceCard = new JPanel(new BorderLayout());
        adviceCard.setBackground(CARD);
        adviceCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(10, 16, 10, 16)));
        dashWorkoutAdvice = new JLabel("Set up your profile to get smart workout recommendations.");
        dashWorkoutAdvice.setFont(new Font("SansSerif", Font.ITALIC, 13));
        dashWorkoutAdvice.setForeground(ACCENT);
        adviceCard.add(dashWorkoutAdvice, BorderLayout.CENTER);

        JPanel centerGrid = new JPanel(new BorderLayout(0, 8));
        centerGrid.setOpaque(false);
        centerGrid.add(cards,      BorderLayout.NORTH);
        centerGrid.add(cards2,     BorderLayout.CENTER);
        centerGrid.add(adviceCard, BorderLayout.SOUTH);

        p.add(centerGrid, BorderLayout.CENTER);

        // Bottom: rank summary
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JLabel rl = new JLabel("Current Rank");
        rl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rl.setForeground(MUTED);
        rl.setAlignmentX(CENTER_ALIGNMENT);
        bottom.add(rl);
        bottom.add(Box.createVerticalStrut(6));

        dashRank = new JLabel("Beginner 🌱");
        dashRank.setFont(new Font("Georgia", Font.BOLD, 28));
        dashRank.setForeground(TEXT);
        dashRank.setAlignmentX(CENTER_ALIGNMENT);
        bottom.add(dashRank);
        bottom.add(Box.createVerticalStrut(6));

        dashPoints = new JLabel("0 pts");
        dashPoints.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dashPoints.setForeground(MUTED);
        dashPoints.setAlignmentX(CENTER_ALIGNMENT);
        bottom.add(dashPoints);
        bottom.add(Box.createVerticalStrut(12));

        dashBar = new JProgressBar(0, 100);
        dashBar.setValue(0);
        dashBar.setStringPainted(false);
        dashBar.setForeground(ACCENT);
        dashBar.setBackground(BORDER);
        dashBar.setMaximumSize(new Dimension(400, 10));
        dashBar.setAlignmentX(CENTER_ALIGNMENT);
        bottom.add(dashBar);

        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    /** Creates a stat card inside {@code parent} and returns the value label. */
    private JLabel statCard(JPanel parent, String header, String initialValue, Color accent)
    {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel h = new JLabel(header);
        h.setFont(new Font("SansSerif", Font.PLAIN, 12));
        h.setForeground(MUTED);
        h.setAlignmentX(CENTER_ALIGNMENT);
        card.add(h);
        card.add(Box.createVerticalStrut(10));

        JLabel val = new JLabel(initialValue);
        val.setFont(new Font("Georgia", Font.BOLD, 26));
        val.setForeground(accent);
        val.setAlignmentX(CENTER_ALIGNMENT);
        card.add(val);

        parent.add(card);
        return val;
    }

    private void refreshDashboard()
    {
        syncRank();
        double calIn     = food.getTotalCalories();
        double calBurned = workout.getTotalCaloriesBurned();
        double net       = calIn - calBurned;

        dashCalIn.setText(calIn + " kcal");
        dashCalBurned.setText(calBurned + " kcal");
        dashNet.setText(String.format("%.1f kcal", net));
        dashNet.setForeground(net > 500 ? ORANGE : net < 0 ? GREEN : ACCENT);
        dashRank.setText(fitnessRank.calculateRank() + " " + fitnessRank.getRankEmoji());
        dashPoints.setText(fitnessRank.getTotalPoints() + " pts");
        dashBar.setValue(fitnessRank.getTierProgress());

        if (userProfile.isProfileSet())
        {
            double goal = userProfile.getDailyCalorieGoal();
            dashCalGoal.setText(String.format("%.0f kcal", goal));
            dashCalGoal.setForeground(ACCENT);

            double bmi = userProfile.getBMI();
            String cat = userProfile.getBMICategory();
            dashBmiStatus.setText(String.format("%.1f — %s %s", bmi, cat, userProfile.getBMIEmoji()));
            Color bmiColor = cat.equals("Normal weight") ? GREEN
                           : cat.equals("Underweight")   ? ACCENT
                           : cat.equals("Overweight")    ? ORANGE : RED;
            dashBmiStatus.setForeground(bmiColor);

            dashWorkoutAdvice.setText(userProfile.getWorkoutRecommendationReason(calIn));
            Effort rec = userProfile.getRecommendedEffort(calIn);
            Color advColor = rec == Effort.HIGH ? RED : rec == Effort.MEDIUM ? ORANGE : GREEN;
            dashWorkoutAdvice.setForeground(advColor);
        }
    }

    private void refreshWorkoutAdvice()
    {
        // Pre-select effort in workout tab based on profile recommendation
        if (userProfile.isProfileSet() && effortCombo != null)
        {
            Effort rec = userProfile.getRecommendedEffort(food.getTotalCalories());
            effortCombo.setSelectedItem(rec);
        }
    }

   
    //  FOOD PANEL
    private JPanel buildFoodPanel()
    {
        JPanel p = darkPanel();
        p.setLayout(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(32, 36, 32, 36));
        p.add(sectionTitle("🍽  Food Log"), BorderLayout.NORTH);

        //Input card
        JPanel inputCard = card();
        inputCard.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));

        inputCard.add(muted("Food:"));
        foodCombo = new JComboBox<>(FoodList.values());
        style(foodCombo);
        foodCombo.setPreferredSize(new Dimension(210, 30));
        inputCard.add(foodCombo);

        inputCard.add(muted("Amount (g):"));
        foodAmountField = styledField(70);
        inputCard.add(foodAmountField);

        JButton addBtn = accentButton("+ Add", GREEN);
        JButton remBtn = accentButton("− Remove", RED);
        inputCard.add(addBtn);
        inputCard.add(remBtn);

        addBtn.addActionListener(e -> doAddFood());
        remBtn.addActionListener(e -> doRemoveFood());

        // ── Table ────────────────────────────────────────────────────────────
        String[] cols = {"Food", "Calories / g", "Amount (g)", "Total kcal"};
        foodTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        foodTable = new JTable(foodTableModel);
        styleTable(foodTable);
        JScrollPane scroll = darkScroll(foodTable);

        // ── Footer ───────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        totalCalLabel = new JLabel("Total: 0.0 kcal");
        totalCalLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        totalCalLabel.setForeground(ORANGE);
        footer.add(totalCalLabel);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(inputCard, BorderLayout.NORTH);
        center.add(scroll,    BorderLayout.CENTER);
        center.add(footer,    BorderLayout.SOUTH);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void doAddFood()
    {
        double amount = parseAmount(foodAmountField);
        if (Double.isNaN(amount)) return;
        FoodList selected = (FoodList) foodCombo.getSelectedItem();
        food.selectFood(selected, amount);
        foodAmountField.setText("");
        refreshFoodTable();
    }

    private void doRemoveFood()
    {
        double amount = parseAmount(foodAmountField);
        if (Double.isNaN(amount)) return;
        FoodList selected = (FoodList) foodCombo.getSelectedItem();
        if (!food.removeFood(selected, amount))
            JOptionPane.showMessageDialog(this,
                "Cannot remove: amount exceeds what's logged (or none found).",
                "Remove Failed", JOptionPane.WARNING_MESSAGE);
        foodAmountField.setText("");
        refreshFoodTable();
    }

    private void refreshFoodTable()
    {
        foodTableModel.setRowCount(0);
        for (Map.Entry<FoodList, Double> e : food.getAllFood().entrySet())
        {
            FoodList f   = e.getKey();
            double grams = e.getValue();
            double total = Math.round(f.getCaloriesPerGram() * grams * 10.0) / 10.0;
            foodTableModel.addRow(new Object[]{
                f.name(),
                f.getCaloriesPerGram() + " cal/g",
                grams + " g",
                total + " kcal"
            });
        }
        totalCalLabel.setText("Total: " + food.getTotalCalories() + " kcal");
    }

    //Workout Panel

    private JPanel buildWorkoutPanel()
    {
        JPanel p = darkPanel();
        p.setLayout(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(32, 36, 32, 36));
        p.add(sectionTitle("🏋  Workout"), BorderLayout.NORTH);

        //Filter card
        JPanel filterCard = card();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));

        filterCard.add(muted("Effort:"));
        effortCombo = new JComboBox<>(Effort.values());
        style(effortCombo);
        effortCombo.setPreferredSize(new Dimension(110, 30));
        filterCard.add(effortCombo);

        filterCard.add(muted("Time (min):"));
        timeSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 180, 5));
        timeSpinner.setPreferredSize(new Dimension(70, 30));
        styleSpinner(timeSpinner);
        filterCard.add(timeSpinner);

        JButton suggestBtn = accentButton("🔍 Find Workouts", ACCENT);
        filterCard.add(suggestBtn);

        //Suggestion list
        suggestionList = new JList<>();
        suggestionList.setBackground(CARD);
        suggestionList.setForeground(TEXT);
        suggestionList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        suggestionList.setSelectionBackground(new Color(31, 78, 120));
        suggestionList.setSelectionForeground(TEXT);
        suggestionList.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane listScroll = darkScroll(suggestionList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER), " Suggested Workouts ",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.PLAIN, 11), MUTED));

        //Log card
        JPanel logCard = card();
        logCard.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));
        logCard.add(muted("Minutes done:"));
        logMinField = styledField(60);
        logCard.add(logMinField);
        JButton logBtn = accentButton("✔ Log Workout", GREEN);
        logCard.add(logBtn);
        logCard.add(Box.createHorizontalStrut(20));
        burnedLabel = new JLabel("Last: — kcal burned");
        burnedLabel.setForeground(GREEN);
        burnedLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logCard.add(burnedLabel);

        JPanel bottomRow = new JPanel(new BorderLayout(0, 8));
        bottomRow.setOpaque(false);
        totalBurnedLabel = new JLabel("Total burned: 0.0 kcal");
        totalBurnedLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        totalBurnedLabel.setForeground(GREEN);
        bottomRow.add(logCard,          BorderLayout.CENTER);
        bottomRow.add(totalBurnedLabel, BorderLayout.SOUTH);

        suggestBtn.addActionListener(e -> doSuggest());
        logBtn.addActionListener(e -> doLog());

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(filterCard, BorderLayout.NORTH);
        center.add(listScroll, BorderLayout.CENTER);
        center.add(bottomRow,  BorderLayout.SOUTH);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void doSuggest()
    {
        workout.setEffort((Effort) effortCombo.getSelectedItem());
        workout.setTime((Integer) timeSpinner.getValue());
        List<WorkoutList> list = workout.getSuggestedWorkouts();
        if (list.isEmpty())
        {
            suggestionList.setListData(new WorkoutList[]{});
            JOptionPane.showMessageDialog(this,
                "No workouts match your filters. Try more time or lower effort.",
                "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            suggestionList.setListData(list.toArray(new WorkoutList[0]));
            suggestionList.setSelectedIndex(0);
        }
    }

    private void doLog()
    {
        WorkoutList selected = suggestionList.getSelectedValue();
        if (selected == null)
        {
            JOptionPane.showMessageDialog(this,
                "Please search and select a workout first.", "No Workout", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String minText = logMinField.getText().trim();
        int minutes;
        try { minutes = Integer.parseInt(minText); }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Enter a valid number of minutes.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (minutes <= 0)
        {
            JOptionPane.showMessageDialog(this, "Minutes must be greater than 0.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double burned = workout.logWorkout(selected, minutes);
        burnedLabel.setText("Last: " + burned + " kcal burned");
        totalBurnedLabel.setText("Total burned: " + workout.getTotalCaloriesBurned() + " kcal");
        logMinField.setText("");
    }

    
    
    
    //Rank Panel
    private JPanel buildRankPanel()
    {
        JPanel p = darkPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(32, 36, 32, 36));
        p.add(sectionTitle("🏆  Fitness Rank"), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(30, 0, 0, 0));

        rankEmojiLabel = new JLabel("🌱");
        rankEmojiLabel.setFont(new Font("SansSerif", Font.PLAIN, 72));
        rankEmojiLabel.setAlignmentX(CENTER_ALIGNMENT);
        center.add(rankEmojiLabel);
        center.add(Box.createVerticalStrut(12));

        rankLabel = new JLabel("Beginner");
        rankLabel.setFont(new Font("Georgia", Font.BOLD, 42));
        rankLabel.setForeground(TEXT);
        rankLabel.setAlignmentX(CENTER_ALIGNMENT);
        center.add(rankLabel);
        center.add(Box.createVerticalStrut(8));

        pointsLabel = new JLabel("0 points");
        pointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pointsLabel.setForeground(MUTED);
        pointsLabel.setAlignmentX(CENTER_ALIGNMENT);
        center.add(pointsLabel);
        center.add(Box.createVerticalStrut(24));

        // Progress bar
        rankBar = new JProgressBar(0, 100);
        rankBar.setValue(0);
        rankBar.setStringPainted(true);
        rankBar.setString("0% to next tier");
        rankBar.setForeground(ACCENT);
        rankBar.setBackground(BORDER);
        rankBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rankBar.setMaximumSize(new Dimension(420, 18));
        rankBar.setAlignmentX(CENTER_ALIGNMENT);
        center.add(rankBar);
        center.add(Box.createVerticalStrut(20));

        motivLabel = new JLabel("Keep going — every step counts!");
        motivLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
        motivLabel.setForeground(MUTED);
        motivLabel.setAlignmentX(CENTER_ALIGNMENT);
        center.add(motivLabel);
        center.add(Box.createVerticalStrut(32));

        // Tier legend
        JPanel tiers = new JPanel(new GridLayout(1, 4, 12, 0));
        tiers.setOpaque(false);
        tiers.setMaximumSize(new Dimension(560, 70));
        tiers.setAlignmentX(CENTER_ALIGNMENT);

        addTier(tiers, "🌱", "Beginner",      "0–99 pts",    MUTED);
        addTier(tiers, "🥈", "Intermediate",  "100–299 pts", new Color(192,192,192));
        addTier(tiers, "🥇", "Advanced",      "300–599 pts", ORANGE);
        addTier(tiers, "🏆", "Elite",         "600+ pts",    new Color(255,215,0));
        center.add(tiers);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void addTier(JPanel parent, String emoji, String name, String range, Color col)
    {
        JPanel t = new JPanel();
        t.setBackground(CARD);
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(10, 6, 10, 6)
        ));
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));

        JLabel e = new JLabel(emoji);
        e.setFont(new Font("SansSerif", Font.PLAIN, 24));
        e.setAlignmentX(CENTER_ALIGNMENT);
        t.add(e);

        JLabel n = new JLabel(name);
        n.setFont(new Font("SansSerif", Font.BOLD, 12));
        n.setForeground(col);
        n.setAlignmentX(CENTER_ALIGNMENT);
        t.add(n);

        JLabel r = new JLabel(range);
        r.setFont(new Font("SansSerif", Font.PLAIN, 10));
        r.setForeground(MUTED);
        r.setAlignmentX(CENTER_ALIGNMENT);
        t.add(r);

        parent.add(t);
    }

    private void refreshRank()
    {
        syncRank();
        rankLabel.setText(fitnessRank.calculateRank());
        rankEmojiLabel.setText(fitnessRank.getRankEmoji());
        motivLabel.setText(fitnessRank.getMotivation());
        pointsLabel.setText(fitnessRank.getTotalPoints() + " points");
        int pct = fitnessRank.getTierProgress();
        rankBar.setValue(pct);
        rankBar.setString(pct + "% to next tier");
        if (fitnessRank.calculateRank().equals("Elite"))
            rankBar.setString("Max tier reached! 🏆");
    }

    private void syncRank()
    {
        fitnessRank.updatePoints(
            (int) food.getTotalCalories(),
            workout.getWorkoutPoints()
        );
    }

  
    //Ui Helpers

    private JPanel darkPanel()
    {
        JPanel p = new JPanel();
        p.setBackground(BG);
        return p;
    }

    private JPanel card()
    {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return p;
    }

    private JLabel sectionTitle(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Georgia", Font.BOLD, 22));
        l.setForeground(TEXT);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

    private JLabel muted(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(MUTED);
        return l;
    }

    private JTextField styledField(int width)
    {
        JTextField f = new JTextField();
        f.setBackground(new Color(30, 36, 44));
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(3, 6, 3, 6)
        ));
        f.setPreferredSize(new Dimension(width, 30));
        return f;
    }

    private <T> void style(JComboBox<T> cb)
    {
        cb.setBackground(new Color(30, 36, 44));
        cb.setForeground(TEXT);
        cb.setForeground(Color.BLACK);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private void styleSpinner(JSpinner sp)
    {
        sp.setBackground(new Color(30, 36, 44));
        sp.setForeground(TEXT);
        sp.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setBackground(new Color(30, 36, 44));
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setForeground(TEXT);
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setCaretColor(ACCENT);
    }

    private JButton accentButton(String text, Color color)
    {
        JButton b = new JButton(text)
        {
            @Override protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = color;
                if (getModel().isArmed())
                    base = base.darker();
                else if (getModel().isRollover())
                    base = base.brighter();
                g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(base);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(color);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTable(JTable t)
    {
        t.setBackground(CARD);
        t.setForeground(TEXT);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(31, 78, 120));
        t.setSelectionForeground(TEXT);
        t.getTableHeader().setBackground(new Color(30, 36, 44));
        t.getTableHeader().setForeground(MUTED);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        // Alternate row shading
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected)
                    setBackground(row % 2 == 0 ? CARD : new Color(28, 33, 40));
                setForeground(isSelected ? TEXT : TEXT);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    private JScrollPane darkScroll(Component view)
    {
        JScrollPane sp = new JScrollPane(view,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBackground(CARD);
        sp.getViewport().setBackground(CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        return sp;
    }

    private double parseAmount(JTextField field)
    {
        String text = field.getText().trim();
        if (text.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Amount cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return Double.NaN;
        }
        try
        {
            double v = Double.parseDouble(text);
            if (v <= 0)
            {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return Double.NaN;
            }
            return v;
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return Double.NaN;
        }
    }

    
    
    
    
    
    
    public static void main(String[] args)
    {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::new);
    }
}
