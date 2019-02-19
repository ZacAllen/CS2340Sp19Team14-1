package models

class Player(id: Int, name: String, email: String) {

  def this(id: Int, name: String) = {
    this(id, name, null)
  }

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
