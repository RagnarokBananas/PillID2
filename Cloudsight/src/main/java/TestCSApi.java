import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.File;

public class TestCSApi {

  private static final String API_KEY = "Fa3GonwiTw3RwyIWgNhwVQ";

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final JsonFactory JSON_FACTORY = new JacksonFactory();



  public static void main(String[] args) throws Exception {
    CSApi api = new CSApi(
      HTTP_TRANSPORT,
      JSON_FACTORY,
      API_KEY
    );
    CSPostConfig imageToPost = CSPostConfig.newBuilder()
      .withRemoteImageUrl("http://www.greycloaktech.com/wp-content/uploads/2015/07/url-small.jpg")
      .build();
    
    CSApi test = new CSApi(
    	      HTTP_TRANSPORT,
    	      JSON_FACTORY,
    	      API_KEY
    	    );
    File temp = new File("C:\\Users\\Jacob\\Desktop\\Temp\\ICon.jpg");
    CSPostConfig imageToPost2 = CSPostConfig.newBuilder()
    	      .withImage(temp)
    	      .build();
    CSPostResult portResult = api.postImage(imageToPost2);

    System.out.println("Post result: " + portResult);

    Thread.sleep(30000);

    CSGetResult scoredResult = api.getImage(portResult);

    System.out.println(scoredResult);
  }
}