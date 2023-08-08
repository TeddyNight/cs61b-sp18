package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    private double normalize(double s) {
        return (s - period / 2) / (double) (period / 2);
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        int weirdState = state & (state >>> 3) % period;
        return normalize(weirdState);
    }
}
