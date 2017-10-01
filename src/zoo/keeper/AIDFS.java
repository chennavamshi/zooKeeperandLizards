/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zoo.keeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.lang.*;

/**
 *
 * @author Vamshi
 */
public class AIDFS {

    int ass = 0;
    int size;
    int lizards;
    int trees;
    int[] treemap;
    // ArrayList<ArrayList<Integer>> lizardMap = new ArrayList<ArrayList<Integer>>();
    int arr[][];

    public static void main(String[] args) {

        long startime = System.currentTimeMillis();
        AIDFS d = new AIDFS();
        try {
            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            try {
                String line = br.readLine();

                int n = Integer.parseInt(line);
                d.size = n;
                d.arr = new int[n][n];
                d.treemap = new int[n];
                System.out.println("Value of first line is " + n);
                line = br.readLine();
                int liz = Integer.parseInt(line);
                d.lizards = liz;

                System.out.println("Value of first second is " + liz);
                line = br.readLine();

                int temp;
                int i = 0;
                while (line != null) {
                    // sb.append(line);
                    //       sb.append(System.lineSeparator());
                    for (int j = 0; j < n; j++) {
                        temp = Character.getNumericValue(line.charAt(j));
                        d.arr[i][j] = temp;
                        if (temp == 2) {
                            d.trees++;
                            d.treemap[i] = d.treemap[i] + 1;
                        }

                    }
                    i++;
                    line = br.readLine();
                }
                //  String everything = sb.toString();
//    System.out.println("Everything is "+everything);
            } finally {
                br.close();

            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }

//d.print();
        //      d.printfile();
        //   System.out.println("Started! "+Arrays.toString(d.treemap));
        System.out.println(d.dfs(0, 0));
        d.print();
        long endtime = System.currentTimeMillis();
        System.out.println("Time is "+(endtime-startime));
    }

    public boolean shouldgonextrow(int r) {
        int ttrees = 0;
        for (int i = r; i < size; i++) {
            ttrees += treemap[i];
        }
        if (ttrees + size - r >= lizards - ass) {
            return true;
        }
        return false;
    }

    public void print() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(" " + arr[i][j]);
            }
            System.out.println();
        }
        System.out.println();

    }

    public void printfile() {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter("output.txt", true);
            bw = new BufferedWriter(fw);
            /*for(int i =0;i<size;i++)
                       {
                
                           for(int j=0;j<size;j++)
                           bw.write((char)arr[i][j]);
                                                       bw.write("Val");

                           bw.newLine();
                       }
             */
            for (int i = 0; i < size; i++) {
                bw.write(Arrays.toString(arr[i]));
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            //		}System.out.println("Done");

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }

    }

    public boolean dfs(int r, int c) {
        if (ass == lizards) {
            return true;
        }
        if (r >= size) {
            return false;
        }
        boolean isin = false;
        for (int i = c; i < size; i++) {
            if (isValid(r, i)) {
                isin = true;
                arr[r][i] = 1;
                ass++;
                if (ass == lizards) {
                    return true;
                }
                boolean brk = false;
                for (int j = i; j < size; j++) {
                    if (arr[r][j] == 2) {
                        if (dfs(r, j + 1)) {
                            return true;
                        } else {
                            ass--;
                            arr[r][i] = 0;
                            brk = true;
                            break;
                        }
                    }
                }
                if (!brk) {
                    
                   if(!shouldgonextrow(r))
                   {
                       ass--;
                            arr[r][i] = 0;
                       
                       return false;
                   }
                    if (dfs(++r, 0)) {
                        return true;
                    } else {
                        r--;
                        ass--;
                        arr[r][i] = 0;
                    }
                }

            }
        }
        if (trees == 0) {
            return false;
        }
        if(!shouldgonextrow(r))
            return false;
        return dfs(++r, 0);
        
    }

    public boolean isValid(int row, int col) {
        if (arr[row][col] > 0) {
            return false;
        }
        if (checkisValidrowdec(row - 1, 0, row, col) && checkisValiddiagdec(row - 1, 0, row, col)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkisValidrowdec(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }
        for (int i = high; i >= low; i--) {

            if (i + col - row < size && i + col - row >= 0 && arr[i][i + col - row] == 1) {
                if (row - i > 1) {
                    for (int k = i + 1; k < row; k++) {
                        if (arr[k][k + col - row] == 2) {
                            return true;
                        }
                    }
                }
                return false;
            }

            if (col + row - i < size && row + col - i >= 0 && arr[i][row + col - i] == 1) {
                if (row - i > 1) {
                    for (int k = i + 1; k < row; k++) {
                        if (arr[k][-k + col + row] == 2) {
                            return true;
                        }
                    }
                }
                return false;
            }

        }
        return true;
    }

    public boolean checkisValidrowinc(int low, int high, int row, int col) {
        if (low > size - 1) {
            return true;
        }

        for (int i = low; i <= high; i++) {
            if (i + col - row < size && i + col - row >= 0 && arr[i][i + col - row] == 1) {
                if (i - row > 1) {
                    for (int k = row + 1; k <= i; k++) {
                        if (arr[k][k + col - row] == 2) {
                            return true;
                        }

                    }

                }
                return false;
            }

            if (col + row - i < size && row + col - i >= 0 && arr[i][row + col - i] == 1) {
                if (i - row > 1) {
                    for (int k = i + 1; k < row; k++) {
                        if (arr[k][-k + col + row] == 2) {
                            return true;
                        }
                    }
                }
                return false;
            }

        }
        return true;
    }

    public boolean checkisValiddiagdec(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }

        for (int i = high; i >= low; i--) {

            if (arr[i][col] == 1) {
                if (row - i > 1) {
                    for (int k = i + 1; k < row; k++) {
                        if (arr[k][col] == 2) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        return true;
    }

    public boolean checkisValiddiaginc(int low, int high, int row, int col) {
        if (low > size - 1) {
            return true;
        }

        for (int i = low; i <= high; i++) {
            if (arr[i][col] == 1) {
                if (i - row > 1) {
                    for (int k = row + 1; k <= i; k++) {
                        if (arr[k][col] == 2) {
                            return true;
                        }

                    }

                }
                return false;
            }
        }
        return true;
    }

}
