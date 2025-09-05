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
        
        farmondto = clientService.callMonthlyAppResRptService(farmondto);        
        List<ResourceCropDTO> rescroplist = farmondto.getRescroplist();
        List<String> stringListApp = new ArrayList<>();
        String norecStringApp = "";
        String norecStringExp = "";
        if (!rescroplist.isEmpty()) {            
            for (int i = 0; i < rescroplist.size(); i++) {
                stringListApp.add(convertToCsv(rescroplist.get(i)));
            }
        } else {
            norecStringApp = "no resource application record found for this month";
        }
        
        farmondto = clientService.callMonthlyExpRptService(farmondto);
        List<ExpenseDTO> expenselist = farmondto.getExpenselist(); 
        List<String> stringLstExpense = new ArrayList<>();
        if (!expenselist.isEmpty()) {            
            for (int i = 0; i < expenselist.size(); i++) {
                stringLstExpense.add(convertToCsv(expenselist.get(i)));
            }
        } else {
            norecStringExp = "no expense record found for this month";
        }
        
        String mainHeader = "Applied resource cost";
        String headerText = "Resource,Applied Date,Cost";       
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write main header
            bw.write(mainHeader);
            bw.newLine();
            // Write header
            bw.write(headerText);
            bw.newLine();
            // Write each row
            if (stringListApp.isEmpty()) {
                bw.write(norecStringApp);
                bw.newLine();
            } else {
                for (String line : stringListApp) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.newLine();//next report
            
            mainHeader = "Monthly expense list";
            headerText = "Category,Name,Date,Cost";
            
            // Write main header
            bw.write(mainHeader);
            bw.newLine();
            // Write header
            bw.write(headerText);
            bw.newLine();
            if (stringLstExpense.isEmpty()) {
                bw.write(norecStringExp);
                bw.newLine();
            } else {
                for (String line : stringLstExpense) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
        
//        String temp = convertToCsv(rescroplist.get(0));
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
