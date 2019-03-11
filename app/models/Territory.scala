package models

class Territory(id: Int, name: String, owner: Player, numUnits: Int) {

  def getId: Int = this.id

  def getName: String = this.name

  def getUnits: Int = this.numUnits

  //Adds units and returns new total # of units in the territory
  def addUnits(newUnits: Int): Int = {
    numUnits += newUnits
    numUnits
  }
}

object Territory {

  def apply(id: Int, name: String, owner: Player, numUnits: Int): Territory = new Territory(id, name, owner, numUnits)
}
