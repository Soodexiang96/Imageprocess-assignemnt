
import java.io.*;


public class A5 {

    public static void main(String[] args) {
        String file1 = "yoda.raw";
        File file = new File(file1);
        try {
            FileInputStream z = new FileInputStream(file);
            String fileName = file.getName();
            System.out.println("File Name: " + fileName);
            int weight = 123;
            int height = 62;
            int value;
            int[][] imagearray = new int[height][weight];
            int[][] passfilterarray = new int[height][weight];

            

            int col = 0;
            int row = 0;

            int[][] matricsl = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};

            int[][] matricsh = {
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}};

            while ((value = z.read()) != -1) {
                imagearray[row][col] = value;

                if (col == weight - 1) {
                    col = 0;
                    row++;
                } else if (row < height) {
                    col++;
                }
            }

            File outputFile_lowpass = new File("a5lowpass.raw");
            File outputFile_highpass = new File("a5highpass.raw");
            FileOutputStream output_lowpass = new FileOutputStream(outputFile_lowpass);
            FileOutputStream output_highpass = new FileOutputStream(outputFile_highpass);

            for (int rowIndex = 1; rowIndex < imagearray.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < imagearray[rowIndex].length - 2; colIndex++) {
                    int k1 = calulateKernelNum(matricsl[0][0], imagearray[rowIndex + 1][colIndex + 1], true);
                    int k2 = calulateKernelNum(matricsl[0][1], imagearray[rowIndex + 1][colIndex], true);
                    int k3 = calulateKernelNum(matricsl[0][2], imagearray[rowIndex + 1][colIndex - 1], true);
                    int k4 = calulateKernelNum(matricsl[1][0], imagearray[rowIndex][colIndex + 1], true);
                    int k5 = calulateKernelNum(matricsl[1][1], imagearray[rowIndex][colIndex], true);
                    int k6 = calulateKernelNum(matricsl[1][2], imagearray[rowIndex][colIndex - 1], true);
                    int k7 = calulateKernelNum(matricsl[2][0], imagearray[rowIndex - 1][colIndex + 1], true);
                    int k8 = calulateKernelNum(matricsl[2][1], imagearray[rowIndex - 1][colIndex], true);
                    int k9 = calulateKernelNum(matricsl[2][2], imagearray[rowIndex - 1][colIndex - 1], true);

                    int sum = k1 + k2 + k3 + k4 + k5 + k6 + k7 + k8 + k9;

                    if (sum < 0) {
                        sum = 0;
                    } else if (sum > 255) {
                        sum = 255;
                    }

                    passfilterarray[rowIndex][colIndex] = sum;

                }
            }

            for (int[] img : passfilterarray) {
                for (int i = 0; i < img.length; i++) {
                    output_lowpass.write(img[i]);
                }
            }

            for (int rowIndex = 1; rowIndex < imagearray.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < imagearray[rowIndex].length - 2; colIndex++) {
                    int k1 = calulateKernelNum(matricsh[0][0], imagearray[rowIndex + 1][colIndex + 1], false);
                    int k2 = calulateKernelNum(matricsh[0][1], imagearray[rowIndex + 1][colIndex], false);
                    int k3 = calulateKernelNum(matricsh[0][2], imagearray[rowIndex + 1][colIndex - 1], false);
                    int k4 = calulateKernelNum(matricsh[1][0], imagearray[rowIndex][colIndex + 1], false);
                    int k5 = calulateKernelNum(matricsh[1][1], imagearray[rowIndex][colIndex], false);
                    int k6 = calulateKernelNum(matricsh[1][2], imagearray[rowIndex][colIndex - 1], false);
                    int k7 = calulateKernelNum(matricsh[2][0], imagearray[rowIndex - 1][colIndex + 1], false);
                    int k8 = calulateKernelNum(matricsh[2][1], imagearray[rowIndex - 1][colIndex], false);
                    int k9 = calulateKernelNum(matricsh[2][2], imagearray[rowIndex - 1][colIndex - 1], false);

                    int sum = k1 + k2 + k3 + k4 + k5 + k6 + k7 + k8 + k9;

                    if (sum < 0) {
                        sum = 0;
                    } else if (sum > 255) {
                        sum = 255;
                    }

                    passfilterarray[rowIndex][colIndex] = sum;

                }
            }

            for (int[] img : passfilterarray) {
                for (int i = 0; i < img.length; i++) {
                  output_highpass.write(img[i]);
                }
            }

            output_lowpass.flush();
            output_lowpass.close(); // close file output stream
            output_highpass.flush();
            output_highpass.close(); // close file output stream
            z.close(); // close file input stream

        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static int checkKernelValue(int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }

        return value;
    }

    private static int calulateKernelNum(int hVal, int fValue, boolean dividable) {
        if (dividable) {
            return Math.round((hVal * fValue) / 9);
        } else {
            return hVal * fValue;
        }
    }
}