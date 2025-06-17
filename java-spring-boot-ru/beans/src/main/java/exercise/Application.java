package exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import exercise.daytime.Daytime;
import exercise.daytime.Day;
import exercise.daytime.Night;

// BEGIN
import org.springframework.context.annotation.Bean;
import java.time.LocalTime;
// END

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @Bean
    public Daytime getDayTime() {
        var currentTime = LocalTime.now();
        var dayTimeStart = LocalTime.parse("06:00:00");
        var dayTimeEnd = LocalTime.parse("21:59:59");

        if (currentTime.isAfter(dayTimeStart) && currentTime.isBefore(dayTimeEnd)) {
            return new Day();
        }

        return new Night();
    }
    // END
}
