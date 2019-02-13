package Objects.Utility.Maths;

import static java.lang.Math.*;

// mutable 2D vectors
public final class Vector2D {
    public double x, y;

    // constructor for zero vector
    public Vector2D() {
        x = 0;
        y = 0;
    }

    // constructor for vector with given coordinates
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // constructor that copies the argument vector
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    // set coordinates
    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    // set coordinates based on argument vector
    public Vector2D set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    // compare for equality (note Object type argument)
    public boolean equals(Object o) {
        return this == o;
    }

    // String for displaying vector as text
    public String toString() {
        return "["+x+", "+y+"]";
    }

    //  magnitude (= "length") of this vector
    public double mag() {
        return sqrt(pow(x, 2)+pow(y, 2));
    }

    // angle between vector and horizontal axis in radians in range [-PI,PI]
    // can be calculated using Math.atan2
    public double angle() {
        return Math.atan2(y, x);
    }

    // angle between this vector and another vector in range [-PI,PI]
    public double angle(Vector2D other) {
        return Math.atan2(other.y-y, other.x-x);
    }

    public double polarAngle(Vector2D other) {
        double diffX = other.x-x;
        double diffY = other.y-y;
        double angle = atan(diffY/diffX);

        if(diffX > 0 && diffY > 0) return angle;
        else if(diffX < 0 && diffY > 0) return Math.PI+angle;
        else if(diffX < 0 && diffY < 0) return Math.PI+angle;
        else if(diffX > 0 && diffY < 0) return (Math.PI*2)+angle;
        return 0;
    }

    // add argument vector
    public Vector2D add(Vector2D v) {
        double newX = x + v.x;
        double newY = y + v.y;
        return new Vector2D(newX, newY);
    }

    // add values to coordinates
    public Vector2D add(double x, double y) {
        double newX = this.x + x;
        double newY = this.y + y;
        return new Vector2D(newX, newY);
    }

    // weighted add - surprisingly useful
    public Vector2D addScaled(Vector2D v, double fac) {
        double otherX = v.x * fac;
        double otherY = v.y * fac;
        return this.add(otherX, otherY);
    }

    // subtract argument vector
    public Vector2D subtract(Vector2D v) {
        double newX = this.x - v.x;
        double newY = this.y - v.y;
        return new Vector2D(newX, newY);
    }

    // subtract values from coordinates
    public Vector2D subtract(double x, double y) {
        double newX = this.x - x;
        double newY = this.y - y;
        return new Vector2D(newX, newY);
    }

    // multiply with factor "multiply"
    public Vector2D scale(double fac) {
        double newX = this.x*fac;
        double newY = this.y*fac;
        return new Vector2D(newX, newY);
    }

    public Vector2D invert() {
        return new Vector2D(-this.x, -this.y);
    }

    // rotate by angle given in radians
    public Vector2D rotate(double angle) {
        double newX = cos(angle*x)-sin(angle*y);
        double newY = sin(angle*x)-cos(angle*y);
        return new Vector2D(newX,newY);
    }

    // "dot product" ("scalar product") with argument vector
    public double dot(Vector2D v) {
        return (x*v.x)+(y*v.y);
    }

    // distance to argument vector
    public double dist(Vector2D v) {
        double newX = v.x - x;
        double newY = v.y - y;
        return sqrt(pow(newX, 2) + pow(newY,2));
    }

    public Vector2D getDirectionVector(Vector2D v) {
        double newX = v.x - x;
        double newY = v.y - y;
        return new Vector2D(newX, newY);
    }

    // normalise vector so that magnitude becomes 1 "normalise"
    public Vector2D getUnitVector() {
        double distance = this.mag();
        double newX = x/distance;
        double newY = y/distance;
        return new Vector2D(newX, newY);
    }

    // wrap-around operation, assumes w > 0 and h > 0
    // remember to manage negative values of the coordinates todo
    public Vector2D wrap(double w, double h) {
        return this;
    }

    // construct vector with given polar coordinates
    public static Vector2D polar(double angle, double mag) {
        return new Vector2D(mag*cos(angle), mag*sin(angle));
    }
}