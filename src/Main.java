import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Main {
    public static void main(String[] args){
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2);
        for (int i = 0; i < 20; i++) {
            System.out.println(robot.distance(5));
            Delay.msDelay(1000);
        }

    }
}
