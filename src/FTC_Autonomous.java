package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.helpers.DriveTrain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Main - Autonomous", group = "Steel Surge")
public class FTC_Autonomous extends LinearOpMode {


    public static double clamp(double val, double min, double max) { return (double)Math.max(min, (double)Math.min(max, val)); } 


    public DriveTrain driveTrain;


    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(this);

        
        telemetry.addData("Status", driveTrain != null && driveTrain.DriveReady ? "Ready" : "Drive train not setup");

        // ==========================================
        // WAIT FOR START
        // ==========================================
        waitForStart();
        
        if (isStopRequested()) {
            return;
        }

        driveTrain.timeDrive(1000, 0.5, DriveTrain.MoveType.FORWARD);
        sleep(100);
        driveTrain.timeDrive(200, 0.5, DriveTrain.MoveType.STRAFE_RIGHT);       
        sleep(500);     
        driveTrain.timeDrive(200, 0.5, DriveTrain.MoveType.STRAFE_LEFT);
        sleep(100);
        driveTrain.timeDrive(850, 0.5, DriveTrain.MoveType.BACKWARD);
    }
}
