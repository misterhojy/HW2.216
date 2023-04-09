package arithmetic;

import core.Group;

enum PlusOrMinusOne {
    PLUS(1),
    MINUS_ONE(-1);
    private final int value;
    PlusOrMinusOne(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

public class FiniteGroupOfOrderTwo implements Group<PlusOrMinusOne> {

    @Override
    public PlusOrMinusOne binaryOperation(PlusOrMinusOne one, PlusOrMinusOne other) {
        return (one.getValue() * other.getValue() > 0) ? PlusOrMinusOne.PLUS : PlusOrMinusOne.MINUS_ONE;
    }

    @Override
    public PlusOrMinusOne identity() {
        return PlusOrMinusOne.PLUS;
    }

    @Override
    public PlusOrMinusOne inverseOf(PlusOrMinusOne one) {
        return one;
    }
}

