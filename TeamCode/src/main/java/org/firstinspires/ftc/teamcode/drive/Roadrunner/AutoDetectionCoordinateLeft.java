package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveREV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Autonomous(name = "Auto Skystone Left Roadrunner w/ Coordinate (Blue)")
public class AutoDetectionCoordinateLeft extends LinearOpMode {

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

    Servo servo;
    Servo servo1;
    Servo hookRight;
    Servo hookLeft;

    @Override
    public void runOpMode(){
        initMotors();
        initVuforia();
        initTfod();
        tfod.activate();
        CameraDevice.getInstance().setField("opti-zoom", "opti-zoom-on");
        CameraDevice.getInstance().setField("zoom", "35");
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);
        telemetry.addLine("Ready@");
        telemetry.update();
        waitForStart();
        hookRight.setPosition(0.6);
        hookLeft.setPosition(0.22);
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
                position = 2;
            }
        }



        telemetry.addData("Position of SkyStone: ", position);
        telemetry.update();

        drive.setPoseEstimate(new Pose2d(-36, 63, -Math.PI/2));

        tfod.deactivate();

        position = 1;

        servo.setPosition(.17);



        if (position == 1){



            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(10)
                            .strafeLeft(10)
                            .forward(20)
                            .build()
            );


            servo1.setPosition(0);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );

            servo.setPosition(.3);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(10)
                            .reverse()
                            .splineTo(new Pose2d(6, 42, Math.PI))
                            .splineTo(new Pose2d(36, 60, -Math.PI/2))
                            .reverse()
                            .splineTo(new Pose2d(40, 33, -Math.PI/2))
                            .build()
            );
            sleep(300);
            // Movie forward
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(8)
                            .build()
            );

            sleep(200);

            servo1.setPosition(.6);
            sleep(200);
            /**
            // Clamp foundation
            hookRight.setPosition(0.26);
            hookLeft.setPosition(0.56);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );

            sleep(300);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .back(60)
                            .build()
            );


            drive.turnSync(Math.toRadians(80));


            drive.setMotorPowers(1, 1, 1, 1);
            sleep(1000);
            drive.setMotorPowers(0,0,0,0);

            drive.setPoseEstimate(new Pose2d(45, 37, 0));




            // Release foundation
            hookRight.setPosition(0.6);
            hookLeft.setPosition(0.22);
            sleep(200);
            */
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .back(20)
                            .build()
            );


            servo.setPosition(.17);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .reverse()
                            .splineTo(new Pose2d(-10, 35, 0) )
                            .splineTo(new Pose2d(-40, 50, -Math.PI/2))
                            .strafeTo(new Vector2d(-53, 30))

                            .build()
            );


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(6)
                            .build()
            );

            servo1.setPosition(0);
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );
            servo.setPosition(.3);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(20)

                            //Move to the board
                            .splineTo(new Pose2d(-12, 28, 0))
                            .splineTo(new Pose2d(44, 10, -Math.PI/2))
                            .build()
            );

            servo1.setPosition(.6);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .reverse()
                            .splineTo(new Pose2d(-15, 20, 0))
                            .build()

            );




        }

        servo.setPosition(.17);

        if (position == 2){


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(10)
                            .strafeRight(3)
                            .forward(20)
                            .build()
            );


            servo1.setPosition(0);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );

            servo.setPosition(.3);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(10)
                            .reverse()
                            .splineTo(new Pose2d(6, 42, Math.PI))
                            .splineTo(new Pose2d(34, 60, -Math.PI/2))
                            .reverse()
                            .splineTo(new Pose2d(35, 33, -Math.PI/2))
                            .build()
            );
            sleep(300);
            // Movie forward
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(6)
                            .build()
            );

            sleep(200);

            servo1.setPosition(.6);
            sleep(200);

//            // Clamp foundation
//            hookRight.setPosition(0.25);
//            hookLeft.setPosition(0.57);
//
//            drive.followTrajectorySync(
//                    drive.trajectoryBuilder()
//                            .forward(2)
//                            .build()
//            );
//
//            sleep(300);
//
//            drive.followTrajectorySync(
//                    drive.trajectoryBuilder()
//                            .back(62)
//                            .build()
//            );
//
//
//            drive.turnSync(Math.toRadians(87));
//
//
//            drive.setMotorPowers(1, 1, 1, 1);
//            sleep(1000);
//            drive.setMotorPowers(0,0,0,0);
//
//            drive.setPoseEstimate(new Pose2d(45, 37, 0));
//
//
//
//
//            // Release foundation
//            hookRight.setPosition(0.6);
//            hookLeft.setPosition(0.22);
//            sleep(200);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .back(5)
                            .build()
            );


            servo.setPosition(.17);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .reverse()
                            .splineTo(new Pose2d(-10, 35, 0) )
                            .splineTo(new Pose2d(-40, 50, -Math.PI/2))
                            .strafeTo(new Vector2d(-57, 30))

                            .build()
            );


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(6)
                            .build()
            );

            servo1.setPosition(0);
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );
            servo.setPosition(.3);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(20)

                            //Move to the board
                            .splineTo(new Pose2d(-10, 28, 0))
                            .splineTo(new Pose2d(42, 23, 0))
                            .build()
            );

            servo1.setPosition(.6);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .reverse()
                            .splineTo(new Pose2d(0, 23, 0))
                            .build()

            );



        }

        servo.setPosition(.17);

        if (position == 3){

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(10)
                            .strafeRight(13)
                            .forward(18)
                            .build()
            );


            servo1.setPosition(0);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );

            servo.setPosition(.3);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(7)
                            .reverse()
                            .splineTo(new Pose2d(6, 42, Math.PI))
                            .splineTo(new Pose2d(36, 55, -Math.PI/2))
                            .reverse()
                            .splineTo(new Pose2d(34, 35, -Math.PI/2))
                            .forward(8)
                            .build()
            );



            servo1.setPosition(.6);
            sleep(200);

            // Clamp foundation
            hookRight.setPosition(0.25);
            hookLeft.setPosition(0.56);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );

            sleep(100);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .back(60)
                            .build()
            );


            drive.turnSync(Math.toRadians(87));


            drive.setMotorPowers(1, 1, 1, 1);
            sleep(1000);
            drive.setMotorPowers(0,0,0,0);

            drive.setPoseEstimate(new Pose2d(45, 37, 0));




            // Release foundation
            hookRight.setPosition(0.6);
            hookLeft.setPosition(0.22);
            sleep(200);

            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .back(5)
                            .build()
            );


            servo.setPosition(.17);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .reverse()
                            .splineTo(new Pose2d(-10, 38, 0) )
                            .splineTo(new Pose2d(-40, 50, -Math.PI/2))
                            .strafeTo(new Vector2d(-61, 35))

                            .build()
            );

            drive.turnSync(Math.toRadians(-15));
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .strafeRight(1)
                            .forward(8)
                            .build()
            );

            servo1.setPosition(0);
            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            .forward(2)
                            .build()
            );
            servo.setPosition(.3);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder()
                            //Move back
                            .back(20)

                            //Move to the board
                            .splineTo(new Pose2d(-10, 28, 0))
                            .splineTo(new Pose2d(40, 23, 0))
                            .build()
            );

            servo1.setPosition(.6);


            drive.setMotorPowers(-1, -1, -1, -1);
            sleep(3000);
            drive.setMotorPowers(0, 0,0,0);



        }


    }


    public void initMotors(){
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        hookRight = hardwareMap.servo.get("hookRight");
        hookLeft = hardwareMap.servo.get("hookLeft");

        servo.setPosition(0.9);
        servo1.setPosition(0.5);

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

}
