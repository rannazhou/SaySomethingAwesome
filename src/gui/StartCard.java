package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Client;

public class StartCard extends JPanel{
    private static final long serialVersionUID = 1L;

    //private final BufferedImage logo;
    private final JLabel logoLabel;// = new JLabel();
    
    private final JLabel createUsernameLabel = new JLabel("Create Username: ", SwingConstants.TRAILING);
    private JTextField createUsernameField = new JTextField(20);
    private final JLabel serverIPLabel = new JLabel("Server IP: ", SwingConstants.TRAILING);
    private final JTextField serverIPField = new JTextField(20);
    private final JLabel portLabel = new JLabel("Port: ", SwingConstants.TRAILING);
    private final JTextField portField = new JTextField(20);
    private final JButton connectButton = new JButton("Connect");
    private final ChatGUI mygui;
    
    public StartCard(final ChatGUI mygui) {
        this.mygui = mygui;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        try {
            logoLabel = new JLabel(new ImageIcon(ImageIO.read(new File("resources/logo.png"))));
        } catch (IOException e) {           
            throw new RuntimeException("Logo file not found.", e);                 
        }
        
//        createUsernameField = null;
//        
//        try {
//            createUsernameField = new JFormattedTextField(new MaskFormatter("AAAAAAAAAAAAAAAAAAAA"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        
        for (Component c: getComponents()){
            ChatGUI.makeNonResizeable(c);
        }
        
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
                
        add(logoLabel, createNewGridBagConstraints(0, 0, 3, GridBagConstraints.CENTER));
        add(createUsernameLabel, createNewGridBagConstraints(0, 1, 1, GridBagConstraints.EAST));
        add(createUsernameField, createNewGridBagConstraints(1, 1, 1, GridBagConstraints.WEST));
        add(serverIPLabel, createNewGridBagConstraints(0, 2, 1, GridBagConstraints.EAST));
        add(serverIPField, createNewGridBagConstraints(1, 2, 1, GridBagConstraints.WEST));
        add(portLabel, createNewGridBagConstraints(0, 3, 1, GridBagConstraints.EAST));
        add(portField, createNewGridBagConstraints(1, 3, 1, GridBagConstraints.WEST));
        add(connectButton, createNewGridBagConstraints(2, 2, 1, GridBagConstraints.CENTER));
        
        final StartCard sc = this;
        connectButton.addActionListener(new ActionListener() {    
            @Override
            public void actionPerformed(ActionEvent ae) {
                String username = createUsernameField.getText();
                String serverIP = serverIPField.getText();
                int port;
                
                if (serverIP.equals("") || username.equals("") || portField.getText().equals("")){
                    JOptionPane.showMessageDialog(sc,
                            "Please fill out all fields.",
                            "Empty Field Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (username.contains(" ")){
                    JOptionPane.showMessageDialog(sc,
                            "Usernames cannot contain spaces. Please try another.",
                            "Invalid username",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    port = Integer.parseInt(portField.getText());
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(sc,
                            "The port number must be an integer.",
                            "Invalid Port",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Client finalClient;
                try {
                    finalClient = Client.connect(serverIP, port, username, mygui);
                } catch (IOException e){
                    JOptionPane.showMessageDialog(sc,
                            "Could not connect to server. Please check your server IP address and port number again.",
                            "Connection error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (finalClient == null) {
                    return;
                }
                mygui.setFinalClient(finalClient);
                mygui.getLayout().next(sc.getParent());
                
            }
        });
    }
    
    
    private static GridBagConstraints createNewGridBagConstraints(int gridx, int gridy, int gridwidth, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.anchor = anchor;
        return gbc;
    }
    
    
}
