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
//    DcMotor armmotor;
//    DcMotor armmotor2;
//
//    Servo   servo;
//    Servo servo1;
//    Servo servo2;
//    Servo hookRight;
//    Servo hookLeft;
//    double position;

    double nitro;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");

//        armmotor = hardwareMap.dcMotor.get("arm");
//        armmotor2 = hardwareMap.dcMotor.get("arm2");
//        hookRight = hardwareMap.servo.get("hookRight");
//        hookLeft = hardwareMap.servo.get("hookLeft");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        armmotor2.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        armmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        armmotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        nitro = 2.0;

//        servo = hardwareMap.servo.get("servo");
//        servo.setPosition(0.14);
//     //   servo.setPosition(position);
//        servo1 = hardwareMap.servo.get("servo1");
//        servo2 = hardwareMap.servo.get("servo2");
//        servo2.setPosition(.6);
//        //servo2.setPosition(servo1.getPosition()+0.5);
//
//        hookLeft.setPosition(0.3);
//        hookRight.setPosition(0.3);


//
//        position = servo;
//        servo.setPosition(position);


    }

    @Override
    public void loop() {




//        if(gamepad1.x) {
//
//            hookRight.setPosition(0.27);
//            hookLeft.setPosition(0.54);
//        }
//
//
//        if(gamepad1.y) {
//
//            hookRight.setPosition(0.6);
//            hookLeft.setPosition(.22);
//
//        }



        drive = -gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        rotate = -gamepad1.left_stick_x;

        double frontLeftPower = drive + strafe - rotate;
        double backLeftPower = drive - strafe - rotate;
        double frontRightPower = drive - strafe + rotate;
        double backRightPower = drive + strafe + rotate;

        frontLeft.setPower(frontLeftPower/nitro);
        backLeft.setPower(backLeftPower/nitro);
        frontRight.setPower(frontRightPower/nitro);
        backRight.setPower(backRightPower/nitro);

        if (gamepad1.a){
            nitro = 1;
        }
        else if (gamepad1.b){
            nitro = 2;
        }

        if (gamepad1.left_trigger > 0){
            nitro = 4;
        }else if (gamepad1.right_trigger > 0) {
            nitro = 1;
        }
        else{
            nitro = 2;

        }

//
//        if (gamepad2.left_bumper){
//            armmotor.setPower(-1);
//            armmotor2.setPower(-1);
//        }else if (gamepad2.right_bumper){
//            armmotor.setPower(1);
//            armmotor2.setPower(1);
//        }else{
//            armmotor.setPower(0);
//            armmotor2.setPower(0);
//        }
//        if (gamepad2.x)
//            position = 0.06;
//        if (gamepad2.y){
//            position = 0.17;
//        }
//        if (gamepad2.b){
//            position = 0.86;
//        }
//        if (gamepad2.a){
//            position = .3;
//        }


//
//        try {
//            servo.setPosition(position);
//        }
//        catch(Exception e) {
//
//            int x = 5;
//
//        }
//
//        if (gamepad2.dpad_up){
//            try{
//                servo1.setPosition(servo1.getPosition() + .01);
//            }catch(Exception e){
//                int x = 5;
//            }
//
//        }
//
//        if (gamepad2.dpad_down){
//            try{
//                servo1.setPosition(servo1.getPosition() - .01);
//            }catch(Exception e){
//                int x = 5;
//            }
//        }
//
//        if (gamepad1.left_stick_button){
//            servo1.setPosition(0);
//        }
//        if (gamepad1.right_stick_button){
//            servo1.setPosition(.6);
//        }
//
//       // servo2.setPosition(0);
//
//        if (gamepad2.dpad_left){
//            try{
//                servo2.setPosition(0);
//            }catch(Exception e){
//                int x = 5;
//            }
//
//        }
//        if (gamepad2.dpad_right){
//            try{
//                servo2.setPosition(0);
//            }catch(Exception e){
//
//                int x = 5;
//            }
//        }

        telemetry.addData("Nitro: ", (nitro == 1) ? "High":( (nitro==2) ? "Medium":"Low"));
//        telemetry.addData("Twisty Position: ", servo2.getPosition());
        telemetry.update();
    }
}
