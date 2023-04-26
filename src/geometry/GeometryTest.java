package geometry;

import java.util.Arrays;
import java.util.Collection;


/**
 * This class is given to you as an outline for testing your code. You can modify this as you want, but please keep in
 * mind that the lines already provided here as expected to work exactly as they are.
 *
 * @author Ritwik Banerjee
 */
public class GeometryTest {

     public static void main(String... args) {
//        testRadialGraphSymmetries();
        testSquareSymmetries();
    }

//    private static void testRadialGraphSymmetries() {
//        Point center    = new Point("center", 3, 3);
//
//        Point p1     = new Point("p1", 3, 5);
//
//        Point p2 = new Point("p2", 3, 1);
//
//        Point p3 = new Point("p3", 5, 3);
//
//        Point p4 = new Point("p4", 1, 3);
//
//        Point p5 = new Point("p5", 3, -.5);
//
//
//        RadialGraph g1 = new RadialGraph(center, Arrays.asList(p1, p2, p3, p4, p5));
//        RadialGraph g2 = g1.rotateBy(45);
//        RadialGraph g3 = g1.rotateBy(360);
//        RadialGraph g4 = g1.rotateBy(180);
//
//        RadialGraphSymmetries graphSymmetries = new RadialGraphSymmetries();
//        System.out.println(graphSymmetries.areSymmetric(g1,g2));
//        System.out.println(graphSymmetries.areSymmetric(g1,g3));
//        System.out.println(graphSymmetries.areSymmetric(g3,g4));
//
//        graphSymmetries.areSymmetric(g1, g2); // must return false
//        graphSymmetries.areSymmetric(g1, g3); // must return true
//        graphSymmetries.areSymmetric(g3, g4); // must return true
//
//        // obtain all the symmetries (including the identity) of g1, and print them one by one (remember that printing
//        // will give the string representation of each radial graph, which must follow the specification of Shape's
//        // toString() method)
//        Collection<RadialGraph> symmetries = graphSymmetries.symmetriesOf(g1);
//        for (RadialGraph g : symmetries) System.out.println(g);
//    }

    private static void testSquareSymmetries() {
        Point  origin       = new Point("origin", 0, 0);

        Point  right        = new Point("right", 1, 0);

        Point  upright      = new Point("upright", 1, 1);

        Point  up           = new Point("up", 0, 1);
        Square sq1 = new Square(origin, right, upright, up);
        Square sq2 = sq1.rotateBy(30);
        Square sq3 = sq1.rotateBy(180);

        SquareSymmetries squareSymmetries = new SquareSymmetries();
        System.out.println(squareSymmetries.areSymmetric(sq1, sq2));
        System.out.println(squareSymmetries.areSymmetric(sq1, sq3));
        squareSymmetries.areSymmetric(sq1, sq2); // must return false
        squareSymmetries.areSymmetric(sq1, sq3); // must return true

        // obtain all the 8 symmetries (including the identity) of sq1, and print them one by one (remember that printing
        // will give the string representation of each square, which must follow the specification of Shape's toString()
        // method)
        Collection<Square> symmetries = squareSymmetries.symmetriesOf(sq1);
        for (Square s : symmetries) System.out.println(s);


    }
}
