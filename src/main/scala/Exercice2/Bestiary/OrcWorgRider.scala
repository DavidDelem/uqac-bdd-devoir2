package Exercice2.Bestiary

import Exercice2.{Attack, Monster}

class OrcWorgRider extends Monster {
  this.name = "Orc worg Rider"
  this.hp = 13
  this.armor = 18
  this.regeneration = 0
  this.speeds = ("ft", 20) :: this.speeds

  this.melee = new Attack("mwk battleaxe", 4, 12, List(6), 5)
  this.ranged = new Attack("shortbow", 1, 6, List(4), 110)
}
