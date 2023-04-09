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
            sortRadial();
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
        //convert to rads
        double theta = degrees * (Math.PI/180);
        //translate to (0,0) if not already (0,0)
        this.neighbors = translateBy(-(this.center.x), -(this.center.y)).neighbors;
        //the new graph to be rotated
        RadialGraph rotatedGraph = new RadialGraph(this.center);
        //each point to be rotated and added to new graph
        for (Point p : this.neighbors) {
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            double rotatedX = p.x * cosTheta - p.x * sinTheta;
            double rotatedY = p.y * sinTheta + p.y * cosTheta;
            rotatedGraph.neighbors.add(new Point(p.name, rotatedX, rotatedY));
        }
        //translate back to original spot
        this.neighbors = translateBy((this.center.x), (this.center.y)).neighbors;
        rotatedGraph.neighbors = translateBy((this.center.x), (this.center.y)).neighbors;
        //update the neighbors
        this.neighbors = rotatedGraph.neighbors;
        return rotatedGraph;
    }

    /* move center first and create the new translatedGraph to be returned, then also translate all the points keeping
    * the same name for each point */
    @Override
    public RadialGraph translateBy(double xAmount, double yAmount) {
        //translate the center
        double xTranslated = this.center.x + xAmount;
        double yTranslated = this.center.y + yAmount;
        Point newCenter = new Point(this.center.name, xTranslated,yTranslated);
        //update old center
        this.center = newCenter;
        //create new translatedGraph with the new center
        RadialGraph translatedGraph = new RadialGraph(newCenter);
        //each point needs to be translated and added to the new RadialGraph
        for (Point p : this.neighbors) {
            double px = p.x + xAmount;
            double py = p.y + yAmount;
            translatedGraph.neighbors.add(new Point(p.name, px, py));
        }
        //update the neighbors
        this.neighbors = translatedGraph.neighbors;
        return translatedGraph;
    }

    private void sortRadial() {
        //center is (0,0), if not translate it so that it is.
        this.neighbors = translateBy(-(this.center.x), -(this.center.y)).neighbors;
        //sorting the points in angle order respect to x-axis
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
        this.neighbors = translateBy((this.center.x), (this.center.y)).neighbors;
    }

    @Override
    public String toString() {
        //checking if it is only a center then only return the center if not then do the rest
        if (neighbors == null || neighbors.isEmpty()) {
            return "[(" + center.name + ", " + String.format("%.2f", center.x) + ", " + String.format("%.2f", center.y) + ")]";
        } else {
            sortRadial();
            StringBuilder sb = new StringBuilder();
            sb.append("[(").append(center.name).append(", ").append(String.format("%.2f", center.x)).append(", ").append(String.format("%.2f", center.y)).append(")");
            for (Point p : neighbors) {
                sb.append("; (").append(p.name).append(", ").append(String.format("%.2f", p.x)).append(", ").append(String.format("%.2f", p.y)).append(")");
            }
            sb.append("]");
            return sb.toString();
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
//        System.out.println(g);

        // After this counterclockwise rotation by 90 degrees, "north" must be at (-1, 0), and similarly for all the
        // other radial points. The center, however, must remain exactly where it was.
        g = g.rotateBy(90);

        // [(center, 0.0, 0.0); (south, 1.0, 0.0); (east, 0.0, 1.0); (north, -1.0, 0.0); (west, 0.0, -1.0)]
        System.out.println(g);

        // you should similarly add tests for the translateBy(x, y) method
    }
}
