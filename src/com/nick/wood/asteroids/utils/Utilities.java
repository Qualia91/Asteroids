package com.nick.wood.asteroids.utils;

import javafx.beans.property.SimpleDoubleProperty;

public class Utilities {

    static public double rotateX(double x, double y, double rotationAngle) {
        return (x*Math.cos(rotationAngle)) - (y*Math.sin(rotationAngle));
    }

    static public double rotateY(double x, double y, double rotationAngle) {
        return (y*Math.cos(rotationAngle)) + (x*Math.sin(rotationAngle));
    }

    static public Vector2D rotateVector(Vector2D vec, double rotationAngle) {
        double a = (vec.x*Math.cos(rotationAngle)) - (vec.y*Math.sin(rotationAngle));
        double b = (vec.x*Math.sin(rotationAngle)) + (vec.y*Math.cos(rotationAngle));
        return new Vector2D(a,b);
    }

    static public Vector2D getVector(Vector2D pointOne, Vector2D pointTwo) {
        return new Vector2D(pointTwo.x - pointOne.x, pointTwo.y - pointOne.y);
    }

    static public boolean initialCollisionCheck(Vector2D positionOne, Vector2D positionTwo, int radiusOne, int radiusTwo) {
        // check if x positions are within one largest radius of asteroid {
        double diffx = (positionOne.x - positionTwo.x) * (positionOne.x - positionTwo.x);
        double rsquared = (radiusOne < radiusTwo) ? radiusTwo*radiusTwo : radiusOne*radiusOne;
        if (diffx < rsquared) {
            double diffy = (positionOne.y - positionTwo.y) * (positionOne.y - positionTwo.y);
            return diffy < rsquared;
        }
        return false;
    }

    static public double collisionCheck(Vector2D positionOne, int radiusOne, Vector2D positionTwo, int radiusTwo) {
        double diffXSquared = (positionOne.x - positionTwo.x) * (positionOne.x - positionTwo.x);
        double diffYSquared = (positionOne.y - positionTwo.y) * (positionOne.y - positionTwo.y);
        double diffRadiusSquared = (radiusOne * radiusTwo) + (radiusOne * radiusTwo);
        return diffXSquared + diffYSquared - diffRadiusSquared;
    }

    static public int checkCollision(Vector2D positionOne, int radiusOne, Vector2D positionTwo, int radiusTwo) {
        // first check they are relatively close to each other, otherwise there is no point
        if (initialCollisionCheck(positionOne, positionTwo, radiusOne, radiusTwo)) {
            // Then check for collisions between circles
            double collisionCheck = collisionCheck(positionOne, radiusOne, positionTwo, radiusTwo);
            if (collisionCheck < -5000) return 2;
            else if (collisionCheck < 0) return 1;
        }
        return 0;
    }

    static public double toRadians(double degrees) {
        return (degrees * Math.PI) / 180.0;
    }

    static public boolean lineItersectsCircleInCenter(Vector2D velocity, Vector2D startPosition, double radiusOfCircle) {

        Vector2D endPosition = startPosition.add(velocity.multiply(100));

        double dx = endPosition.x - startPosition.x;
        double dy = endPosition.y - startPosition.y;

        double d_r_squared = (dx*dx) + (dy*dy);

        double modD = (startPosition.x * endPosition.y) - (endPosition.x * startPosition.y);

        double del = (d_r_squared * radiusOfCircle * radiusOfCircle) - (modD * modD);

        return del >= 0;

    }

    static public double distanceBetween(Vector2D a, Vector2D b){
        return Math.sqrt((a.x*b.x) + (a.y*b.y));
    }

    static public boolean inFoV(Vector2D positionOne, Vector2D positionTwo, double lookDirection, double fov, double viewDistance) {
        // TODO: do connection lines from edges of asteroids
        Vector2D connectionLine = getVector(positionOne, positionTwo);
        Vector2D lookLine = rotateVector(new Vector2D(0,1), lookDirection);
        double angleBetween = angleBetweenTwoVectors(connectionLine, lookLine);
        // also check for distance away
        if (Math.abs(angleBetween) <= fov/2) {
            return distanceBetween(positionOne, positionTwo) < viewDistance;
        }
        return false;
    }

    static public int fovDirection(Vector2D positionOne, Vector2D positionTwo, double lookDirection, double fov) {
        // TODO: do connection lines from edges of asteroids
        Vector2D connectionLine = getVector(positionOne, positionTwo);
        Vector2D lookLine = new Vector2D(rotateX(0, 1, lookDirection), rotateY(0, 1, lookDirection));
        double angleBetween = angleBetweenTwoVectors(connectionLine, lookLine);
        if (Math.abs(angleBetween) < fov / 2){
            if (angleBetween < 0) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    static public double angleBetweenTwoVectors(Vector2D vector1, Vector2D vector2) {
        double dotprod = vector1.dot(vector2);
        double mag = vector1.length() * vector2.length();
        return Math.acos(dotprod / mag);
    }

    static public double gradient(Vector2D vector) {
        if (vector.x == 0) {
            return 0;
        }
        return vector.y / vector.x;
    }

    public static double getAngleTo(Vector2D playerPosition, Vector2D targetPosition, double playerRotationAngle) {
        Vector2D connectionLine = getVector(playerPosition, targetPosition);
        Vector2D lookLine = rotateVector(new Vector2D(0,1), playerRotationAngle);
        return angleBetweenTwoVectors(connectionLine, lookLine);
    }

    public static double getDistanceBetween(Vector2D positionOne, Vector2D positionTwo) {
        return getVector(positionOne, positionTwo).length();
    }
}
