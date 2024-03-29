package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous(name = "Auto Right Skystone", group = "")
@Disabled
public class AutoRightSkystone extends LinearOpMode {

    /**Define all values */

    private static final String VUFORIA_KEY =
            "AYB3aIf/////AAABmVsFy8hU5UyTr8m8XGdHacUpYHHs5DLZRsZZEecjQWh++KemDvtSZd2zzmehn3XuFnGrndzQv7Py1zgNj7A27g4QunIq3mUlk/Z7A6vHHjQ0f+DJ1yG2k+r7TcDUakert9hDcu8xmsrzl2Zvw3uj0o4zPB0ASxrTaV8/0J3/Pg3uo5nn6gBk8oBt/jlhJuvtS5l8Ayw/wKJbtbM0xBnQBovbT8GVyGO0V/bDN+gdBegIznVYk0rx/2EYXRBB9+Id/DWbdSX/4laPQT0zpc5CHyxLcSY7ZdoBi8+NLn7g80nRFhAiM+KRMeHOghdTu0QHVGXgWVMZR4ZWEdqU+xmPX/X1Hm5MZvYfams5nZP/JjRL";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

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
        initVuforia();
        initTfod();
        tfod.activate();
        initIMU();


        waitForStart();
        position = 0.7;
        servo.setPosition(position);
        sleep(500);
        hook.setPosition(0.65);
        //driveWithStrafe(.2, -200, 0);


        driveWithEncoder(.25, 19.5, 19.5, 30);
        sleep(1000);

        int i = 0;
        while (!runScanner())
        {
//            backLeft.setPower(.15);
//
//            backRight.setPower(-.15);
//            frontLeft.setPower(-.45);
//            frontRight.setPower(.15);
//            sleep(350);
//            strafeWithTime(.03, .5, 'e');
            strafeWithCorrection(.3, .15, 'l');
            //strafeWithTime(.02, .3, 'q');
            //sleep(500);
            i++;
        }

        if (i != 0){
            strafeWithCorrection(.2, .13, 'r');

        }

        strafeWithTime(.24, .4, 'b');
        servo.setPosition(0.14);
        sleep(200);
        driveWithEncoder(.3, 13, 13, 30);
        servo1.setPosition(0);
        sleep(500);
        strafeWithTime(.4, 0.5, 'b');

        if (i==0) {
            strafeWithTime(0.82, .4, 'p');
        }
        else {
            strafeWithTime(0.76, .4, 'p');
        }

        driveWithEncoder(.4, 36, 36, 30);

            strafeWithTime(.1, .3, 'p');
            servo1.setPosition(0.8);
            sleep(500);

            strafeWithTime(1.76, .5, 'b');
            strafeWithTime(.3, .2, 'r');

            if (i>10) {
                driveWithEncoder(.5, 12, 12, 30);

                strafeWithTime(0.63, .52, 'q');
                //strafeWithTime(0.9, .6, 'l');
                strafeWithTime(.6, .4, 'l');

                sleep(200);
                driveWithEncoder(.25, 16, 16, 30);
                sleep(500);
                servo1.setPosition(0);
                sleep(200);

                strafeWithTime(.7, 0.3, 'b');
                strafeWithTime(0.66, .5, 'p');
                driveWithEncoder(.4, 60, 60, 30);
                strafeWithTime(.5, 0.6, 'l');
                servo1.setPosition(0.8);

            }
            else {
                //servo.setPosition(0.7);
                sleep(500);
                strafeWithTime(0.60, .5, 'q');


                //strafeWithTime(.15, .5, 'b');
                //servo.setPosition(0.14);
                sleep(200);
                driveWithEncoder(.3, 13, 13, 30);
                servo1.setPosition(0);
                sleep(200);

                strafeWithTime(.7, 0.3, 'b');
                strafeWithTime(0.55, .5, 'p');
                if (i != 0) {
                    driveWithEncoder(.4, 8, 8, 30);
                    //strafeWithTime(.7, .3, 'l');

                    driveWithEncoder(.4, 50, 50, 30);
                }
                else {
                    driveWithEncoder(.4, 58, 58, 30);

                }
                strafeWithTime(.3, 0.6, 'l');
                servo1.setPosition(0.8);
                strafeWithTime(.35, .35, 'b');
            }
            telemetry.addData("Strafe", i);
            telemetry.update();









        /**
        strafeWithTime(.3, .5, 'b');
        servo.setPosition(0.1);

        driveWithEncoder(.3, 12, 12, 30);
        servo1.setPosition(0);
        sleep(1000);
        strafeWithTime(.25, 0.5, 'b');
        strafeWithTime(1.3, .25, 'p');

        driveWithEncoder(.3, 65, 65, 30);
         */

//        strafeWithTime(1.3, .25, 'q');
//
//        servo.setPosition(0.1);
//        servo1.setPosition(0);
//        sleep(500);
//
//        armmotor.setPower(0.8);
//        sleep(800);
//        armmotor.setPower(0);
//        sleep(800);
//
//        driveWithEncoder(.3, 5, 5, 30);
//        sleep(800);
//        servo1.setPosition(0.8);
//        sleep(800);
//
//        strafeWithTime(0.3, 0.5, 'b');
//        servo.setPosition(0.14);
//
//        armmotor.setPower(-0.8);
//        sleep(800);
//        armmotor.setPower(0);
//
//        strafeWithTime(1.3, .25, 'p');
        /**
        servo1.setPosition(0.8);
        sleep(500);
        //strafeWithTime(1.2, .3, 'p');
        strafeWithTime(.3, .3, 'b');
        strafeWithTime(.7, .3, 'b');

        */
        /**
         sleep(500);
         strafeWithTime(1, .2, 'b');
         servo.setPosition(0);
         sleep(200);
         */

    }



    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.65;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
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


    public boolean runScanner(){
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());

                    if(recognition.getLabel().equals("Skystone")) {
                        return true;
                    }
                }
                telemetry.update();
            }



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