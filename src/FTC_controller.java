package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Steel Surge One Controller-TEST", group = "Steel Surge")
public class FTC_controller extends LinearOpMode {

    // wedge2_servo positions
    // HOME = starting position
    // ACTIVE = approximately 90 degrees from HOME
    private static final double WEDGE2_HOME = 0.5;
    private static final double WEDGE2_ACTIVE = 1.0;

    @Override
    public void runOpMode() {

        // ==========================================
        // DRIVE MOTORS
        // ==========================================

        DcMotor wheel_frontLeft = hardwareMap.tryGet(DcMotor.class, "frontLeft");
        DcMotor wheel_frontRight = hardwareMap.tryGet(DcMotor.class, "frontRight");
        DcMotor wheel_backLeft = hardwareMap.tryGet(DcMotor.class, "backLeft");
        DcMotor wheel_backRight = hardwareMap.tryGet(DcMotor.class, "backRight");

        // ==========================================
        // MECHANISM MOTORS
        // ==========================================

        DcMotor intake = hardwareMap.tryGet(DcMotor.class, "intake");
        DcMotor midtake_Front = hardwareMap.tryGet(DcMotor.class, "midtake_1");
        CRServo midtake_Back = hardwareMap.tryGet(CRServo.class, "midtake_2");
        DcMotor outakeLeft = hardwareMap.tryGet(DcMotor.class, "outakeLeft");
        DcMotor outakeRight = hardwareMap.tryGet(DcMotor.class, "outakeRight");

        DcMotor[] drive = {
                wheel_frontLeft,
                wheel_frontRight,
                wheel_backLeft,
                wheel_backRight
        };

        DcMotor[] mechanisms = {
                intake,
                midtake_Front,
                outakeLeft,
                outakeRight
        };

        boolean driveReady =
                wheel_frontLeft != null &&
                wheel_frontRight != null &&
                wheel_backLeft != null &&
                wheel_backRight != null;

        // ==========================================
        // DRIVE MOTOR DIRECTIONS
        // ==========================================

        if (wheel_frontLeft != null) {
            wheel_frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (wheel_backLeft != null) {
            wheel_backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        if (wheel_frontRight != null) {
            wheel_frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (wheel_backRight != null) {
            wheel_backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        // ==========================================
        // DRIVE MOTOR SETUP
        // ==========================================

        for (DcMotor motor : drive) {

            if (motor != null) {

                motor.setMode(
                        DcMotor.RunMode.RUN_WITHOUT_ENCODER
                );

                motor.setZeroPowerBehavior(
                        DcMotor.ZeroPowerBehavior.BRAKE
                );

                motor.setPower(0);
            }
        }

        // ==========================================
        // MECHANISM MOTOR SETUP
        // ==========================================

        for (DcMotor motor : mechanisms) {

            if (motor != null) {

                motor.setMode(
                        DcMotor.RunMode.RUN_WITHOUT_ENCODER
                );

                motor.setPower(0);
            }
        }

        // ==========================================
        // INITIAL TELEMETRY
        // ==========================================

     telemetry.addData("Gamepad1", gamepad1 == null ? "MISSING" : "OK");
     telemetry.addData("Gamepad2", gamepad2 == null ? "MISSING" : "OK");

        telemetry.addData(
                "Drive",
                driveReady ? "READY" : "MISSING MOTOR"
        );

        telemetry.addData(
                "intake",
                intake == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "front_midtake",
                midtake_Front == null ? "MISSING" : "OK"
        );
        telemetry.addData("back_midtake", midtake_Back == null ? "MISSING" : "OK");

        telemetry.addData(
                "outakeLeft",
                outakeLeft == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "outakeRight",
                outakeRight == null ? "MISSING" : "OK"
        );



        telemetry.update();

        // ==========================================
        // WAIT FOR START
        // ==========================================

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        // ==========================================
        // IMU
        // ==========================================

        {
            com.qualcomm.robotcore.hardware.IMU sensor =
                    hardwareMap.tryGet(
                            com.qualcomm.robotcore.hardware.IMU.class,
                            "imu"
                    );

            if (sensor != null) {

                try {

                    sensor.initialize(
                            new com.qualcomm.robotcore.hardware.IMU.Parameters(

                                    new com.qualcomm.hardware.rev.RevHubOrientationOnRobot(

                                            com.qualcomm.hardware.rev.RevHubOrientationOnRobot
                                                    .LogoFacingDirection.UP,

                                            com.qualcomm.hardware.rev.RevHubOrientationOnRobot
                                                    .UsbFacingDirection.FORWARD
                                    )
                            )
                    );

                    telemetry.addAction(new Runnable() {

                        double lastYaw = Double.NaN;
                        double totalRotation = 0;
                        double maximumTilt = 0;
                        double maximumTurnSpeed = 0;

                        @Override
                        public void run() {

                            if (!opModeIsActive()) {
                                return;
                            }

                            try {

                                org.firstinspires.ftc.robotcore.external.navigation
                                        .YawPitchRollAngles a =
                                        sensor.getRobotYawPitchRollAngles();

                                org.firstinspires.ftc.robotcore.external.navigation
                                        .AngleUnit unit =
                                        org.firstinspires.ftc.robotcore.external.navigation
                                                .AngleUnit.DEGREES;

                                double yaw =
                                        a.getYaw(unit);

                                double tilt =
                                        Math.hypot(
                                                a.getPitch(unit),
                                                a.getRoll(unit)
                                        );

                                double turnSpeed =
                                        Math.abs(
                                                sensor
                                                        .getRobotAngularVelocity(unit)
                                                        .zRotationRate
                                        );

                                if (!Double.isNaN(lastYaw)) {

                                    totalRotation +=
                                            Math.abs(
                                                    Math.IEEEremainder(
                                                            yaw - lastYaw,
                                                            360
                                                    )
                                            );
                                }

                                lastYaw = yaw;

                                maximumTilt =
                                        Math.max(
                                                maximumTilt,
                                                tilt
                                        );

                                maximumTurnSpeed =
                                        Math.max(
                                                maximumTurnSpeed,
                                                turnSpeed
                                        );

                            } catch (RuntimeException ignored) {
                            }
                        }
                    });

                } catch (RuntimeException ignored) {
                }
            }
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

                if (driveReady) {

                    double forward =
                            -gamepad1.left_stick_y;

                    double strafe =
                            gamepad1.left_stick_x;

                    double turn =
                            gamepad1.right_stick_x;

                    double divisor =
                            Math.max(
                                    1.0,
                                    Math.abs(forward)
                                            + Math.abs(strafe)
                                            + Math.abs(turn)
                            );

                    double power_fl =
                            (forward + strafe + turn)
                                    / divisor
                                    * 0.80;

                    double power_fr =
                            (forward - strafe - turn)
                                    / divisor
                                    * 0.80;

                    double power_bl =
                            (forward - strafe + turn)
                                    / divisor
                                    * 0.80;

                    double power_br =
                            (forward + strafe - turn)
                                    / divisor
                                    * 0.80;

                    // ==========================================
                    // D-PAD SLOW CONTROL
                    // ==========================================

                    if (
                            gamepad1.dpad_up ||
                            gamepad1.dpad_right ||
                            gamepad1.dpad_left ||
                            gamepad1.dpad_down
                    ) {

                        power_fl =
                                gamepad1.dpad_up
                                        ? 0.20
                                        : 0.0;

                        power_fr =
                                gamepad1.dpad_right
                                        ? 0.20
                                        : 0.0;

                        power_bl =
                                gamepad1.dpad_left
                                        ? 0.20
                                        : 0.0;

                        power_br =
                                gamepad1.dpad_down
                                        ? 0.20
                                        : 0.0;
                    }

                    wheel_frontLeft.setPower(power_fl);
                    wheel_frontRight.setPower(power_fr);
                    wheel_backLeft.setPower(power_bl);
                    wheel_backRight.setPower(power_br);
                }
//#endregion
                
                // ==========================================
                // INTAKE / MIDTAKE
                //
                // B IS NO LONGER USED FOR THESE
                // ==========================================

                double intakePower = 0.0;
                double f_midtakePower = 0.0;
                double b_midtakePower = 0.0;
                double outakePower = 0.0;


                //Intake controls
                if (gamepad2.right_trigger >= .20) {
                        intakePower = 0.80;
                }
                if (gamepad2.right_bumper) {
                        intakePower = -0.40;
                }

                //Midtake controls
                if (Math.abs(gamepad2.left_stick_y) >= .55) f_midtakePower = .80 * Math.signum(gamepad2.left_stick_y);
                if (Math.abs(gamepad2.left_stick_x) >= .55) b_midtakePower = .80 * Math.signum(gamepad2.left_stick_x);

                //Outake controls
                if (gamepad2.left_trigger >= .20){
                        outakePower = .45;
                }
                
                // ==========================================
                // APPLY MECHANISM POWER
                // ==========================================

                if (intake != null) {

                    intake.setPower(intakePower);
                }

                if (midtake_Front != null) {

                    midtake_Front.setPower(f_midtakePower);
                }
                if (midtake_Back != null) {

                    midtake_Back.setPower(b_midtakePower);
                    //midtake_Back.setPosition(b_midtakePower);
                }

                if (outakeLeft != null) {

                    outakeLeft.setPower(
                            -outakePower
                    );
                }

                if (outakeRight != null) {

                    outakeRight.setPower(
                            outakePower
                    );
                }

                // ==========================================
                // TELEMETRY
                // ==========================================

                telemetry.addData(
                        "Intake",
                        intakePower
                );

                telemetry.addData(
                        "front_Midtake",
                        f_midtakePower
                );
                telemetry.addData(
                        "back_Midtake",
                        b_midtakePower
                );

                telemetry.addData(
                        "Outake",
                        outakePower
                );

                telemetry.update();

                idle();
            }

        } finally {

            // ==========================================
            // STOP ALL MOTORS
            // ==========================================

            for (DcMotor motor : drive) {

                if (motor != null) {

                    motor.setPower(0);
                }
            }

            for (DcMotor motor : mechanisms) {

                if (motor != null) {

                    motor.setPower(0);
                }
            }
            //if (midtake_Back != null) midtake_Back.setPower(0);
        }
    }
}
