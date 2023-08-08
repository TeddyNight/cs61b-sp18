package lab14lib;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double factor;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    private double normalize() {
        return (state - period / 2) / (double) (period / 2);
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        if (state == 0) {
            period = (int) Math.floor(period * factor);
        }
        return normalize();
    }
}
