package Exercice2.Bestiary

import Exercice2.{Attack, Monster}

class BrutalWarlord extends Monster {
  this.name = "Brutal Warlord"
  this.hp = 141
  this.armor = 27
  this.regeneration = 0
  this.speeds = ("ft", 30) :: this.speeds

  this.melee = new Attack("+1 vicious flail", 11, 18, List(20, 15, 10), 5)
  this.ranged = new Attack("mwk throwing axe", 6, 11, List(19), 110)

}
