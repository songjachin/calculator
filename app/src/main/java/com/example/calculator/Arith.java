package com.example.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Stack;

public class Arith {
    public String calculate(String str) {
        if(!containOperator(str)) return str;
        //分割
        String[] Str = segment(str);
        // 中缀转后缀
        String newStr = infToSuf(Str);
        // 后缀计算
        String result = sufToRes(newStr);
        return sufToRes(result);
    }

    private boolean containOperator(String str){
        return str.contains("+") || str.contains("-") || str.contains("×") || str.contains("÷") || str.contains("(") || str.contains(")");
    }
    private static String[] segment(String str) {
        String[] exp = new String[str.length() + 1];
        //找最近的索引并截取字符串
        int len = str.length();
        for (int i = 0; i < len + 1; i++) {
            int index;
            int[] ind = new int[6];
            ind[0] = str.indexOf('+');
            ind[1] = str.indexOf('-');
            ind[2] = str.indexOf('×');
            ind[3] = str.indexOf('÷');
            ind[4] = str.indexOf('(');
            ind[5] = str.indexOf(')');
            if (ind[1] == 0) {//第一个是负号
                Arrays.sort(ind);
                int t;
                for (t = 0; t < 6; t++) {
                    if (ind[t] >= 0)
                        break;
                }
                int r = ind[t + 1];
                exp[i] = str.substring(0, r);
                i++;
                exp[i] = str.substring(r, r + 1);
                str = str.substring(r + 1);
            } else if (((ind[1] - ind[4]) == 1) && (ind[4] == 0)) {//"-("结构
                Arrays.sort(ind);
                int t;
                for (t = 0; t < 6; t++) {
                    if (ind[t] >= 0)
                        break;
                }
                int r = ind[t + 1];
                exp[i] = str.substring(0, 1);
                i++;
                exp[i] = str.substring(1, r + 2);
                i++;
                exp[i] = str.substring(r + 2, r + 3);
                str = str.substring(r + 3);
            } else {
                Arrays.sort(ind);
                int t;
                for (t = 0; t < 6; t++) {
                    if (ind[t] >= 0)
                        break;
                }
                if (t == 6)
                    break;
                index = ind[t];
                if (index != 0) {
                    exp[i] = str.substring(0, index);
                    i++;
                }
                exp[i] = str.substring(index, index + 1);
                str = str.substring(index + 1);
            }
        }
        int j = 0;
        int k = 0;
        for (; exp[j] != null; j++) {
        }
        if (!exp[j - 1].equals(")")) {
            exp[j] = str;
            str = "";
            k = j;
        } else {
            k = j - 1;
        }
        String[] expp = new String[k + 1];
        for (int t = 0; t < k + 1; t++) {
            expp[t] = exp[t];
        }
        return expp;
        //System.out.println("分割的字符串:");
    }
    private static String infToSuf(String[] exp) {
        String newStrs = "";
        //初始化栈
        Stack<String> stack = new Stack<>();
	        /*
	                     判断并放入后缀表达式中：
	            for循环遍历整个str进行判断
	                     循环结束若栈不为空全部出栈
	        */
        int l = exp.length;
        for (int i = 0; i < l; i++) {
            if ((stack.empty()) && (exp[i].equals("+") || exp[i].equals("-") || exp[i].equals("×") || exp[i].equals("÷"))) {
                stack.push(exp[i]);
            } else if (exp[i].equals("(")) {
                stack.push(exp[i]);
            } else if (exp[i].equals("×") || exp[i].equals("÷")) {
                while (stack.peek().equals("×") || stack.peek().equals("÷")) {
                    newStrs = newStrs.concat(stack.pop() + " ");
                    if (stack.isEmpty()) {
                        break;
                    }
                }
                stack.push(exp[i]);
            } else if (exp[i].equals("+") || exp[i].equals("-")) {
                while (!(stack.isEmpty()) && ((stack.peek()).equals("×") || (stack.peek()).equals("÷") || (stack.peek()).equals("+") || (stack.peek()).equals("-"))) {
                    newStrs = newStrs.concat(stack.pop() + " ");
                    if (stack.isEmpty()) {
                        break;
                    }
                }
                stack.push(exp[i]);
            } else if (exp[i].equals(")")) {
                int t = stack.search("(");
                for (int k = 1; k < t; k++) {
                    newStrs = newStrs.concat(stack.pop() + " ");
                }
                String tstr = stack.pop();
            } else {
                newStrs = newStrs.concat(exp[i] + " ");
            }
        }
        while (!stack.empty()) {
            if (!stack.peek().equals("(") || !stack.peek().equals(")")) {
                newStrs = newStrs.concat(stack.pop() + " ");
            } else if (stack.peek().equals("(") || stack.peek().equals(")")) {
                String tstr = stack.pop();
            }
        }
//      System.out.println("后缀:"+newStrs);
        return newStrs;
    }

    private static String sufToRes(String sufStr) {
        String[] exp = sufStr.split(" ");
        Stack<String> stack = new Stack<>();
        String Res = "";
        for (int i = 0; i < exp.length; i++) {
            if (!exp[i].equals("+") && !exp[i].equals("-") && !exp[i].equals("×") && !exp[i].equals("÷")) {
                stack.push(exp[i]);
            } else if (exp[i].equals("+")) {
                BigDecimal b2 = new BigDecimal(stack.pop());
                BigDecimal b1 = new BigDecimal(stack.pop());
                BigDecimal b3 = b1.add(b2);
                stack.push(b3.toString());
            } else if (exp[i].equals("-")) {
                BigDecimal b2 = new BigDecimal(stack.pop());
                BigDecimal b1 = new BigDecimal(stack.pop());
                BigDecimal b3 = b1.subtract(b2);
                stack.push(b3.toString());
            } else if (exp[i].equals("×")) {
                BigDecimal b2 = new BigDecimal(stack.pop());
                BigDecimal b1 = new BigDecimal(stack.pop());
                BigDecimal b3;
                if (b1.compareTo(BigDecimal.ZERO) == 0 || b2.compareTo(BigDecimal.ZERO) == 0) {
                    b3 = BigDecimal.ZERO;
                } else {
                    b3 = b1.multiply(b2);
                }
                stack.push(b3.toString());
            } else if (exp[i].equals("÷")) {
                BigDecimal b2 = new BigDecimal(stack.pop());
                BigDecimal b1 = new BigDecimal(stack.pop());
                BigDecimal b3 ;
                double d1 = b1.doubleValue();
                double d2 = b2.doubleValue();
                if (d1 % d2 == 0) {
                    b3 = (b1.divide(b2));
                    stack.push(b3.toString());
                } else {
                    b3 = b1.divide(b2, 10, RoundingMode.HALF_UP);
                    stack.push(b3.toString());
                }
            }
        }
        Res = stack.pop();
        boolean flag = false;
        for (int m = 0; m < Res.length() - 1; m++) {
            if (Res.charAt(m) == '.') {
                flag = true;
            }
        }
        if (flag) {
            for (int m = Res.length() - 1; m >= 0; m--) {
                if (Res.charAt(m) == '0') {
                } else {
                    Res = Res.substring(0, m + 1);
                    break;
                }
            }
            if (Res.charAt(Res.length() - 1) == '.') {
                Res = Res.substring(0, Res.length() - 1);
            }
        }
        return Res;
    }
}
