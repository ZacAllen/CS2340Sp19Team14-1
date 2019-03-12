package models

class Territory(id: Int, name: String, var owner: Player, var numUnits: Int, district: District, var canAttack: Boolean) {

  def getId: Int = this.id

  def getName: String = this.name

  def getUnits: Int = this.numUnits

  def getOwner = this.owner

  def getDistrict = this.district

  def getCanAttack = this.canAttack

  def setCanAttack(canAttack: Boolean) {this.canAttack = canAttack}

  def setOwner(player: Player) {this.owner = player}

  def ArmyChange(num: Int) {this.numUnits += num}
}
