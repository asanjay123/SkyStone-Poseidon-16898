package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Autonomous 1", group = "")
public class Autonomous_1 extends LinearOpMode {

    /**Define all values */
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
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode(){

        /**Initialize autonomous
         * Strafe a certain measurement
         * Move backward about 4 ft
         * Latch onto  platform
         * drive straight
         * Drive right (Forward straight, turn right)[not rotate]
         * Drive back after platform is rotated 90 degrees
         * Unlatch
         * Drive forward and stop under bar
         */
        initValues();
        hook.setPosition(0.65);
        //driveWithStrafe(.2, -200, 0);







        strafeWithTime(7, .2, 'l');





         driveWithEncoder(.2, 24, 24, 30);
         //hook.setPosition(0.3);
        strafeWithTime(2.6, .2, 'b');
         hook.setPosition(0.3);
         sleep(500);

        //strafeWithTime(2,0.2,'l');
         driveWithEncoder(.20, 40, 40,50);
         hook.setPosition(.65);
        strafeWithTime(0.75,0.2,'r');
        hook.setPosition(.3);
        driveWithEncoder(0.3,0,24,30);
        hook.setPosition(.65);
        sleep(500);


        strafeWithTime(1, .2, 'l');
        driveWithEncoder(.3, 10, 10, 30);
        strafeWithTime(7, .2, 'b');

         //driveWithEncoder(0.5, 150, 150, 30);


        //hook.setPosition(0.65);
        //driveWithStrafe(0,5, 0);



    }

    public void initValues(){
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


    //Method to strafe
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