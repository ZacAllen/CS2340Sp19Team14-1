package models


class Player(id: Int, name: String, email: String, var turn: Int, color: Int, numArmies: Int, armyList: List[Soldier]) {

  def getName: String = this.name

  def getId: Int = this.id

  def getEmail: String = this.email

  def getTurn: Int = this.turn

  def getColor: Int = this.color

  def getNumArmies: Int = this.numArmies

  def getArmyList: List[Soldier] = this.armyList

  def setTurn(turn: Int){this.turn = turn}
}

object Player {

  def apply(id: Int, name: String, email: String, turn: Int, color: Int, numArmies: Int): Player = new Player(id, name, email, turn, color, numArmies)
}
