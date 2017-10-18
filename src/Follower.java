import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Follower {

    private SwagBot robot;

    public Follower(){
        this.robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S1);
    }

    public void toutourien() {
        robot.speed(500, 500);
        while (!robot.isPush()) {
            if (robot.distance() > 0.15) {
                robot.forward();
            } else {
                robot.stop();
            }
        }
    }

    public void aunpoint() {
        robot.forward();
        while(!robot.isPush()) {
            int vitesse = (int)Math.max(Math.min(1000, 1000*(robot.distance()-0.15)) , 0);
            robot.speed(vitesse, vitesse);
        }
    }
}

