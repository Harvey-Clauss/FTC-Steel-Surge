package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Steel Surge One Controller", group = "Steel Surge")
public class NewCode2 extends LinearOpMode {

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

        DcMotor fl = hardwareMap.tryGet(DcMotor.class, "frontLeft");
        DcMotor fr = hardwareMap.tryGet(DcMotor.class, "frontRight");
        DcMotor bl = hardwareMap.tryGet(DcMotor.class, "backLeft");
        DcMotor br = hardwareMap.tryGet(DcMotor.class, "backRight");

        // ==========================================
        // MECHANISM MOTORS
        // ==========================================

        DcMotor intake = hardwareMap.tryGet(DcMotor.class, "intake");
        DcMotor midtake_1 = hardwareMap.tryGet(DcMotor.class, "midtake_1");
        DcMotor outakeLeft = hardwareMap.tryGet(DcMotor.class, "outakeLeft");
        DcMotor outakeRight = hardwareMap.tryGet(DcMotor.class, "outakeRight");

        DcMotor[] drive = {
                fl,
                fr,
                bl,
                br
        };

        DcMotor[] mechanisms = {
                intake,
                midtake_1,
                outakeLeft,
                outakeRight
        };

        boolean driveReady =
                fl != null &&
                fr != null &&
                bl != null &&
                br != null;

        // ==========================================
        // DRIVE MOTOR DIRECTIONS
        // ==========================================

        if (fl != null) {
            fl.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (bl != null) {
            bl.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        if (fr != null) {
            fr.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (br != null) {
            br.setDirection(DcMotorSimple.Direction.FORWARD);
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
        // SERVOS
        // ==========================================

        Servo wedgeServo =
                hardwareMap.get(
                        Servo.class,
                        "wedge_servo"
                );

        Servo wedge2Servo =
                hardwareMap.get(
                        Servo.class,
                        "wedge2_servo"
                );

        /*
         * IMPORTANT:
         *
         * Do NOT set wedge2Servo position here.
         *
         * This prevents the servo from moving
         * when the program starts.
         */

        // false = servo is considered at HOME
        // true = servo has moved approximately 90 degrees
        boolean wedge2Active = false;

        // Used to detect a NEW press of B
        boolean previousB = false;

        // Used only for telemetry
        boolean wedge2HasBeenCommanded = false;

        // ==========================================
        // INITIAL TELEMETRY
        // ==========================================

	 telemtry.addData("Gamepad1", gamepad1 == null ? "MISSING" : "OK")
	 telemtry.addData("Gamepad2", gamepad2 == null ? "MISSING" : "OK")

        telemetry.addData(
                "Drive",
                driveReady ? "READY" : "MISSING MOTOR"
        );

        telemetry.addData(
                "intake",
                intake == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "midtake",
                midtake == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "outakeLeft",
                outakeLeft == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "outakeRight",
                outakeRight == null ? "MISSING" : "OK"
        );

        telemetry.addData(
                "wedge2_servo",
                "READY"
        );

        telemetry.addData(
                "Wedge 2",
                "Press B to move"
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

                    double pfl =
                            (forward + strafe + turn)
                                    / divisor
                                    * 0.80;

                    double pfr =
                            (forward - strafe - turn)
                                    / divisor
                                    * 0.80;

                    double pbl =
                            (forward - strafe + turn)
                                    / divisor
                                    * 0.80;

                    double pbr =
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

                        pfl =
                                gamepad1.dpad_up
                                        ? 0.20
                                        : 0.0;

                        pfr =
                                gamepad1.dpad_right
                                        ? 0.20
                                        : 0.0;

                        pbl =
                                gamepad1.dpad_left
                                        ? 0.20
                                        : 0.0;

                        pbr =
                                gamepad1.dpad_down
                                        ? 0.20
                                        : 0.0;
                    }

                    fl.setPower(pfl);
                    fr.setPower(pfr);
                    bl.setPower(pbl);
                    br.setPower(pbr);
                }

                // ==========================================
                // ORIGINAL WEDGE SERVO
                //
                // LEFT BUMPER = 0.0
                // RIGHT BUMPER = 1.0
                // ==========================================

                if (gamepad1.left_bumper) {

                    wedgeServo.setPosition(0.0);

                } else if (gamepad1.right_bumper) {

                    wedgeServo.setPosition(1.0);
                }

                // ==========================================
                // WEDGE 2 SERVO
                //
                // PRESS B #1:
                // Move approximately 90 degrees
                //
                // PRESS B #2:
                // Return to original position
                //
                // PRESS B #3:
                // Move 90 degrees again
                // ==========================================

                if (gamepad1.b && !previousB) {

                    if (!wedge2Active) {

                        // FIRST PRESS
                        // Move approximately 90 degrees
                        wedge2Servo.setPosition(
                                WEDGE2_ACTIVE
                        );

                        wedge2Active = true;

                    } else {

                        // SECOND PRESS
                        // Return to original position
                        wedge2Servo.setPosition(
                                WEDGE2_HOME
                        );

                        wedge2Active = false;
                    }

                    wedge2HasBeenCommanded = true;
                }

                // Remember B state for next loop
                previousB = gamepad1.b;

                // ==========================================
                // INTAKE / MIDTAKE
                //
                // B IS NO LONGER USED FOR THESE
                // ==========================================

                double intakePower = 0.0;
                double midtakePower = 0.0;

                if (gamepad1.a) {
                    intakePower = 0.80;
                }

                if (gamepad1.y) {
                	intakePower = -0.40;
                }

                if (gamepad1.x) {
                	midtakePower = -0.40;
                }
                

                // ==========================================
                // OUTAKE
                // ==========================================

                double outakePower = 0.0;

                if (
                        gamepad1.right_trigger > 0.20 &&
                        gamepad1.left_trigger <= 0.20
                ) {

                    outakePower = 0.45;

                } else if (
                        gamepad1.left_trigger > 0.20 &&
                        gamepad1.right_trigger <= 0.20
                ) {

                    outakePower = -0.50;
                }

                // ==========================================
                // APPLY MECHANISM POWER
                // ==========================================

                if (intake != null) {

                    intake.setPower(intakePower);
                }

                if (midtake != null) {

                    midtake.setPower(midtakePower);
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
                        "Midtake",
                        midtakePower
                );

                telemetry.addData(
                        "Outake",
                        outakePower
                );

                telemetry.addData(
                        "B Button",
                        gamepad1.b
                );

                if (!wedge2HasBeenCommanded) {

                    telemetry.addData(
                            "Wedge 2",
                            "WAITING FOR FIRST B PRESS"
                    );

                } else if (wedge2Active) {

                    telemetry.addData(
                            "Wedge 2",
                            "90 DEGREE POSITION"
                    );

                    telemetry.addData(
                            "Wedge 2 Position",
                            WEDGE2_ACTIVE
                    );

                } else {

                    telemetry.addData(
                            "Wedge 2",
                            "HOME POSITION"
                    );

                    telemetry.addData(
                            "Wedge 2 Position",
                            WEDGE2_HOME
                    );
                }

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

            /*
             * Do NOT command wedge2Servo here.
             *
             * This prevents the servo from moving
             * automatically when the program stops.
             */
        }
    }
}
