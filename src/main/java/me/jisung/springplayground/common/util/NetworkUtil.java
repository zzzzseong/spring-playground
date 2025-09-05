package me.jisung.springplayground.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkUtil {

    private static final String UNKNOWN = "unknown";
    private static final String[] PROXY_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED"
    };

    public static String getRequestIp(HttpServletRequest request) {
        for (String header : PROXY_HEADERS) {
            String ip = request.getHeader(header);
            if (!StringUtil.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                /* 프록시를 통한 경우 첫 번째 IP를 반환 */
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}
