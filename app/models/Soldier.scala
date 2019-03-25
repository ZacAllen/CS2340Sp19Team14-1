package models

abstract class Soldier(id: Int, name: String, var territory: Territory, price: Int, mobility: Int, var canMove: Boolean) extends Ordered[Soldier] {

  def compare(that: Soldier): Int = this.price - that.getPrice

  def getName: String = this.name

  def getId: Int = this.id

  def getLocation: Territory = this.territory

  def getPrice: Int = this.price

  def getMobility: Int = this.mobility

  def getCanMove: Boolean = this.canMove

  def setCanMove(boolean: Boolean){this.canMove = boolean}

  def setTerritory(territory: Territory) {this.territory = territory}
}



case class Student(id: Int, override var territory: Territory) extends Soldier(id, "Student", territory,
  1, 1, true) {
}

case class  TA(id: Int, override var territory: Territory) extends Soldier(id, "TA", territory,
  2, 2, true) {
}

case class Professor(id: Int, override var territory: Territory) extends Soldier(id, "Professor", territory,
  3, 1, true) {
}
