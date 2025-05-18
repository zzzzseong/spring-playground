package me.jisung.springplayground.weather;

import lombok.RequiredArgsConstructor;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("")
    public ApiResponse<String> getWeatherForecastByLocation(@RequestParam double latitude, @RequestParam double longitude) {
        return success(weatherService.getWeatherForecastByLocation(latitude, longitude));
    }
}
