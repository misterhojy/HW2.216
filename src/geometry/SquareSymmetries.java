package geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SquareSymmetries implements Symmetries<Square>{
    @Override
    public boolean areSymmetric(Square s1, Square s2) {
        Collection<Square> syms = symmetriesOf(s1);
        for (Square changes : syms) {
            if (s2.equals(changes)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Square> symmetriesOf(Square square) {
        List<Square> a = new ArrayList<>();
        Square rotatedNeg90 = square.rotateBy(90);
        Square rotatedNeg180 = square.rotateBy(180);
        Square rotatedNeg270 = square.rotateBy(270);
        Square vertReflect = square.verticalReflection();
        Square horizReflect = square.horizontalReflection();
        Square diagReflect = square.diagonalReflection();
        Square countDiag = square.counterDiagonalReflection();

        a.add(square);
        a.add(rotatedNeg90);
        a.add(rotatedNeg180);
        a.add(rotatedNeg270);
        a.add(vertReflect);
        a.add(horizReflect);
        a.add(diagReflect);
        a.add(countDiag);

        return a;
    }
}
