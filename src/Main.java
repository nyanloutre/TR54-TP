import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Main {
    public static void main(String[] args){
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S3);
        for (int i = 0; i < 100; i++) {
            System.out.println(robot.color());
            Delay.msDelay(1000);
        }

    }
}
