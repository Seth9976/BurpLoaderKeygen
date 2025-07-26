package com.h3110w0r1d.json;

import java.util.HashMap;

public class JSONObject {
   private JSONType type;
   private JSONObject[] list;
   public String string;
   private String number;
   private Boolean bool;
   private HashMap<String, JSONObject> dict;

   public JSONObject(JSONType type, Object value) {
      this.type = type;
      switch (type) {
         case List:
            this.list = (JSONObject[])value;
            break;
         case Dict:
            this.dict = (HashMap<String, JSONObject>)value;
            break;
         case String:
            this.string = (String)value;
            break;
         case Number:
            this.number = (String)value;
            break;
         case Boolean:
            this.bool = (Boolean)value;
         case Null:
      }
   }

   public void set(String key, JSONObject value) {
      if (this.type != JSONType.Dict) {
         throw new RuntimeException("Not a dict");
      } else {
         this.dict.put(key, value);
      }
   }

   public JSONObject get(String key) {
      if (this.type != JSONType.Dict) {
         throw new RuntimeException("Not a dict");
      } else {
         return this.dict.get(key);
      }
   }

   public String getString(String key) {
      if (this.dict.get(key) == null) {
         throw new RuntimeException("Unknown key");
      } else if (this.dict.get(key).type == JSONType.String) {
         return this.dict.get(key).string;
      } else {
         throw new RuntimeException("Not a string");
      }
   }

   public String getNumber(String key) {
      if (this.dict.get(key) == null) {
         throw new RuntimeException("Unknown key");
      } else if (this.dict.get(key).type == JSONType.Number) {
         return this.dict.get(key).number;
      } else {
         throw new RuntimeException("Not a number");
      }
   }

   public Boolean getBoolean(String key) {
      if (this.dict.get(key) == null) {
         throw new RuntimeException("Unknown key");
      } else if (this.dict.get(key).type == JSONType.Boolean) {
         return this.dict.get(key).bool;
      } else {
         throw new RuntimeException("Not a boolean");
      }
   }

   public JSONObject[] getList(String key) {
      if (this.dict.get(key) == null) {
         throw new RuntimeException("Unknown key");
      } else if (this.dict.get(key).type == JSONType.List) {
         return this.dict.get(key).list;
      } else {
         throw new RuntimeException("Not a list");
      }
   }

   public Boolean Boolean() {
      if (this.type == JSONType.Boolean) {
         return this.bool;
      } else {
         throw new RuntimeException("Not a boolean");
      }
   }

   public String Number() {
      if (this.type == JSONType.Number) {
         return this.number;
      } else {
         throw new RuntimeException("Not a number");
      }
   }

   public String String() {
      if (this.type == JSONType.String) {
         return this.string;
      } else {
         throw new RuntimeException("Not a string");
      }
   }
}
