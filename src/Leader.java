import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;

public class Leader {
    public static void main(String[] args) {
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C);
        robot.forward();
        Button.waitForAnyPress();
    }
}
