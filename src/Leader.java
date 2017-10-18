import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Leader {
    public static void main(String[] args) {
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C);

        while(true) {
            robot.forward();
            Delay.msDelay(5000);
            robot.stop();
            Delay.msDelay(5000);
        }
    }
}
