package Exercice2.Bestiary

import Exercice2.Monster

class BrutalWarlord extends Monster {
  this.name = "Brutal Warlord"
  this.hp = 141
  this.armor = 27
  this.regeneration = 0
  this.speeds = ("ft", 30) :: this.speeds
  this.melee = ("+1 vicious flail", List(20, 15, 10), List(11, 18), 5)
  this.ranged = ("mwk throwing axe", List(19), List(6, 11), 110)

}
