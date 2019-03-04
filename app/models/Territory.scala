package models

class Territory(id: Int, name: String, owner: Player, numUnits: Int) {

  def getId: Int = this.id

  def getName: String = this.name

  def getUnits: Int = this.numUnits

  def getOwner: Player = this.owner
}

object Territory {

  def apply(id: Int, name: String, owner: Player, numUnits: Int): Territory = new Territory(id, name, owner, numUnits)
}
