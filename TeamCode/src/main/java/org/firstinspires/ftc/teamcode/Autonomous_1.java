package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Autonomous 1", group = "")
public class Autonomous_1 extends LinearOpMode {

    /**Define all values */
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    Servo hook;
    double  ElapsedTime;
    double COUNTS_PER_MOTOR_REV;
    double     DRIVE_GEAR_REDUCTION;
    double     WHEEL_DIAMETER_INCHES;

    double     COUNTS_PER_INCH;
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode(){

        /**Initialize autonomous
         * Strafe to the corner
         * Move backward
         * Latch onto  platform
         * Drive right (Forward straight, turn right)[not rotate]
         * Drive back after platform is rotated 90 degrees
         * Unlatch
         * Drive forward and stop under bar
         */
        initValues();
        driveWithStrafe(0, 0, 0, 0);
        driveWithEncoder(.2, -0.1, -0.1, 30);
        hook.setPosition(0.3);
        driveWithEncoder(.2, 0.1, 0.1, 30);
        driveWithEncoder(.2, -0.1, -0.1, 30);
        hook.setPosition(0.65);
        driveWithEncoder(0.2, .1, .1, 30);



    }

    //Initialize values
    public void initValues(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");
        hook = hardwareMap.servo.get("hook");

        //Defining front and back using clockwise and counterclockwise movement
        // of the wheels
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        double drive = 0;
        double strafe = 1;
        double rotate = 0;

        /**Movements for each motor*/
        double frontLeftPower = drive + strafe - rotate;
        double backLeftPower = drive - strafe - rotate;
        double frontRightPower = drive - strafe + rotate;
        double backRightPower = drive + strafe + rotate;

        /**Revolutions for wheels given by gobuilda*/
        COUNTS_PER_MOTOR_REV    = 28;
        DRIVE_GEAR_REDUCTION    = 19.2 ;
        WHEEL_DIAMETER_INCHES   = 3.93701 ;
        COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    }


    //Method to strafe
    public void driveWithStrafe(double speed, double strafeLeft, double strafeRight, double rotate) {
        int newLeftTarget;
        int newRightTarget;


        newLeftTarget = backLeft.getCurrentPosition() + (int)(strafeLeft * COUNTS_PER_INCH);
        newRightTarget = backRight.getCurrentPosition() + (int)(strafeRight * COUNTS_PER_INCH);


        backLeft.setTargetPosition(newLeftTarget);
        backRight.setTargetPosition(newRightTarget);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();
        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));


        while (
                backLeft.getCurrentPosition() < newLeftTarget &&
                        backRight.getCurrentPosition() < newRightTarget )
        {

            telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    backLeft.getCurrentPosition(),
                    backRight.getCurrentPosition());
            telemetry.update();

        }

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);

        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);

    }

    public void driveWithEncoder(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;



            newLeftTarget = backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);


            backLeft.setTargetPosition(newLeftTarget);
            backRight.setTargetPosition(newRightTarget);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();
            backLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));
            frontLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));


            while (
                      backLeft.getCurrentPosition() < newLeftTarget &&
                backRight.getCurrentPosition() < newRightTarget )
                           {

                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        backLeft.getCurrentPosition(),
                        backRight.getCurrentPosition());
                telemetry.update();

            }

            backLeft.setPower(0);
            backRight.setPower(0);
            frontLeft.setPower(0);
            frontRight.setPower(0);

            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);

    }

}