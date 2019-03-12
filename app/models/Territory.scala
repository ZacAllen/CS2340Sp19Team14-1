package models

class Territory(id: Int, name: String, var owner: Player, var numUnits: Int, district: District, var canAttack: Boolean) {

  def getId: Int = this.id

  def getName: String = this.name

  def getUnits: Int = this.numUnits

  //Adds units and returns new total # of units in the territory
  def addUnits(newUnits: Int): Int = {
    numUnits += newUnits
    numUnits
  }
  def getOwner: Player = this.owner

  def getDistrict = this.district

  def getCanAttack = this.canAttack

  def setCanAttack(canAttack: Boolean) {this.canAttack = canAttack}

  def setOwner(player: Player) {this.owner = player}

  def ArmyChange(num: Int) {this.numUnits += num}
}
