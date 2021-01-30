import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;

public class Calculator implements ActionListener
{
    JFrame frame;
    JTextField textField;
    JButton[] buttons = new JButton[18];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, eqButton, delButton, clrButton, spcButton;
    JPanel panel;

    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

    private final ArrayList<Double> historyList;
    private final static String OPERATORS = "/*+-";
    private double savedResult;
    private double currentValue;

    public Calculator()
    {
        frame = new JFrame("Calculator");
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 520);
        frame.setLayout(null);

        textField = new JTextField();
        textField.setBounds(10, 25, 400, 50);
        textField.setFont(font);
        textField.setEditable(false);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        eqButton = new JButton("=");
        delButton = new JButton("DEL");
        clrButton = new JButton("CLR");
        spcButton = new JButton("SPACE");

        buttons[10] = addButton;
        buttons[11] = subButton;
        buttons[12] = mulButton;
        buttons[13] = divButton;
        buttons[14] = decButton;
        buttons[15] = eqButton;
        buttons[16] = spcButton;

        for (int i = 0; i < 10; i++) {
            buttons[i] = new JButton(String.valueOf(i));
            buttons[i].addActionListener(this);
            buttons[i].setFont(font);
            buttons[i].setFocusable(false);
        }
        for (int i = 10; i < 17; i++) {
            buttons[i].addActionListener(this);
            buttons[i].setFont(font);
            buttons[i].setFocusable(false);
        }

        delButton.addActionListener(this);
        delButton.setFont(font);
        delButton.setFocusable(false);
        delButton.setBounds(215,410,87,50);

        clrButton.addActionListener(this);
        clrButton.setFont(font);
        clrButton.setFocusable(false);
        clrButton.setBounds(312, 410, 87, 50);

        spcButton.setBounds(20, 410, 185, 50);

        panel = new JPanel();
        panel.setBounds(20, 100, 380, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.add(buttons[7]);
        panel.add(buttons[8]);
        panel.add(buttons[9]);
        panel.add(divButton);
        panel.add(buttons[4]);
        panel.add(buttons[5]);
        panel.add(buttons[6]);
        panel.add(mulButton);
        panel.add(buttons[1]);
        panel.add(buttons[2]);
        panel.add(buttons[3]);
        panel.add(subButton);
        panel.add(decButton);
        panel.add(buttons[0]);
        panel.add(addButton);
        panel.add(eqButton);

        frame.add(panel);
        frame.add(spcButton);
        frame.add(delButton);
        frame.add(clrButton);

        frame.add(textField);
        frame.setVisible(true);
        savedResult = 0.0;
        historyList = new ArrayList<>();
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getMemoryValue() {
        return savedResult;
    }

    public void setMemoryValue(double memoryValue) {
        savedResult = memoryValue;
    }

    public void clearMemory() {
        savedResult = 0.0;
    }

    public void addToHistoryList(double memVal) {
        historyList.add(memVal);
    }

    public void printFullHistory() {
        System.out.println(historyList);
    }

    public double getHistoryValue(int index) {
        return historyList.get(index);
    }

    public static void main(String[] args)
    {
        Calculator bruh = new Calculator();
        System.out.println(bruh.evaluate("4+34*2"));
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        String text = textField.getText();
        for (int i = 0; i < 15; i++)
        {
            if (e.getSource() == buttons[i])
                textField.setText(text + e.getActionCommand());
        }
        if (e.getSource() == spcButton)
            textField.setText(text + " ");

        if (e.getSource() == eqButton)
            textField.setText((evaluate(text)));

        if (e.getSource() == clrButton)
            textField.setText("");

        if (e.getSource() == delButton)
            textField.setText(text.substring(0, text.length()-1));
    }

    private boolean isNumber(String exp)
    {
        try {
            Double.parseDouble(exp);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String evaluate(String expression)
    {
        double result = Double.MIN_VALUE;

        if(OPERATORS.indexOf(expression.charAt(0)) != -1 && !Character.isDigit(expression.charAt(1)))
        {
            expression = Double.toString(getMemoryValue())+ " " + expression;
        }

        if(isValid(expression))
        {
            String[] arrOfExp = expression.split(" ");
            int c = 0;
            int numOfTerms = arrOfExp.length;
            String[] arrTemp = new String[3];

            for(int i = 1; i < numOfTerms; i= i+2)
            {
                if(arrOfExp[i].equals("-"))
                {
                    arrOfExp[i] = "+";
                    arrOfExp[i+1] = Double.toString(0.0d - Double.parseDouble(arrOfExp[i+1]));
                }

            }

            while(numOfTerms > 1)
            {
                for(int i = numOfTerms-2 ; i > 0; i--)
                {
                    if(arrOfExp[i].equals(Character.toString(OPERATORS.charAt(c))))
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            arrTemp[j] = arrOfExp[i+j-1];
                        }
                        arrOfExp[i-1] = Double.toString(evalSimple(arrTemp));
                        for(int j = i; j < numOfTerms-2; j++)
                        {
                            arrOfExp[j] = arrOfExp[j+2];
                        }
                        numOfTerms -= 2;
                    }
                }
                c++;
            }
            result = Double.parseDouble(arrOfExp[0]);
        }

        currentValue = result;
        if(result == Double.MIN_VALUE)
        {
            return "INVALID ENTRY";
        }
        return String.valueOf(result);
    }

    private double evalSimple(String[] arrOfExp)
    {
        switch(arrOfExp[1])
        {
            case "/":
                return(Double.parseDouble(arrOfExp[0]) / Double.parseDouble(arrOfExp[2]));

            case "*":
                return(Double.parseDouble(arrOfExp[0]) * Double.parseDouble(arrOfExp[2]));

            case "+":
                return(Double.parseDouble(arrOfExp[0]) + Double.parseDouble(arrOfExp[2]));

            case "-":
                return(Double.parseDouble(arrOfExp[0]) - Double.parseDouble(arrOfExp[2]));
        }
        return Double.MIN_VALUE;
    }

    private boolean isValid(String exp)
    {
        String[] arr = exp.split(" ");
        if(arr.length % 2 != 1 || !Character.isDigit(exp.charAt(exp.length() -1)) || exp.charAt(exp.length()-1) == ' ')
        {
            return false;
        }
        for(int i = 0; i < arr.length; i++)
        {
            if(arr[i].equals("/"))
            {
                if(Float.parseFloat(arr[i+1]) == 0)
                {
                    return false;
                }
            }
            if(i % 2 == 0)
            {
                if(!isNumber(arr[i]))
                {
                    return false;

                }
            } else {
                if(OPERATORS.indexOf(arr[i]) == -1)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
