package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveREV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Autonomous(name = "Auto Skystone Right Wile E. Coyote")
@Disabled
public class AutoRightWileECoyote extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    int objectsSize;
    double position;
    boolean skystoneFound;
    int skystonePos;
    double  ElapsedTime;
    private static final String VUFORIA_KEY = "AYB3aIf/////AAABmVsFy8hU5UyTr8m8XGdHacUpYHHs5DLZRsZZEecjQWh++KemDvtSZd2zzmehn3XuFnGrndzQv7Py1zgNj7A27g4QunIq3mUlk/Z7A6vHHjQ0f+DJ1yG2k+r7TcDUakert9hDcu8xmsrzl2Zvw3uj0o4zPB0ASxrTaV8/0J3/Pg3uo5nn6gBk8oBt/jlhJuvtS5l8Ayw/wKJbtbM0xBnQBovbT8GVyGO0V/bDN+gdBegIznVYk0rx/2EYXRBB9+Id/DWbdSX/4laPQT0zpc5CHyxLcSY7ZdoBi8+NLn7g80nRFhAiM+KRMeHOghdTu0QHVGXgWVMZR4ZWEdqU+xmPX/X1Hm5MZvYfams5nZP/JjRL";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    DcMotor                 frontLeft, frontRight, backLeft, backRight;
    int currentOrientation = 0;
    BNO055IMU               imu;
    Orientation lastAngles = new Orientation();
    double                  globalAngle, correction;
    double COUNTS_PER_MOTOR_REV;
    double     DRIVE_GEAR_REDUCTION;
    double     WHEEL_DIAMETER_INCHES;
    double COUNTS_PER_INCH;

    Servo servo;
    Servo servo1;
    Servo hookRight;
    Servo hookLeft;

    @Override
    public void runOpMode(){
        initIMU();
        initMotors();
        //initVuforia();
        //initTfod();
        //tfod.activate();
        //CameraDevice.getInstance().setField("opti-zoom", "opti-zoom-on");
        //CameraDevice.getInstance().setField("zoom", "35");


        telemetry.addLine("Ready@");
        telemetry.update();

        waitForStart();
/**
        tfod.setClippingMargins(100,20,100,850);
        sleep(300);
        if (runScanner()){
            position = 1;
        }else{
            tfod.setClippingMargins(0,0,0,0);
            CameraDevice.getInstance().setField("zoom", "58");
            sleep(1000);
            if (runScanner()){
                position = 2;
            }else{
                position = 3;
            }
        }
*/
        position = 2;

        servo.setPosition(.17);
        servo.setPosition(.17);


        telemetry.addData("Position of SkyStone: ", position);
        telemetry.update();

        //tfod.deactivate();



        servo.setPosition(.17);

        if (position == 1){
        }

        servo.setPosition(.17);

        if (position == 2){


            driveStraightWithCorrectionAndEncoder(24, 24, .4);
            rotate(getAngle(), .2);
            servo1.setPosition(0);
            sleep(750);
            servo.setPosition(.3);

            rotate(115, .4);
            sleep(100);
            driveStraightWithCorrectionAndEncoder(8, 8, .3);


            rotate(-25, .5);
            driveStraightWithCorrectionAndEncoder(65, 65, .4);

            rotate(-90, .7);


            driveStraightWithCorrectionAndEncoder(14, 14, .3);

            servo1.setPosition(1);








        }

        servo.setPosition(.17);

        if (position == 3){
        }

    }


    public void initMotors(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");

        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        COUNTS_PER_MOTOR_REV    = 28;
        DRIVE_GEAR_REDUCTION    = 19.2 ;
        WHEEL_DIAMETER_INCHES   = 3.93701 ;
        COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        hookRight = hardwareMap.servo.get("hookRight");
        hookLeft = hardwareMap.servo.get("hookLeft");

        servo.setPosition(0.85);
        servo1.setPosition(0.6);
        hookRight.setPosition(.22);
        hookLeft.setPosition(.6);

    }

    public boolean runScanner(){
        int i = 2;
        while (tfod == null) {
        }


        sleep(100);
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        sleep(100);
        while (updatedRecognitions == null){

        }



        Collections.sort(updatedRecognitions, new Comparator<Recognition>(){
            @Override
            public int compare(Recognition p1, Recognition p2){
                return (int)(p1.getLeft() - p2.getLeft());
            }

        });

        for (Recognition recognition : updatedRecognitions) {
            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                    recognition.getLeft(), recognition.getTop());
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                    recognition.getRight(), recognition.getBottom());
            telemetry.addData("ALL THE BLOCKS", recognition);
            telemetry.update();
            if (recognition.getLabel().equals("Skystone")) {
                skystoneFound = true;
                skystonePos = i;
                return true;
            }
            i++;
        }
        return false;

    }



    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;


        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.5;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
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

    private void driveStraightWithCorrectionAndEncoder(double leftInches, double rightInches, double power){


        resetAngle();

        if (leftInches < 0){
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        int newLeftTarget;
        int newRightTarget;

        newLeftTarget = backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        newRightTarget = backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);



        backLeft.setTargetPosition(newLeftTarget);
        backRight.setTargetPosition(newRightTarget);

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        while (backLeft.getCurrentPosition() < newLeftTarget &&
                backRight.getCurrentPosition() < newRightTarget){
            correction = checkDirection();
            if (correction > .02){
                correction = .02;
            }

                frontLeft.setPower(power - correction);
                backLeft.setPower(power - correction);
                frontRight.setPower(power + correction);
                backRight.setPower(power + correction);


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
        sleep(250);

        if (leftInches < 0){
            backRight.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }


        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        double  leftPower;
        double rightPower;

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

                double m = Math.abs(getAngle()/degrees);
                if (Math.abs(rightPower * (1-m)) < .1){
                    frontLeft.setPower(-.1);
                    backLeft.setPower(-.1);
                    frontRight.setPower(.1);
                    backRight.setPower(.1);
                }else {
                    frontLeft.setPower(leftPower * (1 - m));
                    backLeft.setPower(leftPower * (1 - m));
                    frontRight.setPower(rightPower * (1 - m));
                    backRight.setPower(rightPower * (1 - m));
                }
            }

        }
        else
        if (degrees > 0){
            while (opModeIsActive() && -getAngle() < degrees) {
                telemetry.addData("Target", degrees);
                telemetry.addData("Actual", getAngle());
                telemetry.update();



                double m = Math.abs(getAngle()/degrees);
                if (Math.abs(leftPower * (1-m)) < .1){
                    frontLeft.setPower(.1);
                    backLeft.setPower(.1);
                    frontRight.setPower(-.1);
                    backRight.setPower(-.1);
                }else{
                    frontLeft.setPower(leftPower * (1 - m));
                    backLeft.setPower(leftPower * (1 - m));
                    frontRight.setPower(rightPower * (1 - m));
                    backRight.setPower(rightPower * (1 - m));
                }


            }
        }


        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);


        resetAngle();
    }







}