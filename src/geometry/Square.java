package geometry;

import java.util.*;

public class Square extends Shape {

    private List<Point> points;
    private Point a;
    private Point b;
    private Point c;
    private Point d;

    public List<Point> getPoints() {
        return points;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public Point getC() {
        return c;
    }

    public Point getD() {
        return d;
    }

    public Square(Point a, Point b, Point c, Point d) {
        try {
            //if it not a valid square throw error
           if (!isValidSquare(a, b, c, d)) {
               throw new IllegalArgumentException("The square is not valid");
           }
           //creating a list for all points in order
            this.points = new ArrayList<>(Arrays.asList(a, b, c, d));
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        } catch (IllegalArgumentException ignored){

        }
    }

    //method to check if the points form a square
    private boolean isValidSquare(Point a, Point b, Point c, Point d) {
        //creating an array of all distances, a->b,b->c,c->d. check if all equal
        List<Double> sideDistance = new ArrayList<>(4);
        double initialSideDist = round(dist(a, b), 4);
        double sideDistance1 = round(dist(a, b), 4);
        double sideDistance2 = round(dist(b, c), 4);
        double sideDistance3 = round(dist(c, d), 4);
        double sideDistance4 = round(dist(d, a), 4);
        sideDistance.add(sideDistance1);
        sideDistance.add(sideDistance2);
        sideDistance.add(sideDistance3);
        sideDistance.add(sideDistance4);
        for (Double distance : sideDistance) {
            if (initialSideDist != distance || distance <= 0) {
                return false;
            }
        }
        
        //check diagonals
        double d2 = round(dist(a, c), 4); // from a to c
        double d3 = round(dist(b, d), 4); // from b to d
        return d2 == d3;
    }
    private double dist(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public Point center() {
        double xCenter = (a.x + b.x + c.x + d.x)/4.0;
        double yCenter = (a.y + b.y + c.y + d.y)/4.0;
        return new Point("Center", xCenter, yCenter);
    }

    @Override
    public Square rotateBy(int degrees) {
        //degrees into radians
        double radians = Math.toRadians(degrees);
        //translate to (0,0) if not already (0,0)
        //new Radial graph to be made
        ArrayList<Point> rotatedPoints = new ArrayList<>();
        Square rotatedSquare = (Square) translateBy(-(center().x), -(center().y));
        //rotate counterclockwise
        for(Point p : rotatedSquare.points) {
            double rotatedX = p.x * Math.cos(radians) - p.y * Math.sin(radians);
            double rotatedY = p.x * Math.sin(radians) + p.y * Math.cos(radians);
            rotatedPoints.add(new Point(p.name, rotatedX, rotatedY));
        }
        rotatedSquare.points = rotatedPoints;
        rotatedSquare.a = rotatedSquare.points.get(0);
        rotatedSquare.b = rotatedSquare.points.get(1);
        rotatedSquare.c = rotatedSquare.points.get(2);
        rotatedSquare.d = rotatedSquare.points.get(3);
        //after comparing translate it back to original
        rotatedSquare = (Square) rotatedSquare.translateBy(center().x, center().y);
        return rotatedSquare;
    }

    /* move center first and create the new translatedGraph to be returned, then also translate all the points keeping
     * the same name for each point */
    @Override
    public Shape translateBy(double xAmount, double yAmount) {
        Point translatedA = new Point(a.name, a.x + xAmount, a.y + yAmount);
        Point translatedB = new Point(b.name, b.x + xAmount, b.y + yAmount);
        Point translatedC = new Point(c.name, c.x + xAmount, c.y + yAmount);
        Point translatedD = new Point(d.name, d.x + xAmount, d.y + yAmount);
        return new Square(translatedA,translatedB,translatedC,translatedD);
    }

    private Square sort() {
        //if it is not at (0,0) already we bring it there
        Square sortedSquare = (Square) translateBy(-(center().x), -(center().y));
        //sorting counterclockwise in respect to x-axis
        sortedSquare.points.sort((p1, p2) -> {
            double p1AngleRadian = Math.atan2(p1.y, p1.x);
            double p2AngleRadian = Math.atan2(p2.y, p2.x);
            p1AngleRadian = round(p1AngleRadian, 3);
            p2AngleRadian = round(p2AngleRadian, 3);
            // Normalize angle values to the range [0, 2π]
            if (p1AngleRadian < 0) {
                p1AngleRadian += 2 * Math.PI;
            }
            if (p2AngleRadian < 0) {
                p2AngleRadian += 2 * Math.PI;
            }
            // Compare the normalized angle values
            int angleComparison = Double.compare(p1AngleRadian, p2AngleRadian);
            // If angles are the same, compare by distance from center
            if (angleComparison == 0) {
                double p1DistanceFromCenter = dist(p1, center());
                double p2DistanceFromCenter = dist(p2,center());
                // Compare by distance from center
                return Double.compare(p1DistanceFromCenter, p2DistanceFromCenter);
            } else {
                return angleComparison;
            }
        });
        sortedSquare.a = sortedSquare.points.get(0);
        sortedSquare.b = sortedSquare.points.get(1);
        sortedSquare.c = sortedSquare.points.get(2);
        sortedSquare.d = sortedSquare.points.get(3);
        //after comparing translate it back to original
        sortedSquare = (Square) sortedSquare.translateBy(center().x, center().y);
        return sortedSquare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Square)) {
            return false;
        }
        Square other = (Square) o;
        return (this.a.x == other.a.x) && (this.a.y == other.a.y) && (this.b.x == other.b.x) && (this.b.y == other.b.y) &&
                (this.c.x == other.c.x) && (this.c.y == other.c.y) && (this.d.x == other.d.x) && (this.d.y == other.d.y);
    }

    public Square verticalReflection() {
        Square vertReflectSquare = (Square) translateBy(0, 0);
        // Swap points b and c
        Point newA = new Point(a.name, d.x, d.y);
        Point newC = new Point(c.name, b.x, b.y);
        Point newB = new Point(b.name, c.x, c.y);
        Point newD = new Point(d.name, a.x, a.y);
        //update the new array
        ArrayList<Point> vertReflectPoints = new ArrayList<>();
        vertReflectPoints.add(newA);
        vertReflectPoints.add(newB);
        vertReflectPoints.add(newC);
        vertReflectPoints.add(newD);
        vertReflectSquare.points = vertReflectPoints;
        vertReflectSquare.a = vertReflectSquare.points.get(0);
        vertReflectSquare.b = vertReflectSquare.points.get(1);
        vertReflectSquare.c = vertReflectSquare.points.get(2);
        vertReflectSquare.d = vertReflectSquare.points.get(3);
        return vertReflectSquare;
    }

    public Square horizontalReflection() {
        Square horiReflectSquare = (Square) translateBy(0, 0);
        // Swap points b and c
        Point newA = new Point(a.name, b.x, b.y);
        Point newB = new Point(b.name, a.x, a.y);
        Point newC = new Point(c.name, d.x, d.y);
        Point newD = new Point(d.name, c.x, c.y);
        //update the new array
        ArrayList<Point> horiReflectPoints = new ArrayList<>();
        horiReflectPoints.add(newA);
        horiReflectPoints.add(newB);
        horiReflectPoints.add(newC);
        horiReflectPoints.add(newD);
        horiReflectSquare.points = horiReflectPoints;
        horiReflectSquare.a = horiReflectSquare.points.get(0);
        horiReflectSquare.b = horiReflectSquare.points.get(1);
        horiReflectSquare.c = horiReflectSquare.points.get(2);
        horiReflectSquare.d = horiReflectSquare.points.get(3);
        return horiReflectSquare;
    }

    public Square diagonalReflection() {
        Square diagReflectSquare = (Square) translateBy(0, 0);
        // Swap points b and c
        Point newA = new Point(a.name, c.x, c.y);
        Point newB = new Point(b.name, b.x, b.y);
        Point newC = new Point(c.name, a.x, a.y);
        Point newD = new Point(d.name, d.x, d.y);
        //update the new array
        ArrayList<Point> diagReflectPoints = new ArrayList<>();
        diagReflectPoints.add(newA);
        diagReflectPoints.add(newB);
        diagReflectPoints.add(newC);
        diagReflectPoints.add(newD);
        diagReflectSquare.points = diagReflectPoints;
        diagReflectSquare.a = diagReflectSquare.points.get(0);
        diagReflectSquare.b = diagReflectSquare.points.get(1);
        diagReflectSquare.c = diagReflectSquare.points.get(2);
        diagReflectSquare.d = diagReflectSquare.points.get(3);
        return diagReflectSquare;
    }

    public Square counterDiagonalReflection() {
        Square countDiagReflectSquare = (Square) translateBy(0, 0);
        // Swap points b and c
        Point newA = new Point(a.name, a.x, a.y);
        Point newB = new Point(b.name, d.x, d.y);
        Point newC = new Point(c.name, c.x, c.y);
        Point newD = new Point(d.name, b.x, b.y);
        //update the new array
        ArrayList<Point> countDiagReflectPoints = new ArrayList<>();
        countDiagReflectPoints.add(newA);
        countDiagReflectPoints.add(newB);
        countDiagReflectPoints.add(newC);
        countDiagReflectPoints.add(newD);
        countDiagReflectSquare.points = countDiagReflectPoints;
        countDiagReflectSquare.a = countDiagReflectSquare.points.get(0);
        countDiagReflectSquare.b = countDiagReflectSquare.points.get(1);
        countDiagReflectSquare.c = countDiagReflectSquare.points.get(2);
        countDiagReflectSquare.d = countDiagReflectSquare.points.get(3);
        return countDiagReflectSquare;
    }

    @Override
    public String toString() {
        Square sortedSquare = this.sort();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Point p : sortedSquare.points) {
            sb.append("(").append(p.name).append(", ").append(round(p.x, 2)).append(", ").append(round(p.y,2)).append("); ");
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        return sb.toString();
    }
//    public static void main(String... args) {
//        Point a = new Point("A", 2, 3);
//        Point b = new Point("B", 3, 3);
//        Point c = new Point("C", 3, 2);
//        Point d = new Point("D", 2, 2);
//
//        Square s = new Square(a, b, c, d);
//        System.out.println(s.center());
////
////
//        System.out.println("Original: ");
//        System.out.println(s);
////
////        s  = (Square) s.translateBy(1,0);
////        System.out.println("Translated: ");
////        System.out.println(s);
////
////        s  = s.rotateBy(90);
////        System.out.println("Rotated: ");
////        System.out.println(s);
//
//        s = s.verticalReflection();
//        System.out.println("Vertical Reflect: ");
//        System.out.println(s);
//
//        s = s.verticalReflection();
//        System.out.println("Reset: ");
//        System.out.println(s);
//
//        s = s.horizontalReflection();
//        System.out.println("Horizontal Reflect: ");
//        System.out.println(s);
//
//        s = s.horizontalReflection();
//        System.out.println("Reset: ");
//        System.out.println(s);
//
//        s = s.diagonalReflection();
//        System.out.println("Diag: ");
//        System.out.println(s);
//
//        s = s.diagonalReflection();
//        System.out.println("reset: ");
//        System.out.println(s);
//
//        s = s.counterDiagonalReflection();
//        System.out.println("counDiag: ");
//        System.out.println(s);
//
//        s = s.counterDiagonalReflection();
//        System.out.println("reset: ");
//        System.out.println(s);
//
//    }
}
