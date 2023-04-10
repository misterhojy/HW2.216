package geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RadialGraphSymmetries implements Symmetries<RadialGraph> {
    @Override
    public boolean areSymmetric(RadialGraph s1, RadialGraph s2) {
        Collection<RadialGraph> syms = symmetriesOf(s1);
        for (RadialGraph changes : syms) {
            if (s2.equals(changes)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<RadialGraph> symmetriesOf(RadialGraph radialGraph) {
        //amount of spoke is amount of symmetries
        int amountOfSpoke;
        int symDegInterval;
        //if it is only a center there is one spoke
        if(radialGraph.getNeighbors() == null){
            amountOfSpoke = 1;
        } else {
            //if not it is the amount of neighbors
            amountOfSpoke = radialGraph.getNeighbors().size();
        }
        //the amount that needs to be added
        symDegInterval = 360/amountOfSpoke;
        int degree = 0;
        //create an array of size
        List<RadialGraph> a = new ArrayList<>(amountOfSpoke);
        a.add(radialGraph);
        //for loop to iterate the remaining of the list and add the degree
        for(int i = 1; i < amountOfSpoke; i++) {
            degree += symDegInterval;
            a.add(radialGraph.rotateBy(degree));
        }
        return a;
    }
}
