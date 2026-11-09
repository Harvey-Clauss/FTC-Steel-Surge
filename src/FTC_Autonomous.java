package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Main - Autonomous", group = "Steel Surge")
public class FTC_Autonomous extends LinearOpMode {


    public static double clamp(double val, double min, double max) { return (double)Math.max(min, (double)Math.min(max, val)); } 
    public final double[][] moveValues =
    {
        {1.0, 1.0, 1.0, 1.0}, // Forward
        {-1.0, -1.0, -1.0, -1.0}, //Backward 
        {1.0, -1.0, 1.0, -1.0}, //Spin right
        {-1.0, 1.0, -1.0, 1.0}, //Spin left
        {1.0, 1.0, -1.0, -1.0}, //Strafe right
        {-1.0, -1.0, 1.0, 1.0} //Strafe left
    };
    public enum MoveType
    {
        FORWARD,
        BACKWARD,
        TURN_RIGHT,
        TURN_LEFT,
        STRAFE_RIGHT,
        STRAFE_LEFT
    }
    public DcMotor[] drive = new DcMotor[4];


    @Override
    public void runOpMode() {

        /*
                        DRIVE MOTORS:
                        gets all four movement wheels.
                
                !!MUST BE USING "Steel Surge - Main" Configuration!!                   
         */
        DcMotor wheel_frontLeft = hardwareMap.tryGet(DcMotor.class, "frontLeft");
        DcMotor wheel_frontRight = hardwareMap.tryGet(DcMotor.class, "frontRight");
        DcMotor wheel_backLeft = hardwareMap.tryGet(DcMotor.class, "backLeft");
        DcMotor wheel_backRight = hardwareMap.tryGet(DcMotor.class, "backRight");

        drive[0] = wheel_frontLeft;
        drive[1] = wheel_frontRight;
        drive[2] = wheel_backLeft;
        drive[3] = wheel_backRight;
        
        boolean driveReady = wheel_frontLeft != null && wheel_frontRight != null && wheel_backLeft != null && wheel_backRight != null;

        // ==========================================
        // DRIVE MOTOR DIRECTIONS
        // ==========================================
        if (wheel_frontLeft != null) wheel_frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);      

        if (wheel_backLeft != null) wheel_backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
 
        if (wheel_frontRight != null) wheel_frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        if (wheel_backRight != null) wheel_backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        
        // ==========================================
        // DRIVE MOTOR SETUP
        // ==========================================      
        for (DcMotor motor : drive) {
            if (motor != null) {
                motor.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                motor.setPower(0);
            }
        }
        
        telemetry.addData("Status", driveReady ? "Ready" : "Drivers not setup");

        // ==========================================
        // WAIT FOR START
        // ==========================================
        waitForStart();
        
        if (isStopRequested()) {
            return;
        }

        timeDrive(1000, 0.5, MoveType.FORWARD);
        sleep(100);
        timeDrive(200, 0.5, MoveType.STRAFE_RIGHT);
        
        sleep(500);
        
        timeDrive(200, 0.5, MoveType.STRAFE_LEFT);
        sleep(100);
        timeDrive(850, 0.5, MoveType.BACKWARD);
    }
    public void stopDrive() {for (DcMotor motor : drive) motor.setPower(0);}
    public void timeDrive(long milli, double power, MoveType movement)
    {
        applyMovement(moveValues[movement.ordinal()], power);
        sleep(milli);
        stopDrive();
    }
    public void applyMovement(double[] moveValue, double power)
    {
        for (int i = 0; i < 4; i++) drive[i].setPower(moveValue[i] * power);
    }
}
