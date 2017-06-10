package rabin.utils;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Jacobi {

    private static final int[] jacobiTable = {0, 1, 0, -1, 0, -1, 0, 1};

    /**
     * Computes the value of the Jacobi symbol (A|B). The following properties
     * hold for the Jacobi symbol which makes it a very efficient way to
     * evaluate the Legendre symbol
     * <p>
     * (A|B) = 0 IF gcd(A,B) &gt; 1<br>
     * (-1|B) = 1 IF n = 1 (mod 1)<br>
     * (-1|B) = -1 IF n = 3 (mod 4)<br>
     * (A|B) (C|B) = (AC|B)<br>
     * (A|B) (A|C) = (A|CB)<br>
     * (A|B) = (C|B) IF A = C (mod B)<br>
     * (2|B) = 1 IF N = 1 OR 7 (mod 8)<br>
     * (2|B) = 1 IF N = 3 OR 5 (mod 8)
     *
     * @param A integer value
     * @param B integer value
     * @return value of the jacobi symbol (A|B)
     */
    public static int jacobi(BigInteger A, BigInteger B)
    {
        BigInteger a, b, v;
        long k = 1;

        k = 1;

        // test trivial cases
        if (B.equals(ZERO))
        {
            a = A.abs();
            return a.equals(ONE) ? 1 : 0;
        }

        if (!A.testBit(0) && !B.testBit(0))
        {
            return 0;
        }

        a = A;
        b = B;

        if (b.signum() == -1)
        { // b < 0
            b = b.negate(); // b = -b
            if (a.signum() == -1)
            {
                k = -1;
            }
        }

        v = ZERO;
        while (!b.testBit(0))
        {
            v = v.add(ONE); // v = v + 1
            b = b.divide(BigInteger.valueOf(2)); // b = b/2
        }

        if (v.testBit(0))
        {
            k = k * jacobiTable[a.intValue() & 7];
        }

        if (a.signum() < 0)
        { // a < 0
            if (b.testBit(1))
            {
                k = -k; // k = -k
            }
            a = a.negate(); // a = -a
        }

        // main loop
        while (a.signum() != 0)
        {
            v = ZERO;
            while (!a.testBit(0))
            { // a is even
                v = v.add(ONE);
                a = a.divide(BigInteger.valueOf(2));
            }
            if (v.testBit(0))
            {
                k = k * jacobiTable[b.intValue() & 7];
            }

            if (a.compareTo(b) < 0)
            { // a < b
                // swap and correct intermediate result
                BigInteger x = a;
                a = b;
                b = x;
                if (a.testBit(1) && b.testBit(1))
                {
                    k = -k;
                }
            }
            a = a.subtract(b);
        }

        return b.equals(ONE) ? (int)k : 0;
    }


    public static void main(String[] args) {
        int x = 18;
        int y = 31;
        System.out.println(jacobi(BigInteger.valueOf(x), BigInteger.valueOf(y)));
    }
}
