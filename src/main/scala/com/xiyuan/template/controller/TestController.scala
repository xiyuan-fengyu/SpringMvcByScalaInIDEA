package com.xiyuan.template.controller

import com.google.gson.JsonObject
import com.xiyuan.template.dao.Dao
import com.xiyuan.template.model.TestBo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import com.xiyuan.template.extension.JsonObjectExt._

/**
  * Created by YT on 2016/6/30.
  */
@Controller
class TestController {

  @Autowired
  private val dao: Dao = null

  /**
    * 使用scala开发的时候，返回scala的HashMap的时候，通过Jackson无法解析成正确的String（虽能解析，但内容不正确），所以就改用Gson了
 *
    * @return
    */
  @RequestMapping(value = Array("/test"))
  @ResponseBody
  def test(): JsonObject = {
    val result = new JsonObject
    // 要添加扩展implicit
    // import com.xiyuan.template.extension.JsonObjectExt._
    // 否则不能用下面的写法
    result("success") = true
    result("message") = "test"

    result("list") = dao.all(classOf[TestBo])

    result("data") = dao.find(1L, classOf[TestBo])

    result
  }

  @RequestMapping(value = Array("/test/string"))
  @ResponseBody
  def testStr(): String = {
    "Hello, scala!"
  }

  @RequestMapping(value = Array("/test/update"))
  @ResponseBody
  def testUpdate(id: Long): String = {
    val item = dao.find(id, classOf[TestBo])
    if (item != null) {
      item.setName("new name")
      dao.update(item)
    }
    "update!"
  }

  @RequestMapping(value = Array("/test/delete"))
  @ResponseBody
  def testDelete(id: Long): String = {
    val item = dao.find(id, classOf[TestBo])
    if (item != null) {
      dao.delete(item)
    }
    "delete!"
  }

  @RequestMapping(value = Array("/test/insert"))
  @ResponseBody
  def testInsert(): String = {
    val newItem = new TestBo
    newItem.setName("1L")
    newItem.setContent("1L")
    dao.insert(newItem)
    "insert!"
  }

}
