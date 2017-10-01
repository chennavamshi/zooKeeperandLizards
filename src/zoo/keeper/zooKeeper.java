package zoo.keeper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.lang.*;
import java.util.Timer;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.TimerTask;

public class zooKeeper {

    int ass = 0;
    int size;
    int lizards;
    int trees;
    int[] treemap;
    int arr[][];
    ArrayList<ArrayList<Integer>> lizardMap = new ArrayList<ArrayList<Integer>>();
    int nxtbrd[][];
    int lizarr[][];
    final static Timer timer = new Timer();

    public static void main(String[] args) {
        long delay = (long) (4.5*60) * 1000;
        long startime = System.currentTimeMillis();

        zooKeeper d = new zooKeeper();
       
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Toolkit.getDefaultToolkit().beep();
                //System.out.println("ASDF");
                d.printfile(false);
                        long endtime = System.currentTimeMillis();
        System.out.println("Time is "+(endtime-startime));

                System.exit(0);
                return;
            }
        };

        timer.schedule(task, delay);
         try {
            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            try {
                String line = br.readLine();
                String algo = line;
                line = br.readLine();
                int n = Integer.parseInt(line);
                d.size = n;
                d.arr = new int[n][n];
                d.treemap = new int[n];
                line = br.readLine();
                int liz = Integer.parseInt(line);
                d.lizards = liz;
                line = br.readLine();
                int temp;
                int i = 0;
                while (line != null) {
                    d.lizardMap.add(new ArrayList<Integer>());
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
                if (d.lizards == 0) {
                  //  System.out.println("TRUEEEEE");
                    d.printfile(true);
                    return;
                }
                if (!d.shouldgonextrow(0)) {
                   // System.out.println("False");
                    d.printfile(false);
                    return;
                }
                if (algo.equals("DFS")) {
                    d.printfile(d.dfs(0, 0));
                   // System.out.println(d.caltotalattacks());
                    //d.print();
                }
                if (algo.equals("BFS")) {
                    d.printfile(d.bfs());
                    //System.out.println(d.caltotalattacks());
                    // d.print();
                }
                if (algo.equals("SA")) {
                    d.nxtbrd = new int[d.size][d.size];
                    d.lizarr = new int[d.lizards][2];
                    d.sa();
                    if (d.caltotalattacks() == 0) {
                        d.printfile(true);
                    } else {
                        d.printfile(false);
                    }
                    //System.out.println(d.caltotalattacks());
                    //d.print();
                }

                        long endtime = System.currentTimeMillis();
        System.out.println("Time is "+(endtime-startime));

            } finally {
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }

        //System.out.println("Current attacks are " +d.caltotalattacks());
        //  long endtime = System.currentTimeMillis();
        // System.out.println("Time is " + (endtime - startime));
    }

    public void printfile(boolean result) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
            if (!result) {
                bw.write("FAIL");
            } else {
                bw.write("OK");
                bw.newLine();
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        bw.write(Integer.toString(arr[i][j]));
                    }
                    bw.newLine();
                }
                //		}System.out.println("Done");
            }

            timer.cancel();
            timer.purge();
        } catch (Exception e) {
            //  System.out.println("Here");
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
                // System.out.println("Here");
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

                    if (!shouldgonextrow(r)) {
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
        if (!shouldgonextrow(r)) {
            return false;
        }
        return dfs(++r, 0);

    }

    public boolean bfs() {
        Queue<ArrayList<ArrayList<Integer>>> queue = new LinkedList<ArrayList<ArrayList<Integer>>>();
        ArrayList<ArrayList<Integer>> child = getallchildren(0, lizardMap);
        for (int i = 0; i < child.size(); i++) {
            ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
            newList.add(new ArrayList<Integer>());
            newList.get(0).addAll(child.get(i));
            if (checkiflizardplaced(newList)) {
                for (int k = 0; k < newList.size(); k++) {
                    ArrayList<Integer> asd = newList.get(k);
                    for (int p = 0; p < asd.size(); p++) {
                        arr[k][asd.get(p)] = 1;
                    }
                }
                return true;
            }

            queue.add(newList);
        }
        ArrayList<ArrayList<Integer>> nL = new ArrayList<ArrayList<Integer>>();
        nL.add(new ArrayList<Integer>());

        if (checkifgotonextrow(nL)) {
            queue.add(nL);
        }
        while (!queue.isEmpty()) {

            ArrayList<ArrayList<Integer>> liMap = queue.remove();
            if (liMap.size() == size) {
                continue;
            }

            /*  System.out.println("Lizard Map is :");

            for (int i = 0; i < liMap.size(); i++) {
                System.out.println(Arrays.toString(liMap.get(i).toArray()));
            }
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
             */
            ArrayList<ArrayList<Integer>> nChild = getallchildren(liMap.size(), liMap);
            for (int i = 0; i < nChild.size(); i++) {
                ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>(liMap);
                newList.add(new ArrayList<Integer>());
                newList.get(liMap.size()).addAll(nChild.get(i));
                if (checkiflizardplaced(newList)) {
                    for (int k = 0; k < newList.size(); k++) {
                        ArrayList<Integer> asd = newList.get(k);
                        for (int p = 0; p < asd.size(); p++) {
                            arr[k][asd.get(p)] = 1;
                        }
                    }
                    return true;
                }
                if (checkifgotonextrow(newList)) {
                    queue.add(newList);
                }
            }
            ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>(liMap);
            newList.add(new ArrayList<Integer>());
            if (checkifgotonextrow(newList)) {
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

    public boolean checkifgotonextrow(ArrayList<ArrayList<Integer>> li) {
        if (li.size() >= size) {
            return false;
        }
        int noOflizards = 0;
        for (int i = 0; i < li.size(); i++) {
            noOflizards += li.get(i).size();
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

    public void sa() {
        //System.out.println("SA ");
        int freeztemp = 0;
        double curtemppp = 5;
        double curtemp = 5;
        double stabiz = 5;
        double stabizz = 5;
        double coolfac = 0.05;
        double stafac = 1.08;
        genrandpostions();
        int ener = calcurrattacks();
        if(ener==0)
            return;
        int newener;
        while(true){
        while (curtemp > freeztemp) {
            double i = 0;
            while (i < stabiz) {
             i++;
                genneighbour();
                newener = calnextattacks();
                if (newener == 0) {
                 System.out.println("Value of temp is "+curtemppp);
                    acceptneighbour();
                    return;
                }
                double delta = newener - ener;
                if (probfunc(curtemp, delta)) {
                    acceptneighbour();
                 
                    ener = newener;
                }
            }

            curtemp = curtemp - coolfac;
           stabiz = stabiz * stafac;
        }
        curtemppp+=1;
        curtemp = curtemppp;
        stabiz = stabizz;
        }
    }

    public void genrandpostions() {
        for (int i = 0; i < lizards; i++) {
            boolean brk = false;
            while (!brk) {
                Random rand = new Random();
                int row = rand.nextInt(size);
                rand = new Random();
                int col = rand.nextInt(size);
                if (arr[row][col] == 2 || arr[row][col] == 1) {
                    brk = false;
                } else {
                    lizarr[i][0] = row;
                    lizarr[i][1] = col;
                        arr[row][col] = 1;
                    brk = true;
                }
            }
        }
    }

    public int caltotalattacks() {
        int val = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arr[i][j] == 1) {
                    if (!isValidarr(i, j)) {
                        val++;
                    }
                }
            }
        }
        return val;
    }

    public void genneighbour() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                nxtbrd[i][j] = arr[i][j];
            }
        }
        Random rand = new Random();
        int liz = rand.nextInt(lizards);
        boolean brk = false;
        int row, col, nwrow, nwcol;
        while (!brk) {
            row = lizarr[liz][0];
            col = lizarr[liz][1];
            rand = new Random();

            nwrow = rand.nextInt(size);
            rand = new Random();
            nwcol = rand.nextInt(size);
            if (arr[nwrow][nwcol] == 1 || arr[nwrow][nwcol] == 2) {
                brk = false;
            } else {
                nxtbrd[row][col] = 0;
                nxtbrd[nwrow][nwcol] = 1;
                brk = true;
            }
        }
    }

    public boolean probfunc(double temp, double delta) {
        if (delta < 0) {
            return true;
        }
       double func = Math.exp(-delta / temp);
      //double func = 1/Math.log(delta/temp);
        
        Random rand = new Random();
        double r = rand.nextDouble();
        
     //  System.out.println("func is "+func + "rand is "+r+"delta is "+delta + " Temp is "+temp);
        if(r < func) {
            return true;
        }
        return false;
    }

    public void acceptneighbour() {
        int liz = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr[i][j] = nxtbrd[i][j];
                if (arr[i][j] == 1) {
                    lizarr[liz][0] = i;
                    lizarr[liz][1] = j;
                    liz++;
                }
            }
        }
    }

    public int calcurrattacks() {
        int val = 0;
        for (int i = 0; i < lizards; i++) {
            if (!isValidarr(lizarr[i][0], lizarr[i][1])) {
                val++;
            }
        }
        return val;
    }

    public boolean isValidarr(int row, int col) {
        if (checkisValidsamerow(row, col) && checkisValidrowdecarr(row - 1, 0, row, col) && checkisValidrowincarr(row + 1, size - 1, row, col) && checkisValiddiagdecarr(row - 1, 0, row, col) && checkisValiddiagincarr(row + 1, size - 1, row, col)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkisValidsamerow(int row, int col) {
        if (row >= size || col >= size) {
            return true;
        }
        for (int i = col - 1; i >= 0; i--) {
            if (arr[row][i] == 2) {
                break;
            }
            if (arr[row][i] == 1) {

                return false;
            }
        }
        for (int i = col + 1; i < size; i++) {
            if (arr[row][i] == 2) {
                break;
            }
            if (arr[row][i] == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isValid(int row, int col) {
        if (arr[row][col] > 0) {
            return false;
        }
        if (checkisValidrowdecarr(row - 1, 0, row, col) && checkisValiddiagdecarr(row - 1, 0, row, col)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValid(int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        if (arr[row][col] == 2) {
            return false;
        }
        if (checkisValidrowdec(row - 1, 0, row, col, lmap) && checkisValiddiagdec(row - 1, 0, row, col, lmap)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkisValidrowdec(int high, int low, int row, int col, ArrayList<ArrayList<Integer>> lmap) {
        if (high < 0) {
            return true;
        }
        for (int i = high; i >= low; i--) {
            ArrayList<Integer> temp = lmap.get(i);
            for (int j : temp) {
                if (j == col) {
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
        if (high < 0) {
            return true;
        }

        for (int i = high; i >= low; i--) {
            ArrayList<Integer> temp = lmap.get(i);
            for (int j : temp) {
                if (Math.abs(col - j) == row - i) {
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
                    return false;

                }
            }
        }
        return true;
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

    public boolean checkisValidrowdecarr(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }
        for (int i = high, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (arr[i][j] == 2) {
                break;
            }
            if (arr[i][j] == 1) {
                return false;
            }
        }

        for (int i = high, j = col + 1; i >= 0 && j < size; i--, j++) {
            if (arr[i][j] == 2) {
                break;
            }
            if (arr[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    public boolean checkisValidrowincarr(int low, int high, int row, int col) {
        if (low > size - 1) {

            return true;
        }

        for (int i = low, j = col - 1; i < size && j >= 0; i++, j--) {
            if (arr[i][j] == 2) {
                break;
            }
            if (arr[i][j] == 1) {

                return false;
            }
        }
        for (int i = low, j = col + 1; i < size && j < size; i++, j++) {
            if (arr[i][j] == 2) {
                break;
            }
            if (arr[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    public boolean checkisValiddiagdecarr(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }

        for (int i = high; i >= low; i--) {

            if (arr[i][col] == 1) {
                return false;
            }
            if (arr[i][col] == 2) {
                break;
            }
        }
        return true;
    }

    public boolean checkisValiddiagincarr(int low, int high, int row, int col) {
        if (low > size - 1) {
            return true;
        }
        for (int i = low; i <= high; i++) {
            if (arr[i][col] == 1) {
                return false;
            }
            if (arr[i][col] == 2) {
                break;
            }
        }
        return true;
    }

    public int calnextattacks() {
        int val = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (nxtbrd[i][j] == 1) {
                    if (!isValidnei(i, j)) {
                        val++;
                    }
                }
            }
        }
        return val;
    }

    public boolean isValidnei(int row, int col) {
        if (checkisValidsamerownei(row, col) && checkisValidrowdecnei(row - 1, 0, row, col) && checkisValidrowincnei(row + 1, size - 1, row, col) && checkisValiddiagdecnei(row - 1, 0, row, col) && checkisValiddiagincnei(row + 1, size - 1, row, col)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkisValidsamerownei(int row, int col) {
        if (row >= size || col >= size) {
            return false;
        }
        for (int i = col - 1; i >= 0; i--) {
            if (nxtbrd[row][i] == 2) {
                break;
            }
            if (nxtbrd[row][i] == 1) {
                return false;
            }
        }
        for (int i = col + 1; i < size; i++) {
            if (nxtbrd[row][i] == 2) {
                break;
            }
            if (nxtbrd[row][i] == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean checkisValidrowdecnei(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }
        for (int i = high, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (nxtbrd[i][j] == 2) {
                break;
            }
            if (nxtbrd[i][j] == 1) {
                return false;
            }
        }

        for (int i = high, j = col + 1; i >= 0 && j < size; i--, j++) {
            if (nxtbrd[i][j] == 2) {
                break;
            }
            if (nxtbrd[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean checkisValidrowincnei(int low, int high, int row, int col) {
        if (low > size - 1) {
            return true;
        }

        for (int i = low, j = col - 1; i < size && j >= 0; i++, j--) {
            if (nxtbrd[i][j] == 2) {
                break;
            }
            if (nxtbrd[i][j] == 1) {
                return false;
            }
        }
        for (int i = low, j = col + 1; i < size && j < size; i++, j++) {
            if (nxtbrd[i][j] == 2) {
                break;
            }
            if (nxtbrd[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean checkisValiddiagdecnei(int high, int low, int row, int col) {
        if (high < 0) {
            return true;
        }

        for (int i = high; i >= low; i--) {

            if (nxtbrd[i][col] == 1) {
                return false;
            }
            if (nxtbrd[i][col] == 2) {
                break;
            }
        }

        return true;
    }

    public boolean checkisValiddiagincnei(int low, int high, int row, int col) {
        if (low > size - 1) {
            return true;
        }
        for (int i = low; i <= high; i++) {
            if (nxtbrd[i][col] == 1) {
                return false;
            }
            if (nxtbrd[i][col] == 2) {
                break;
            }
        }
        return true;
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

}
