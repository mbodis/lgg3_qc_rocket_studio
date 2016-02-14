package sk.svb.lgg3.svb_circlecase_rocket.logic;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by mbodis on 2/13/16.
 */
public class ServoLimits extends SugarRecord {



    // limits
    public static final int LEFT_OFFSET = - 150;
    public static final int RIGHT_OFFSET = 150;

    // rotation range  600 - 2200
    public static int LOCATION_CENTER = 1000; // use as center default

    // speed range 100 - 9999
    public static final int SPEED_NORMAL = 1500;
    public static final int SPEED_FASTEST = 100;
    public static final int SPEED_SLOWEST = 9000;

    // servo limits
    public static final int LOCATION_MIN = 600;
    public static final int LOCATION_MAX = 2200;

    private int rotationCenter = LOCATION_CENTER;
    private int rotationLeftOffset = LEFT_OFFSET;
    private int rotationRightOffset = RIGHT_OFFSET;

    @Ignore
    private int actualPosition = rotationCenter;

    public int getRotationSpeed() {
        return SPEED_NORMAL;
    }

    public int getRotationCenter() {
        return rotationCenter;
    }

    public void incRotationCenter() {
        this.rotationCenter ++;
    }

    public void decRotationCenter() {
        this.rotationCenter --;
    }

    public void setRotationCenter(int rotationCenter) {
        this.rotationCenter = rotationCenter;
    }


    public int getRotationLeftOffset() {
        return rotationLeftOffset;
    }

    public void incRotationLeftOffset() {
        this.rotationLeftOffset ++;
    }

    public void decRotationLeftOffset() {
        this.rotationLeftOffset --;
    }



    public void setRotationLeftOffset(int rotationLeftOffset) {
        this.rotationLeftOffset = rotationLeftOffset;
    }

    public int getRotationRightOffset() {
        return rotationRightOffset;
    }

    public void incRotationRightOffset() {
        this.rotationRightOffset ++;
    }

    public void decRotationRightOffset() {
        this.rotationRightOffset --;
    }

    public void setRotationRightOffset(int rotationRightOffset) {
        this.rotationRightOffset = rotationRightOffset;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(int actualPosition) {
        this.actualPosition = actualPosition;
    }

    public void incActualPosition(int i) {
        if (actualPosition+i <= rotationCenter+rotationRightOffset){
            this.actualPosition += i;
        }
    }

    public void decActualPosition(int d) {
        if (actualPosition-d > rotationCenter+rotationLeftOffset){
            this.actualPosition -= d;
        }
    }
}
