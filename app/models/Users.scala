package models

import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._

import scala.io.Source

import play.api.libs.json._

import net.liftweb.json.DefaultFormats
import net.liftweb.json._



case class User(id: Long, name: String, isActive: String)

object User {
    implicit val formats = net.liftweb.json.DefaultFormats
    
    val user = {
      get[Long]("id") ~ 
      get[String]("name") ~
      get[String]("isActive") map
      {
        case id~name~isActive => User(id, name, isActive)
      }
    }
  
  def all(): List[User] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM USERS").as(user *)
  }
  
  def filter(isActive: String): List[User] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM USERS WHERE isActive = {isActive}").on('isActive -> isActive).as(user *)
  }
  
  def addUser(id: Long, name: String, isActive: String) {
       DB.withConnection { implicit c =>
    SQL("INSERT INTO USERS (id, name, isActive) VALUES ({id}, {name}, {isActive})").on(
      'id -> id, 'name -> name, 'isActive -> isActive
    ).executeInsert()
  }
  }
  
  
  def findById(id: Long): User = 
    DB.withConnection { implicit c =>
        SQL("SELECT * FROM USERS WHERE id = {id}").on(
          'id -> id).as(user *).head
      }
  
  def delete(id: Long) {
      DB.withConnection { implicit c =>
        SQL("DELETE FROM USERS WHERE id = {id}").on(
          'id -> id
        ).executeUpdate()
      }
    }
    
  def update(id: Long, name: String, isActive: String) {
      DB.withConnection { implicit c =>
        SQL("UPDATE USERS SET name = {name}, isActive = {isActive} WHERE id = {id}").on(
          'id -> id, 'name -> name, 'isActive -> isActive
        ).executeUpdate()
      }
    }
    
  def setup()
  {
      val source: String = Source.fromFile("app/assets/jsons/users.json").getLines.mkString
     val json = JsonParser.parse(source)
      val userData = (json \ "users").children
      println(userData)
      for(user <- userData) 
      { 
          val item = new User (
                    (user \ "id").extract[String].toLong,
                    (user \ "name").extract[String],
                    (user \ "isActive").extract[String] )
                    
          addUser(item.id, item.name, item.isActive) }
  }
}