package com.xiyuan.template.dao

import org.hibernate._
import org.hibernate.transform.Transformers
import org.hibernate.`type`._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.persistence.Table
import java.lang.reflect.Field
import java.math.BigDecimal
import java.util.Date

import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

@Repository
@Transactional
class Dao {

  @Autowired
  private val sessionFactory: SessionFactory = null

  def all[T: ClassTag](clazz: Class[T]): Array[T] = {
    all(clazz, "")
  }

  @SuppressWarnings(Array("unchecked"))
  def all[T: ClassTag](clazz: Class[T], queryStr: String): Array[T] = {
    val session: Session = sessionFactory.getCurrentSession
    val all = new ArrayBuffer[T]()
    try {
      val query: Query = session.createQuery("from " + clazz.getSimpleName + " " + queryStr)
      all ++= query.list.toArray.map(_.asInstanceOf[T])
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    all.toArray
  }

  @SuppressWarnings(Array("unchecked"))
  def find[T](id: Long, clazz: Class[T]): T = {
    val session: Session = sessionFactory.getCurrentSession
    val query: Query = session.createQuery("from " + clazz.getSimpleName + " where id=" + id)
    query.uniqueResult.asInstanceOf[T]
  }

  @SuppressWarnings(Array("unchecked"))
  def query[T: ClassTag](clazz: Class[T], queryStr: String): Array[T] = {
    val session: Session = sessionFactory.getCurrentSession
    val all = new ArrayBuffer[T]()
    try {
      val query: Query = session.createQuery("from " + clazz.getSimpleName + " queryStr")
      all ++= query.list.toArray.map(_.asInstanceOf[T])
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    all.toArray
  }

  @SuppressWarnings(Array("unchecked"))
  def sqlQuery[T: ClassTag](clazz: Class[T], sql: String): Array[T] = {
    val session: Session = sessionFactory.getCurrentSession

    val tempSql = if (sql.contains("_TABLE")) {
      val tableName: String = getTableName(clazz)
      sql.replaceAll("_TABLE", tableName)
    }
    else sql

    val all = new ArrayBuffer[T]()
    try {
      val query: SQLQuery = session.createSQLQuery(sql)
      setSqlResultTransformer(clazz, query)
      all ++= query.list.toArray.map(_.asInstanceOf[T])
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    all.toArray
  }

  def getTableName(clazz: Class[_]): String = {
    val table: Table = clazz.getAnnotation(classOf[Table])
    if (table != null) {
      table.name
    }
    else {
      ""
    }
  }

  private def setSqlResultTransformer(clazz: Class[_], query: SQLQuery): Boolean = {
    if (query == null) {
      return false
    }
    val fields: Array[Field] = clazz.getDeclaredFields
    for (field <- fields) {
      val fieldName: String = field.getName
      if (fieldName != null && !(fieldName == "serialVersionUID") && fieldName.length > 0) {
        val fieldType: Class[_] = field.getType
        if (fieldType != null) {
          if (fieldType == classOf[String]) {
            query.addScalar(fieldName, new StringType)
          }
          else if (fieldType == classOf[Int] || fieldType == classOf[Integer]) {
            query.addScalar(fieldName, new IntegerType)
          }
          else if (fieldType == classOf[Long] || fieldType == classOf[Long]) {
            query.addScalar(fieldName, new LongType)
          }
          else if (fieldType == classOf[Date]) {
            query.addScalar(fieldName, new TimestampType)
          }
          else if (fieldType == classOf[Boolean] || fieldType == classOf[Boolean]) {
            query.addScalar(fieldName, new BooleanType)
          }
          else if (fieldType == classOf[Short] || fieldType == classOf[Short]) {
            query.addScalar(fieldName, new ShortType)
          }
          else if (fieldType == classOf[Double] || fieldType == classOf[Double]) {
            query.addScalar(fieldName, new DoubleType)
          }
          else if (fieldType == classOf[Float] || fieldType == classOf[Float]) {
            query.addScalar(fieldName, new FloatType)
          }
          else if (fieldType == classOf[Character]) {
            query.addScalar(fieldName, new CharacterType)
          }
          else if (fieldType == classOf[BigDecimal]) {
            query.addScalar(fieldName, new BigDecimalType)
          }
          else if (fieldType == classOf[Array[Byte]] || fieldType == classOf[Array[Byte]]) {
            query.addScalar(fieldName, new BinaryType)
          }
        }
      }
    }
    query.setResultTransformer(Transformers.aliasToBean(clazz))
    true
  }

  
  def insert[T](obj: T): Boolean = {
    try {
      val session: Session = sessionFactory.getCurrentSession
      session.save(obj)
      true
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  
  def insertList[T](obj: Array[T]): Boolean = {
    try {
      val session: Session = sessionFactory.getCurrentSession
      for (t <- obj) {
        session.save(t)
      }
      true
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  
  def update[T](obj: T): Boolean = {
    try {
      val session: Session = sessionFactory.getCurrentSession
      session.update(obj)
      true
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  
  def updateList[T](objs: Array[T]): Boolean = {
    try {
      val session: Session = sessionFactory.getCurrentSession
      for (t <- objs) {
        session.update(t)
      }
      true
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

  
  def delete[T](obj: T): Boolean = {
    try {
      val session: Session = sessionFactory.getCurrentSession
      session.delete(obj)
      true
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        false
    }
  }

}
