package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This class represents the drivetrain of a robot, providing methods to control the motors and manage movement.
 * It is designed to work with a four-motor drivetrain configuration, allowing for various movement types
 * @author Harvey Clauss
 * @version 1.0
 * @since 11/10/2026
 */
public class DriveTrain {
    // Define the properties and methods for the DriveTrain class here
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor[] drive;

    /**
     * Indicates whether the drive train is ready for operation.
     */
    public boolean DriveReady;
    
    private LinearOpMode opMode;
    /**      
     * A 2D array that defines the power values for each motor in the drivetrain for different movement types.
     * Each row corresponds to a specific movement type, and each column corresponds to a motor in the drivetrain.
     * The order of the motors is: frontLeft, frontRight, backLeft, backRight.
     * The values are used to control the direction and speed of each motor during different movements.
     * The movement types are defined in the {@link MoveType} enum.
     * @apiNote The values in this array should be used in conjunction with the {@link #applyMovement(double[], double)} method to control the drivetrain.
     * @author Harvey Clauss
     * @since 1.0
     * @see MoveType
    */
    public static final double[][] moveValues =
    {
        {1.0, 1.0, 1.0, 1.0}, // Forward
        {-1.0, -1.0, -1.0, -1.0}, //Backward 
        {1.0, -1.0, 1.0, -1.0}, //Spin right
        {-1.0, 1.0, -1.0, 1.0}, //Spin left
        {1.0, 1.0, -1.0, -1.0}, //Strafe right
        {-1.0, -1.0, 1.0, 1.0} //Strafe left
    };
    /**
     * Enumerates the different types of movement that the drivetrain can perform.
     * @author Harvey Clauss
     * @since 1.0
     */
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
    /**
     * Sets the default direction of each motor in the drivetrain.
     * @author Harvey Clauss
     * @apiNote This method should be called during initialization to set the default direction for each motor.
     */
    private void INIT_setMotorDirection()
    {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);      

        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
 
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    /**
     * Sets the zero power behavior for each motor in the drivetrain.
     * @apiNote This method should be called during initialization to set the zero power behavior for each motor.
     * @author Harvey Clauss
     */
    private void INIT_zeroPowerBehavior()
    {
        for (DcMotor motor : drive) {
            if (motor != null) {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
    }
    /**
    * Reloads the four drivetrain motors and updates the drive-ready status.
    * @apiNote This method should be called if the drivetrain motors are changed or reconfigured during runtime.
    * @return {@code true} when all four drivetrain motors are available
    * @author Harvey Clauss
    */
    public boolean refresh()
    {
        frontLeft = opMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = opMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = opMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = opMode.hardwareMap.get(DcMotor.class, "backRight");
        
        DriveReady = frontLeft != null && frontRight != null && backLeft != null && backRight != null;
        return DriveReady;
    }
    /**
     * Stops all motors in the drivetrain.
     * @apiNote This method does not check if the drivetrain is ready. Ensure that {@link #DriveReady} is true before calling this method.
     * @author Harvey Clauss
     */
    public void stop()
    {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    /**
     * Sets the power for each motor in the drivetrain.
     * @author Harvey Clauss
     * @apiNote This method does not check if the drivetrain is ready. Ensure that {@link #DriveReady} is true before calling this method.
     * @param frontLeftPower    
     * @param frontRightPower
     * @param backLeftPower
     * @param backRightPower
     */
    public void setPower(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower)
    {
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
    /**
     * Sets the power for each motor in the drivetrain.
     * @author Harvey Clauss
     * @apiNote This method does not check if the drivetrain is ready. Ensure that {@link #DriveReady} is true before calling this method.
     * @param powers an array of power values for each motor
     */
    public void setPower(double[] powers)
    {
        frontLeft.setPower(powers[0]);
        frontRight.setPower(powers[1]);
        backLeft.setPower(powers[2]);
        backRight.setPower(powers[3]);
    }
    /**
     * Applies the specified movement values to the drivetrain motors with the given power.
     * @param moveValue contains the direction values for each motor in the drivetrain
     * @param power magnitude of power to apply to the motors (0.0 to 1.0)
     * @author Harvey Clauss
     * @apiNote This method applies power magnitude uniformly to all motors based on the provided movement values.
     * @see #moveValues
     */
    private void applyMovement(double[] moveValue, double power)
    {
        for (int i = 0; i < 4; i++) {
            drive[i].setPower(moveValue[i] * power);
        }
    }
    /**
     * Drives the robot for a specified duration with a given power and movement type.
     * <STRONG>Soon to be deprecated</STRONG>
     * @author Harvey Clauss
     * @apiNote This method does not check if the drivetrain is ready. Ensure that {@link #DriveReady} is true before calling this method.
     * @param milliseconds time in milliseconds to drive
     * @param power the power level to apply to the motors (0.0 to 1.0)
     * @param movement
     */
    public void timeDrive(long milliseconds, double power, MoveType movement)
    {
        applyMovement(DriveTrain.moveValues[movement.ordinal()], power);
        opMode.sleep(milliseconds);
        stop();
    }
    /**
     * Drives the robot for a specified distance with a given power and movement type.
     * @author Harvey Clauss
     * @apiNote This method does not check if the drivetrain is ready. Ensure that {@link #DriveReady} is true before calling this method.
     * @param distance the distance to drive in inches
     * @param power the power level to apply to the motors (0.0 to 1.0)
     * @param movement the type of movement to perform (e.g., FORWARD, BACKWARD, STRAFE_LEFT, STRAFE_RIGHT)
     * @see MoveType
     * @todo Implement the logic to calculate the required encoder counts based on the distance and power
     */
    public void distanceDrive(double distance, double power, MoveType movement)
    {

    }
    public void turn(double angle, double power)
    {

    }
}
