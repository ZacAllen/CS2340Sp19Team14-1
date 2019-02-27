package models

class Territory(id: Int, name: String, owner: Player, numUnits: Int) {

  def getId: Int = this.id

  def getOwner(): Player = this.owner

  def getName: String = this.name

  def getUnits: Int = this.numUnits
}

object Territory {

  def apply(id: Int, name: String, owner: Player, numUnits: Int) : Territory = new Territory(id, name, owner, numUnits)
}
