package zoo.keeper;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class AISA {

    int ass = 0;
    /**
     * @param args the command line arguments
     */
                    /*System.out.println("Current Board VALUE OF i is "+i + " stabiz is "+stabiz );
                for (int p = 0; p < size; p++) {
                    for (int j = 0; j < size; j++) {
                        System.out.print(arr[p][j]);
                    }

                    System.out.println();
                }
                System.out.println("Next Borad");
                for (int p = 0; p < size; p++) {
                    for (int j = 0; j < size; j++) {
                        System.out.print(nxtbrd[p][j]);
                    }

                    System.out.println();
                }
*/

    int size;
    int lizards;
    int trees;
    int[] treemap;
    int nxtbrd[][];
    ArrayList<ArrayList<Integer>> lizardMap = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> treeMap = new ArrayList<ArrayList<Integer>>();
    int lizarr[][];
    int arr[][];

    public static void main(String[] args) {

        long startime = System.currentTimeMillis();
        AISA d = new AISA();
        try {
            BufferedReader br = new BufferedReader(new FileReader("file.txt"));
            try {
                String line = br.readLine();

                int n = Integer.parseInt(line);
                d.size = n;
                d.arr = new int[n][n];

                d.nxtbrd = new int[d.size][d.size];

                d.treemap = new int[n];
                System.out.println("Value of first line is " + n);
                line = br.readLine();
                int liz = Integer.parseInt(line);
                d.lizards = liz;
                d.lizarr = new int[d.lizards][2];

                System.out.println("Value of first second is " + liz);
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
            } finally {
                br.close();

            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
        //  System.out.println(d.bfs());
        d.sa();
                for (int p = 0; p < d.size; p++) {
                    for (int j = 0; j < d.size; j++) {
                        System.out.print(d.arr[p][j]);
                    }

                    System.out.println();
                }
                
//   d.print();
System.out.println("Answers is "+d.calcurrattacks());

/* int val = 0;
        for (int i = 0; i < d.lizards; i++) {
            System.out.println("Value of lizard is  i" +i + " " + d.lizarr[i][0] + " "+d.lizarr[i][1]);
            if (!d.isValidarr(d.lizarr[i][0], d.lizarr[i][1]));
            val++;
        }*/
        long endtime = System.currentTimeMillis();
        System.out.println("Time is " + (endtime - startime));
    }

    public void sa() {
        int freeztemp = 0;
        double curtemp = 5;
        double stabiz = 8;
        double coolfac = 0.05;
        double stafac = 1.08;
        genrandpostions();
        int ener = calcurrattacks();
        int newener;
        while (curtemp > freeztemp) {
            double i = 0;
            while (i < stabiz) {
              //  System.out.println("4--------------------------------------------");

                i++;
                genneighbour();

                newener = calnextattacks();
                double delta = newener - ener;
                if (probfunc(curtemp, delta)) {
                    acceptneighbour();
                    ener = newener;
                }
         }

            curtemp = curtemp - coolfac;
            stabiz = stabiz * stafac;
        }

        return;
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
            if (!isValidarr(lizarr[i][0], lizarr[i][1]))
            val++;
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

    public void genrandpostions() {
        for (int i = 0; i < lizards; i++) {
            boolean brk = false;
            while (!brk) {
                Random rand = new Random();
                int row = rand.nextInt(size);
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
            nwrow = rand.nextInt(size);
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
        Random rand = new Random();
        double r = rand.nextDouble();
        if (r < func) {
            return true;
        }
        return false;
    }

}
