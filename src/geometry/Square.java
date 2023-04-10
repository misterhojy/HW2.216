package geometry;

import java.util.*;

public class Square extends Shape {

    private List<Point> points;
    private Point a;
    private Point b;
    private Point c;
    private Point d;

    public Square(Point a, Point b, Point c, Point d) {
        try {
            //if it not a valid square throw error
           if (!isValidSquare(a, b, c, d)) {
               throw new IllegalStateException("The square is not valid");
           }
           //creating a list for all points in order
            this.points = new ArrayList<>(Arrays.asList(a, b, c, d));
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        } catch (IllegalStateException exception){
            System.out.println("Caught Exception: " + exception.getMessage());
            System.exit(2);
        }
    }

    //method to check if the points form a square
    private boolean isValidSquare(Point a, Point b, Point c, Point d) {
        //creating an array of all distances, a->b,b->c,c->d. check if all equal
        List<Double> sideDistance = new ArrayList<>(4);
        double initialSideDist = dist(a, b);
        sideDistance.add(dist(a, b));
        sideDistance.add(dist(b, c));
        sideDistance.add(dist(c, d));
        for (Double distance : sideDistance) {
            if (initialSideDist != distance || distance <= 0) {
                return false;
            }
        }
        //all distances
        double d2 = dist(a, b); // from a to b
        double d3 = dist(a, c); // from a to c
        double d4 = dist(a, d); // from a to d

        // If lengths if (p1, p2) and (p1, p3) are same, then
        // following conditions must met to form a square.
        // 1) Square of length of (p1, p4) is same as twice
        // the square of (p1, p2)
        // 2) Square of length of (p2, p3) is same
        // as twice the square of (p2, p4)
        if (d2 == d3 && 2 * d2 == d4 && 2 * dist(b, d) == dist(b, c)) {
            return true;
        }
        // The below two cases are similar to above case
        if (d3 == d4 && 2 * d3 == d2 && 2 * dist(c, b) == dist(c, d)) {
            return true;
        }
        return d2 == d4 && 2 * d2 == d3 && 2 * dist(b, c) == dist(b, d);
    }

    private double dist(Point a, Point b) {
        return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
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
    public String toString() {
        Square sortedSquare = this.sort();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Point p : sortedSquare.points) {
            sb.append("(").append(p.name).append(", ").append(round(p.x, 1)).append(", ").append(round(p.y,1)).append("); ");
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("]");
        return sb.toString();
    }
    public static void main(String... args) {
        Point a = new Point("A", 0, 0);
        Point b = new Point("B", 0, 3);
        Point c = new Point("C", 3, 3);
        Point d = new Point("D", 3, 0);

        Square s = new Square(a, b, c, d);
        System.out.println(s.center());


        System.out.println("Original: ");
        System.out.println(s);

        s  = (Square) s.translateBy(1,0);
        System.out.println("Translated: ");
        System.out.println(s);

        s  = s.rotateBy(90);
        System.out.println("Rotated: ");
        System.out.println(s);

    }
}
