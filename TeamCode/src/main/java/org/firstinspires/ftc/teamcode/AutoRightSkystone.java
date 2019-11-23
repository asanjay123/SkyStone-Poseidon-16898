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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

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
@Autonomous(name ="Right Skystone", group = "Concept")

public class AutoRightSkystone extends LinearOpMode {

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



    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initValues();
        sleep(1000);
        driveWithEncoder(0.4,3,3,30);
        //for(int i = 0; i<3; i++) {

            pickUp();
        //    strafeWithTime(1,0.3,'l');

        //}




    }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
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

    public void pickUp() {
        servo1.setPosition(0.75);
        sleep(600);

        driveWithEncoder(0.4,12.5,12.5,30);
        servo1.setPosition(0);
        sleep(800);
        strafeWithTime(1.4,0.2,'p');
        driveWithEncoder(0.4,5,5,30);
        servo1.setPosition(0.75);

     /*   strafeWithTime(0.5,0.5,'b');
        strafeWithTime(3,0.6,'r');
        servo2.setPosition(0.5);
        strafeWithTime(3,0.6,'l'); */


    }
    public void initValues(){
        armmotor = hardwareMap.dcMotor.get("arm");
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        position = 0.08;
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
}
