import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.*;
import static jdk.internal.net.http.common.Utils.close;


public class Main {
    public static final String REMOTE_SERVICE_URI =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setUserAgent("My Test Service")
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            response = httpClient.execute(request);
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Converter> converters = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<List<Converter>>() {
                });

        converters.stream().filter(value -> value.getUpvotes() > 0)
                .forEach(System.out::println);
    }
}