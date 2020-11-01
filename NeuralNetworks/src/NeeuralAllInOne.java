import java.util.Arrays;
import java.util.Random;
/*
 * https://gist.github.com/Jeraldy/7d4262db0536d27906b1e397662512bc
 * https://deeplearning4j.konduit.ai/getting-started/tutorials
 * https://medium.com/coinmonks/implementing-an-artificial-neural-network-in-pure-java-no-external-dependencies-975749a38114
 * backpropagation algorithm (for NNs, CNNs and RNNs)
 * 
    Artificial NeuralNetworks ( ANN )
    Multilayer Perceptrons (MLPs)
    Convolutional Neural Networks (CNNs)
    Recurrent Neural Networks (RNNs)

ANN can be used to solve problems related to:
    Tabular data
    Image data
    Text data                                   problem:  Vanishing and Exploding Gradient.


Use Use MLPs For:
    Tabular datasets
    Classification prediction problems
    Regression prediction problems

Use CNNs For:
    Image data
    Classification prediction problems
    Regression prediction problems


RNNs For:
    Text data
    Speech data
    Classification prediction problems
    Regression prediction problems
    Generative models
Don’t Use RNNs For:
    Tabular data
    Image data
An alternative division defines these symmetrically as:

    a generative model is a model of the conditional probability of the observable X, given a target y, symbolically, P ( X | Y = y ) { P(X|Y=y)} { P(X|Y=y)}[4]
    a discriminative model is a model of the conditional probability of the target Y, given an observation x, symbolically, P ( Y | X = x ) { P(Y|X=x)} { P(Y|X=x)}[5]


In ML, cost functions are used to estimate how badly models are performing. 
Put simply, a cost function is a measure of how wrong the model is in terms of its ability to estimate the relationship between X and y. 
This is typically expressed as a difference or distance between the predicted value and the actual value.Nov 27, 2017

In machine learning and pattern recognition, a feature is an individual measurable property or 
characteristic of a phenomenon being observed. 
Choosing informative, discriminating and independent features is a crucial step 
for effective algorithms in pattern recognition
, classification and regression.
 */
public class NeeuralAllInOne {
/**
*
* @author Deus Jeraldy
* @Email: deusjeraldy@gmail.com
* BSD License
*/

//np.java -> https://gist.github.com/Jeraldy/7d4262db0536d27906b1e397662512bc


   public static void main(String[] args) {
	   int loops=1200, nodes=100;
	   
	   train( loops, nodes);
	   
//	   for (  loops=100; loops<5000; loops+=100) {
//		   for (  nodes=10;nodes< 1000; nodes = (int) (1.2*nodes)) {
//			   train( loops, nodes);
//		   }   
//	   }
	   
   }
   
   private static void train(int iterations, int nodes) {
	   System.out.println("\n\nIterations = ".concat(String.valueOf(iterations)).concat(" nodes ").concat(String.valueOf( nodes)));
	   //int iterations=1000;
       double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
       double[][] Y = {{0}, {1}, {0}, {1}};

       int m = Y.length;
       //int nodes = 160;  /// was  int nodes = 400;
       long startTime = System.nanoTime();
       long startCycleTime=startTime;
       X = np.T(X);
       Y = np.T(Y);

       double[][] W1 = np.random(nodes, 2);
       double[][] b1 = new double[nodes][m];

       double[][] W2 = np.random(1, nodes);
       double[][] b2 = new double[1][m];

       for (int i = 0; i < iterations; i++) {///  was  for (int i = 0; i < 1000; i++)
           // Foward Prop
           // LAYER 1
           double[][] Z1 = np.add(np.dot(W1, X), b1);
           double[][] A1 = np.sigmoid(Z1);

           //LAYER 2
           double[][] Z2 = np.add(np.dot(W2, A1), b2);
           double[][] A2 = np.sigmoid(Z2);

           double cost = np.cross_entropy(m, Y, A2);
           //costs.getData().add(new XYChart.Data(i, cost));
        
           // Back Prop
           //LAYER 2
           double[][] dZ2 = np.subtract(A2, Y);
           double[][] dW2 = np.divide(np.dot(dZ2, np.T(A1)), m);
           double[][] db2 = np.divide(dZ2, m);

           //LAYER 1
           double[][] dZ1 = np.multiply(np.dot(np.T(W2), dZ2), np.subtract(1.0, np.power(A1, 2)));
           double[][] dW1 = np.divide(np.dot(dZ1, np.T(X)), m);
           double[][] db1 = np.divide(dZ1, m);

           // G.D
           W1 = np.subtract(W1, np.multiply(0.01, dW1));
           b1 = np.subtract(b1, np.multiply(0.01, db1));

           W2 = np.subtract(W2, np.multiply(0.01, dW2));
           b2 = np.subtract(b2, np.multiply(0.01, db2));

           if (i % (iterations/6) == 0) {
               np.print("\n============== i = " + i);
               np.print("Cost = " + cost);
               np.print("Predictions = " + Arrays.deepToString(A2));
               long endTime   = System.nanoTime();
               long totalTime = endTime - startTime;
               System.out.println(totalTime/1000000 +"[ms]  delta =" + (endTime - startCycleTime )/1000000 +"[ms]" ); 
               startCycleTime =  endTime ;     
               System.out.println( "b: " +b2[0][0] + "  " + b2[0][1] + "  " + b1[2][0] + "  " +  b1[nodes-1][0]);
               System.out.println( "W: " +W2[0][0] + "  " + W2[0][1] + "  " + W1[2][0] + "  " +  W1[nodes-1][0]);
           }
       }

   }
}


/**
 *
 * @author Deus Jeraldy
 * @Email: deusjeraldy@gmail.com
 */
class np {

    private static Random random;
    private static long seed;

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    /**
     * Sets the seed of the pseudo-random number generator. This method enables
     * you to produce the same sequence of "random" number for each execution of
     * the program. Ordinarily, you should call this method at most once per
     * program.
     *
     * @param s the seed
     */
    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }

    /**
     * Returns the seed of the pseudo-random number generator.
     *
     * @return the seed
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     *
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and {@code n}
     * (exclusive)
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public static int uniform(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }
        return random.nextInt(n);
    }

    /**
     * Returns a random long integer uniformly in [0, n).
     *
     * @param n number of possible {@code long} integers
     * @return a random long integer uniformly between 0 (inclusive) and
     * {@code n} (exclusive)
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public static long uniform(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }

        long r = random.nextLong();
        long m = n - 1;

        // power of two
        if ((n & m) == 0L) {
            return r & m;
        }

        // reject over-represented candidates
        long u = r >>> 1;
        while (u + m - (r = u % n) < 0L) {
            u = random.nextLong() >>> 1;
        }
        return r;
    }

    /**
     * Returns a random integer uniformly in [a, b).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @return a random integer uniformly in [a, b)
     * @throws IllegalArgumentException if {@code b <= a}
     * @throws IllegalArgumentException if {@code b - a >= Integer.MAX_VALUE}
     */
    public static int uniform(int a, int b) {
        if ((b <= a) || ((long) b - a >= Integer.MAX_VALUE)) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + uniform(b - a);
    }

    /**
     * Returns a random real number uniformly in [a, b).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @return a random real number uniformly in [a, b)
     * @throws IllegalArgumentException unless {@code a < b}
     */
    public static double uniform(double a, double b) {
        if (!(a < b)) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + uniform() * (b - a);
    }

    /**
     * @param m
     * @param n
     * @return random m-by-n matrix with values between 0 and 1
     */
    public static double[][] random(int m, int n) {
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = uniform(0.0, 1.0);
            }
        }
        return a;
    }

    /**
     * Transpose of a matrix
     *
     * @param a matrix
     * @return b = A^T
     */
    public static double[][] T(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    /**
     * @param a matrix
     * @param b matrix
     * @return c = a + b
     */
    public static double[][] add(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    /**
     * @param a matrix
     * @param b matrix
     * @return c = a - b
     */
    public static double[][] subtract(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }

    /**
     * Element wise subtraction
     *
     * @param a scaler
     * @param b matrix
     * @return c = a - b
     */
    public static double[][] subtract(double a, double[][] b) {
        int m = b.length;
        int n = b[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a - b[i][j];
            }
        }
        return c;
    }

    /**
     * @param a matrix
     * @param b matrix
     * @return c = a * b
     */
    public static double[][] dot(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < n1; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x matrix
     * @return y = a * x
     */
    public static double[][] multiply(double[][] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        if (x.length != m || x[0].length != n) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x[j][i];
            }
        }
        return y;
    }

    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x scaler
     * @return y = a * x
     */
    public static double[][] multiply(double x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x;
            }
        }
        return y;
    }

    /**
     * Element wise power
     *
     * @param x matrix
     * @param a scaler
     * @return y
     */
    public static double[][] power(double[][] x, int a) {
        int m = x.length;
        int n = x[0].length;

        double[][] y = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                y[i][j] = Math.pow(x[i][j], a);
            }
        }
        return y;
    }

    /**
     * @param a matrix
     * @return shape of matrix a
     */
    public static String shape(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        String Vshape = "(" + m + "," + n + ")";
        return Vshape;
    }

    /**
     * @param a matrix
     * @return sigmoid of matrix a
     */
    public static double[][] sigmoid(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (1.0 / (1 + Math.exp(-a[i][j])));
            }
        }
        return z;
    }

    /**
     * Element wise division
     *
     * @param a scaler
     * @param x matrix
     * @return x / a
     */
    public static double[][] divide(double[][] x, int a) {
        int m = x.length;
        int n = x[0].length;

        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (x[i][j] / a);
            }
        }
        return z;
    }
    
    /**
     * Element wise division
     *
     * @param A matrix
     * @param Y matrix
     * @param batch_size scaler
     * @return loss
     */
    public static double cross_entropy(int batch_size, double[][] Y, double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (Y[i][j] * Math.log(A[i][j])) + ((1 - Y[i][j]) * Math.log(1 - A[i][j]));
            }
        }

        double sum = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += z[i][j];
            }
        }
        return -sum / batch_size;
    }
    
    public static double[][] softmax(double[][] z) {
        double[][] zout = new double[z.length][z[0].length];
        double sum = 0.;
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                sum += Math.exp(z[i][j]);
            }
        }
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                zout[i][j] = Math.exp(z[i][j]) / sum;
            }
        }
        return zout;
    }

    public static void print(String val) {
        System.out.println(val);
    }
}