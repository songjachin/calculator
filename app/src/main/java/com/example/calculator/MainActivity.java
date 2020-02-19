package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv_problem;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_problem = findViewById(R.id.tv_problem);
        tv_result = findViewById(R.id.tv_result);

        findViewById(R.id.btn_0).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_1).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_2).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_3).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_4).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_5).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_6).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_7).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_8).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_9).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_cal).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_clc).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_dev).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_dot).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_equal).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_minus).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_mul).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_plus).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_left).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.btn_right).setOnClickListener(new MyOnClickListener());
    }

    private String problemText = "";
    private String resultText = "";
    private String lastInput = "";
    private Arith arith = new Arith();

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int btn = view.getId();
            String inputText;
            inputText = ((TextView) view).getText().toString();
            Log.d(TAG, "  onClick: " + btn + " inputText " + inputText);
            switch (view.getId()) {
                case R.id.btn_clc:
                    handleC();
                    break;
                case R.id.btn_0:
                case R.id.btn_1:
                case R.id.btn_2:
                case R.id.btn_3:
                case R.id.btn_4:
                case R.id.btn_5:
                case R.id.btn_6:
                case R.id.btn_7:
                case R.id.btn_8:
                case R.id.btn_9:
                    if (!lastInput.equals(")")) {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    break;
                case R.id.btn_dot://输入是点时需判定
                    if (!lastInput.equals("") && lastIsNum(lastInput) && !numContainsDot(problemText)) //上一个数有小数点，一个数不能有两个小数点
                    {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    break;
                case R.id.btn_left:
                    if (!lastIsNum(lastInput)) {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    break;
                case R.id.btn_right:
                    if (lastIsNum(lastInput)) {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    //
                    break;
                case R.id.btn_minus:
                    if (!lastInput.equals("-")) {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    break;

                case R.id.btn_plus:
                case R.id.btn_dev:
                case R.id.btn_mul:
                    if (problemText.length() < 1) break;
                    if (!lastInput.equals("(") && !lastInput.equals("+") && !lastInput.equals("-") && !lastInput.equals("÷") && !lastInput.equals("×")) {
                        problemText = problemText + inputText;
                        tv_problem.setText(problemText);
                        lastInput = inputText;
                    }
                    break;
                case R.id.btn_equal:
                    if (lastInput.equals("=")) {
                        problemText = resultText;
                        tv_problem.setText(problemText);
                        resultText = "";
                        tv_result.setText(resultText);
                        lastInput = problemText.substring(problemText.length() - 1);
                    } else {
                            resultText = arith.calculate(problemText);
                            tv_result.setText(resultText);
                            lastInput = inputText;
                    }
                    break;
                case R.id.btn_cal:
                    if (problemText.length() <= 1) handleC();
                    if (problemText.length() > 1) {
                        handleBack();
                        Log.d(TAG, "onClick: lastinput " + lastInput);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void handleC() {
        problemText = "";
        resultText = "";
        lastInput = "";
        tv_problem.setText(problemText);
        tv_result.setText(resultText);
    }

    private void handleBack() {
        int i = problemText.length();
        problemText = problemText.substring(0, i - 1);
        tv_problem.setText(problemText);
        lastInput = problemText.substring(i - 1);
    }


    private boolean numContainsDot(String str) {
        if (str.length() == 0) return false;
        int p = str.length() - 1;
        char c = str.charAt(p);
        while (c != '+' && c != '-' && c != '÷' && c != '×' && p > 0) {
            p--;
            c = str.charAt(p);
        }
        return str.substring(p, str.length() - 1).contains(".");
    }

    private boolean lastIsNum(String last) {
        if (last == null) return false;
        char c = last.charAt(0);
        boolean b = c <= '9' && c >= '0';
        return b;
    }
}
