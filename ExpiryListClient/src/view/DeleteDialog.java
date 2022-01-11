package view;

import model.Consumable;
import model.DrinkStatistics;
import model.FoodStatistics;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * +
 * Creates a dialog from scratch for deleting an item
 */
public class DeleteDialog extends JDialog {
    private static JDialog d;
    ArrayList<Consumable> listOfConsumables;

    public DeleteDialog(Frame f, ArrayList<Consumable> consumableArrayList) {
        super(f, true);

        listOfConsumables = consumableArrayList;
        d = new JDialog(f, "Delete Item", true);
        d.setLayout(new BorderLayout());
        //Splits the frame into different top, center, and bottom layouts
        JPanel top = new JPanel();
        d.add(top, BorderLayout.NORTH);
        top.setLayout(new FlowLayout());

        JPanel center = new JPanel();
        d.add(center, BorderLayout.CENTER);
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));

        JPanel bottom = new JPanel();
        d.add(bottom, BorderLayout.SOUTH);
        bottom.setLayout(new FlowLayout());

        //Sets up the top layout with the title
        JLabel title = new JLabel("Delete Item");
        title.setFont(new Font("Roboto Light", Font.PLAIN, 24));
        top.add(title);

        // Creates a JList and uses DefaultListModel to populate it
        JList listItems = new JList();
        DefaultListModel fullListModel = new DefaultListModel();
        JTextField itemIndex = new JTextField(10);
        listItems.setModel(fullListModel);
        fullListModel.removeAllElements();
        sortConsumables();
        int counter = 1;
        for (Consumable c : listOfConsumables) {
            if (c instanceof FoodStatistics) {
                fullListModel.addElement("<html><b>Item # " + counter + " (Food)</b><br>" + c);
            } else {
                fullListModel.addElement("<html><b>Item # " + counter + " (Drink)</b><br>" + c);
            }
            ++counter;
        }
        if (counter == 1) {
            fullListModel.addElement("No Items to show.");
        }

        //Aligns the list of elements to the left
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) listItems.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        listItems.setFont(new Font("Roboto Light", Font.PLAIN, 16));

        // Adds the Jlist to a new JScrollPane and sets some border around it
        JScrollPane scrollPane = new JScrollPane(listItems);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        listItems.setFixedCellHeight(155);
        // Add the JScrollPane into the center layout
        center.add(scrollPane);

        // Create a deleteButton and attach a listener to it
        JButton deleteButton = new JButton("Delete Item Number");
        deleteButton.addActionListener(e -> {
            if (itemIndex.getText().matches("[0-9]+") && Integer.parseInt(itemIndex.getText()) <= listOfConsumables.size() && !Objects.equals(itemIndex.getText(), "0")) {
                // get the id number from the consumable
                long id = 0;
                Consumable item = listOfConsumables.get(Integer.parseInt(itemIndex.getText()) - 1);
                if (item instanceof FoodStatistics) {
                    id = ((FoodStatistics) item).getId();
                } else {
                    id = ((DrinkStatistics) item).getId();
                }

                //Use the id number to remove the item from the server
                URL url = null;
                try {
                    url = new URL("http://localhost:8080/removeItem/" + id);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                HttpURLConnection http = null;
                try {
                    http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Length", "0");

                    http.getResponseCode();
                    http.getResponseMessage();
                    http.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Item removed successfully.");
                d.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Item Number entered.");
            }
        });

        // Add the button and label to the bottom layout
        bottom.add(itemIndex);
        bottom.add(deleteButton);

        //Size and frame and make it visible
        d.setSize(550, 650);
        d.setLocationRelativeTo(null); // centers dialog
        d.setVisible(true);
    }

    // Sorts main ArrayList
    private void sortConsumables() {
        for (int i = listOfConsumables.size() - 1; i > 0; i--) {
            for (int j = i; j > 0; j--) {
                if (listOfConsumables.get(j).compareTo(listOfConsumables.get(j - 1)) == 1) {
                    Collections.swap(listOfConsumables, j, j - 1);
                }
            }
        }
    }
}
