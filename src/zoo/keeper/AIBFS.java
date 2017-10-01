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
import java.util.*;

/**
 *
 * @author Vamshi
 */
public class AIBFS {

    int ass = 0;
    /**
     * @param args the command line arguments
     */
    int size;
    int lizards;
    int trees;
    int[] treemap;
    ArrayList<ArrayList<Integer>> lizardMap = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> treeMap = new ArrayList<ArrayList<Integer>>();

    int arr[][];

    public static void main(String[] args) {
        long startime = System.currentTimeMillis();
        AIBFS d = new AIBFS();
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
                    d.lizardMap.add(new ArrayList<Integer>());
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
        System.out.println(d.bfs());
        d.print();
        long endtime = System.currentTimeMillis();
        System.out.println("Time is " + (endtime - startime));
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

            for (int i = 0; i < size; i++) {
                bw.write(Arrays.toString(arr[i]));
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();

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

    public boolean bfs() {
        Queue<ArrayList<ArrayList<Integer>>> queue = new LinkedList<ArrayList<ArrayList<Integer>>>();

        ArrayList<ArrayList<Integer>> child = getallchildren(0, lizardMap);

        for (int i = 0; i < child.size(); i++) {
            ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
            newList.add(new ArrayList<Integer>());
            newList.get(0).addAll(child.get(i));
            queue.add(newList);
        }
        ArrayList<ArrayList<Integer>> nL = new ArrayList<ArrayList<Integer>>();
        nL.add(new ArrayList<Integer>());
          if(checkifgotonextrow(nL))
            {
      queue.add(nL);
            }
        //int as = 0;
        /*  
        while (!queue.isEmpty()) {
            ArrayList<ArrayList<Integer>> liMap = queue.remove();
            System.out.println("Printing value " + as);
            for (int i = 0; i < liMap.size(); i++) {
                System.out.println(Arrays.toString(liMap.get(i).toArray()));
            }
            as++;
        }
         */
        while (!queue.isEmpty()) {

            ArrayList<ArrayList<Integer>> liMap = queue.remove();

  /*          System.out.println("Lizard Map is :");

            for (int i = 0; i < liMap.size(); i++) {
                System.out.println(Arrays.toString(liMap.get(i).toArray()));
            }
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
    */       
            if (liMap.size() == size) {
                continue;
            }
            ArrayList<ArrayList<Integer>> nChild = getallchildren(liMap.size(), liMap);
            /*
            System.out.println("Values are --------------------------------");
            for (int i = 0; i < nChild.size(); i++) {
                System.out.println(Arrays.toString(nChild.get(i).toArray()));
            }
            System.out.println("--------------------------------");
            System.out.println("--------------------------------");
             */

            for (int i = 0; i < nChild.size(); i++) {
                ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>(liMap);
                newList.add(new ArrayList<Integer>());
                newList.get(liMap.size()).addAll(nChild.get(i));
                if (checkiflizardplaced(newList)) {
                    System.out.println("Answer is :");
                    for (int k = 0; k < newList.size(); k++) {
                        ArrayList<Integer> asd = newList.get(k);
                        for (int p = 0; p < asd.size(); p++) {
                            arr[k][asd.get(p)] = 1;
                        }
                    }
                    return true;
                }
                if(checkifgotonextrow(newList))
                {
                 //   System.out.println("Pushing");
                queue.add(newList);
                }
            }
            ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>(liMap);
            newList.add(new ArrayList<Integer>());
            if(checkifgotonextrow(newList))
            {
                            //                    System.out.println("Pdushing");

            queue.add(newList);
            }
        }

        return false;
    }

    public boolean checkiflizardplaced(ArrayList<ArrayList<Integer>> li) {
        int noOflizards = 0;
        for (int i = 0; i < li.size(); i++) {
            noOflizards += li.get(i).size();
        }
        if (noOflizards == lizards) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean checkifgotonextrow(ArrayList<ArrayList<Integer>> li)
    {
        if(li.size()>=size)
            return false;
        int noOflizards = 0;
        for(int i =0;i<li.size();i++){
            noOflizards+=li.get(i).size();
        }
                    int ttrees = 0;

             for (int i = li.size(); i < size; i++) {
            ttrees += treemap[i];
        }
        if (ttrees + size - li.size() >= lizards - noOflizards) {
            return true;
        }
        return false;

    }

    public void rec(ArrayList<ArrayList<Integer>> li, int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        if (col >= size || row >= size) {
            return;
        }
        int s = li.size();
        for (int i = col; i < size; i++) {
            if (isValid(row, i, lmap)) {
                ArrayList<Integer> newList = new ArrayList<>(li.get(s - 1));
                newList.add(i);
                li.add(newList);
                for (int j = i + 1; j < size; j++) {
                    if (arr[row][j] == 2) {
                        rec(li, row, j + 1, lmap);
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> getallchildren(int row, ArrayList<ArrayList<Integer>> lmap) {

        ArrayList<ArrayList<Integer>> li = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < size; i++) {
            if (isValid(row, i, lmap)) {
                li.add(new ArrayList<Integer>());
                li.get(li.size() - 1).add(i);
                for (int j = i + 1; j < size; j++) {
                    if (arr[row][j] == 2) {
                        // System.out.println("Calling rec with " +i + " "+j +" "+(j+1));
                        rec(li, row, j + 1, lmap);
                        break;
                    }
                }
            }
        }
        li.sort(Comparator.comparing(ArrayList<Integer>::size).reversed());
        return li;
    }

    public boolean isValid(int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        if (arr[row][col] == 2) {
            return false;
        }
        //System.out.println("Checking isValid for " + row + " " + col);

        /*  ArrayList<Integer> li = lmap.get(row);
        if (li.size() > 0) {
            for (int i : li) {
                if (col <= i) {
                    return false;
                }

            }
            int lastlizard = li.get(li.size() - 1);
            boolean brk = false;
            for (int j = lastlizard + 1; j < col; j++) {

                if (arr[row][j] == 2) {
                    brk = true;
                    break;
                }
            }
            if (!brk) {
                return false;
            }

        }
         */
        //System.out.println("checkin for " + row + " col " + col);
        if (checkisValidrowdec(row - 1, 0, row, col, lmap) && checkisValiddiagdec(row - 1, 0, row, col, lmap)) {
            //System.out.println("Returning True");
            return true;
        } else {
            // System.out.println("Returning False");
            return false;
        }
    }

    public boolean checkisValidrowdec(int high, int low, int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        if (high < 0) {
            return true;
        }
        for (int i = high; i >= low; i--) {
            //   System.out.println("Value of i is " + i);
            ArrayList<Integer> temp = lmap.get(i);

            for (int j : temp) {
                if (j == col) {
                    //    System.out.println("Value of j is " + j);

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
        }

        return true;
    }

    public boolean checkisValiddiagdec(int high, int low, int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        //System.out.println("Checking diag valid for " + high + " " + low + " " + row + " " + col);

        if (high < 0) {
            return true;
        }

        for (int i = high; i >= low; i--) {
            //System.out.println("Value of i is " + i);
            ArrayList<Integer> temp = lmap.get(i);

            for (int j : temp) {
                if (Math.abs(col - j) == row - i) {
                    // System.out.println("Value of j is " + j);
                    if (row - i > 1) {

                        for (int k = i + 1; k < row; k++) {
                            if (col - j > 0) {
                                if ((k - row + col >= 0) && arr[k][k - row + col] == 2) {
                                    return true;
                                }
                            } else {
                                if (row + col - k <= size && arr[k][row + col - k] == 2) {
                                    return true;
                                }
                            }
                        }
                    }

                    //   System.out.println("Returning False");
                    return false;

                }
            }
        }
        // System.out.println("Returning TT");

        return true;
    }

}
