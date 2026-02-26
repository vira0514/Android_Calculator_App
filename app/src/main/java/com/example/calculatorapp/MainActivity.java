package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult, tvFormula;
    private String currentInput = "";
    private double firstNumber = Double.NaN;
    private String operator = "";
    private boolean isNewOp = true;
    private final DecimalFormat df = new DecimalFormat("#.##########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvFormula = findViewById(R.id.tvFormula);
        tvResult.setText("0");

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;
            handleInput(btn.getText().toString());
        };

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot,
                R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv,
                R.id.btnClear, R.id.btnEqual
        };
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void handleInput(String text) {
        switch (text) {
            case "C":
                currentInput = "";
                firstNumber = Double.NaN;
                operator = "";
                isNewOp = true;
                tvResult.setText("0");
                tvFormula.setText("");
                break;

            case "+":
            case "-":
            case "*":
            case "/":
                handleOperation(text);
                break;

            case "=":
                if (!operator.isEmpty() && !currentInput.isEmpty() && !isNewOp) {
                    String formula = df.format(firstNumber) + " " + operator + " " + currentInput + " =";
                    tvFormula.setText(formula);
                    calculate();
                    operator = "";
                }
                break;

            case ".":
                if (isNewOp) {
                    currentInput = "0.";
                    isNewOp = false;
                } else if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
                tvResult.setText(currentInput);
                break;

            default: // Is a number
                if (isNewOp) {
                    currentInput = text;
                    isNewOp = false;
                } else {
                    currentInput += text;
                }
                tvResult.setText(currentInput);
                break;
        }
    }

    private void handleOperation(String newOperator) {
        if (!currentInput.isEmpty() && !isNewOp) {
            if (!Double.isNaN(firstNumber)) {
                calculate();
            }
            firstNumber = Double.parseDouble(currentInput);
            operator = newOperator;
            tvFormula.setText(df.format(firstNumber) + " " + operator);
            isNewOp = true;
        } else if (!Double.isNaN(firstNumber)) {
            operator = newOperator;
            tvFormula.setText(df.format(firstNumber) + " " + operator);
        }
    }

    private void calculate() {
        if (Double.isNaN(firstNumber) || operator.isEmpty() || currentInput.isEmpty()) {
            return;
        }

        double secondNumber = Double.parseDouble(currentInput);
        double result = 0.0;
        boolean hasError = false;

        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "*": result = firstNumber * secondNumber; break;
            case "/":
                if (secondNumber == 0) hasError = true;
                else result = firstNumber / secondNumber;
                break;
        }

        if (hasError) {
            tvResult.setText("Error");
            currentInput = "";
            firstNumber = Double.NaN;
            operator = "";
        } else {
            String resultStr = df.format(result);
            tvResult.setText(resultStr);
            currentInput = resultStr;
            firstNumber = result;
        }
        isNewOp = true;
    }
}
