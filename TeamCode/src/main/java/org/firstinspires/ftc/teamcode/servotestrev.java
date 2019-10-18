package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "Servo Rev Contols", group = "test")
/**
 * Main Class that holds the user control code.
 * Implements LinearOpMode
 */
public class servotestrev extends OpMode {


    Servo   servo;
    Servo servo1;
    Servo servo2;
    double position;

    @Override
    public void init(){
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        position = 1;
        servo.setPosition(1);
        servo1.setPosition(0.216);
        servo2.setPosition(.02);


    }

    @Override
    public void loop() {
        /**
        if (gamepad2.right_stick_y <=  -1 && gamepad2.right_stick_y < 0){
            servo1.setPosition(servo1.getPosition() - .01);
        }
        if (gamepad2.right_stick_y <=  1 && gamepad2.right_stick_y > 0){
            servo1.setPosition(servo1.getPosition() + .01);
        }
        */
        telemetry.addData("Servo 1", servo1.getPosition());
        telemetry.addData("Servo 2", servo2.getPosition());
        telemetry.update();

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



    }
}