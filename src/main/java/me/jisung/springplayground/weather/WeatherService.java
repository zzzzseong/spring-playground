package me.jisung.springplayground.weather;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.HttpHelper;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.JsonUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "WeatherService")
public class WeatherService {

    private final HttpHelper httpHelper;

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        URI uri = httpHelper.buildUri("https", "api.weather.gov", "/points/" + latitude + "," + longitude);

        Consumer<HttpHeaders> headers = httpHeaders -> {
            httpHeaders.add("Accept", "application/geo+json");
            httpHeaders.add("User-Agent", "WeatherApiClient/1.0 jisung.develop@gmail.com");
        };

        Weather weather = JsonUtil.fromJson(httpHelper.get(uri, headers), Weather.class);

        try {
            uri = new URI(weather.getProperties().getForecast().toString());
            weather = JsonUtil.fromJson(httpHelper.get(uri), Weather.class);
            return JsonUtil.toJson(weather.getProperties().getPeriods());
        } catch (URISyntaxException e) {
            log.error("Failed to parse forecast URI", e);
            throw new ApiException(Api5xxErrorCode.HTTP_REQUEST_FAILED);
        }
    }

//    @Tool(description = "Get weather alerts for a US state")
    public String getAlerts(
            @ToolParam(description = "Two-letter US state code (e.g. CA, NY)") String state
    ) {
        // Returns active alerts including:
        // - Event type
        // - Affected area
        // - Severity
        // - Description
        // - Safety instructions
        return null;
    }


    @Getter
    public static class Weather {

        private Properties properties;

        @Getter
        public static class Properties {
            private Object forecast;
            private Object periods;
        }
    }
}
