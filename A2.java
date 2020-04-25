
import java.io.*;
import java.nio.*;



public class A2 {

    public static void main(String[] args) {
        String filename = "yoda.raw";
        File file = new File(filename);
        try {
            FileInputStream z = new FileInputStream(file);
            File outputFile = new File("a2_patterning.raw");
            FileOutputStream output = new FileOutputStream(outputFile);
            String fileName = file.getName();
            System.out.println("Source File Info");
            System.out.println("File Name: " + fileName);
            System.out.println("Patterning Data");
            int height = 62; 
            int weight = 123;
            int newweight = weight*3;
            int newheight = height*3;
            int totalPatternSeq = newweight * newheight;
            int value;
            int[][] data = new int[newweight][newheight];
            int rowCount = 0;
            int colCount = 0;
            int index = 0;
            while ((value = z.read()) != -1) {
                int[] patterntype = checkpattern(value);
                int patternWriteIndex = 0;

                int rowdata1 = rowCount * 3;
                int rowdata2 = rowdata1 + 1;
                int rowdata3 = rowdata2 + 1;
                int coldata1 = colCount * 3;
                int coldata2 = coldata1 + 1;
                int coldata3 = coldata2 + 1;

                writeData(data, rowdata1, coldata1, patterntype[0]);
                writeData(data, rowdata1, coldata2, patterntype[1]);
                writeData(data, rowdata1, coldata3, patterntype[2]);
                writeData(data, rowdata2, coldata1, patterntype[3]);
                writeData(data, rowdata2, coldata2, patterntype[4]);
                writeData(data, rowdata2, coldata3, patterntype[5]);
                writeData(data, rowdata3, coldata1, patterntype[6]);
                writeData(data, rowdata3, coldata2, patterntype[7]);
                writeData(data, rowdata3, coldata3, patterntype[8]);

                if (rowCount < height && colCount == (weight - 1)) {
                    colCount = 0;
                    rowCount++;
                } else if (rowCount < height && colCount < weight) {
                    colCount++;
                }
                index++;
            }

            int writeCount = 0;

            for (int[] data1 : data) {
                for (int j = 0; j < data1.length; j++) {
                    output.write(data1[j]);
                    writeCount++;
                }
            }

            output.flush();
            output.close();
            z.close();

            System.out.println("Dithering D1 Data");
            FileInputStream z_d1 = new FileInputStream(file);
            File outputFiled1 = new File("a2_dithering.raw");
            FileOutputStream outputd1 = new FileOutputStream(outputFiled1);
            int[] d1Model = {0, 128, 192, 64};
            int[][] d1Data = new int[height][weight];
            index = 0;
            colCount = 0;
            rowCount = 0;
            while ((value = z_d1.read()) != -1) {

                if ((rowCount == 0 || rowCount % 2 == 0) && (colCount == 0 || colCount % 2 == 0)) {
                    
                    d1Data[rowCount][colCount] = value > d1Model[0] ? 255 : 0;
                } else if ((rowCount == 0 || rowCount % 2 == 0) && (colCount + 1) % 2 == 0) {
                    
                    d1Data[rowCount][colCount] = value > d1Model[1] ? 255 : 0;
                } else if (rowCount > 0 && (rowCount + 1) % 2 == 0 && (colCount == 0 || colCount % 2 == 0)) {
                    
                    d1Data[rowCount][colCount] = value > d1Model[2] ? 255 : 0;
                } else if (rowCount > 0 && (rowCount + 1) % 2 == 0) {
                   
                    d1Data[rowCount][colCount] = value > d1Model[3] ? 255 : 0;
                }

                if (colCount == (weight - 1) && rowCount < height) {
                    colCount = 0;
                    rowCount++;
                } else if (colCount < weight && rowCount < height) {
                    colCount++;
                } else {
                    throw new Error("condition error");
                }

                index++;
            }

            for (int[] dataSet : d1Data) {
                for (int i = 0; i < dataSet.length; i++) {
                    outputd1.write(dataSet[i]);
                }
            }

            outputd1.flush();
            outputd1.close();
            z_d1.close();

            System.out.println("Dithering D1 Data");
            FileInputStream z_d2 = new FileInputStream(file);
            File outputFile_d2 = new File("assignment2_d2.raw");
            FileOutputStream output_d2 = new FileOutputStream(outputFile_d2);
            int[][] d2Model = {
                {   0, 128,  32, 160 },
                { 192,  64, 224,  96 },
                {  48, 176,  16, 144 },
                { 240, 112, 208,  80 },
            };
            int[][] d2Data = new int[height][weight];
            index = 0;
            colCount = 0;
            rowCount = 0;
            while ((value = z_d2.read()) != -1) {
                
                int modelRowIndex;
                int modelColIndex;
                
                if ((rowCount + 1) % 4 == 0) {
                    modelRowIndex = 3;
                } else if ((rowCount + 1) % 3 == 0) {
                    modelRowIndex = 2;
                } else if ((rowCount + 1) % 2 == 0) {
                    modelRowIndex = 1;
                } else {
                    modelRowIndex = 0;
                }
                
                if ((colCount + 1) % 4 == 0) {
                    modelColIndex = 3;
                } else if ((colCount + 1) % 3 == 0) {
                    modelColIndex = 2;
                } else if ((colCount + 1) % 2 == 0) {
                    modelColIndex = 1;
                } else {
                    modelColIndex = 0;
                }
                
                d2Data[rowCount][colCount] = value > d2Model[modelRowIndex][modelColIndex] ? 255 : 0;
                  
                if (colCount == (weight - 1) && rowCount < height) {
                    colCount = 0;
                    rowCount++;
                } else if (colCount < weight && rowCount < height) {
                    colCount++;
                } else {
                    throw new Error("condition error");
                }

                index++;
            }

            for (int[] dataSet : d2Data) {
                for (int i = 0; i < dataSet.length; i++) {
                    output_d2.write(dataSet[i]);
                }
            }

            output_d2.flush();
            output_d2.close();
            z_d2.close();

        } catch (IOException ex) {
            System.out.println("File is not exists");
        }
    }

    private static int[] checkpattern(int colorDec) {
        int[] result = new int[9];
        double baseNum = 25.5;

        for (int i = 0; i < 10; i++) { // 10 type pattern
            double start = i * baseNum;
            double end = start + baseNum;
            if (start <= colorDec && colorDec <= end) {
                result = getPatternArr(i);
                break;
            }
        }

        return result;
    }

    private static int[] getPatternArr(int pattern) {
        int[] arr = new int[]{};

        switch (pattern) {
            case 0:
                arr = new int[]{
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 0};
                break;
            case 1:
                arr = new int[]{
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 2:
                arr = new int[]{
                    255, 0, 0,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 3:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 4:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    255, 0, 255};
                break;
            case 5:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    255, 255, 255};
                break;
            case 6:
                arr = new int[]{
                    255, 0, 255,
                    255, 0, 0,
                    255, 255, 255};
                break;
            case 7:
                arr = new int[]{
                    255, 255, 255,
                    255, 0, 0,
                    255, 255, 255};
                break;
            case 8:
                arr = new int[]{
                    255, 255, 255,
                    255, 0, 255,
                    255, 255, 255};
                break;
            case 9:
                arr = new int[]{
                    255, 255, 255,
                    255, 255, 255,
                    255, 255, 255};
                break;
        }

        return arr;
    }

    private static boolean isBetween(int x, int start, int end) {
        return start <= x && x <= end;
    }

    private static void writeData(int[][] data, int x, int y, int value) {
        if (data[x][y] == 0) {
            data[x][y] = value;
        } else {
            throw new Error("the array position has value");
        }
    }
}