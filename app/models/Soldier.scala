package models

abstract class Soldier(id: Int, name: String, territory: Territory, price: Int, mobility: Int, var canMove: Boolean) {
  def getName: String = this.name

  def getId: Int = this.id

  def getLocation: Territory = this.territory

  def getPrice: Int = this.price

  def getMobility: Int = this.mobility

  def getCanMove: Boolean = this.canMove

  def setCanMove(boolean: Boolean){this.canMove = boolean}
}

class Students(id: Int, territory: Territory) extends Soldier(id, "Student", territory,
  1, 1, true) {
}

class  TA(id: Int, territory: Territory) extends Soldier(id, "TA", territory,
  2, 2, true) {
}

class Professors(id: Int, territory: Territory) extends Soldier(id, "Professor", territory,
  3, 1, true) {
}
