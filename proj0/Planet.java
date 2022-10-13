public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11d;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p){
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double distX = p.xxPos-xxPos;
        double distY = p.yyPos-yyPos;
        return Math.sqrt(distX*distX+distY*distY);
    }

    public double calcForceExertedBy(Planet p) {
        double dist = calcDistance(p);
        return G*mass*p.mass/(dist*dist);
    }

    public double calcForceExertedByX(Planet p){
        double force = calcForceExertedBy(p);
        double dist = calcDistance(p);
        double distX = p.xxPos-xxPos;
        return force*distX/dist;
    }

    public double calcForceExertedByY(Planet p){
        double force = calcForceExertedBy(p);
        double dist = calcDistance(p);
        double distY = p.yyPos-yyPos;
        return force*distY/dist;
    }

    public double calcNetForceExertedByX(Planet[] s) {
        double force=0.0d;
        for(Planet p : s) {
            if(this.equals(p)) {
                continue;
            }
            force += calcForceExertedByX(p);
        }
        return force;
    }

    public double calcNetForceExertedByY(Planet[] s) {
        double force=0.0d;
        for(Planet p : s) {
            if(this.equals(p)) {
                continue;
            }
            force += calcForceExertedByY(p);
        }
        return force;
    }

    public void update(double dt, double forceX, double forceY) {
        xxVel = xxVel + dt * forceX / mass;
        yyVel = yyVel + dt * forceY / mass;
        xxPos = xxPos + dt * xxVel;
        yyPos = yyPos + dt * yyVel;
    }

    public void draw(){
        StdDraw.picture(xxPos,yyPos,"./images/"+imgFileName);
    }
}
