package models

class Territory(id: Int, name: String) {

  def getId() = this.id

  def getName() = this.name
}

object Territory {

  def apply(id: Int, name: String) = new Territory(id, name)
}
