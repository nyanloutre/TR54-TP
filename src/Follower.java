import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Follower {

    private SwagBot robot;
    private PrintWriter file;

    public Follower(){
        this.robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S1);
        try {
            file = new PrintWriter("follower.csv", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        file.println("Distance,Vitesse");
    }

    public void ecrire_statistiques(double distance, int vitesse) {
        file.println(distance + "," + vitesse);
    }

    public void tout_ou_rien() {
        robot.speed((int) (0.5 * robot.getMaxSpeed()), (int) (0.5 * robot.getMaxSpeed()));
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
            int vitesse = (int)(Math.max(Math.min(0.5, a*(robot.distance()-distance)) , 0) * robot.getMaxSpeed());
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
            int vitesse = (int)(vitesse_pourcentage * robot.getMaxSpeed());
            robot.speed(vitesse, vitesse);
            derniere_vitesse = vitesse_pourcentage;
            this.ecrire_statistiques(mesure_distance, vitesse);
            Delay.msDelay(delai);
        }
        file.close();
    }
}

