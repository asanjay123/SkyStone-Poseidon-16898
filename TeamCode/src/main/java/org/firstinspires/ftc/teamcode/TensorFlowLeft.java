/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Tensor Flow Left", group = "Concept")

public class TensorFlowLeft extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    int objectsSize;
    double position;

    boolean skystoneFound;
    int skystonePos;
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
    DcMotor armmotor;

    Servo   servo;
    Servo servo1;
    Servo servo2;

    com.qualcomm.robotcore.util.ElapsedTime runtime = new ElapsedTime();
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */


    /**
     * DO NOT EDIT AT ANY TIME OR U DIE
     */
    private static final String VUFORIA_KEY =
            "AYB3aIf/////AAABmVsFy8hU5UyTr8m8XGdHacUpYHHs5DLZRsZZEecjQWh++KemDvtSZd2zzmehn3XuFnGrndzQv7Py1zgNj7A27g4QunIq3mUlk/Z7A6vHHjQ0f+DJ1yG2k+r7TcDUakert9hDcu8xmsrzl2Zvw3uj0o4zPB0ASxrTaV8/0J3/Pg3uo5nn6gBk8oBt/jlhJuvtS5l8Ayw/wKJbtbM0xBnQBovbT8GVyGO0V/bDN+gdBegIznVYk0rx/2EYXRBB9+Id/DWbdSX/4laPQT0zpc5CHyxLcSY7ZdoBi8+NLn7g80nRFhAiM+KRMeHOghdTu0QHVGXgWVMZR4ZWEdqU+xmPX/X1Hm5MZvYfams5nZP/JjRL";
    /**
     * DO NOT EDIT AT ANY TIME OR U DIE
     */



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
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initValues();
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {

            driveWithEncoder(0.2,4,4,30);

            strafeWithTime(2,0.2,'r');

            driveWithEncoder(0.2,44,44,30);

            while (opModeIsActive()) {
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
                                skystoneFound = true;
                                skystonePos = i;
                            }
                        }
                        telemetry.update();
                    }

                    if(skystoneFound) {

                        break;

                    }
                    if(objectsSize < updatedRecognitions.size()) {

                        objectsSize++;
                        strafeWithTime(0.1,0.4,'l');

                    }

                }
            }
            servo1.setPosition(0.7);

            armmotor.setPower(-0.8);
            sleep(600);

            servo.setPosition(0.08);
            servo1.setPosition(0.216);
            armmotor.setPower(0.8);
            sleep(600);

        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    public void initValues(){
        armmotor = hardwareMap.dcMotor.get("arm");
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        position = 0.8;
        servo.setPosition(position);
        servo1.setPosition(0.216);
        servo2.setPosition(0);

        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");
        hook = hardwareMap.servo.get("hook");

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
}