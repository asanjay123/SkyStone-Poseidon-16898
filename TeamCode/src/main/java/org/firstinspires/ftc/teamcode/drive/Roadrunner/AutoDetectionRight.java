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


@Autonomous(name = "Auto Skystone Right ROadrunner")
public class AutoDetectionRight extends LinearOpMode {

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

    @Override
    public void runOpMode(){
        initMotors();
        initVuforia();
        initTfod();
        tfod.activate();
        CameraDevice.getInstance().setField("opti-zoom", "opti-zoom-on");
        CameraDevice.getInstance().setField("zoom", "61");
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);
        telemetry.addLine("Ready@");
        telemetry.update();

        waitForStart();

        ArrayList<Integer> sPosition = new ArrayList<Integer>();
        for (int a = 0; a < 5; a++){
            telemetry.addData("Position of SkyStone: ", runScanner());
            sPosition.add(runScanner());
        }

        servo.setPosition(.14);


        for (int a = 0; a < 5; a++){
            position += sPosition.get(a);

        }

        position = Math.round(position/5);

        telemetry.addData("Position: ", position);
        telemetry.update();

        if (position == 1){

        }
        if (position == 2){

            drive.followTrajectorySync(
                    drive.trajectoryBuilder().strafeTo(new Vector2d(12, 0))
                            .build()
            );

            servo1.setPosition(0);
            sleep(500);


            drive.followTrajectorySync(
                    drive.trajectoryBuilder().back(3)
                            .build()
            );

            drive.turnSync(Math.toRadians(-90));

            drive.followTrajectorySync(
                    drive.trajectoryBuilder().forward(52)
                            .build()
            );

            drive.turnSync(Math.toRadians(90));

            servo1.setPosition(0);

        }
        if (position == 3){

        }





    }


    public void initMotors(){
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
    }

    public int runScanner(){
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
                return i;
            }
            i++;
        }
        return 1;

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
        tfodParameters.minimumConfidence = 0.65;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

}
