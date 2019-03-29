package models


class Player(id: Int, name: String, email: String, var turn: Int, color: String, numArmies: Int) extends AnyRef{

  def getName: String = this.name

  def getId: Int = this.id

  def getEmail: String = this.email

  def getTurn: Int = this.turn

  def getColor: String = this.color

  def getNumArmies: Int = this.numArmies

  def setTurn(turn: Int){this.turn = turn}
}

object Player {

  def apply(id: Int, name: String, email: String, turn: Int, color: String, numArmies: Int):
   Player = new Player(id, name, email, turn, color, numArmies)
}
