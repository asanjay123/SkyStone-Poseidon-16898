package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous(name="IMU Testing Stuff", group="")

public class Turn90Degrees extends LinearOpMode
{
    DcMotor                 frontLeft, frontRight, backLeft, backRight;
    int currentOrientation = 0;
//    BNO055IMU               imu;
    Orientation             lastAngles = new Orientation();
    double                  globalAngle, correction;
    double COUNTS_PER_MOTOR_REV;
    double     DRIVE_GEAR_REDUCTION;
    double     WHEEL_DIAMETER_INCHES;
    double COUNTS_PER_INCH;



    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    int objectsSize;
    double position;

    boolean skystoneFound;
    int skystonePos;
    double  ElapsedTime;

    private static final String VUFORIA_KEY =
            "AYB3aIf/////AAABmVsFy8hU5UyTr8m8XGdHacUpYHHs5DLZRsZZEecjQWh++KemDvtSZd2zzmehn3XuFnGrndzQv7Py1zgNj7A27g4QunIq3mUlk/Z7A6vHHjQ0f+DJ1yG2k+r7TcDUakert9hDcu8xmsrzl2Zvw3uj0o4zPB0ASxrTaV8/0J3/Pg3uo5nn6gBk8oBt/jlhJuvtS5l8Ayw/wKJbtbM0xBnQBovbT8GVyGO0V/bDN+gdBegIznVYk0rx/2EYXRBB9+Id/DWbdSX/4laPQT0zpc5CHyxLcSY7ZdoBi8+NLn7g80nRFhAiM+KRMeHOghdTu0QHVGXgWVMZR4ZWEdqU+xmPX/X1Hm5MZvYfams5nZP/JjRL";




    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    @Override
    public void runOpMode()
    {




        initVuforia();
        initTfod();
        tfod.activate();
        telemetry.addLine("Ready@");
        telemetry.update();

        waitForStart();



         {
            telemetry.addLine("Position of SkyStone: " + runScanner());
            telemetry.update();
            sleep(8000);

        }

    }



    public int runScanner(){
        int i = 1;
        while (tfod == null) {
        }
        sleep(1000);
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        sleep(2000);
        while (updatedRecognitions == null){

        }

        if (updatedRecognitions.size() < 2){
            return runScanner();
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
            telemetry.update();
            sleep(5000);
            if (recognition.getLabel().equals("Skystone")) {
                skystoneFound = true;
                skystonePos = i;
                break;
            }
            i++;
        }
        if (!skystoneFound){
            return 3;
        }
        return skystonePos;

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
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    private void goToPoint(double updown, double leftright, double movementspeed, double rotatespeed, int rotateangle){
        int dirAngle;
        try {


            dirAngle = (int) Math.toDegrees(Math.atan(updown / leftright));
            if (updown < 0 && leftright < 0) {
                dirAngle = -dirAngle;
            }
            if (updown > 0 && leftright < 0) {
                dirAngle = -dirAngle;
            }
        }catch (Exception e){
            dirAngle = 0;
        }


        if (updown < 0 && leftright == 0){
            dirAngle = 180;
        }

        if (leftright < 0 && updown == 0){
            dirAngle = -90;
        }
        if (leftright < 0 && updown == 0){
            dirAngle = 90;
        }

        rotate(dirAngle, rotatespeed);

        double distance = Math.sqrt(Math.pow(Math.abs(updown), 2) + Math.pow(Math.abs(leftright), 2));


        driveStraightWithCorrectionAndEncoder(distance, distance, movementspeed);


        rotate(-dirAngle + rotateangle, rotatespeed);

    }


    private void resetAngle()
    {
        lastAngles = null; //imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }


    private double getAngle()
    {
        Orientation angles = null; // imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

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
        double  leftPower = 0;
        double rightPower = 0;

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

    private void driveStraightWithCorrection(int time, double power, char direction){
        long initialTime = System.currentTimeMillis();
        double finalTime = initialTime + time*1000;

        resetAngle();

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
        sleep(250);

        if (getAngle() > 0){
            while (getAngle() > 0){
                frontLeft.setPower(.1);
                backLeft.setPower(.1);
                frontRight.setPower(-.1);
                backRight.setPower(-.1);
            }

        }
        if (getAngle() < 0){
            while (getAngle() < 0){
                frontLeft.setPower(-.1);
                backLeft.setPower(-.1);
                frontRight.setPower(.1);
                backRight.setPower(.1);
            }

        }

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }














    private void driveStraightWithCorrectionAndEncoder(double leftInches, double rightInches, double power){


        resetAngle();

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
            if (correction > .1){
                correction = .05;
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


        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }




























    private void strafeWithCorrection(int time, double power, char direction){
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
                if (correction < -.05){
                    correction = -.05;
                }
                frontLeft.setPower(-power - correction);
                backLeft.setPower(power - correction);
                frontRight.setPower(power + correction);
                backRight.setPower(-power + correction);
            }
        }


        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

 /*   public void initIMU(){

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

    } */

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
    }
}