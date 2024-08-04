package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Mechanium TeleOp", group="Linear OpMode")
public class MechaniumTeleOp extends LinearOpMode {

    // Motor tanımlamaları
    private DcMotor rightFront;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor leftBack;

    @Override
    public void runOpMode() {
        // Oyun kollarının giriş değişkenleri
        float y;        // İleri-geri hareket
        double x;       // Yanlara hareket
        float rx;       // Dönme hareketi
        double denominator; // Motor güçlerini normalize etmek için kullanılan değer

        // Motorları hardware map'ten alma
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightRear");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "LeftRear");

        // Sağ ön ve sağ arka motorların yönünü ters çevirme
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Otonom mod başlatma sinyalini bekleme
        waitForStart();

        // Eşik değeri (threshold) belirleme
        double threshold = 0.05;

        // Otonom mod devam ederken sürekli çalışacak döngü
        while (opModeIsActive()) {
            // Oyun kollarından gelen girişleri okuma
            y = -gamepad1.left_stick_y; // İleri-geri hareket (sol joystick)
            x = gamepad1.left_stick_x * 1.1; // Yanlara hareket (sol joystick, x ekseni)
            rx = -gamepad1.right_stick_x; // Dönme hareketi (sağ joystick, x ekseni)

            // Eşik değerinin altındaki sapmaları sıfırlama
            if (Math.abs(y) < threshold) y = 0;
            if (Math.abs(x) < threshold) x = 0;
            if (Math.abs(rx) < threshold) rx = 0;

            // Motor güçlerini normalize etmek için en büyük mutlak değeri belirleme
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));

            // Motorlara güç değerlerini atama
            leftFront.setPower((y + x + rx) / denominator);
            leftBack.setPower((y - x + rx) / denominator);
            rightFront.setPower((y - x - rx) / denominator);
            rightBack.setPower((y + x - rx) / denominator);
        }
    }
}
