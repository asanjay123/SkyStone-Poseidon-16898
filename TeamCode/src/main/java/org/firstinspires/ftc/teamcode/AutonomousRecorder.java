package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "RECORD", group = "")
public class AutonomousRecorder extends OpMode {
    File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "movements.txt");



    double drive;   // Power for forward and back motion
    double strafe;  // Power for left and right motion
    double rotate;  // Power for rotating the robot
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor armmotor;

    int blIndex = 0;
    int brIndex = 0;
    int flIndex = 0;
    int frIndex = 0;
    int tIndex = 1;
    Servo   servo;
    Servo servo1;
    Servo servo2;

    double nitro;
    double backLeftCurrent;
    double backRightCurrent;
    double frontRightCurrent;
    double frontLeftCurrent;
    ArrayList<Double> backLeftList = new ArrayList<Double>();
    ArrayList<Double> backRightList = new ArrayList<Double>();
    ArrayList<Double> frontLeftList = new ArrayList<Double>();
    ArrayList<Double> frontRightList = new ArrayList<Double>();
    ArrayList<Double> times = new ArrayList<Double>();

    double currentTime;

    public void init(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");
        armmotor = hardwareMap.dcMotor.get("arm");

        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo.setPosition(1);
        servo1.setPosition(0.216);
        servo2.setPosition(.02);

        Scanner in = new Scanner(System.in);
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(in.hasNext()) {

            backLeftList.add(in.nextDouble());
            backRightList.add(in.nextDouble());
            frontLeftList.add(in.nextDouble());
            frontRightList.add(in.nextDouble());
            times.add(in.nextDouble());

        }
        currentTime = System.currentTimeMillis();


    }

    @Override
    public void loop() {


        if((System.currentTimeMillis() - currentTime) >= times.get(tIndex)) {

            blIndex+=1;
            brIndex+=1;
            flIndex+=1;
            frIndex+=1;
            tIndex+=1;
            if(tIndex > (times.size()-1)) {



                int x = 5/0;
            }

            currentTime = System.currentTimeMillis();


        }

        backLeft.setPower(backLeftList.get(blIndex));
        backRight.setPower(backRightList.get(brIndex));
        frontLeft.setPower(frontLeftList.get(flIndex));
        frontRight.setPower(frontRightList.get(frIndex));

    }

}
