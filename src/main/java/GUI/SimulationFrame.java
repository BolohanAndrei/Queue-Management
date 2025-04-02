package GUI;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame {
    private JFrame frame;

    private JTextField timeLimitField;
    private JTextField clientsField;
    private JTextField serversField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeField;

    private JTextArea outputField;

    private JButton startButton;
    private JButton showPeakHourButton;
    private JButton showAverageWaitingTimeButton;
    private JButton showAverageServiceTimeButton;
    private JButton clearButton;

    private JComboBox<String> choseStrategy;

    private SimulationManager gen;

    public SimulationFrame() {
        frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        timeLimitField = new JTextField(5);
        clientsField = new JTextField(5);
        serversField = new JTextField(5);
        minArrivalTimeField = new JTextField(5);
        maxArrivalTimeField = new JTextField(5);
        minServiceTimeField = new JTextField(5);
        maxServiceTimeField = new JTextField(5);

        choseStrategy = new JComboBox<>(new String[]{"SHORTEST_QUEUE", "SHORTEST_TIME"});

        addInputRow(inputPanel, gbc, 0, "Time Limit:", timeLimitField);
        addInputRow(inputPanel, gbc, 1, "Number of Clients:", clientsField);
        addInputRow(inputPanel, gbc, 2, "Number of Servers:", serversField);
        addInputRow(inputPanel, gbc, 3, "Min Arrival Time:", minArrivalTimeField);
        addInputRow(inputPanel, gbc, 4, "Max Arrival Time:", maxArrivalTimeField);
        addInputRow(inputPanel, gbc, 5, "Min Service Time:", minServiceTimeField);
        addInputRow(inputPanel, gbc, 6, "Max Service Time:", maxServiceTimeField);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Choose Strategy:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        inputPanel.add(choseStrategy, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        startButton = new JButton("Start Simulation");
        showPeakHourButton = new JButton("Show Peak Hour");
        showAverageWaitingTimeButton = new JButton("Show Average Waiting Time");
        showAverageServiceTimeButton = new JButton("Show Average Service Time");
        clearButton = new JButton("Clear Output");

        buttonPanel.add(startButton);
        buttonPanel.add(showPeakHourButton);
        buttonPanel.add(showAverageWaitingTimeButton);
        buttonPanel.add(showAverageServiceTimeButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        outputField = new JTextArea(15, 60);
        outputField.setEditable(false);
        outputField.setMargin(new Insets(5, 5, 5, 5));
        outputField.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(outputField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        frame.add(outputPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    gen = new SimulationManager(SimulationFrame.this);
                    Thread thread = new Thread(gen);
                    thread.start();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        showPeakHourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (gen != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Peak Hour is " + gen.peakHour);
                }
            }
        });

        showAverageWaitingTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (gen != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Average Waiting Time is " + gen.getAverageWaitingTime());
                }
            }
        });

        showAverageServiceTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (gen != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Average Service Time is " + gen.getAverageServiceTime());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                outputField.setText("");
            }
        });
    }

    private void addInputRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField textField) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(textField, gbc);
    }

    public void updateOutput(String text) {
        outputField.append(text + "\n");
    }

    public int getTimeLimit() {
        return Integer.parseInt(timeLimitField.getText());
    }

    public int getNumberOfClients() {
        return Integer.parseInt(clientsField.getText());
    }

    public int getNumberOfServers() {
        return Integer.parseInt(serversField.getText());
    }

    public int getMinArrivalTime() {
        return Integer.parseInt(minArrivalTimeField.getText());
    }

    public int getMaxArrivalTime() {
        return Integer.parseInt(maxArrivalTimeField.getText());
    }

    public int getMinServiceTime() {
        return Integer.parseInt(minServiceTimeField.getText());
    }

    public int getMaxServiceTime() {
        return Integer.parseInt(maxServiceTimeField.getText());
    }

    public String getStrategy() {
        return (String) choseStrategy.getSelectedItem();
    }
}
