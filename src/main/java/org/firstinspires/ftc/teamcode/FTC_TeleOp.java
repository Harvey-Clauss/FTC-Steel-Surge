package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.helpers.DriveTrain;
import org.firstinspires.ftc.teamcode.helpers.Mechanism;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Main - Controller", group = "Steel Surge")
public class FTC_TeleOp extends LinearOpMode {

    //Custom clamp function
    public static double clamp(double val, double min, double max) { return (double)Math.max(min, (double)Math.min(max, val)); } 
    public DriveTrain driveTrain;
    public Mechanism mechanism;

    @Override
    public void runOpMode() {
        driveTrain = new DriveTrain(this);
        mechanism = new Mechanism(this);        
        // ==========================================
        // INITIAL TELEMETRY
        // ==========================================
        telemetry.addData("Gamepad1", gamepad1 == null ? "MISSING" : "OK");
        telemetry.addData("Gamepad2", gamepad2 == null ? "MISSING" : "OK");

        telemetry.addData("Drive", driveTrain != null && driveTrain.DriveReady ? "READY" : "MISSING MOTOR");

        telemetry.addData("Mechanism", mechanism != null && mechanism.mecReady ? "READY" : "MISSING MOTOR");

        telemetry.update();

        // ==========================================
        // WAIT FOR START
        // ==========================================
        waitForStart();

        if (isStopRequested()) {
            return;
        }

        // ==========================================
        // MAIN LOOP
        // ==========================================
        try {

            while (opModeIsActive()) {
                //#region Drive

                // ==========================================
                // DRIVE
                // ==========================================
                if (driveTrain != null && driveTrain.DriveReady) {

                    double powerMultiplier = .80; // Changes range. Ex: powerMultiplier = .8 -> Range: -0.8 to 0.8 
                    double slowSpeed = .20; //D-pad speed

                    double forward = -gamepad1.left_stick_y;

                    double strafe = gamepad1.left_stick_x;

                    double turn = (Math.abs(gamepad1.right_stick_x) >= .2) ? gamepad1.right_stick_x : 0.0;

                    double divisor = Math.max(1.0, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn)); //Maintains -1.0 to 1.0 range

                    // Each of these evaluates to a number within the range -1.0 to 1.0 inclusively
                    // When d-pad binds are not being pressed stick inputers are used. Otherwise is uses the "slowSpeed"
                    // variable
                    double[] powers = new double[4];
                    powers[0] = !gamepad1.dpad_up ? (forward + strafe + turn) / divisor * powerMultiplier : slowSpeed;
                    powers[1] = !gamepad1.dpad_right ? (forward - strafe - turn) / divisor * powerMultiplier : slowSpeed;
                    powers[2] = !gamepad1.dpad_left ? (forward - strafe + turn) / divisor * powerMultiplier : slowSpeed;
                    powers[3] = !gamepad1.dpad_down ? (forward + strafe - turn) / divisor * powerMultiplier: slowSpeed;

                    telemetry.addData("Drive x-value", gamepad1.left_stick_x);
                    telemetry.addData("Drive y-value", gamepad1.left_stick_y);

                    driveTrain.setPower(powers);
                }
//#endregion

                double intakePower = 0.0;
                double f_midtakePower = 0.0;
                double b_midtakePower = 0.0;
                double outakePower = 0.0;

                //Intake controls
                if (gamepad2.right_trigger >= .20) intakePower = 0.80;
                
                if (gamepad2.right_bumper) intakePower = -0.40;
                
                // Midtake controls
                // Forward on left stick is front midtake and 
                // right on left stick is back midtake.
                
                //Can be used is sync by applying diagonal force
                
                final double midtake_deadzone = .55; // Range: (0.0-1.0) Extreme values are not recommended. This is the deadzone for the midtake motors. The midtake motors will not run unless the stick is pushed past this value.

                //Direction is controlled by stick direction UP & RIGHT is POSTIVE.
                if (Math.abs(gamepad2.left_stick_y) >= midtake_deadzone) f_midtakePower = .80 * Math.signum(gamepad2.left_stick_y); //Forward midtake
                if (Math.abs(gamepad2.left_stick_x) >= midtake_deadzone) b_midtakePower = .80 * Math.signum(gamepad2.left_stick_x); //Back midtake
                
                //Outake controls
                if (gamepad2.left_trigger >= .20) outakePower = .45;

                mechanism.applyInput(intakePower, f_midtakePower, b_midtakePower, outakePower);


                
                // ==========================================
                // TELEMETRY
                // ==========================================
                telemetry.addData("Intake", intakePower);

                telemetry.addData("Front Midtake", f_midtakePower);
                telemetry.addData("Back Midtake", b_midtakePower);

                telemetry.addData("Outake", outakePower);

                telemetry.update();
                idle();
                driveTrain.refresh();
            }

        } finally {
            if (driveTrain != null) driveTrain.stop(); //Drive motors are set to 0 power when the opmode is stopped.
            if (mechanism != null) mechanism.stopAll(); //Mechanism motors are set to 0 power when the opmode is stopped.
        }
    }
}
