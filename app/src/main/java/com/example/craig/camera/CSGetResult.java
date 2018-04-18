package com.example.craig.camera;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class CSGetResult extends GenericJson {
  @Key
  private String status;

  @Key
  private String name;

  public String getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }
}
