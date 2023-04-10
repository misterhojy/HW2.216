package geometry;

import java.util.*;

public class RadialGraph extends Shape {
    private Point center;
    private List<Point> neighbors;

    /* constructor with neighbors, check if the edges are the same length away from center */
    public RadialGraph(Point center, List<Point> neighbors) {
        try {
            //got first distance, every other distance must match this
            double dist = Math.sqrt(Math.pow(neighbors.get(0).x - center.x, 2) + Math.pow(neighbors.get(0).y - center.y, 2));
            //getting distance of everyone
            for (Point p : neighbors) {
                double currentDist = Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2));
                //if it doesn't match error
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

    /* create the new rotatedGraph to be return,same center, use formula to rotate each point keep same name, add to the new Graph */
    @Override
    public RadialGraph rotateBy(int degrees) {
        //degrees into rads
        double radians = Math.toRadians(degrees);
        //translate to (0,0) if not already (0,0)
        //new Radial graph to be made
        ArrayList<Point> rotatedPoints = new ArrayList<>();
        RadialGraph rotatedGraph = translateBy(-(center.x), -(center.y));
        //each point to be rotated and added to new graph
        for (Point p : rotatedGraph.neighbors) {
            double rotatedX = p.x * Math.cos(radians) - p.y * Math.sin(radians);
            double rotatedY = p.x * Math.sin(radians) + p.y * Math.cos(radians);
            rotatedPoints.add(new Point(p.name, rotatedX, rotatedY));
        }
        rotatedGraph.neighbors = rotatedPoints;
        //translate back to origin
        rotatedGraph = rotatedGraph.translateBy((center.x), (center.y));
        //update the neighbors
        return rotatedGraph;
    }

    /* move center first and create the new translatedGraph to be returned, then also translate all the points keeping
     * the same name for each point */
    @Override
    public RadialGraph translateBy(double xAmount, double yAmount) {
        //translate the center
        double xTranslatedCenter = center.x + xAmount;
        double yTranslatedCenter = center.y + yAmount;
        Point translatedCenter = new Point(center.name, xTranslatedCenter,yTranslatedCenter);
        //creating translatedGraph with the new center
        RadialGraph translatedGraph = new RadialGraph(translatedCenter);
        translatedGraph.neighbors = new ArrayList<>();
        for (Point p : neighbors) {
            double xTran = p.x + xAmount;
            double yTran = p.y + yAmount;
            translatedGraph.neighbors.add(new Point(p.name, xTran, yTran));
        }
        return translatedGraph;
    }

    private RadialGraph sortAngle() {
        //if it is not at (0,0) already we bring it there
        RadialGraph sortedGraph = translateBy(-(center.x), -(center.y));
        //sorting counterclockwise in respect to x-axis
        sortedGraph.neighbors.sort((p1, p2) -> {
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
        //after comparing translate it back to original
        sortedGraph = sortedGraph.translateBy(center.x, center.y);
        return sortedGraph;
    }

    private double dist(Point a, Point b) {
        return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public String toString() {
        //checking if it is only a center then only return the center if not then do the rest
        if (neighbors == null || neighbors.isEmpty()) {
            return "[" + this.center + "]";
        } else {
            RadialGraph sortedGraph = this.sortAngle();
            StringBuilder sb = new StringBuilder();
            sb.append("[(").append(sortedGraph.center.name).append(", ").append(round(center.x, 1)).append(", ").append(round(center.y, 1)).append(")");
            for (Point p : sortedGraph.neighbors) {
                sb.append("; (").append(p.name).append(", ").append(round(p.x, 1)).append(", ").append(round(p.y,1)).append(")");
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
//        Point toofarsouth = new Point("south", 0, -2);

//        RadialGraph lonely = new RadialGraph(center);
        // Must print: [(center, 0.0, 0.0)]
//        System.out.println(lonely);


        // This line must throw IllegalArgumentException, since the edges will not be of the same length
//        RadialGraph nope = new RadialGraph(center, Arrays.asList(north, toofarsouth, east, west));

        Shape g = new RadialGraph(center, Arrays.asList(north, south, east, west));
        // [(center, 0.0, 0.0); (east, 1.0, 0.0); (north, 0.0, 1.0); (west, -1.0, 0.0); (south, 0.0, -1.0)]
        System.out.println("Original: ");
        System.out.println(g);

        // [(center, 1.0, 0.0); (east, 2.0, 0.0); (north, 1.0, 1.0); (west, 0.0, 0.0); (south, 1.0, -1.0)]
        g = g.translateBy(1,0);
        System.out.println("Translated(1, 0): ");
        System.out.println(g);


//         [(center, 0.0, 0.0); (south, 2.0, 0.0); (east, 1.0, 1.0); (north, 0.0, 0.0); (west, 1.0, -1.0)]
        System.out.println("Rotated(90): ");
        g = g.rotateBy(90);
        System.out.println(g);


        // [(center, 1.5, .5); (south, 2.5, 0.5); (east, 1.5, 1.5); (north, 0.5, 0.5); (west, 1.5, -0.5)]
        System.out.println("Translated(.5, .5): ");
        g = g.translateBy(.5,.5);
        System.out.println(g);

        // [(center, 1.5, .5); (east, 2.5, 0.5); (north, 1.5, 1.5); (west, 0.5, 0.5); (south, 1.5, -0.5)]
        System.out.println("Rotated(270): ");
        g = g.rotateBy(270);
        System.out.println(g);

    }
}