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
    //DcMotor armmotor;

    Servo   servo;
    Servo servo1;
    Servo servo2;
    Servo hook;
    double position;

    double nitro;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");

        //armmotor = hardwareMap.dcMotor.get("arm");
        hook = hardwareMap.servo.get("hook");


        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        nitro = 2.0;

        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo.setPosition(1);
        servo1.setPosition(0.216);
        servo2.setPosition(.02);

        position = 1;
    }

    @Override
    public void loop() {



        if(gamepad1.x) {

            hook.setPosition(0.3);

        }


        if(gamepad1.y) {

            hook.setPosition(0.65);

        }


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
        if (gamepad1.b){
            nitro = 2;
        }

        /**
        if (gamepad1.dpad_up){
            armmotor.setPower(0.5);
        }else if (gamepad1.dpad_down){
            armmotor.setPower(-0.5);
        }else{
            armmotor.setPower(0);
        }
*/


        if (gamepad2.x){
            position = 0;
        }
        if (gamepad2.y){
            position = .43;
        }
        if (gamepad2.b){
            position = .67;
        }
        if (gamepad2.a){
            position = 1;
        }
        servo.setPosition(position);

        if (gamepad2.dpad_up){
            try{
                servo1.setPosition(servo1.getPosition() + .01);
            }catch(Exception e){
                int x = 5;
            }

        }
        if (gamepad2.dpad_down){
            try{
                servo1.setPosition(servo1.getPosition() - .01);
            }catch(Exception e){
                int x = 5;
            }
        }

        if (gamepad2.dpad_left){
            try{
                servo2.setPosition(servo2.getPosition() + .01);
            }catch(Exception e){
                int x = 5;
            }

        }
        if (gamepad2.dpad_right){
            try{
                servo2.setPosition(servo2.getPosition() - .01);
            }catch(Exception e){
                int x = 5;
            }
        }





        telemetry.addData("Nitro Type: ", nitro);
        telemetry.update();
    }
}