package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@TeleOp(name = "TeleopsRecorder", group = "")
@Disabled
public class MovementRecorder extends OpMode {
    File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "movements.txt");

    double drive;   // Power for forward and back motion
    double strafe;  // Power for left and right motion
    double rotate;  // Power for rotating the robot
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor armmotor;
    ArrayList<Double> backLeftList = new ArrayList<Double>();
    ArrayList<Double> backRightList = new ArrayList<Double>();
    ArrayList<Double> frontLeftList = new ArrayList<Double>();
    ArrayList<Double> frontRightList = new ArrayList<Double>();
    ArrayList<Double> times = new ArrayList<Double>();


    /**
    Servo   servo;
    Servo servo1;
    Servo servo2;
    */
    double nitro;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");
        armmotor = hardwareMap.dcMotor.get("arm");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        nitro = 2.0;

        times.add((double) System.currentTimeMillis());
        /**
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo.setPosition(1);
        servo1.setPosition(0.216);
        servo2.setPosition(.02);
        */

    }

    @Override
    public void loop() {

        drive = -gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        rotate = -gamepad1.left_stick_x;
        int i = 0;

        double frontLeftPower = drive + strafe + rotate;
        double backLeftPower = drive - strafe + rotate;
        double frontRightPower = drive - strafe - rotate;
        double backRightPower = drive + strafe - rotate;

        frontLeft.setPower(frontLeftPower*0.71474/nitro);
        backLeft.setPower(backLeftPower/nitro);
        frontRight.setPower(frontRightPower*0.71474/nitro);
        backRight.setPower(backRightPower/nitro);

        backLeftList.add(backLeftPower/nitro);
        backRightList.add(backRightPower/nitro);
        frontLeftList.add(frontLeftPower/nitro);
        frontRightList.add(frontRightPower/nitro);
        times.add(System.currentTimeMillis() - times.get(i));
        i++;

        try {
            FileWriter fw = new FileWriter(file,true);
            fw.write("\n"+backLeftList.get(backLeftList.size()-1)+" "+backRightList.get(backRightList.size()-1)+" "+frontLeftList.get(frontLeftList.size()-1)+" "+frontRightList.get(frontRightList.size()-1)+"\n");
            fw.close();
        } catch (IOException e) {
            telemetry.addData("Screwed", "");
        }


        if (gamepad1.a){
            nitro = 1;
        }
        if (gamepad1.b){
            nitro = 2;
        }


        if (gamepad1.dpad_up){
            armmotor.setPower(0.5);
        }else if (gamepad1.dpad_down){
            armmotor.setPower(-0.5);
        }else{
            armmotor.setPower(0);
        }

        telemetry.addData("Nitro Type: ", nitro);
        telemetry.update();
    }

}