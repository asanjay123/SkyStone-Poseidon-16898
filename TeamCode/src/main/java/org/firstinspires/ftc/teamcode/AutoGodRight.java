package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Auto God Right", group = "")
public class AutoGodRight extends LinearOpMode {

    /**Define all values */



    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    Servo   servo;
    Servo hook;
    double  ElapsedTime;
    double COUNTS_PER_MOTOR_REV;
    double     DRIVE_GEAR_REDUCTION;
    double     WHEEL_DIAMETER_INCHES;
    double position;
    DcMotor armmotor;
    Servo servo1;
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double                  globalAngle, correction;


    double     COUNTS_PER_INCH;
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode(){


        /**Initialize autonomous
         * Strafe a certain measurement
         * Move backward about 4 ft
         * Latch onto  platform
         * drive straight
         * Drive right (Forward straight, turn right)[not rotate]
         * Drive back after platform is rotated 90 degrees
         * Unlatch
         * Drive forward and stop under bar
         */
        initValues();

        initIMU();


        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();





        telemetry.addData("1 imu heading", lastAngles.firstAngle);
        telemetry.addData("2 global heading", globalAngle);
        telemetry.update();

        waitForStart();

        servo.setPosition(.14);
        driveStraightWithCorrection(1, .4, 'f');

        servo1.setPosition(0);
        sleep(500);
        driveStraightWithCorrection(.5, .3, 'b');

        //strafeWithCorrection(2.6, .5, 'r');
        //sleep(1000);
        rotate(90, .2);
        driveStraightWithCorrection(4, .35, 'f');
        rotate(-90, .2);
        armmotor.setPower(0.9);
        sleep(850);
        armmotor.setPower(0);
        driveStraightWithCorrection(.65, .3, 'f');

        servo1.setPosition(1);
        sleep(300);
        driveStraightWithCorrection(.4, .2, 'b');
        armmotor.setPower(-0.9);
        sleep(850);
        armmotor.setPower(0);

        //strafeWithCorrection(2.1, 1, 'l');
        rotate(-90, .2);

        driveStraightWithCorrection(2.35, .5, 'f');
        rotate(93, .2);

        driveStraightWithCorrection(.36, .3, 'f');

        servo1.setPosition(0);
        sleep(500);
        //driveStraightWithCorrection(.6, 1, 'b');
        //driveStraightWithCorrection(1, .4, 'f');
        driveStraightWithCorrection(1.5, .2, 'b');
        sleep(500);
        rotate(90, .2);
        driveStraightWithCorrection(3.4, .4, 'f');
        rotate(-90, .2);
        //strafeWithCorrection(3, .5, 'r');
        armmotor.setPower(0.9);
        sleep(850);
        armmotor.setPower(0);
        driveStraightWithCorrection(.75, .3, 'f');

        servo1.setPosition(1);
        sleep(300);


        /**
        servo1.setPosition(1);
        driveStraightWithCorrection(5, .3, 'l');
        */




    }




    public void initValues(){





        servo = hardwareMap.servo.get("servo");
        armmotor = hardwareMap.dcMotor.get("arm");


        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");
        hook = hardwareMap.servo.get("hook");
        servo1 = hardwareMap.servo.get("servo1");


        //Defining front and back using clockwise and counterclockwise movement
        // of the wheels
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //Initialize values.REVERSE);
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
    public void driveWithStrafe(double speed, double strafe, double rotate) {

        int newLeftTarget;
        int newRightTarget;


        newLeftTarget = backLeft.getCurrentPosition() + (int)(strafe * COUNTS_PER_INCH);
        newRightTarget = backRight.getCurrentPosition() + (int)(strafe * COUNTS_PER_INCH);


        backLeft.setTargetPosition(newLeftTarget);
        backRight.setTargetPosition(newRightTarget);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();

        if (strafe > 0){
            //Strafe right
            backLeft.setPower(speed);
            backRight.setPower(speed);
            frontLeft.setPower(speed);
            frontRight.setPower(speed);
        }
        else if (strafe < 0){
            //Strafe left
            backLeft.setPower(-speed);
            backRight.setPower(speed);
            frontLeft.setPower(speed);
            frontRight.setPower(-speed);


        }



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

    public void strafeWithTime(double time, double power, char direction){
        long initialTime = System.currentTimeMillis();
        double finalTime = initialTime + time*1000;

        if (direction == 'l'){
            backLeft.setPower(power);
            backRight.setPower(-power);
            frontLeft.setPower(-power);
            frontRight.setPower(power);
        }

        if (direction == 'r'){
            backLeft.setPower(-power);
            backRight.setPower(power);
            frontLeft.setPower(power);
            frontRight.setPower(-power);
        }
        if (direction == 'b'){
            backLeft.setPower(-power);
            backRight.setPower(-power);
            frontLeft.setPower(-power);
            frontRight.setPower(-power);
        }
        if (direction == 'p'){
            backLeft.setPower(power);
            backRight.setPower(-power);
            frontLeft.setPower(power);
            frontRight.setPower(-power);
        }
        if (direction == 'q'){
            backLeft.setPower(-power);
            backRight.setPower(power);
            frontLeft.setPower(-power);
            frontRight.setPower(power);
        }

        //arcLeft
        if(direction == 'a')  {


            backLeft.setPower(power/7);
            backRight.setPower(power);
            frontLeft.setPower(power/7);
            frontRight.setPower(power);

        }

        if(direction == 'e') {

            backLeft.setPower(power);
            backRight.setPower(power/7);
            frontLeft.setPower(power);
            frontRight.setPower(power/7);


        }
        while (System.currentTimeMillis() < finalTime){

        }
        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void driveWithEncoder(double speed,
                                 double leftInches, double rightInches,
                                 double timeoutS) {
        int newLeftTarget;
        int newRightTarget;



        newLeftTarget = backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        newRightTarget = backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

        if (speed > 0){

            backLeft.setTargetPosition(newLeftTarget);
            backRight.setTargetPosition(newRightTarget);
        }else{
            backLeft.setTargetPosition(-newLeftTarget);
            backRight.setTargetPosition(-newRightTarget);

        }
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();

        backLeft.setPower(speed);
        backRight.setPower(speed);


        frontLeft.setPower(speed);
        frontRight.setPower(speed);


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










    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }


    private double getAngle()
    {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    private double checkDirection()
    {

        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;
        else
            correction = -angle;

        correction = correction * gain;

        return correction;
    }


    private void rotate(double degrees, double power)
    {
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = -power;
            rightPower = power;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = power;
            rightPower = -power;
        }
        else return;

        // set power to rotate.
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);

        if (degrees < 0)
        {

            while (opModeIsActive() && getAngle() < -degrees) {
                telemetry.addData("Target", degrees);
                telemetry.addData("Actual", getAngle());
                telemetry.update();
            }
        }
        else
            while (opModeIsActive() && -getAngle() < degrees) {
                telemetry.addData("Target", degrees);
                telemetry.addData("Actual", getAngle());
                telemetry.update();
            }

        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        frontLeft.setPower(0);


        resetAngle();
    }

    private void driveStraightWithCorrection(double time, double power, char direction){
        long initialTime = System.currentTimeMillis();
        double finalTime = initialTime + time*1000;

        if (direction == 'f') {

            while (System.currentTimeMillis() < finalTime){
                correction = checkDirection();
                if (correction > .1){
                    correction = .05;
                }
                frontLeft.setPower(power - correction);
                backLeft.setPower(power - correction);
                frontRight.setPower(power + correction);
                backRight.setPower(power + correction);
            }
        }

        if (direction == 'b') {
            while (System.currentTimeMillis() < finalTime){
                correction = checkDirection();
                if (correction > .1){
                    correction = .05;
                }
                frontLeft.setPower(-power - correction);
                backLeft.setPower(-power - correction);
                frontRight.setPower(-power + correction);
                backRight.setPower(-power + correction);
            }
        }


        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    private void strafeWithCorrection(double time, double power, char direction){
        long initialTime = System.currentTimeMillis();
        double finalTime = initialTime + time*1000;

        if (direction == 'r') {
            while (System.currentTimeMillis() < finalTime){
                correction = checkDirection();
                if (correction > .1){
                    correction = .05;
                }
                frontLeft.setPower(power - correction);
                backLeft.setPower(-power - correction);
                frontRight.setPower(-power + correction);
                backRight.setPower(power + correction);
            }
        }

        if (direction == 'l') {
            while (System.currentTimeMillis() < finalTime){
                correction = checkDirection();
                if (correction > .1){
                    correction = -.05;
                }
                frontLeft.setPower(-power + correction);
                backLeft.setPower(power + correction);
                frontRight.setPower(power - correction);
                backRight.setPower(-power - correction);
            }
        }


        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void initIMU(){

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();

    }







































}