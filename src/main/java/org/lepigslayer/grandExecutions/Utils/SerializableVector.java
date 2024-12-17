package org.lepigslayer.grandExecutions.Utils;

import org.bukkit.util.Vector;

import java.io.Serializable;

public class SerializableVector implements Serializable {
    private double x;
    private double y;
    private double z;
    private SerializableVector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public static SerializableVector of(Object vector) {
        return new SerializableVector((Vector) vector);
    }

    public static Vector as(Object serializableVector) {
        SerializableVector vector = (SerializableVector) serializableVector;
        return new Vector(vector.x, vector.y, vector.z);
    }
}
