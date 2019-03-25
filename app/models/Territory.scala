package models

class Territory(id: Int, name: String, var owner: Player, var numUnits: Int, var soldiers: List[Soldier], district: District, var canAttack: Boolean) {

  def getId: Int = this.id

  def getName: String = this.name

  def getUnits: Int = this.numUnits

  //Adds units and returns new total # of units in the territory
  def addUnits(newUnits: Int): Int = {
    numUnits += newUnits
    numUnits
  }

  def getSoldiers: List[Soldier] = this.soldiers

  def setSoldiers(newSoldiers: List[Soldier]): Unit = this.soldiers = newSoldiers

  def addSoldier(soldier: Soldier): List[Soldier] = soldier::soldiers

  def eliminateSoldier: Soldier = {
    val eliminated = soldiers.head
    soldiers = soldiers.tail
    eliminated
  }

  def getOwner: Player = this.owner

  def getDistrict: District = this.district

  def getCanAttack: Boolean = this.canAttack

  def setCanAttack(canAttack: Boolean) {this.canAttack = canAttack}

  def setOwner(player: Player) {this.owner = player}

  def armyChange(num: Int) {this.numUnits += num}
}
