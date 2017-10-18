import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Follower {

    private SwagBot robot;

    public Follower(){
        this.robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S1);
    }

    public void tout_ou_rien() {
        robot.speed(500, 500);
        while (!robot.isPush()) {
            if (robot.distance() > 0.15) {
                robot.forward();
            } else {
                robot.stop();
            }
        }
    }

    public void a_un_point(int a, double distance) {
        robot.forward();
        while(!robot.isPush()) {
            int vitesse = (int)(Math.max(Math.min(50, a*(robot.distance()-distance)) , 0) * 10);
            robot.speed(vitesse, vitesse);
        }
    }

    public void a_deux_point(int a, double distance, long delai) {
        robot.forward();
        double derniere_vitesse = 0;
        while(!robot.isPush()){
            float mesure_distance = robot.distance();
            double vitesse_pourcentage = Math.min(Math.max(2.5*(mesure_distance-0.20), Math.min(Math.max(a*(mesure_distance-distance), 0) , derniere_vitesse)) , 0.5);
            System.out.println(mesure_distance);
            System.out.println(vitesse_pourcentage);
            int vitesse = (int)(vitesse_pourcentage * 1000);
            robot.speed(vitesse, vitesse);
            derniere_vitesse = vitesse_pourcentage;
            Delay.msDelay(delai);
        }
    }
}

