import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class Main {
    public static void main(String[] args){
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S3);
        int return_color;
        boolean forward = false;
        while (true){
            return_color = robot.color();
            if(return_color == Color.BLACK){
                if (!forward){
                    robot.forward();
                    forward = true;
                }
            }else{
                robot.stop();
                robot.rotate((float) 0.1);
                forward = false;
            }
        }

//        for (int i = 0; i < 100; i++) {
//            switch (robot.color()){
//                case Color.BLACK : System.out.println("Oui"); break;
//                default : System.out.println("Non");
//            }
//            Delay.msDelay(1000);
//        }

    }
}
