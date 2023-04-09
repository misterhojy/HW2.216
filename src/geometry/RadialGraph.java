package geometry;

import java.util.*;

public class RadialGraph extends Shape {
    private Point center;
    private List<Point> neighbors;

    /* constructor with neighbors, check if the edges are the same length away from center */
    public RadialGraph(Point center, List<Point> neighbors) {
        try {
            double dist = Math.sqrt(Math.pow(neighbors.get(0).x - center.x, 2) + Math.pow(neighbors.get(0).y - center.y, 2));
            for (Point p : neighbors) {
                double currentDist = Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2));
                if (dist != currentDist) {
                    throw new IllegalStateException("Edges are not the same length for creating a RadialGraph");
                }
            }
            this.center = center;
            this.neighbors = neighbors;
        } catch (IllegalStateException exception){
                System.out.println("Caught Exception: " + exception.getMessage());
                System.exit(-1);
        }
    }

    /* lonely RadialGraph, he is by himself */
    public RadialGraph(Point center) {
        this.center = center;
    }

    /* create the new rotatedGraph to be return, keep same center, use formula to rotate each point keep same name and
    * add to the new Graph */
    @Override
    public RadialGraph rotateBy(int degrees) {
        double angleInRadians = degrees * (Math.PI/180);
        RadialGraph rotatedGraph = new RadialGraph(this.center);
        rotatedGraph.neighbors = new ArrayList<>();
        for (Point p : this.neighbors) {
            double x = p.x * Math.cos(angleInRadians) - p.y * Math.sin(angleInRadians);
            double y = p.x * Math.sin(angleInRadians) - p.y * Math.cos(angleInRadians);
            rotatedGraph.neighbors.add(new Point(p.name, x, y));
        }
        return rotatedGraph;
    }

    /* move center first and create the new translatedGraph to be returned, then also translate all the points keeping
    * the same name for each point */
    @Override
    public RadialGraph translateBy(double x, double y) {
        double x_cord = this.center.x + x;
        double y_cord = this.center.y + y;
        Point newCenter = new Point(this.center.name, x_cord,y_cord);
        RadialGraph translatedGraph = new RadialGraph(newCenter);
        translatedGraph.neighbors = new ArrayList<>();
        for (Point p : this.neighbors) {
            double px = p.x + x;
            double py = p.y + y;
            translatedGraph.neighbors.add(new Point(p.name, px, py));
        }
        return translatedGraph;
    }

    @Override
    public String toString() {
        //checking if it is only a center then only return the center if not then do the rest
        if (this.neighbors == null || neighbors.isEmpty()) {
            return "[" + this.center + "]";
        } else {
            //center is (0,0), if not translate it so that it is.
            this.neighbors = translateBy(-(this.center.x), -(this.center.y)).neighbors;
            this.neighbors.sort((p1, p2) -> {
                double p1AngleRadian = Math.atan2(p1.y, p1.x);
                double p2AngleRadian = Math.atan2(p2.y, p2.x);
                // Normalize angle values to the range [0, 2π]
                if (p1AngleRadian < 0) {
                    p1AngleRadian += 2 * Math.PI;
                } if (p2AngleRadian < 0) {
                    p2AngleRadian += 2 * Math.PI;
                }
                // Compare the normalized angle values
                return Double.compare(p1AngleRadian, p2AngleRadian);
            });
            //after comparing translate it back to original
            this.neighbors = translateBy(this.center.x, this.center.y).neighbors;

            StringJoiner sj = new StringJoiner("; ", "[", "]");
            sj.add(this.center.toString());
            for(Point p : this.neighbors) {
                sj.add(p.toString());
            }
            return sj.toString();
        }
    }

    @Override
    public Point center() {
        return this.center;
    }

    /* Driver method given to you as an outline for testing your code. You can modify this as you want, but please keep
     * in mind that the lines already provided here as expected to work exactly as they are (some lines have additional
     * explanation of what is expected) */
    public static void main(String... args) {
        Point center = new Point("center", 0, 0);
        Point east = new Point("east", 1, 0);
        Point west = new Point("west", -1, 0);
        Point north = new Point("north", 0, 1);
        Point south = new Point("south", 0, -1);
        Point toofarsouth = new Point("south", 0, -2);

        // A single node is a valid radial graph.
        RadialGraph lonely = new RadialGraph(center);

        // Must print: [(center, 0.0, 0.0)]
        System.out.println(lonely);


        // This line must throw IllegalArgumentException, since the edges will not be of the same length
//        RadialGraph nope = new RadialGraph(center, Arrays.asList(north, toofarsouth, east, west));

        Shape g = new RadialGraph(center, Arrays.asList(north, south, east, west));

        // Must follow the documentation in the Shape abstract class, and print the following string:
        // [(center, 0.0, 0.0); (east, 1.0, 0.0); (north, 0.0, 1.0); (west, -1.0, 0.0); (south, 0.0, -1.0)]
        System.out.println(g);

        // After this counterclockwise rotation by 90 degrees, "north" must be at (-1, 0), and similarly for all the
        // other radial points. The center, however, must remain exactly where it was.
        g = g.rotateBy(90);

        // [(center, 0.0, 0.0); (south, 1.0, 0.0); (east, 0.0, 1.0); (north, -1.0, 0.0); (west, 0.0, -1.0)]
        System.out.println(g);

        // you should similarly add tests for the translateBy(x, y) method
    }
}
