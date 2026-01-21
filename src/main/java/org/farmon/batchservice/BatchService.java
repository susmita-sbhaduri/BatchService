/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.farmon.batchservice;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.farmon.farmonclient.FarmonClient;
import org.farmon.farmondto.AllExpenseReportDTO;
import org.farmon.farmondto.BatchExpenseDTO;
import org.farmon.farmondto.ExpenseDTO;
import org.farmon.farmondto.FarmonDTO;
import org.farmon.farmondto.ResourceCropDTO;
/**
 *
 * @author sb
 */
public class BatchService {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        FarmonDTO farmondto= new FarmonDTO();
        FarmonClient clientService = new FarmonClient();
        
        LocalDate today = LocalDate.now();
        int year = today.getYear();              // Gets year as integer, e.g., 2025
        LocalDate prevMonth = today.minusMonths(1);
        DateTimeFormatter mmmFormatter = DateTimeFormatter.ofPattern("MMM");
        String prevMonthMMM = prevMonth.format(mmmFormatter);
        String filePath = "/home/sb/Downloads/expense"+prevMonthMMM+String.valueOf(year)+".csv";
        
        farmondto = clientService.callMonthlyExpRptService(farmondto);        
        List<AllExpenseReportDTO> exprptlist = farmondto.getAllexplist();
        List<String> stringListExp = new ArrayList<>();
        String norecStringExp = "";
        if (!exprptlist.isEmpty()) {            
            for (int i = 0; i < exprptlist.size(); i++) {
                stringListExp.add(convertToCsv(exprptlist.get(i)));
            }
        } else {
            norecStringExp = "no expense record found for this month";
        }
        String headerText = "Category,Name,Date,Cost";       
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            bw.write(headerText);
            bw.newLine();
            // Write each row
            if (stringListExp.isEmpty()) {
                bw.write(norecStringExp);
                bw.newLine();
            } else {
                for (String line : stringListExp) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
    }
    
    public static String convertToCsv(Object obj) {
        StringBuilder csv = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                Object value = fields[i].get(obj);
                if(value != null){
                    csv.append(value);
                    if (i != fields.length - 1) {
                        csv.append(",");
                    }
                }
            } catch (IllegalAccessException e) {
                csv.append("");
            }
            
        }
        return csv.toString();
    }
}
