package Exercice2.Bestiary

import Exercice2.Monster

class DoubleAxeFury extends Monster {
  this.name = "Double Axe Fury"
  this.hp = 142
  this.armor = 17
  this.regeneration = 0
  this.speeds = ("ft", 40) :: this.speeds
  this.melee = ("+1 orc double axe", List(19, 14, 9), List(11, 18), 5)
  this.ranged = ("mwk composite longbow", List(16,11,6), List(7, 14), 110)
}
