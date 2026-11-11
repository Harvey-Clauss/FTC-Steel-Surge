package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Mechanism {
    private DcMotor intake;
    private DcMotor midtake_Front;
    private CRServo midtake_Back;
    private DcMotor outakeLeft;
    private DcMotor outakeRight;

    private DcMotor[] motors = new DcMotor[5];

    public boolean mecReady;

    public double intakeMaxPower = 1.0;
    public double midtakeMaxPower = 1.0;
    public double outakeMaxPower = 1.0;


    public Mechanism(LinearOpMode opMode)
    {
        intake = opMode.hardwareMap.tryGet(DcMotor.class, "intake");
        midtake_Front = opMode.hardwareMap.tryGet(DcMotor.class, "midtake_1");
        midtake_Back = opMode.hardwareMap.tryGet(CRServo.class, "midtake_2");
        outakeLeft = opMode.hardwareMap.tryGet(DcMotor.class, "outakeLeft");
        outakeRight = opMode.hardwareMap.tryGet(DcMotor.class, "outakeRight");

        motors[0] = intake;
        motors[1] = midtake_Front;
        motors[2] = outakeLeft;
        motors[3] = outakeRight;

        refresh();
    }

    public boolean refresh()
    {
        mecReady = intake != null && midtake_Front != null && midtake_Back != null && outakeLeft != null && outakeRight != null;
        return mecReady;
    }

    public void INIT_setMode()
    {
        for (DcMotor motor : motors) {
            if (motor != null) {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setPower(0);
            }
        }
        midtake_Back.setPower(0);
    }
    public void applyInput(double intakePower, double midtakePower, double outakePower) {
        if (intake != null) {
            intake.setPower(intakePower);
        }
        if (midtake_Front != null) {
            midtake_Front.setPower(midtakePower);
        }
        if (midtake_Back != null) {
            midtake_Back.setPower(midtakePower);
        }
        if (outakeLeft != null) {
            outakeLeft.setPower(outakePower);
        }
        if (outakeRight != null) {
            outakeRight.setPower(outakePower);
        }
    }
    public void applyInput(double intakePower, double front_midtakePower, double back_midtakePower, double outakePower) {
        if (intake != null) {
            intake.setPower(intakePower);
        }
        if (midtake_Front != null) {
            midtake_Front.setPower(front_midtakePower);
        }
        if (midtake_Back != null) {
            midtake_Back.setPower(back_midtakePower);
        }
        if (outakeLeft != null) {
            outakeLeft.setPower(outakePower);
        }
        if (outakeRight != null) {
            outakeRight.setPower(outakePower);
        }
    }
    public void stopAll() {
        applyInput(0, 0, 0, 0);
    }
}