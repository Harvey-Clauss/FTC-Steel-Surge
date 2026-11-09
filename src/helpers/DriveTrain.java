package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class DriveTrain {
    // Define the properties and methods for the DriveTrain class here
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor[] drive;

    public boolean DriveReady;
    
    private LinearOpMode opMode;

    public static final double[][] moveValues =
    {
        {1.0, 1.0, 1.0, 1.0}, // Forward
        {-1.0, -1.0, -1.0, -1.0}, //Backward 
        {1.0, -1.0, 1.0, -1.0}, //Spin right
        {-1.0, 1.0, -1.0, 1.0}, //Spin left
        {1.0, 1.0, -1.0, -1.0}, //Strafe right
        {-1.0, -1.0, 1.0, 1.0} //Strafe left
    };
    public static enum MoveType
    {
        FORWARD,
        BACKWARD,
        TURN_RIGHT,
        TURN_LEFT,
        STRAFE_RIGHT,
        STRAFE_LEFT
    }

    public DriveTrain(LinearOpMode opMode) {
        this.opMode = opMode;
        frontLeft = opMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = opMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = opMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = opMode.hardwareMap.get(DcMotor.class, "backRight");
        
        DriveReady = frontLeft != null && frontRight != null && backLeft != null && backRight != null;
        drive = new DcMotor[] {
            frontLeft,
            frontRight,
            backLeft,
            backRight
        };

        INIT_setMotorDirection();
        INIT_zeroPowerBehavior();
    }

    private void INIT_setMotorDirection()
    {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);      

        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
 
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    private void INIT_zeroPowerBehavior()
    {
        for (DcMotor motor : drive) {
            if (motor != null) {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
    }

    public boolean refresh()
    {
        frontLeft = opMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = opMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = opMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = opMode.hardwareMap.get(DcMotor.class, "backRight");
        
        DriveReady = frontLeft != null && frontRight != null && backLeft != null && backRight != null;
        return DriveReady;
    }

    public void stop()
    {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    public void setPower(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower)
    {
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
    public void setPower(double[] powers)
    {
        frontLeft.setPower(powers[0]);
        frontRight.setPower(powers[1]);
        backLeft.setPower(powers[2]);
        backRight.setPower(powers[3]);
    }
    private static void applyMovement(double[] moveValue, double power)
    {
        if (driveTrain != null) for (int i = 0; i < 4; i++) driveTrain.drive[i].setPower(moveValue[i] * power);
    }
    public void timeDrive(long milli, double power, MoveType movement)
    {
        applyMovement(DriveTrain.moveValues[movement.ordinal()], power);
        sleep(milli);
        stopDrive();
    }

}