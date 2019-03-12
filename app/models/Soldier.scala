package models

abstract class Soldier(id: Int, name: String, territory: Territory, price: Int, mobility: Int, var canMove: Boolean) {
  def getName = this.name

  def getId = this.id

  def getLocation = this.territory

  def getPrice = this.price

  def getMobility = this.mobility

  def getCanMove = this.canMove

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