
import java.io.*;

public class A1 {

    public static String order = null;

    public static void main(String[] args) {
        try {
       String fileName = "yoda.tif";
    
            File file = new File(fileName);
            FileInputStream z = new FileInputStream(file);
            System.out.println("File Name: " + file.getName());
            int value;
            int q = 0; 
            int de = 0;
            int allde = -1;
            int ifd = -1;
            int stripOffsets = -1;
            
            String[][] dearr = null;
            byte[] countbyte = null;
            
            File outputfile = new File("assignment1.raw");
            FileOutputStream output = new FileOutputStream(outputfile);
            
            while ((value = z.read()) != -1) {
                if (q == 0) {
                    int value2 = z.read();
                    String hex = changehex(value) + changehex(value2);
                    if (hex.equals("4949")) {
                        order = "LSB";
                    } else if (hex.equals("4D4D")) {
                        order = "MSB";
                    }
                    System.out.println("LSB / MSB: " + order);
                    q++;
                    
                } else if (q == 2) {
                    int value2 = z.read();
                    int[] nums = {value, value2};
                    String hexString = adddectohex(nums);
                    System.out.println("Version: " + hexString);
                    q++;
                    
                } else if (q == 4) {
                    int value2 = z.read();
                    int value3 = z.read();
                    int value4 = z.read();
                    int[] nums = {value, value2, value3, value4};
                    String hexString = adddectohex(nums);
                    System.out.println("First Offset IFD: " + Integer.parseInt(hexString, 16));
                    q += 3;
                    
                } else if (q == 8) {
                    int value2 = z.read();
                    int[] nums = {value, value2};
                    String hexString = adddectohex(nums);
                    allde = Integer.parseInt(hexString, 16);
                    System.out.println("Total DE: " + allde);
                    dearr = new String[allde][8];
                    q++;
                    
                } else if ((q >= 10 && q < (10 + 12 * allde)) && ((stripOffsets > 0 && q <= stripOffsets && ((q + 12) <= stripOffsets)) || (stripOffsets == -1))) {
                    int tag1 = value;
                    int tag2 = z.read();
                    int type1 = z.read();
                    int type2 = z.read();
                    int length1 = z.read();
                    int length2 = z.read();
                    int length3 = z.read();
                    int length4 = z.read();
                    int value1 = z.read();
                    int value2 = z.read();
                    int value3 = z.read();
                    int value4 = z.read();
                    int[] tagNameNums = { tag1, tag2 };
                    int[] tagTypeNums = { type1, type2 };
                    String tagName = findtag(tagNameNums);
                    String tagTypeName = findtagtype(tagTypeNums);
                    if (tagName != null && tagTypeName != null) {
                        String tagValue = adddectohex(new int[] {value1, value2, value3, value4});
                        String tagLength = adddectohex(new int[] {length1, length2, length3, length4});
                        int tagValueDec = Integer.parseInt(tagValue, 16);
                        int tagLengthDec = Integer.parseInt(tagLength, 16);
                        int wordLength1 = 29 - tagName.length();
                        int wordLength2 = 9 - tagTypeName.length();
                        String wordSpaces1 = "";
                        String wordSpaces2 = "";
                        for (int i = 0; i < wordLength1; i++) {
                            wordSpaces1 += " ";
                        }
                        for (int i = 0; i < wordLength2; i++) {
                            wordSpaces2 += " ";
                        }
                        if (tagName == "StripOffsets") {
                            stripOffsets = tagValueDec;
                            System.out.println("Size of IFD: " + stripOffsets);
                        }
                        dearr[de][0] = Integer.toString(Integer.parseInt(adddectohex(tagNameNums), 16));
                        dearr[de][1] = tagName;
                        de++;
                        
                    }   
                    q += 11;
                    
                } else if (q == (10 + 12 * allde)) {
                    int value2 = z.read();
                    int[] nums = {value, value2};
                    String offsetNextIFDHex = adddectohex(nums);
                    ifd = Integer.parseInt(offsetNextIFDHex, 16);
                    System.out.println("Consecutive Offset IFD (Offset of Next IFD): " + ifd);
                    q++;
                    
                } else if (q >= 10 && stripOffsets > 0 && q >= stripOffsets) {
                    if (q == (stripOffsets)) {
                        int allbyte = (int) (file.length() - stripOffsets);
                        countbyte = new byte[allbyte];
                        for (int i = 0; i < (dearr.length - 1); i++) {
                            if (dearr[i][0] != null) {
                                int tagDec = Integer.parseInt(dearr[i][0]);
                                String tagName = dearr[i][1];
                                System.out.println(tagDec + tagName );
                            }
                        }
                    }
                }
                q++;
            }
            
            output.close();
            z.close();
            
        } catch (IOException e) {
            System.out.println("File is not exists");
        }
    }

    private static String adddectohex(int[] decNums) {
        String hexString = "";

        for (int i = 0; i < decNums.length; i++) {
            if (order == "LSB") {
                hexString += changehex(decNums[decNums.length - (i + 1)]);
            } else if (order == "MSB") {
                hexString += changehex(decNums[i]);
            }
        }

        return hexString;
    }

    private static String changehex(int decNum) {
        if (decNum < 10) {
            return "0" + decNum;
        } else {
            return String.format("%02X", decNum);
        }
    }

    private static String findtag(int tagType) {
        String tagName = null;

        switch (tagType) {
            case 254:
                tagName = "NewSubfileType";
                break;
            case 256:
                tagName = "ImageWidth";
                break;
            case 257:
                tagName = "ImageLength";
                break;
            case 258:
                tagName = "BitsPerSample";
                break;
            case 259:
                tagName = "Compression";
                break;
            case 262:
                tagName = "PhotometricInterpretation";
                break;
            case 273:
                tagName = "StripOffsets";
                break;
            case 277:
                tagName = "SamplesPerPixel";
                break;
            case 278:
                tagName = "RowsPerStrip";
                break;
            case 279:
                tagName = "StripByteCounts";
                break;
            case 282:
                tagName = "XResolution";
                break;
            case 283:
                tagName = "YResolution";
                break;
            case 296:
                tagName = "ResolutionUnit";
                break;
        }

        return tagName;
    }

    private static String findtagtype(int tagTypeDec) {
        String tagTypeName = null;
        
        switch (tagTypeDec) {
            case 1:
                tagTypeName = "byte";
                break;
            case 2:
                tagTypeName = "ASCII";
                break;
            case 3:
                tagTypeName = "short";
                break;
            case 4:
                tagTypeName = "long";
                break;
            case 5:
                tagTypeName = "rational";
                break;
        }

        return tagTypeName;
    }

    private static String findtag(String tagHex) {
        int tagType = Integer.parseInt(tagHex, 16);
        return findtag(tagType);
    }

    private static String findtag(int[] tagNums) {
        String tagHex = adddectohex(tagNums);
        return findtag(tagHex);
    }

    private static String findtagtype(String tagTypeHex) {
        int tagType = Integer.parseInt(tagTypeHex, 16);
        return findtagtype(tagType);
    }
    
    private static String findtagtype(int[] tagTypeNums) {
        String tagTypeHex = adddectohex(tagTypeNums);
        return findtagtype(tagTypeHex);
    }
}