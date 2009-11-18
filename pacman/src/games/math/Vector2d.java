package games.math;

public class Vector2d {
    // of course, also require the methods for adding
    // to these vectors
    public double x, y;

    public Vector2d() {
        this(0, 0);
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void add(Vector2d v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void add(Vector2d v, double w) {
        // weighted addition
        this.x += w * v.x;
        this.y += w * v.y;
    }

    public void subtract(Vector2d v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void mul(double fac) {
        x *= fac;
        y *= fac;
    }

    public void div(double den) {
        x /= den;
        y /= den;
    }

    public void limit(double maxMag) {
        double mag = this.mag();
        if (mag > maxMag) {
            this.mul(maxMag / mag);
        }
    }

    public void setMag(double m) {
        if (mag() != 0) { // can still blow up!
            this.mul( m / mag() );
        }
    }

    public void rotate(double theta) {
        // rotate this vector by the angle made to the horizontal by this line
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double nx = x * cosTheta - y * sinTheta;
        double ny = x * sinTheta + y * cosTheta;

        x = nx;
        y = ny;
    }

    public void rotate(Vector2d start, Vector2d end) {
        // rotate this vector by the angle made to the horizontal by this line
        double r = start.dist(end);
        double cosTheta = (end.x - start.x) / r;
        double sinTheta = (end.y - start.y) / r;

        double nx = x * cosTheta - y * sinTheta;
        double ny = x * sinTheta + y * cosTheta;

        x = nx;
        y = ny;
    }

    public double scalarProduct(Vector2d v) {
        return x * v.x + y * v.y;
    }

    public void contraRotate(Vector2d start, Vector2d end) {
        // rotate this vector by the opposite angle made to the horizontal by this line
        double r = start.dist(end);
        double cosTheta = (end.x - start.x) / r;
        double sinTheta = (end.y - start.y) / r;

        double nx = x * cosTheta + y * sinTheta;
        double ny = -x * sinTheta + y * cosTheta;

        x = nx;
        y = ny;
    }

    public void set(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void zero() {
        x = 0.0;
        y = 0.0;
    }

    public String toString() {
        return x + " : " + y;
    }

    public static double sqr(double x) {
        return x * x;
    }

    public double sqDist(Vector2d v) {
        return sqr(x - v.x) + sqr(y - v.y);
    }

    public double mag() {
        return Math.sqrt(sqr(x) + sqr(y));
    }

    public double sqMag() {
        return sqr( x ) + sqr( y );
    }

    public double dist(Vector2d v) {
        return Math.sqrt(sqDist(v));
    }

}
