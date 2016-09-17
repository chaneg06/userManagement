package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import models._




import javax.inject._

class UserController extends Controller {
  
  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "id" -> of[Long],
      "name" -> nonEmptyText,
      "isActive" -> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }
  
  val userEditForm = Form(
    mapping(
      "id" -> of[Long],
      "name" -> nonEmptyText,
      "isActive" -> nonEmptyText)(User.apply)(User.unapply))

  def index = Action {
      User.setup()
    Redirect(routes.UserController.users)
  }
  
  def users = Action {
    Ok(views.html.index(User.all(), userForm))
  }
  
  def usersFiltered(isActive: String) = Action {
    Ok(views.html.index(User.filter(isActive), userForm))
  }
  
  def addUser = Action {
    Ok(views.html.adduser(userForm))
  }

 /**
   * Display the 'edit form' of a existing employee.
   */
  def editUser(id: Long) = Action { implicit request =>
    val filledForm = userEditForm.fill(User.findById(id))
    Ok(views.html.editform(id, filledForm))
  }
  
   def updateUser(id: Long) = Action { implicit request =>
   println(id)
     userEditForm.bindFromRequest.fold(
    errors => BadRequest(views.html.editform(id, errors)),
    data  => {
        println(data.name)
      User.update(data.id, data.name, data.isActive)
      Redirect(routes.UserController.users)
    })
   }
  
  def save = Action { implicit request =>
  userForm.bindFromRequest.fold(
    errors => BadRequest(views.html.adduser(errors)),
    data  => {
      User.addUser(data.id, data.name, data.isActive)
      Redirect(routes.UserController.users)
    }
  )
}
  
  def deleteUser(id: Long) = Action {
    User.delete(id)
    Redirect(routes.UserController.users)
  }
  
}

case class CreateUserForm(id: Long, name: String, isActive: String)