package models


class Player(id: Int, name: String, email: String, turn: Int, color: Int, numArmies: Int) {

  def getName() = this.name

  def getId() = this.id

  def getEmail() = this.email

  def getTurn() = this.turn

  def getColor() = this.color

  def getNumArmies() = this.numArmies
}

object Player {

  def apply(id: Int, name: String, email: String, turn: Int, color: Int, numArmies: Int) = new Player(id, name, email, turn, color, numArmies)
}
