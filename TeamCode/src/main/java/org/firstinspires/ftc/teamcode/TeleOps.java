package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Teleops", group = "")
public class TeleOps extends OpMode {

    double drive;   // Power for forward and back motion
    double strafe;  // Power for left and right motion
    double rotate;  // Power for rotating the robot
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor armmotor;

    Servo   servo;
    Servo servo1;
    Servo servo2;

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

        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo.setPosition(1);
        servo1.setPosition(0.216);
        servo2.setPosition(.02);


    }

    @Override
    public void loop() {

        drive = -gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        rotate = -gamepad1.left_stick_x;

        double frontLeftPower = drive + strafe + rotate;
        double backLeftPower = drive - strafe + rotate;
        double frontRightPower = drive - strafe - rotate;
        double backRightPower = drive + strafe - rotate;

        frontLeft.setPower(frontLeftPower*0.71474/nitro);
        backLeft.setPower(backLeftPower/nitro);
        frontRight.setPower(frontRightPower*0.71474/nitro);
        backRight.setPower(backRightPower/nitro);

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