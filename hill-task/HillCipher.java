import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math; 

public class HillCipher {
    public static void main(String[] args) {
        
        System.out.println("HillCipher");
        int radix, blockSize;
        File keyfile = null;
        File plainfile = null;
        String cipherfile = null;
        
        if (args.length != 5) {
            System.out.println("Wrong argument format, should be <radix> <blocksize> <keyfile> <plainfile> <cipherfile>.");
            System.exit(1);
        } else {
        try {
            
            radix = Integer.parseInt(args[0]);
            if (radix != 3) {
                System.out.println("Radix need to be 3!");
                System.exit(1);
            }
            blockSize = Integer.parseInt(args[1]);
            if (blockSize != 26) {
                System.out.println("Blocksize need to be 26!");
                System.exit(1);
            }
            keyfile = new File(args[2]);
            if (!keyfile.isFile()) {
                System.out.println("KeyFile is not a file!");
                System.exit(1);
            }
            if (!isSquareMatrix(keyfile)){
                System.out.println("KeyFile is not a squarematrix");
                System.exit(1);
            }
            plainfile = new File(args[3]);
            if (!plainfile.isFile()) {
                System.out.println("PlainFile is not a file!");
                System.exit(1);
            }
            cipherfile = args[4];
            if (!cipherfile.endsWith(".txt")){
                cipherfile = cipherfile + ".txt";
            }
        
        }catch (NumberFormatException nfe){
            System.out.println("radix is not a number");
        }
    }
    
        encrypt(keyfile, plainfile, cipherfile);
    }

    private static void encrypt(File keyfile, File plainfile, String cipherfile) {
        Object[] arr = readFile(plainfile);
        int[][] plainNubersMatrix = createTransposedMatrix(arr);
        Object[] arr2 = readFile(keyfile);
        int[][] keyMatrix = createMatrix(arr2);
        int[][] cipher = multiplyMatrices(keyMatrix, plainNubersMatrix);
        printToFile(cipher, cipherfile);

    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i >= 0 && i < matrix.length && j >= 0 && j < matrix[i].length) {
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Object[] readFile(File file) {
        Scanner readScanner = null;
        try {
            readScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (readScanner.hasNextInt()) {
            list.add(readScanner.nextInt());
        }
        Object[] arr = list.toArray();
        return arr;
    }

    private static void printToFile(int[][] matrix, String file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(int i = 0; i < matrix[0].length; i++) {
                for(int j = 0; j < matrix.length; j++){
                    writer.write(matrix[j][i] + " ");
                }
            }
            writer.close();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private static int[][] createMatrix(Object[] arr) {

        int arrayCounter = 0;
        int rows = 3;
        int columns = arr.length/ rows;
        int[][] matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (arrayCounter >= 0 && arrayCounter < arr.length){
                    matrix[i][j] = (int) arr[arrayCounter++];
                }else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    private static int[][] createTransposedMatrix(Object[] arr) {

        int arrayCounter = 0;
        int rows = 3;
        int columns = arr.length / rows;
        int[][] matrix = new int[rows][columns];
        for (int i = 0; i < columns; i++){
            for (int j = 0; j < rows; j++){
                if (arrayCounter >= 0 && arrayCounter < arr.length){
                    matrix[j][i] = (int) arr[arrayCounter++];
                }else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    private static int[][] multiplyMatrices(int[][] keyMatrix, int[][] plainMatrix){
        if(keyMatrix.length == 0) return new int [0][0];
        if(keyMatrix[0].length != plainMatrix.length) return null;

        int [][] result = new int[keyMatrix[0].length][plainMatrix[0].length];

        for (int i = 0; i < keyMatrix.length; i++){
            for (int j = 0; j < plainMatrix[0].length; j++ ){
                result[i][j] = 0;
                for(int k = 0; k < keyMatrix[0].length; k++) {                
                result[i][j] += (keyMatrix[i][k] * plainMatrix[k][j]);
                result[i][j] = result[i][j] % 26;
                }
            }
        }
        return result;
    }
    private static boolean isSquareMatrix(File matrixFile){
        Scanner readScanner = null;
        int counter = 0;
        int temp;
        try {
            readScanner = new Scanner(matrixFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (readScanner.hasNextInt()) {
            temp = readScanner.nextInt();
            counter++;
        }
        int sqrt = (int) Math.sqrt(counter);
        if (sqrt*sqrt == counter) {
            return true;
        }
        return false;
    }
}

