package ca.cmpt213.a4.view;

import ca.cmpt213.a4.model.Consumable;
import ca.cmpt213.a4.control.GsonHelper;
import ca.cmpt213.a4.model.FoodStatistics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * +
 * MainGUI which is somewhat made from the UI designer provided by IntelliJ
 * Redirects to other dialogs and displays the itemsLists
 * Also loads the list using ConsumablesTracker
 */
public class MainGUI extends JFrame {
    private JTabbedPane listTabs;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JScrollPane allItemsPane; // Never used fields are needed by the UI designer
    private JScrollPane expiredItemsPane;
    private JScrollPane notExpiredItemsPane;
    private JScrollPane expiringInSevenPane;
    private JPanel panel;
    private JList entireList;
    private JList expiredList;
    private JList nonExpiredList;
    private JList expireSevenList;
    private ArrayList<Consumable> listOfConsumables;
    private DefaultListModel fullListModel;
    private DefaultListModel expiredListModel;
    private DefaultListModel nonExpiredListModel;
    private DefaultListModel expireSevenListModel;

    public MainGUI() {
        //Get listAll from spring boot server
        curlGetCommands(1);
        if (listOfConsumables == null) {
            listOfConsumables = new ArrayList<>();
        }
        setContentPane(panel);
        addWindowListener(new WindowCloser());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Consumables Tracker");
        pack();
        setLocationRelativeTo(null); // Centers the frame
        setVisible(true);

        String[] command = {"curl", "-i", "-X", "GET", "localhost:8080/ping"};

        ProcessBuilder process = new ProcessBuilder(command);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }



        fullListModel = new DefaultListModel();
        expiredListModel = new DefaultListModel();
        nonExpiredListModel = new DefaultListModel();
        expireSevenListModel = new DefaultListModel();

        entireList.setModel(fullListModel);
        expiredList.setModel(expiredListModel);
        nonExpiredList.setModel(nonExpiredListModel);
        expireSevenList.setModel(expireSevenListModel);

        refreshFullList();
        refreshExpiredList();
        refreshNonExpiredList();
        refreshExpiredSevenList();

        // Creates listeners for add and delete and refreshes lists after coming back
        addItemButton.addActionListener(e -> {
            JFrame framer = new JFrame();
            AddDialog dialog = new AddDialog(framer, listOfConsumables);
            refreshFullList();
            refreshExpiredList();
            refreshNonExpiredList();
            refreshExpiredSevenList();
        });
        removeItemButton.addActionListener(e -> {
            JFrame framer = new JFrame();
            curlGetCommands(1);
            DeleteDialog dialog = new DeleteDialog(framer, listOfConsumables);
            refreshFullList();
            refreshExpiredList();
            refreshNonExpiredList();
            refreshExpiredSevenList();
        });
    }

    void curlGetCommands(int i) {
        String ending = "All";
        if (i == 1){
        } else if (i == 2) {
            ending = "Expired";
        } else if (i == 3) {
            ending = "NonExpired";
        } else if (i == 4) {
            ending = "ExpiringIn7Days";
        }
        String[] command = {"curl", "-i", "-X", "GET", "localhost:8080/list" + ending};

        ProcessBuilder process = new ProcessBuilder(command);
        Process p;

        GsonHelper cTracker = new GsonHelper();
        Gson gson = cTracker.getGson();
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String line = null;
            for (int ii = 0; ii < 5; ii ++) {
                line = reader.readLine();
            }

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();

            java.lang.reflect.Type typeConsumable = new TypeToken<ArrayList<Consumable>>() {
            }.getType();

            listOfConsumables = gson.fromJson(result, typeConsumable);
            if (listOfConsumables == null) {
                listOfConsumables = new ArrayList<>();
            }
        } catch (IOException ee) {
            System.out.print("error");
            ee.printStackTrace();
        }
    }

    void refreshFullList() {
        fullListModel.removeAllElements();
        curlGetCommands(1);
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
            fullListModel.addElement("No Items to Show");
        }
    }

    void refreshExpiredList() {
        expiredListModel.removeAllElements();
        curlGetCommands(2);
        sortConsumables();
        int counter = 1;
        for (Consumable c : listOfConsumables) {
            if (c.isExpired()) {
                if (c instanceof FoodStatistics) {
                    expiredListModel.addElement("<html><b>Item # " + counter + " (Food)</b><br>" + c);
                } else {
                    expiredListModel.addElement("<html><b>Item # " + counter + " (Drink)</b><br>" + c);
                }
                ++counter;
            }
        }
        if (counter == 1) {
            expiredListModel.addElement("No Items to Show");
        }
    }

    void refreshNonExpiredList() {
        nonExpiredListModel.removeAllElements();
        curlGetCommands(3);
        sortConsumables();
        int counter = 1;
        for (Consumable c : listOfConsumables) {
            if (!c.isExpired()) {
                if (c instanceof FoodStatistics) {
                    nonExpiredListModel.addElement("<html><b>Item # " + counter + " (Food)</b><br>" + c);
                } else {
                    nonExpiredListModel.addElement("<html><b>Item # " + counter + " (Drink)</b><br>" + c);
                }
                ++counter;
            }
        }
        if (counter == 1) {
            nonExpiredListModel.addElement("No Items to Show");
        }
    }

    void refreshExpiredSevenList() {
        expireSevenListModel.removeAllElements();
        curlGetCommands(4);
        sortConsumables();
        int counter = 1;
        for (Consumable c : listOfConsumables) {
            if (!c.isExpired() && c.daysExpired() <= 7) {
                if (c instanceof FoodStatistics) {
                    expireSevenListModel.addElement("<html><b>Item # " + counter + " (Food)</b><br>" + c);
                } else {
                    expireSevenListModel.addElement("<html><b>Item # " + counter + " (Drink)</b><br>" + c);
                }
                ++counter;
            }
        }
        if (counter == 1) {
            expireSevenListModel.addElement("No Items to Show");
        }
    }

    private void sortConsumables() { //
        if(listOfConsumables == null) {
            listOfConsumables = new ArrayList<>();
            return;
        }
        for (int i = listOfConsumables.size() - 1; i > 0; i--) {
            for (int j = i; j > 0; j--) {
                if (listOfConsumables.get(j).compareTo(listOfConsumables.get(j - 1)) == 1) {
                    Collections.swap(listOfConsumables, j, j - 1);
                }
            }
        }
    }
}

