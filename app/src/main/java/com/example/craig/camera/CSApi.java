package com.example.craig.camera;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;


public final class CSApi {
  public static final String BASE_URL = "https://api.cloudsightapi.com";
  private static final String IMAGE_REQUESTS_PATH = "/image_requests";
  private static final String IMAGE_RESPONSES_PATH = "/image_responses";
  public static final String CONTENT_TYPE = "multipart/form-data";
  private static final String AUTHORIZATION_FORMAT = "CloudSight %s";
  private static final String URL_CONCATENATION_FORMAT = "%s/%s";

  private final String mAuthorizationKey;
  private final HttpRequestFactory mHttpRequestFactory;
  private final GenericUrl mImagePostUrl;
  private final GenericUrl mImageGetUrl;


  public CSApi(
    final HttpTransport transport,
    final JsonFactory jsonFactory,
    final String authorizationKey,
    final String baseUrl
  ) {
    mAuthorizationKey = authorizationKey;
    mHttpRequestFactory = transport.createRequestFactory(new HttpRequestInitializer() {
      
      public void initialize(HttpRequest request) {
        request.setParser(new JsonObjectParser(jsonFactory));
      }
    });
    mImagePostUrl = new GenericUrl(baseUrl);
    mImagePostUrl.appendRawPath(IMAGE_REQUESTS_PATH);
    mImageGetUrl = new GenericUrl(baseUrl);
    mImageGetUrl.appendRawPath(IMAGE_RESPONSES_PATH);
  }

  public CSApi(
    final HttpTransport transport,
    final JsonFactory jsonFactory,
    final String authorizationKey
  ) {
    this(transport, jsonFactory, authorizationKey, BASE_URL);
  }

  public CSPostResult postImage(final CSPostConfig imagePostConfig) throws IOException {
    final HttpRequest request = mHttpRequestFactory.buildPostRequest(
      mImagePostUrl,
      imagePostConfig.getContent()
    );
    request.getHeaders()
      .setContentType(CONTENT_TYPE)
      .setAuthorization(String.format(AUTHORIZATION_FORMAT, mAuthorizationKey));
    return request.execute().parseAs(CSPostResult.class);
  }

  public CSGetResult getImage(final String token) throws IOException {
    final HttpRequest request = mHttpRequestFactory.buildGetRequest(
      new GenericUrl(String.format(URL_CONCATENATION_FORMAT, mImageGetUrl.toString(), token))
    );
    request.getHeaders()
      .setContentType(CONTENT_TYPE)
      .setAuthorization(String.format(AUTHORIZATION_FORMAT, mAuthorizationKey));
    return request.execute().parseAs(CSGetResult.class);
  }

  public CSGetResult getImage(final CSPostResult csPostResult) throws IOException {;
    return getImage(csPostResult.getToken());
  }
}