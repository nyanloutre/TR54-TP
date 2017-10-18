import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class Follower {

    public static void main(String[] args) {
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S1);
        System.out.println("Obstacle à : " + robot.distance());
    }
}

