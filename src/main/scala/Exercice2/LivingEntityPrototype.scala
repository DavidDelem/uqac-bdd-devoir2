package Exercice2

import Exercice2.Bestiary.{BrutalWarlord, DoubleAxeFury, OrcWorgRider, Solar}


object LivingEntityPrototype {

  def create(livingEntity: LivingEntity): LivingEntity ={

    var newLivingEntity: LivingEntity = null

    livingEntity match {
      case brutalWarlord: BrutalWarlord => newLivingEntity = new BrutalWarlord(brutalWarlord.position, brutalWarlord.id)
      case doubleAxeFury: DoubleAxeFury => newLivingEntity = new DoubleAxeFury(doubleAxeFury.position, doubleAxeFury.id)
      case orcWorgRider: OrcWorgRider => newLivingEntity = new OrcWorgRider(orcWorgRider.position, orcWorgRider.id)
      case solar: Solar => newLivingEntity = new Solar(solar.position, solar.id)
    }

    newLivingEntity.target = livingEntity.target
    newLivingEntity.hp = livingEntity.hp

    newLivingEntity
  }

}
