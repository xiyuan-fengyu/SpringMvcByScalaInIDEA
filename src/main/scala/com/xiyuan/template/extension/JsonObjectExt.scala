package com.xiyuan.template.extension

import com.google.gson.{JsonNull, Gson, JsonElement, JsonObject}

/**
  * 对JsonObject进行扩展，可以方便的使用jsonObjec(key)获取值，通过jsonObjec(key) = newValue来更新值
  * Created by YT on 2016/6/30.
  */
object JsonObjectExt {

  private val gson = new Gson()

  implicit class JsonObjectApply(jsonObject: JsonObject) {

    def apply(key: String): JsonElement = {
      jsonObject.get(key)
    }

    def update(key: String, value: Any): Any = {
      if (key != null && value != null) {
        value match {
          case jsonElement: JsonElement =>
            jsonObject.add(key, jsonElement)
            jsonElement
          case s: String =>
            jsonObject.addProperty(key, s)
            s
          case n: Number =>
            jsonObject.addProperty(key, n)
            n
          case b: Boolean =>
            jsonObject.addProperty(key, b)
            b
          case c: Character =>
            jsonObject.addProperty(key, c)
            c
          case _ =>
            try {
              val jsonElement = gson.toJsonTree(value)
              if (jsonElement != null && !jsonElement.isJsonNull) {
                jsonObject.add(key, jsonElement)
                jsonElement
              }
              else {
                jsonObject.add(key, null)
                null
              }
            }
            catch {
              case e: Exception =>
                jsonObject.add(key, null)
                null
            }
        }
      }
      else {
        jsonObject.add(key, null)
        null
      }
    }



  }

}
