package lab14lib;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    private double normalize() {
        return (state - period / 2) / (double) (period / 2);
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        return normalize();
    }
}
