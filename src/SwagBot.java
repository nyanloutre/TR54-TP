import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

public class SwagBot{

    private RegulatedMotor motorA;
    private RegulatedMotor motorB;
    private EV3UltrasonicSensor ultrasonic;
    private SampleProvider ultrasonic_provider;

    public SwagBot(Port port_motor_A, Port port_motor_B, Port port_ultrasonic) {
        this.motorA = new EV3LargeRegulatedMotor(port_motor_A);
        this.motorB = new EV3LargeRegulatedMotor(port_motor_B);
        this.ultrasonic = new EV3UltrasonicSensor(port_ultrasonic);
        this.ultrasonic_provider = this.ultrasonic.getDistanceMode();
    }

    public SwagBot(RegulatedMotor motorA, RegulatedMotor motorB) {
        this.motorA = motorA;
        this.motorB = motorB;
    }

    protected void finalize() throws Throwable {
        super.finalize();

        this.motorA.close();
        this.motorB.close();
        this.ultrasonic.close();
    }

    public void stop() {
        this.motorA.stop(true);
        this.motorB.stop();
    }

    public void rotate(float angle) {
        int deg_angle = (int)((180.0/Math.PI)*angle) * 2;
        System.out.println(deg_angle);
        this.motorA.rotate(deg_angle, true);
        this.motorB.rotate(-deg_angle);
    }

    public void forward() {
        this.motorA.forward();
        this.motorB.forward();
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
}
