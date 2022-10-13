public class NBody {
    public static double readRadius(String filepath) {
        In in = new In(filepath);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String filepath) {
        In in = new In(filepath);
        in.readInt();
        in.readDouble();
        Planet[] s = new Planet[5];
        for(int i=0;i<s.length;i++){
            s[i] = new Planet(in.readDouble(),in.readDouble(),in.readDouble(),
                    in.readDouble(),in.readDouble(),in.readString());
        }
        return s;
    }

    public static void main(String[] argv) {
        double T = Double.parseDouble(argv[0]);
        double dt = Double.parseDouble(argv[1]);
        String filename = argv[2];
        Planet[] planets = readPlanets(filename);
        double radius = readRadius(filename);
        StdDraw.enableDoubleBuffering();

        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0,0,"./images/starfield.jpg");

        for (Planet p: planets) {
            p.draw();
        }
        StdDraw.show();

        for(double t=0.0d;t<T;t+=dt) {
            int length = planets.length;
            double xForces[] = new double[length];
            double yForces[] = new double[length];

            for(int i = 0; i< length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0; i< length; i++) {
                planets[i].update(dt,xForces[i],yForces[i]);
            }

            StdDraw.picture(0,0,"./images/starfield.jpg");
            for(Planet p: planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
