package cli.Utilities;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class API_test {

	public static String API_key;

	private static final HttpClient httpClient = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(10))
			.build();


	public static void main(String[] args) throws IOException, InterruptedException {

		String url = "https://api.contentstack.io/v3/stacks/delivery_tokens/blt44d900d8457ba976";

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.setHeader("Content-Type", "application/json")
				.setHeader("authtoken", "blta90a64057a654379")
				.setHeader("api_key", "blt49d5f9742f4c7d2b")// add request header
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print response headers
		HttpHeaders headers = response.headers();
		//headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

		String repsnceJson= response.body().toString() ;
		String a[] =repsnceJson.split("\"token\"");
		String b[] = a[2].split(",");
		System.out.println("Delivery Token is >>> " +b[0].replace("\"", "").replace(":", "")); 

	}

}
