package Exercice2.Bestiary

import Exercice2.{Attack, Monster}

class DoubleAxeFury extends Monster {
  this.name = "Double Axe Fury"
  this.hp = 142
  this.armor = 17
  this.regeneration = 0
  this.speeds = ("ft", 40) :: this.speeds

  this.melee = new Attack("+1 orc double axe", 11, 18, List(19, 14, 9), 5)
  this.ranged = new Attack("mwk composite longbow", 7, 14, List(16,11,6), 110)
}
