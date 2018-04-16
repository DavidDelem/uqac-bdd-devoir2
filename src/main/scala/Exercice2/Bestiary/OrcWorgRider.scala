package Exercice2.Bestiary

import Exercice2.Monster

class OrcWorgRider extends Monster {
  this.name = "Orc worg Rider"
  this.hp = 13
  this.armor = 18
  this.regeneration = 0
  this.speeds = ("ft", 20) :: this.speeds
  this.melee = ("mwk battleaxe", List(6), List(4, 12), 5)
  this.ranged = ("shortbow", List(4), List(1, 6), 110)


}
