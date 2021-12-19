package ca.cmpt213.a4.view;

import ca.cmpt213.a4.model.Consumable;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * +
 * Creates a dialog from scratch for adding an item and adds the item to a server
 */
class AddDialog extends JDialog {
    private static JDialog dialog;

    public AddDialog(Frame f, ArrayList<Consumable> consumableArrayList) {
        super(f, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog = new JDialog(f, "Add Item", true);
        //Splits the frame into different top, center, and bottom layouts
        dialog.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        dialog.add(top, BorderLayout.NORTH);
        top.setLayout(new FlowLayout());

        JPanel center = new JPanel();
        dialog.add(center, BorderLayout.CENTER);
        center.setLayout(new GridLayout(6, 2, 40, 10));

        JPanel bottom = new JPanel();
        dialog.add(bottom, BorderLayout.SOUTH);
        bottom.setLayout(new FlowLayout());

        // Add a title to the top layout
        JLabel title = new JLabel("Add Item");
        title.setFont(new Font("Roboto Light", Font.PLAIN, 24));
        top.add(title);

        //Add labels, JTextFiels, JComboBox, and Date Picker in the center layout
        JLabel typeLabel = new JLabel("Type:", SwingConstants.RIGHT);
        typeLabel.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(typeLabel);

        String[] consumableTypes = {"Food", "Drink"};
        JComboBox type = new JComboBox(consumableTypes);
        center.add(type);

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        nameLabel.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(nameLabel);

        JTextField nameInput = new JTextField(10);
        center.add(nameInput);

        JLabel notesLabel = new JLabel("Notes:", SwingConstants.RIGHT);
        notesLabel.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(notesLabel);

        JTextField notesInput = new JTextField(10);
        center.add(notesInput);

        JLabel priceLabel = new JLabel("Price:", SwingConstants.RIGHT);
        priceLabel.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(priceLabel);

        JTextField priceInput = new JTextField(10);
        center.add(priceInput);

        JLabel weightVolume = new JLabel("Weight:", SwingConstants.RIGHT);
        weightVolume.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(weightVolume);

        JTextField weightVolumeInput = new JTextField(10);
        center.add(weightVolumeInput);

        // Add a listener to the type JComboBox to change the JLabel to Weight or Volume
        type.addActionListener(e -> {
            if (String.valueOf(type.getSelectedItem()).equals("Food")) {
                weightVolume.setText("Weight");
            } else {
                weightVolume.setText("Volume");
            }
        });


        JLabel expiryDate = new JLabel("Expiry Date:", SwingConstants.RIGHT);
        expiryDate.setFont(new Font("Roboto Light", Font.PLAIN, 18));
        center.add(expiryDate);

        DatePicker date = new DatePicker();
        center.add(date);

        //Add the addButton at the bottom layout
        JButton addButton = new JButton("Add Item");
        // Listener makes sure all fields are valid
        addButton.addActionListener(e -> {
            boolean error = false;
            if (!nameInput.getText().matches("[a-zA-Z0-9.]+")) {
                JOptionPane.showMessageDialog(null, "Invalid name entered.");
                error = true;
            } else if (!priceInput.getText().matches("[0-9.]+")) {
                JOptionPane.showMessageDialog(null, "Invalid price entered.");
                error = true;
            } else if ((!weightVolumeInput.getText().matches("[0-9.]+"))) {
                JOptionPane.showMessageDialog(null, "Invalid weight entered.");
                error = true;
            }
            try {
                date.getDate().getMonth();
                date.getDate().getDayOfMonth();
                date.getDate().getYear();
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null, "Invalid date entered.");
                error = true;
            }

            //If no errors found, create a Consumable with the ConsumableFactory
            if (!error) {
                String consumableType = String.valueOf(type.getSelectedItem());
                String nameCurl = "\"" + nameInput.getText() + "\"";
                String notesCurl = "\"" + notesInput.getText() + "\"";

                double price = Math.round((Double.parseDouble(priceInput.getText())) * 100.0) / 100.0;
                String priceCurl = "\"" + price + "\"";

                double weight_volume = (double) (Math.round((Double.parseDouble(weightVolumeInput.getText())) * 100.0) / 100.0);
                String weightVolumeCurl = "\"" + weight_volume + "\"";

                LocalDateTime newExpiry = LocalDateTime.of(date.getDate().getYear(), date.getDate().getMonth(), date.getDate().getDayOfMonth(), 1, 1);
                String newExpiryCurl = "\"" + newExpiry + "\"";

                //Call addFood or addDrink to add the consumable to the server
                URL url = null;
                try {
                    if (consumableType.equals("Food")) {
                        url = new URL("http://localhost:8080/addFood");
                    } else {
                        url = new URL("http://localhost:8080/addDrink");
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                HttpURLConnection http;
                try {
                    http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setRequestProperty("Content-Type", "application/json");

                    String data = "{\"name\": " + nameCurl +
                            ",\"notes\": " + notesCurl +
                            ",\"price\": " + priceCurl +
                            ",\"expiry\": " + newExpiryCurl +
                            ",\"weight\": " + weightVolumeCurl + "}";

                    byte[] out = data.getBytes(StandardCharsets.UTF_8);

                    OutputStream stream = http.getOutputStream();
                    stream.write(out);
                    System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
                    http.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "Added the new " + consumableType + " item.");
                dialog.setVisible(false);
            }
        });

        bottom.add(new JLabel("Click button to add item."));
        bottom.add(addButton);

        //Size the dialog and make it visible
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(null); // centers to screen
        dialog.setVisible(true);
    }
}
