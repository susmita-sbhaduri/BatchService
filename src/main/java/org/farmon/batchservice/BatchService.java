/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.farmon.batchservice;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import org.farmon.farmonclient.FarmonClient;
import org.farmon.farmondto.FarmonDTO;
import org.farmon.farmondto.ResourceCropDTO;
/**
 *
 * @author sb
 */
public class BatchService {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        FarmonDTO farmondto= new FarmonDTO();
        FarmonClient clientService = new FarmonClient();
        
        farmondto = clientService.callMonthlyAppResRptService(farmondto);
        List<ResourceCropDTO> rescroplist = farmondto.getRescroplist();
    }
    
    public static void writeListToCsv(List<String[]> data, String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                // Join each row's elements separated by commas
                String line = String.join(",", row);
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
