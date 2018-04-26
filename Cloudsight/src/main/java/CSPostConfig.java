import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;

import java.io.File;

public class CSPostConfig {
  private static final String MULTIPART_FORM_BOUNDARY_KEY = "boundary";
  private static final String MULTIPART_FORM_BOUNDARY_VALUE = "__END_OF_PART__";
  private static final String CONTENT_DISPOSITION = "Content-Disposition";
  private static final String FORM_DATA_FORMAT = "form-data; name=\"%s\"";
  private static final String FORM_DATA_FORMAT_FILE = "form-data; name=\"%s\"; filename=\"%s\"";
  private static final String IMAGE_REQUEST_FORM_KEY = "image_request[image]";
  private static final String REMOTE_URL_REQUEST_FORM_KEY = "image_request[remote_image_url]";
  private static final String LOCALE_FORM_KEY = "image_request[locale]";
  private static final String LANGUAGE_FORM_KEY = "image_request[language]";
  private static final String DEVICE_ID_FORM_KEY = "image_request[device_id]";
  private static final String LATITUDE_FORM_KEY = "image_request[latitude]";
  private static final String LONGITUDE_FORM_KEY = "image_request[longitude]";
  private static final String ALTITUDE_FORM_KEY = "image_request[altitude]";
  private static final String TTL_FORM_KEY = "image_request[ttl]";
  private static final String FOCUS_X_FORM_KEY = "focus[X]";
  private static final String FOCUS_Y_FORM_KEY = "focus[Y]";


  public static Builder newBuilder() {
    return new Builder();
  }

  private final File mImage;
  private final String mRemoteImageUrl;
  private final String mLocale;
  private final String mLanguage;
  private final String mDeviceId;
  private final Double mLatitude;
  private final Double mLongitude;
  private final Double mAltitude;
  private final Integer mTtl;
  private final Integer mFocusX;
  private final Integer mFocusY;
  private final MultipartContent mContent;

  private CSPostConfig(
    final File image,
    final String remoteImageUrl,
    final String locale,
    final String language,
    final String deviceId,
    final Double latitude,
    final Double longitude,
    final Double altitude,
    final Integer ttl,
    final Integer focusX,
    final Integer focusY,
    final MultipartContent content
  ) {
    mImage = image;
    mRemoteImageUrl = remoteImageUrl;
    mLocale = locale;
    mLanguage = language;
    mDeviceId = deviceId;
    mLatitude = latitude;
    mLongitude = longitude;
    mAltitude = altitude;
    mTtl = ttl;
    mFocusX = focusX;
    mFocusY = focusY;
    mContent = content;
  }

  public File getImage() {
    return mImage;
  }

  public String getRemoteImageUrl() {
    return mRemoteImageUrl;
  }

  public String getLocale() {
    return mLocale;
  }

  public String getLanguage() {
    return mLanguage;
  }

  public String getDeviceId() {
    return mDeviceId;
  }

  public Double getLatitude() {
    return mLatitude;
  }

  public Double getLongitude() {
    return mLongitude;
  }

  public Double getAltitude() {
    return mAltitude;
  }

  public Integer getTtl() {
    return mTtl;
  }

  public Integer getFocusX() {
    return mFocusX;
  }

  public Integer getFocusY() {
    return mFocusY;
  }

  public MultipartContent getContent() {
    return mContent;
  }

  public static class Builder {
    public static final String DEFAULT_LOCALE = "en";

    private File mImage;
    private String mRemoteImageUrl;
    private String mLocale;
    private String mLanguage;
    private String mDeviceId;
    private Double mLatitude;
    private Double mLongitude;
    private Double mAltitude;
    private Integer mTtl;
    private Integer mFocusX;
    private Integer mFocusY;

    private Builder() {
      super();
    }

    public Builder withImage(final File image) {
      mImage = image;
      return this;
    }

    public Builder withRemoteImageUrl(final String url) {
      mRemoteImageUrl = url;
      return this;
    }

    public Builder withLocale(final String locale) {
      mLocale = locale;
      return this;
    }

    public Builder withLanguage(final String language) {
      mLanguage = language;
      return this;
    }

    public Builder withDeviceId(final String deviceId) {
      mDeviceId = deviceId;
      return this;
    }

    public Builder withLatitude(final double latitude ) {
      mLatitude = latitude;
      return this;
    }

    public Builder withLongitude(final double longitude) {
      mLongitude = longitude;
      return this;
    }

    public Builder withAltitude(final double altitude) {
      mAltitude = altitude;
      return this;
    }

    public Builder withTtl(final int ttl) {
      mTtl = ttl;
      return this;
    }

    public Builder withFocusX(final int focusX) {
      mFocusX = focusX;
      return this;
    }

    public Builder withFocusY(final int focusY) {
      mFocusY = focusY;
      return this;
    }

    private void addPart(
      final MultipartContent content,
      final String formKey,
      final Object formValue
    ) {
      if (null != formValue) {
        final MultipartContent.Part part = new MultipartContent.Part(
          ByteArrayContent.fromString(null, formValue.toString())
        );
        part.setHeaders(
          new HttpHeaders().set(
            CONTENT_DISPOSITION,
            String.format(FORM_DATA_FORMAT, formKey)
          )
        );
        content.addPart(part);
      }
    }

    public CSPostConfig build() throws IllegalStateException {
      if ((null == mImage && null == mRemoteImageUrl) || (null != mImage && null != mRemoteImageUrl) ) {
        throw new IllegalStateException("Exactly one of image or remote image url must be provided.");
      }

      final MultipartContent content = new MultipartContent().setMediaType(
        new HttpMediaType(CSApi.CONTENT_TYPE)
          .setParameter(MULTIPART_FORM_BOUNDARY_KEY, MULTIPART_FORM_BOUNDARY_VALUE)
      );


      if (null != mImage) {
        final FileContent fileContent = new FileContent(null, mImage);
        final MultipartContent.Part image = new MultipartContent.Part(fileContent);
        image.setHeaders(
          new HttpHeaders().set(
            CONTENT_DISPOSITION,
            String.format(FORM_DATA_FORMAT_FILE, IMAGE_REQUEST_FORM_KEY, mImage.getName())
          )
        );
        content.addPart(image);
      } else {
        addPart(content, REMOTE_URL_REQUEST_FORM_KEY, mRemoteImageUrl);
      }

      if (null == mLocale) {
        mLocale = DEFAULT_LOCALE;
      }

      addPart(content, LOCALE_FORM_KEY, mLocale);
      addPart(content, LANGUAGE_FORM_KEY, mLanguage);
      addPart(content, DEVICE_ID_FORM_KEY, mDeviceId);
      addPart(content, LATITUDE_FORM_KEY, mLatitude);
      addPart(content, LONGITUDE_FORM_KEY, mLongitude);
      addPart(content, ALTITUDE_FORM_KEY, mAltitude);
      addPart(content, TTL_FORM_KEY, mTtl);
      addPart(content, FOCUS_X_FORM_KEY, mFocusX);
      addPart(content, FOCUS_Y_FORM_KEY, mFocusY);

      return new CSPostConfig(
        mImage,
        mRemoteImageUrl,
        mLocale,
        mLanguage,
        mDeviceId,
        mLatitude,
        mLongitude,
        mAltitude,
        mTtl,
        mFocusX,
        mFocusY,
        content
      );
    }

  }
}
