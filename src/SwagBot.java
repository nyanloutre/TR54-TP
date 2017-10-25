import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.*;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class SwagBot{

    private RegulatedMotor moteur_gauche;
    private RegulatedMotor moteur_droite;
    private EV3UltrasonicSensor ultrasonic;
    private SampleProvider ultrasonic_provider;
    private EV3ColorSensor color_sensor;
    private EV3TouchSensor button_sensor;

    private float white;
    private float black;
    private float midpoint;
    private float max_speed;

    public SwagBot(Port port_motor_A, Port port_motor_B, Port port_ultrasonic, Port port_color_sensor, Port port_button_sensor) {
        this(port_motor_A, port_motor_B, port_ultrasonic,port_button_sensor);
        this.color_sensor = new EV3ColorSensor(port_color_sensor);
    }

    public SwagBot(Port port_motor_A, Port port_motor_B, Port port_ultrasonic, Port port_button_sensor) {
        this(port_motor_A,port_motor_B);
        this.ultrasonic = new EV3UltrasonicSensor(port_ultrasonic);
        this.ultrasonic_provider = this.ultrasonic.getDistanceMode();
        this.button_sensor = new EV3TouchSensor(port_button_sensor);
    }


    public SwagBot(Port port_motor_A, Port port_motor_B) {
        this.moteur_gauche = new EV3LargeRegulatedMotor(port_motor_A);
        this.max_speed = this.moteur_gauche.getMaxSpeed();
        this.moteur_droite = new EV3LargeRegulatedMotor(port_motor_B);
    }

    protected void finalize() throws Throwable {
        super.finalize();

        this.moteur_gauche.close();
        this.moteur_droite.close();
        this.ultrasonic.close();
        this.color_sensor.close();
    }

    public boolean isPush(){
        SensorMode touch = button_sensor.getTouchMode();
        float[] sample = new float[touch.sampleSize()];
        touch.fetchSample(sample,0);
        int is_touch = (int)sample[0];
        return (is_touch == 1);
    }

    public void stop() {
        this.moteur_gauche.stop(true);
        this.moteur_droite.stop();
    }

    public void rotate(float angle) {
        int deg_angle = (int)((180.0/Math.PI)*angle) * 2;
        this.moteur_gauche.rotate(deg_angle, true);
        this.moteur_droite.rotate(-deg_angle);
    }

    public void forward() {
        this.moteur_gauche.forward();
        this.moteur_droite.forward();
    }

    public void speed(int speed_left, int speed_right){
        this.moteur_gauche.setSpeed(speed_left);
        this.moteur_gauche.forward();
        this.moteur_droite.setSpeed(speed_right);
        this.moteur_droite.forward();
    }

    public float distance() {
        RangeFinder sonar = new RangeFinderAdapter(this.ultrasonic);
        return(sonar.getRange());
    }

    public float distance(int iterations) {
        MeanFilter mean_ultrasonic = new MeanFilter(this.ultrasonic_provider, iterations);
        float[] samples = new float[this.ultrasonic_provider.sampleSize()];
        mean_ultrasonic.fetchSample(samples, 0);
        return(samples[0]);
    }

    public int color() {
        SensorMode color_mode = this.color_sensor.getColorIDMode();
        float [] color_sample = new float[color_mode.sampleSize()];
        this.color_sensor.setFloodlight(true);
        color_mode.fetchSample(color_sample, 0);
        int colorId = (int)color_sample[0];
        return colorId;
    }

    public float[] rgb_data() {
        SensorMode rgb_mode = this.color_sensor.getRGBMode();
        float [] rgb_sample = new float[rgb_mode.sampleSize()];
        this.color_sensor.setFloodlight(true);
        rgb_mode.fetchSample(rgb_sample, 0);
        return rgb_sample;
    }

    public float moyenne_rgb() {
        float [] rgb_sample = this.rgb_data();
        return (rgb_sample[0] + rgb_sample[1] + rgb_sample[2])/3;
    }

    public void line_follower_v1() {
        int base_robot_speed = 300;
        int agressivity = 6;

        int return_color;
        double ticks = 0;
        int previous_color = Color.BLACK;
        int actual_color;

        Sound.twoBeeps();

        while (!this.isPush()){
            return_color = this.color();
            System.out.println(ticks);
            if (return_color == Color.BLACK) {
                System.out.println("BLACK");
                double speed = base_robot_speed / Math.exp(ticks / agressivity);
                System.out.println(speed);
                this.speed((int) speed, base_robot_speed);
                actual_color = Color.BLACK;
            } else {
                System.out.println("OTHER");
                //robot.speed(true);
                double speed = base_robot_speed / Math.exp(ticks / agressivity);
                System.out.println(speed);
                this.speed(base_robot_speed, (int) speed);
                actual_color = Color.NONE;
            }

            if (actual_color == previous_color) {
                ticks++;
            } else {
                ticks/=2;
            }

            previous_color = actual_color;
        }
    }

    public float getMaxSpeed(){
        return max_speed;
    }

    public float getWhite() {
        return white;
    }

    public float getBlack() {
        return black;
    }

    public void calibration() {
        System.out.println("Noir");
        while (!this.isPush()){

        }
        this.black = this.moyenne_rgb();
        Delay.msDelay(1000);
        System.out.println("Blanc");
        while (!this.isPush()){

        }
        this.white = this.moyenne_rgb();

        this.midpoint = ( this.white - this.black ) / 2 + this.black;

        Delay.msDelay(1000);
    }

    public void line_follower_pid(int speed, float kp, float ki, float kd) {

        float last_error = 0;
        float integral = 0;

        while(!this.isPush()){
            float value = this.moyenne_rgb();
            float error = this.midpoint - value;
            integral = error + integral;
            float derivative = error - last_error;

            float correction = kp * error + ki * integral + kd * derivative;

            System.out.println(correction);

            float left_speed = speed - correction;
            float right_speed = speed + correction;

            this.speed((int)left_speed, (int)right_speed);

            last_error = error;
        }
    }
}
