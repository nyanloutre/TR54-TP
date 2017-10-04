import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class Main {
    public static void main(String[] args){
        SwagBot robot = new SwagBot(MotorPort.B, MotorPort.C, SensorPort.S2, SensorPort.S3, SensorPort.S1);

        int base_robot_speed = 300;
        int agressivity = 8;

        int return_color;
        double ticks = 0;
        int previous_color = Color.BLACK;
        int actual_color;
        while (!robot.isPush()){
            return_color = robot.color();
            System.out.println(ticks);
            if (return_color == Color.BLACK) {
                System.out.println("BLACK");
                double speed = base_robot_speed / Math.exp(ticks / agressivity);
                System.out.println(speed);
                robot.speed((int) speed, base_robot_speed);
                actual_color = Color.BLACK;
            } else {
                System.out.println("OTHER");
                //robot.speed(true);
                double speed = base_robot_speed / Math.exp(ticks / agressivity);
                System.out.println(speed);
                robot.speed(base_robot_speed, (int) speed);
                actual_color = Color.NONE;
            }

            if (actual_color == previous_color) {
                ticks++;
            } else {
                ticks/=2;
            }

            previous_color = actual_color;
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
