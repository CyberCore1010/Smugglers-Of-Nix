package Objects.Utility;

import java.util.Map;

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
        return this.equals(o);
    }

    // String for displaying vector as text
    public String toString() {
        return "["+x+", "+y+"]";
    }

    //  magnitude (= "length") of this vector
    public double mag() {
        return (x*x)+(y*y);
    }

    // angle between vector and horizontal axis in radians in range [-PI,PI]
    // can be calculated using Math.atan2
    public double angle() {
        return Math.atan2(y, x);
    }

    // angle between this vector and another vector in range [-PI,PI]
    public double angle(Vector2D other) {
        return Math.atan2(y, x) - Math.atan2(other.y, other.x);
    }

    // add argument vector
    public Vector2D add(Vector2D v) {
        x += v.x;
        y += v.y;
        return this;
    }

    // add values to coordinates
    public Vector2D add(double x, double y) {
        this.x = this.x+x;
        this.y = this.y+y;
        return this;
    }

    // weighted add - surprisingly useful todo
    public Vector2D addScaled(Vector2D v, double fac) {
        return this;
    }

    // subtract argument vector
    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    // subtract values from coordinates
    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    // multiply with factor todo
    public Vector2D mult(double fac) {
        return this;
    }

    // rotate by angle given in radians todo
    public Vector2D rotate(double angle) {
        return this;
    }

    // "dot product" ("scalar product") with argument vector todo
    public double dot(Vector2D v) {
        return 0;
    }

    // distance to argument vector todo
    public double dist(Vector2D v) {
        return 0;
    }

    // normalise vector so that magnitude becomes 1 todo
    public Vector2D normalise() {
        return this;
    }

    // wrap-around operation, assumes w> 0 and h>0
    // remember to manage negative values of the coordinates todo
    public Vector2D wrap(double w, double h) {
        return this;
    }

    // construct vector with given polar coordinates
    public static Vector2D polar(double angle, double mag) {
        return new Vector2D(mag*Math.cos(angle), mag*Math.sin(angle));
    }

}