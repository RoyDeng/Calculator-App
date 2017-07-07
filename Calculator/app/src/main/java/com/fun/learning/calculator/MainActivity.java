package com.fun.learning.calculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private Context context;
    String txt;
    private TextView txtShow;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        txtShow = (TextView) findViewById(R.id.txtShow);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txt = "";
    }

    public void btn_Click(View v) {
        Button btn = (Button) v;
        txt += btn.getText().toString();
        txtShow.setText(txt);
    }

    public void btn_Back(View v) {
        if (txt.length() > 0) {
            txt = txt.substring(0, txt.length() - 1);
            txtShow.setText(txt);
        }
    }

    public void btn_Clear(View v) {
        txt = "";
        txtShow.setText("");
        txtResult.setText("0");
    }

    public void btn_Equals(View v) {
        String ex = txt;
        txtResult.setText(String.valueOf(infix(ex)));
    }

    public double infix(String expression) {
        expression = expression.replaceAll("[\t\n ]", "") + "=";
        String operator = "*/+-=";

        StringTokenizer tokenizer = new StringTokenizer(expression, operator, true);
        Stack operatorStack = new Stack();
        Stack valueStack = new Stack();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (operator.indexOf(token) < 0)
                valueStack.push(token);
            else
                operatorStack.push(token);
            resolve(valueStack, operatorStack);
        }
        String lastOne = (String)valueStack.pop();
        return Double.parseDouble(lastOne);
    }

    public int getPriority(String op) {
        if (op.equals("*") || op.equals("/"))
            return 1;
        else if (op.equals("+") || op.equals("-"))
            return 2;
        else if (op.equals("="))
            return 3;
        else
            return Integer.MIN_VALUE;
    }

    public void resolve(Stack values, Stack operators) {
        while (operators.size() >= 2) {
            String first = (String)operators.pop();
            String second = (String)operators.pop();
            if (getPriority(first) < getPriority(second)) {
                operators.push(second);
                operators.push(first);
                return;
            }
            else {
                String firstValue = (String) values.pop();
                String secondValue = (String) values.pop();
                values.push(getResults(secondValue, second, firstValue));
                operators.push(first);
            }
        }
    }

    public String getResults(String operand1, String operator, String operand2) {
        System.out.println("Performing " + operand1 + operator + operand2);
        double op1 = Double.parseDouble(operand1);
        double op2 = Double.parseDouble(operand2);
        if(operator.equals("*"))
            return "" + (op1 * op2);
        else if(operator.equals("/"))
            return "" + (op1 / op2);
        else if(operator.equals("+"))
            return "" + (op1 + op2);
        else if(operator.equals("-"))
            return "" + (op1 - op2);
        else
            return null;
    }
}
