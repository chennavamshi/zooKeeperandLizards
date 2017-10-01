/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zoo.keeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.TimerTask;
import java.awt.Toolkit;

/**
 *
 * @author Vamshi
 */
public class temp {
    public static void main(String[] args)
    {
     long startime = System.currentTimeMillis();
     SA d = new SA();
     try {
            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            try {
                String line = br.readLine();

                int n = Integer.parseInt(line);
                int size = n;
                int[][] arr = new int[n][n];
                //d.treemap = new int[n];
                line = br.readLine();
                int liz = Integer.parseInt(line);
                int lizards = liz;

                //System.out.println("Value of first second is " + liz);
                line = br.readLine();

                int temp;
                int i = 0;
                while (line != null) {
                    // sb.append(line);
                    //       sb.append(System.lineSeparator());
                    for (int j = 0; j < n; j++) {
                        temp = Character.getNumericValue(line.charAt(j));
                        arr[i][j] = temp;
                  
                    }
                    i++;
                    line = br.readLine();
                }
                //  String everything = sb.toString();
    System.out.println("Everything is ");
System.out.println(d.Simulation(liz, n, arr));
            } finally {
                br.close();

            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
     
//d.print();
        //      d.printfile();
        //   System.out.println("Started! "+Arrays.toString(d.treemap));
       
    }
    
    public static void rec(ArrayList<ArrayList<Integer>> li,int[] arr,int col)
    {
        if(col>=arr.length)
            return;
        int s = li.size();
      //  if(arr[col]==2)
        //    return;
                    
        for(int i=col;i<arr.length;i++)
        {
            if(arr[i]==2)
            continue;
           ArrayList<Integer> newList = new ArrayList<>(li.get(s-1));
           newList.add(i);
           li.add(newList);
                for(int j=i+1;j<arr.length;j++)
                {
                    if(arr[j]==2)
                    {
                        rec(li,arr,j+1);
                        break;
                    }
                }
         }
    }
    }


