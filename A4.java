
import java.io.*;
public class A4 {
    public static void main(String[] args) {
        String file1 = "yoda.raw";

        File file = new File(file1);
        try {
            FileInputStream z = new FileInputStream(file);
            String fileName = file.getName();
            int weight = 123;
            int height = 62;
            int[][] imagearray = new int[height][weight];
            int[][] convolutionarray = new int[height][weight];
            System.out.println("File Name: " + fileName);
            int value;
            int row = 0;
            int col = 0;
            int[][] matrics = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1},};

            while ((value = z.read()) != -1) {         
                imagearray[row][col] = value;

                if (col == weight - 1) {
                    col = 0;
                    row++;
                } else if (row < height) {
                    col++;
                }
            }

            File outputFile = new File("a4.raw");
            FileOutputStream output = new FileOutputStream(outputFile);
            for (int rowIndex = 1; rowIndex < imagearray.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < imagearray[rowIndex].length - 2; colIndex++) {
                    int k1 = matrics[0][0] * imagearray[rowIndex + 1] [colIndex + 1]; 
                    int k2 = matrics[0][1] * convolutionarray[rowIndex + 1] [colIndex];  
                    int k3 = matrics[0][2] * imagearray[rowIndex + 1] [colIndex - 1];
                    int k4 = matrics[1][0] * imagearray[rowIndex] [colIndex + 1]; 
                    int k5 = matrics[1][1] * convolutionarray[rowIndex] [colIndex]; 
                    int k6 = matrics[1][2] * imagearray[rowIndex] [colIndex - 1]; 
                    int k7 = matrics[2][0] * imagearray[rowIndex - 1] [colIndex + 1]; 
                    int k8 = matrics[2][1] * convolutionarray[rowIndex - 1] [colIndex];  
                    int k9 = matrics[2][2] * imagearray[rowIndex - 1] [colIndex - 1]; 
					// matrics(0,0)h(0,0) f (X + 1, Y + 1) 
					// matrics(0,1)h(1,0) f (X, Y + 1) 
					// matrics(0,2)h(2,0) f (X - 1, Y + 1) 
					// matrics(1,0)h(0,1) f(X+1,Y)
					// matrics(1,1)h(1,1) f (X, Y)
					// matrics(1,2)h(2,1) f (X - 1, Y)	
					// matrics(2,0)h(0,2) f (X + 1, Y - 1) 
					// matrics(2,1)h(1,2) f (X, Y - 1)
					// matrics(2,)h(2,2) f (X - 1, Y - 1)
                    int sum = k1 + k3 + k4 + k6 + k7 + k9;

                    if (sum < 0) {
                        sum = 0;
                    } else if (sum > 255) {
                        sum = 255;
                    }

                    convolutionarray[rowIndex][colIndex] = sum;

                }
            }

            for (int[] img : convolutionarray) {
                for (int i = 0; i < img.length; i++) {
                    output.write(img[i]);
                }
            }

            output.flush();
            output.close(); // close file output stream
            z.close(); // close file input stream

        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static int findvalue(int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }

        return value;
    }
}