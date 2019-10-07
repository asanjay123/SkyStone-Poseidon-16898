package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Servo Rev Contols", group = "rev")
/**
 * Main Class that holds the user control code.
 * Implements LinearOpMode
 */
public class servotestrev extends OpMode {


    Servo   servo;
    double position;

    @Override
    public void init(){
        servo = hardwareMap.servo.get("servo");
        position = .1;
    }

    @Override
    public void loop() {

        if (gamepad2.x){
            position = 0;
        }
        if (gamepad2.y){
            position = .33;
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