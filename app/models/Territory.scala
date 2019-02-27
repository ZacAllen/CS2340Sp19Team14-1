package models

class Territory(id: Int, name: String, owner: Player, num_units: Int) {

  def getId() = this.id

  def getName() = this.name

  def getOwner() = this.owner

}

object Territory {

  def apply(id: Int, name: String, owner: Player, num_units: Int) = new Territory(id, name, owner, num_units)
}
