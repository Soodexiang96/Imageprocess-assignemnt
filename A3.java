
import java.io.*;
import java.math.BigDecimal;

public class A3 {
    public static void main(String[] args) {
        String file1 = "yoda.raw";
        File file = new File(file1);
        try {
            FileInputStream z = new FileInputStream(file);
            String fileName = file.getName();
            int height = 62;
            int weight = 123;
            
            int[] originalImg = new int[weight * height];
            int[] amountpixel = new int[256];
            
            System.out.println("File Name: " + fileName);

            
            int value;
            int index = 0;
            
            while((value = z.read()) != -1) {
                originalImg[index] = value;
                amountpixel[value]++;
                index++;
            }
            
            
            int[] runsum = new int[256];
            int totalRunningSum = 0;
            
            for (int i = 0; i < amountpixel.length; i++) {
                int acc = 0;
                if (i > 0) {
                    acc = runsum[i - 1];
                }
                totalRunningSum = runsum[i] = acc + amountpixel[i];
          }
            
            
            System.out.println("Total Running Sum: " + totalRunningSum);
            

            double[] nrunsum = new double[256];
            
            for (int i = 0; i < runsum.length; i++) {
                nrunsum[i] = (double) runsum[i] / totalRunningSum;
    }
            

            int[] timesrunsum = new int[256];
            
            System.out.println(" Histogram Equalized Values ");
            System.out.println("Gray-Level   No of Pixel    Run Sum        Normalized             Multiply 255");
            System.out.println("_______________________________________________________________________________");
            
            for (int i = 0; i < nrunsum.length; i++) {
                double multipliedNum = nrunsum[i] * 255;
                timesrunsum[i] = (int)Math.round(multipliedNum);
                
                String space0 = "     |      ";
                String space1 = "         |     ";
                String space2 = "         |     ";
                String space3 = "            |          ";
                
                if (i > 9 && i <= 99) {
                    space0 = "           ";
                } else if (i > 99) {
                    space0 = "          ";
                }
                
                for (int j = 0; j < Integer.toString(amountpixel[i]).length(); j++) {
                    space1 = space1.substring(0, space1.length() - 1);
                }
                
                for (int j = 0; j < Integer.toString(runsum[i]).length(); j++) {
                    space2 = space2.substring(0, space2.length() - 1);
                }
                
                for (int j = 0; j < Double.toString(nrunsum[i]).length(); j++) {
                    space3 = space3.substring(0, space3.length() - 1);
                }
                
                System.out.println(i + space0 + amountpixel[i] + space1 + runsum[i] + space2 + nrunsum[i] + space3 + timesrunsum[i]);
            }
            
            File outputFile = new File("a3.raw");
            FileOutputStream fout = new FileOutputStream(outputFile);
            
            for (int i = 0; i < originalImg.length; i++) {
                fout.write(timesrunsum[originalImg[i]]);
            }
            
            fout.flush();
            fout.close(); 
            z.close(); 
        } catch (Exception e) {
        }
    }
    
    
}