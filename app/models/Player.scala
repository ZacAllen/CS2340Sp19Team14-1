package models

import play.api.mvc.Action
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import net.liftweb.json._
import org.scalatra.{BadRequest, Created}
import play.api.libs.json.Json._


class Player(id: Int, name: String, email: String) {

  def getName() = {
    this.name
  }

  def getId() = {
    this.id
  }

  def getEmail() = {
    this.email
  }

}

object Player {

  def apply(id: Int, name: String, email: String) = new Player(id, name, email)
}
