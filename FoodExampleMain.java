package projectcode;

import javax.swing.*;

public class FoodExampleMain 
{
    private static Food foodManager = new Food();
    private static FitnessRank fitnessRank = new FitnessRank();
    private static JComboBox<FoodList> foodListBox;
    private static JTextField foodAmount;
    private static JTextArea displayArea;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Food Manager");
        frame.setSize(600, 500);

        JLabel amountLabel = new JLabel("Amount (In Grams):");
        amountLabel.setBounds(20, 18, 130, 20);
        frame.add(amountLabel);

        JLabel selectFood = new JLabel("Select Food:");
        selectFood.setBounds(20, 50, 130, 20);
        frame.add(selectFood);

        JLabel foodList = new JLabel("Food List:");
        foodList.setBounds(20, 145, 130, 20);
        frame.add(foodList);

        foodAmount = new JTextField();
        foodAmount.setBounds(140, 20, 80, 20);
        frame.add(foodAmount);

        foodListBox = new JComboBox<>(FoodList.values());
        foodListBox.setBounds(100, 50, 200, 20);
        frame.add(foodListBox);

        JButton addButton = new JButton("Add Food");
        addButton.setBounds(10, 110, 100, 30);
        frame.add(addButton);

        JButton removeButton = new JButton("Remove Food");
        removeButton.setBounds(140, 110, 130, 30);
        frame.add(removeButton);

        // Rank button — new addition
        JButton rankButton = new JButton("Show Rank");
        rankButton.setBounds(290, 110, 120, 30);
        frame.add(rankButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);
        scrollPane.setBounds(10, 170, 560, 270);

        addButton.addActionListener(e -> addFood());
        removeButton.addActionListener(e -> removeFood());
        rankButton.addActionListener(e -> showRank()); // new

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void addFood()
    {
        String amountText = foodAmount.getText();
        if (amountText.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Food amount cannot be empty.");
            return;
        }

        FoodList selectedFood = (FoodList) foodListBox.getSelectedItem();

        double amountSelected;
        try
        {
            amountSelected = Double.parseDouble(amountText);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid input: please enter valid numbers for grams");
            return;
        }

        if (amountSelected < 0)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid input: please enter valid numbers for grams");
            return;
        }

        foodManager.selectFood(selectedFood, amountSelected);
        // update rank points whenever food is added
        fitnessRank.updatePoints((int) foodManager.getTotalCalories(), 0);
        updateDisplayFood();
    }

    private static void removeFood()
    {
        String amountText = foodAmount.getText();
        if (amountText.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Food amount cannot be empty.");
            return;
        }

        FoodList selectedFood = (FoodList) foodListBox.getSelectedItem();

        double amountSelected;
        try
        {
            amountSelected = Double.parseDouble(amountText);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid input: please enter valid numbers for grams");
            return;
        }

        if (amountSelected < 0)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid input: please enter valid numbers for grams");
            return;
        }

        if (!(foodManager.removeFood(selectedFood, amountSelected)))
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid input: The number entered is greater than the current amount (or there is none of that food)");
            return;
        }

        updateDisplayFood();
    }

    // shows rank in the display area
    private static void showRank()
    {
        fitnessRank.updatePoints((int) foodManager.getTotalCalories(), 0);
        displayArea.append("\n--- FITNESS RANK ---\n");
        displayArea.append("Points : " + fitnessRank.getTotalPoints() + "\n");
        displayArea.append("Rank   : " + fitnessRank.calculateRank() + "\n");
    }

    private static void updateDisplayFood()
    {
        displayArea.setText("");
        displayArea.append("Total Calories: " + foodManager.getTotalCalories() + "\n\n");
        for (FoodList food : foodManager.getAllFood().keySet())
        {
            if (food != null)
            {
                double grams = Math.round(foodManager.getAllFood().get(food) * 10.0) / 10.0;
                displayArea.append(food.name() + " (" + grams + " grams)\n");
            }
        }
    }
}
