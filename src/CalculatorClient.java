import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CalculatorClient extends JFrame implements ActionListener {

    private static final int FRAME_WIDTH = 250;
    private static final int FRAME_HEIGHT = 250;
    private static final int FRAME_X = 150;
    private static final int FRAME_Y = 100;

    private JPanel buttonPanel;
    private JPanel inputOutputPanel;

    private JTextField info;

    private boolean editable = true;

    public CalculatorClient(String title) throws HeadlessException {
        super(title);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));
        //for padding
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputOutputPanel = new JPanel();
        inputOutputPanel.setLayout(new FlowLayout());
        //for padding
        inputOutputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setTitle("Socket Calculator");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setLocation(FRAME_X, FRAME_Y);

        info = new JTextField();
        info.setFont(new Font("SansSerif", Font.PLAIN, 16));
        info.setBackground(Color.white);
        info.setBorder(BorderFactory.createLineBorder(Color.black));
        info.setPreferredSize(new Dimension(230, 35));
        info.addActionListener(this);
        inputOutputPanel.add(info);
        contentPane.add(inputOutputPanel);

        //buttons in calculator
        String buttons[] = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+",};

        //adding buttons for buttons array
        for (String i : buttons) {
            JButton button = new JButton(i);
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        contentPane.add(buttonPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addInput(String line) {
        //appending pressed keys to TextField
        //editable will be false if result is displayed
        if (editable) {
            info.setText(info.getText() + line);
        }
        //result is on the screen
        //editable is false
        //no appending
        //we will add the text
        else {
            info.setText(line);
            editable = true;
        }
    }

    public void addOutput() {

        System.out.println("welcome client");
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1111);

            System.out.println("Client connected");
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            Calculator calcule = new Calculator(info.getText());
            os.writeObject(calcule);
            System.out.println("Object sent ...");
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            Calculator returnMessage = (Calculator) is.readObject();
            info.setText(returnMessage.getResult());
            socket.close();
            editable = false;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CalculatorClient("Socket Calculator").setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //source is from button
        if (e.getSource() instanceof JButton) {
            JButton clickedButton = (JButton) e.getSource();
            //if = is clicked then result will be displayed
            if (clickedButton.getText().equals("=")) {
                addOutput();
            }
            //else we will take input
            else {
                addInput(clickedButton.getText());
            }
        }
        //source is from JTextField
        else {
            addOutput();
        }
    }
}
