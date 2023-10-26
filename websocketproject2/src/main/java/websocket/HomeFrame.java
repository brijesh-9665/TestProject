package websocket;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

class HomeFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField tBedNumber, tPatientName, tStatus;
    private JButton btnAdd, btnFind;
    private JTable table;
    public DefaultTableModel dtm;
    private JScrollPane jsp;
    public ClientManager client;
    private Session session;
    private URI uri;
    private boolean isButtonPressed = false;

    public HomeFrame() {
        initializeComponent();
        connectToWebSocket();
    }

    private void initializeComponent() {
        setLayout(null);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set Nimbus Look and Feel for better appearance (if available)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Color bgColor = new Color(230, 240, 250);
        getContentPane().setBackground(bgColor);

        JLabel labelBedNumber = new JLabel("Bed Number:");
        labelBedNumber.setBounds(30, 30, 90, 20);

        JLabel labelPatientName = new JLabel("Patient Name:");
        labelPatientName.setBounds(30, 80, 90, 20);

        JLabel labelStatus = new JLabel("Status:");
        labelStatus.setBounds(30, 130, 50, 20);

        add(labelBedNumber);
        add(labelPatientName);
        add(labelStatus);

        // Text Fields
        tBedNumber = new JTextField();
        tBedNumber.setBounds(140, 30, 200, 30);

        tPatientName = new JTextField();
        tPatientName.setBounds(140, 80, 200, 30);

        tStatus = new JTextField();
        tStatus.setBounds(140, 130, 200, 30);

        add(tBedNumber);
        add(tPatientName);
        add(tStatus);

        // Buttons
        btnAdd = new JButton("Add");
        btnAdd.setBounds(30, 180, 100, 30);

        btnFind = new JButton("Find");
        btnFind.setBounds(150, 180, 100, 30);

        add(btnAdd);
        add(btnFind);

        Font font = new Font("Arial", Font.PLAIN, 14);
        labelBedNumber.setFont(font);
        labelPatientName.setFont(font);
        labelStatus.setFont(font);
        tBedNumber.setFont(font);
        tPatientName.setFont(font);
        tStatus.setFont(font);
        btnAdd.setFont(font);
        btnFind.setFont(font);

        Color backgroundColor = new Color(240, 240, 240);
        tBedNumber.setBackground(backgroundColor);
        tPatientName.setBackground(backgroundColor);
        tStatus.setBackground(backgroundColor);
        btnAdd.setBackground(new Color(0, 123, 255));
        btnFind.setBackground(new Color(0, 123, 255));

        Color foregroundColor = new Color(60, 60, 60);
        labelBedNumber.setForeground(foregroundColor);
        labelPatientName.setForeground(foregroundColor);
        labelStatus.setForeground(foregroundColor);
        tBedNumber.setForeground(foregroundColor);
        tPatientName.setForeground(foregroundColor);
        tStatus.setForeground(foregroundColor);
        btnAdd.setForeground(Color.WHITE);
        btnFind.setForeground(Color.WHITE);

        dtm = new DefaultTableModel();
        dtm.addColumn("Bed Number");
        dtm.addColumn("Patient Name");
        dtm.addColumn("Status");
        dtm.addColumn("Status Image");

        table = new JTable(dtm);
        jsp = new JScrollPane(table);
        jsp.setBounds(300, 30, 450, 200);
        add(jsp);

        setVisible(true);

     // Inside the ActionListener for the "Add" button
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isButtonPressed = true;
                Bed bed = new Bed();
                bed.setBedNumber(Integer.parseInt(tBedNumber.getText()));
                bed.setPatientName(tPatientName.getText());
                bed.setStatus(tStatus.getText());
                bed.setAvailability(true); // or false based on your logic
                try {
                    session.getBasicRemote().sendObject(bed);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isButtonPressed = false;
            }
        });



        btnFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isButtonPressed = true;
                String inputBedNumber = tBedNumber.getText();

                for (int row = 0; row < dtm.getRowCount(); row++) {
                    String tableBedNumber = dtm.getValueAt(row, 0).toString();
                    if (tableBedNumber.equals(inputBedNumber)) {
                        tPatientName.setText(dtm.getValueAt(row, 1).toString());
                        tStatus.setText(dtm.getValueAt(row, 2).toString());
                        break;
                    }
                }
                isButtonPressed = false;
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && !isButtonPressed) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String selectedBedNumber = dtm.getValueAt(selectedRow, 0).toString();
                        String selectedPatientName = dtm.getValueAt(selectedRow, 1).toString();
                        String selectedStatus = dtm.getValueAt(selectedRow, 2).toString();

                        tBedNumber.setText(selectedBedNumber);
                        tPatientName.setText(selectedPatientName);
                        tStatus.setText(selectedStatus);
                    }
                }
            }
        });
    }

    private void connectToWebSocket() {
        try {
            BedTrackingClientEndpoint chatClientEndPoint = new BedTrackingClientEndpoint();
            chatClientEndPoint.addMessageListener(new BedMessageListener() {
                public void actionPerformed(Bed bed) {
                	bed.setStatusImagePath(bed.isAvailability() ? "image/checkmark.png" : "image/crossmark.png");
                	dtm.addRow(new Object[] { bed.getBedNumber(), bed.getPatientName(), bed.getStatus(), bed.getStatusImagePath() });
                }
                }
            );

            uri = new URI("ws://localhost:8026/folder/bed");
            client = ClientManager.createClient();
            session = client.connectToServer(chatClientEndPoint, uri);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
