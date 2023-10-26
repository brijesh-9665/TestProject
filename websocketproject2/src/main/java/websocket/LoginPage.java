package websocket;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField t1;
    private JPasswordField t2;
    private JButton btn;
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private Connection con;
    private ResultSet res;
    private Statement stmt;

    public LoginPage() {
        setTitle("Login Page");
        setLayout(null);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Welcome to MyApp");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(80, 20, 250, 30);
        add(titleLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 80, 20);
        t1 = new JTextField();
        t1.setBounds(140, 80, 150, 30);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 130, 80, 20);
        t2 = new JPasswordField();
        t2.setBounds(140, 130, 150, 30);

        btn = new JButton("Login");
        btn.setBounds(160, 200, 80, 30);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (authenticate(t1.getText(), new String(t2.getPassword()))) {
                    dispose();
                    new HomeFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(usernameLabel);
        add(t1);
        add(passwordLabel);
        add(t2);
        add(btn);

        setConnection(); // Initialize the connection here
        setVisible(true);
    }

    private void setConnection() {
        try {
            String connectUrl = "jdbc:sqlserver://BRIJESH\\SQLEXPRESS;databaseName=StudentDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
            con = DriverManager.getConnection(connectUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean authenticate(String username, String password) {
        boolean result = false;

        try {
            stmt = con.createStatement();
            res = stmt.executeQuery("select * from logindetails");
            while (res.next()) {
                if (username.equals(res.getString(1)) && password.equals(res.getString(2))) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
