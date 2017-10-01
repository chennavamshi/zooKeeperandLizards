package zoo.keeper;

import java.util.Random;

/**
 *
 * @author Vamshi
 */
class SA {

    double temperature = 5;
    double coolingFactor = 0.05;
    double freezingTemperature = 0.0;
    double f = 8;
    double fa = 1.08;
    public void print(int arr[][], int n) {
        System.out.println("-----------");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
        return;
    }

    public boolean Simulation(int p, int n, int[][] zoo) {
        int[][] current = new int[p][2];
        int[][] temp = new int[n][n];

        int currentSystemEnergy = generateRandomPositions(p, n, zoo, current);

        //System.out.println(currentSystemEnergy);
        while (temperature > freezingTemperature) {
            //System.out.println(currentSystemEnergy);
            for(int ak=0;ak<f;ak++)
            {
            int newEnergy = createNeighbour(p, n, zoo, current, temp);
                           print(zoo,n);
                         System.out.println("----------");
   
          System.out.println("System Enerergy is "+newEnergy);
          for(int k=0;k<p;k++)
          {
             System.out.print(current[k][0] + " " + current[k][1] + " ");
          }
          
System.out.println("----------");
               System.out.println(currentSystemEnergy);
            int delta = newEnergy - currentSystemEnergy;
if (newEnergy == 0) {
                print(zoo, n);
                return true;
            }            
//System.out.println(delta);
            if (probabilityFunction(delta, temperature)) {
                permitNeighbour(n, zoo, temp, current);
                currentSystemEnergy = newEnergy;
            }
            }
            f = f*fa;
            temperature -= coolingFactor;
            
        }

        return false;
    }

    public boolean probabilityFunction(int delta, double temperature) {
        if (delta < 0) {
            return true;
        }
        double d = (double) delta;
        double F = Math.exp(-d/temperature);
      // double F = 1/Math.log(delta/temperature);  
       Random rand = new Random();

        double R = rand.nextDouble();

        if (R < F) {
            return true;
        }
        return false;
    }

    public int generateRandomPositions(int p, int n, int[][] zoo, int[][] current) {
        for (int liz = 0; liz < p;) {
                    Random rand = new Random();
                    
            int i = rand.nextInt(n);
            int j = rand.nextInt(n);

            if (checkPlace(i, j, zoo)) {
                zoo[i][j] = 1;
                current[liz][0] = i;
                current[liz][1] = j;
                liz++;
            }
        }
   int c =0;
        for(int as=0;as<p;as++)
        {
            
        c+=countConflicts(current[as][0],current[as][1],p, n, zoo, current);
        
        }// System.out.println(c);
return c;
    }

    public boolean checkPlace(int i, int j, int[][] zoo) {

        if (zoo[i][j] != 2 && zoo[i][j] != 1) {
            return true;
        }
        return false;
    }

    public int createNeighbour(int p, int n, int[][] zoo, int[][] current, int[][] temp) {
                    Random rand = new Random();

        int newLizard = rand.nextInt(p);
        boolean repeating = true;
        int i = current[newLizard][0];
        int j = current[newLizard][1];

        while (repeating) {
            //   System.out.println("In Repeating");
            
            int i1 = rand.nextInt(n);
            int j1 = rand.nextInt(n);
            // System.out.println("In Repeating" +i1 +" "+j1);		
            if (checkPlace(i1, j1, zoo)) {
                //current[ newLizard ][ 0 ] = i1;
                //current[ newLizard ][ 1 ] = j1;
                copyArray(n, temp, zoo);

                temp[i1][j1] = 1;
                temp[i][j] = 0;
                repeating = false;
            }
        }
        int c =0;
        for(int as=0;as<p;as++)
        {
            
        c+=countConflicts(current[as][0],current[as][1],p, n, temp, current);
        
        }// System.out.println(c);

        return c;
    }

    public void copyArray(int n, int[][] temp, int[][] zoo) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = zoo[i][j];
            }
        }
    }

    public static int countConflicts(int r, int c, int p, int n, int[][] zoo, int[][] current) {

      
            int row = r;
            int col = c;
            // Check Right Upper Diagonal
            for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
                if(zoo[i][j] == 2)
                {
                    break;
                }
                if (zoo[i][j] == 1) {
                    return 1;
                }
            }

            // Check Right Lower Diagonal
            for (int i = row + 1, j = col + 1; i < n && j <n; i++, j++) {
                if(zoo[i][j] == 2)
                    break;
                if (zoo[i][j] == 1) {
                    return 1;
                }
            }

            // Check left upper diagonal
            for (int i = row - 1, j = col - 1; i >= 0 && j >= 0;i--, j--) {
                if(zoo[i][j] == 2)
                    break;
                if (zoo[i][j] == 1) {
                    return 1;

                }
            }

            // Check left lower diagonal
            for (int i = row + 1, j = col - 1; i < n && j >= 0; i++, j--) {
                               if(zoo[i][j] == 2)
                    break;
                if (zoo[i][j] == 1) {
                    return 1;

                }
            }

            // Check left
            for (int j = col - 1; j >= 0; j--) {
                if(zoo[row][j]==2)
                    break;
                if (zoo[row][j] == 1) {
                    return 1;
                }
            }

            // Check right
            for (int j = col + 1; j < n; j++) {
                if(zoo[row][j]==2)
                    break;

                if (zoo[row][j] == 1) {
                    return 1;
                }
            }

            // Check up
            for (int i = row - 1; i >= 0; i--) {
                if (zoo[i][col] == 1) {
                    return 1;
                }
                if (zoo[i][col] == 2) {
                    break;
                }
                
            }

            // Check down
            for (int i = row + 1; i < n; i++) {
                if (zoo[i][col] == 1) {
                    return 1;
                }
                if (zoo[i][col] == 2) {
                    break;
                }
                
            }
        
        return 0;

    }

    public void permitNeighbour(int n, int[][] zoo, int[][] temp, int[][] lizard) {
        int liz = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                zoo[i][j] = temp[i][j];
                if (zoo[i][j] == 1) {
                    lizard[liz][0] = i;
                    lizard[liz][1] = j;
                    liz++;
                }
            }
        }
    }
}
